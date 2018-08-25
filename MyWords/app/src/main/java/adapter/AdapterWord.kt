package adapter

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
import applib.MyWord
import com.myandroid.tuanh.mywords.R
import database.FolderDB
import database.SQLiteDataController
import database.WordDB
import java.io.IOException
import java.util.*

/**
 * Created by Tanh on 7/20/2017.
 */
class AdapterWord(private var activity: Activity, private var list: ArrayList<MyWord>) : BaseAdapter() {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater: LayoutInflater= activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View= layoutInflater.inflate(R.layout.layout_my_word,null)
        var txtv: Button= view.findViewById(R.id.buttonWord)
        var word: MyWord= list[p0]
        txtv.setText(word.toString())
        var btnSel: Button= view.findViewById(R.id.buttonSel)

        txtv.setOnClickListener {
            btnSel.visibility= View.VISIBLE
        }
        btnSel.setOnClickListener(View.OnClickListener {
            var dialog: Dialog= Dialog(activity);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.layout_editword)
            dialog.show()
            var edtWord= dialog.findViewById<EditText>(R.id.editTextWord)
            edtWord.setText(word.Word)
            var edtMean= dialog.findViewById<EditText>(R.id.editTextMeaning)
            edtMean.setText(word.Meaning)
            var edtHint= dialog.findViewById<EditText>(R.id.editTextHint)
            edtHint.setText(word.Note)
            var btnSave= dialog.findViewById<Button>(R.id.buttonSave)
            var btnDel= dialog.findViewById<Button>(R.id.buttonDel)
            var btnQuit= dialog.findViewById<Button>(R.id.buttonQuit)
            var btnType= dialog.findViewById<Button>(R.id.buttonType)
            var buttonFolder= dialog.findViewById<Button>(R.id.buttonFolder);
            if(word.IdFolder== 0)
                buttonFolder.text="NULL"
            else{
                var folder: FolderDB = FolderDB(activity)
                for (i in folder.List)
                    if(i.Id== word.IdFolder) {
                        buttonFolder.text = i.Name
                        break
                    }
            }
            btnType.text= word.TypeOfWord
            btnType.setOnClickListener {
                var dialogType: Dialog= Dialog(activity);
                dialogType.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialogType.setContentView(R.layout.layout_typeword)
                dialogType.show()
                var radioType= dialogType.findViewById<RadioGroup>(R.id.radioType)
                radioType.setOnCheckedChangeListener { radioGroup, i ->
                    when(i){
                        R.id.radioButtonNoun-> btnType.text= "noun"
                        R.id.radioButtonVerb-> btnType.text= "verb"
                        R.id.radioButtonAdj-> btnType.text= "adj"
                        else-> btnType.text="adv"
                    }
                    dialogType.hide()
                }
            }
            btnQuit.setOnClickListener {
                dialog.hide()
            }
            var alerDialogDel: AlertDialog.Builder = AlertDialog.Builder(activity)
            alerDialogDel.setTitle("Xác nhận xoá")
            alerDialogDel.setIcon(android.R.drawable.ic_delete)
            alerDialogDel.setMessage("Xoá từ:\n $word")
            alerDialogDel.setPositiveButton("Có",DialogInterface.OnClickListener { dialogInterface, i ->
                createDB()
                var db :WordDB= WordDB(view.context)
                if(db.deleteWord(word.Id)) {
                    Toast.makeText(view.context, "Xóa thành công", Toast.LENGTH_LONG).show()
                    dialog.hide()
                    list.remove(word)
                    notifyDataSetChanged()
                }else
                    Toast.makeText(view.context,"Sai "+word.Id,Toast.LENGTH_LONG).show()
            })
            alerDialogDel.setNegativeButton("Không",DialogInterface.OnClickListener { dialogInterface, i ->
            })
            btnDel.setOnClickListener {
                alerDialogDel.show()
            }
            btnSave.setOnClickListener {
                createDB()
                var db :WordDB= WordDB(view.context)
                word.Word= edtWord.text.toString()
                word.Meaning= edtMean.text.toString()
                word.Note= edtHint.text.toString()
                word.TypeOfWord= btnType.text.toString()
                if(db.updateWord(word)) {
                    Toast.makeText(view.context, "Thay đổi thành công", Toast.LENGTH_LONG).show()
                    //list.add(word)
                    notifyDataSetChanged()
                }else
                    Toast.makeText(view.context,"Sai "+word.Id,Toast.LENGTH_LONG).show()
                dialog.hide()
            }
        })
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