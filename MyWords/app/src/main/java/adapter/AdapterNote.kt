package adapter

import applib.MyNote
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import com.myandroid.tuanh.mywords.R
import database.NoteDB
import database.SQLiteDataController
import java.io.IOException
import java.util.*

/**
 * Created by Tanh on 7/20/2017.
 */
class AdapterNote(private var activity: Activity, private var list: ArrayList<MyNote>) : BaseAdapter() {
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
                dialog.hide()
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
                    dialog.hide()
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
                    notifyDataSetChanged()
                }
                else
                    Toast.makeText(view.context, "Sai " + note.Id, Toast.LENGTH_LONG).show()
                dialog.hide()
            }
        }
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