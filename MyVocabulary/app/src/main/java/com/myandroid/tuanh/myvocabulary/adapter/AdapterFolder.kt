package com.myandroid.tuanh.myvocabulary.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.myandroid.tuanh.myvocabulary.LibActivity
import com.myandroid.tuanh.myvocabulary.R
import com.myandroid.tuanh.myvocabulary.applib.MyFolder
import com.myandroid.tuanh.myvocabulary.database.FolderDB
import com.myandroid.tuanh.myvocabulary.database.NoteDB
import com.myandroid.tuanh.myvocabulary.database.SQLiteDataController
import com.myandroid.tuanh.myvocabulary.database.WordProDB
import java.io.IOException
import java.util.*

/**
 * Created by Tanh on 8/11/2017.
 */
class AdapterFolder (private var activity: Activity, private var list: ArrayList<MyFolder>) : BaseAdapter() {
    @SuppressLint("ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View= layoutInflater.inflate(R.layout.layout_myfolder,null)

        var folder: MyFolder = list[p0]

        createView(view,folder)

        var anim = AnimationUtils.loadAnimation(activity,R.anim.sacle)
        view.startAnimation(anim)
        return  view
    }

    private fun createView(view: View,folder: MyFolder){
        var buttonName = view.findViewById<Button>(R.id.buttonName)
        var buttonSel = view.findViewById<Button>(R.id.buttonSel)
        buttonName.text= folder.Name
        if(folder.Id== 0)
            buttonSel.visibility= View.INVISIBLE
        buttonSel.setOnClickListener {
            dialogSel(folder)
        }
        buttonName.setOnClickListener {
            var int : Intent = Intent(this.activity, LibActivity::class.java)
            int.putExtra("namefolder",folder.Name)
            int.putExtra("idfolder",folder.Id)
            activity.startActivity(int)
        }
    }

    private  fun alerDialogDel(myFolder: MyFolder){
        var alerDialogDel: AlertDialog.Builder = AlertDialog.Builder(activity)
        alerDialogDel.setTitle("Xác nhận xoá")
        alerDialogDel.setIcon(android.R.drawable.ic_delete)
        alerDialogDel.setMessage("Thư mục $myFolder và toàn bộ từ vựng và ghi chú có trong thư mục")
        alerDialogDel.setPositiveButton("Có", DialogInterface.OnClickListener { dialogInterface, i ->
            createDB()
            var db : FolderDB = FolderDB(activity)
            if(db.deleteFolder(myFolder.Id)) {
                Toast.makeText(activity, "Xóa thư mục thành công", Toast.LENGTH_LONG).show()
                list.remove(myFolder)
                notifyDataSetChanged()
                var dbW:WordProDB = WordProDB(activity)
                var i = dbW.deleteFolder(myFolder)
                var dbN: NoteDB = NoteDB(activity)
                var j =dbN.deleteFolder(myFolder)
                Toast.makeText(activity, "Xóa từ vụng và ghi chú thành công, lỗi $i - $j", Toast.LENGTH_LONG).show()
            }
            else
                Toast.makeText(activity,"Sai "+myFolder.Id, Toast.LENGTH_LONG).show()
        })
        alerDialogDel.setNegativeButton("Không", DialogInterface.OnClickListener { dialogInterface, i ->
        })
        alerDialogDel.show();
    }

    private  fun dialogSel(myFolder: MyFolder){
        var dialog : Dialog = Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.layout_editfolder)
        var btnSave= dialog.findViewById<Button>(R.id.buttonSave)
        var btnDel= dialog.findViewById<Button>(R.id.buttonDel)
        var btnQuit= dialog.findViewById<Button>(R.id.buttonQuit)
        var edtName = dialog.findViewById<EditText>(R.id.editTextName)
        edtName.setText(myFolder.Name)
        dialog.show()
        btnQuit.setOnClickListener {
            dialog.dismiss()
        }
        btnDel.setOnClickListener {
            alerDialogDel(myFolder)
            dialog.dismiss()
        }
        btnSave.setOnClickListener {
            createDB()
            var db : FolderDB = FolderDB(activity)
            myFolder.Name= edtName.text.toString()
            if (db.updateFolder(myFolder)) {
                Toast.makeText(activity, "Thay đổi thành công", Toast.LENGTH_LONG).show()
                notifyDataSetChanged()
            }
            else
                Toast.makeText(activity, "Sai " + myFolder.Id, Toast.LENGTH_LONG).show()
            dialog.dismiss()
        }
    }

    private fun createDB() {
        // khởi tạo database
        val sql = SQLiteDataController(activity)
        try {
            sql.isCreatedDatabase
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }
    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }
    override fun getCount(): Int {
        return list.size
    }
}