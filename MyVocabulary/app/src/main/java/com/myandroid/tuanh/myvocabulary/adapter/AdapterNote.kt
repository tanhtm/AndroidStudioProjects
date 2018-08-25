package com.myandroid.tuanh.myvocabulary.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.*
import com.myandroid.tuanh.myvocabulary.applib.MyFolder
import com.myandroid.tuanh.myvocabulary.applib.MyNote
import com.myandroid.tuanh.myvocabulary.R
import com.myandroid.tuanh.myvocabulary.database.FolderDB
import com.myandroid.tuanh.myvocabulary.database.NoteDB
import com.myandroid.tuanh.myvocabulary.database.SQLiteDataController
import java.io.IOException
import java.util.*

/**
 * Created by Tanh on 7/20/2017.
 */
class AdapterNote(private var activity: Activity, private var list: ArrayList<MyNote>) : BaseAdapter() {
    @SuppressLint("SetTextI18n", "ViewHolder")
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater: LayoutInflater= activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View= layoutInflater.inflate(R.layout.layout_my_note,null)
        var txtv: Button= view.findViewById(R.id.buttonNote)
        var note: MyNote= list[p0]
        txtv.setText(note.toString())
        var btnSel: Button= view.findViewById(R.id.buttonSel)
        txtv.setOnClickListener {
            btnSel.visibility= View.VISIBLE
        }
        btnSel.setOnClickListener{
            var dialog: Dialog = Dialog(view.context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.layout_editnote)
            dialog.show()
            var textViewNote= dialog.findViewById<TextView>(R.id.textViewInforNote)
            textViewNote.setText("Num: ${note.Id}\nDay: ${note.Day}")
            var edtNote= dialog.findViewById<EditText>(R.id.editTextNote)
            edtNote.setText(note.Note)
            var btnSave= dialog.findViewById<Button>(R.id.buttonSave)
            var btnDel= dialog.findViewById<Button>(R.id.buttonDel)
            var btnQuit= dialog.findViewById<Button>(R.id.buttonQuit)
            btnQuit.setOnClickListener {
                dialog.dismiss()
            }
            var spinerF: Spinner = dialog.findViewById(R.id.spinnerFolder)
            var folder: FolderDB = FolderDB(activity)
            var listFolder: ArrayList<MyFolder> = folder.List
            var folder_name =""
            var arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, listFolder)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spinerF.adapter = arrayAdapter
            var pos: Int =0
            for(i in listFolder){
                if(i.Id== note.IdFolder)
                    break
                pos++;
            }
            spinerF.setSelection(pos)
            spinerF.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                override fun onNothingSelected(p0: AdapterView<*>?) {
                    TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                }

                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                    note.IdFolder= listFolder[p2].Id
                    folder_name= listFolder[p2].Name
                }
            }
            var alerDialogDel: AlertDialog.Builder = AlertDialog.Builder(activity)
            alerDialogDel.setTitle("Xác nhận xoá")
            alerDialogDel.setIcon(android.R.drawable.ic_delete)
            alerDialogDel.setMessage("Xoá ghi chú:\n $note")
            alerDialogDel.setPositiveButton("Có", DialogInterface.OnClickListener { dialogInterface, i ->
                createDB()
                var db : NoteDB = NoteDB(view.context)
                if(db.deleteNote(note.Id)) {
                    Toast.makeText(view.context, "Xóa thành công", Toast.LENGTH_LONG).show()
                    dialog.dismiss()
                    list.remove(note)
                    notifyDataSetChanged()
                }
                else
                    Toast.makeText(view.context,"Sai "+note.Id, Toast.LENGTH_LONG).show()
            })
            alerDialogDel.setNegativeButton("Không", DialogInterface.OnClickListener { dialogInterface, i ->
            })
            btnDel.setOnClickListener {
                alerDialogDel.show()
            }
            btnSave.setOnClickListener {
                createDB()
                var db : NoteDB = NoteDB(view.context)
                note.Note= edtNote.text.toString()
                if (db.updateNote(note)) {
                    Toast.makeText(view.context, "Thay đổi thành công", Toast.LENGTH_LONG).show()
                    if(note.IdFolder!= listFolder[pos].Id) {
                        Toast.makeText(activity,"Ghi chú số ${note.Id} đã có trong thư mục $folder_name !",Toast.LENGTH_LONG).show()
                        if(pos!=0)
                            list.remove(note)
                    }
                    notifyDataSetChanged()
                }
                else
                    Toast.makeText(view.context, "Sai " + note.Id, Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
        }

        var anim = AnimationUtils.loadAnimation(activity,R.anim.sacle)
        view.startAnimation(anim)
        return view
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