package com.myandroid.tuanh.myvocabulary.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.AsyncTask
import android.support.v7.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.*
import com.myandroid.tuanh.myvocabulary.MyWordActivity
import com.myandroid.tuanh.myvocabulary.R
import com.myandroid.tuanh.myvocabulary.applib.HandlingWeb
import com.myandroid.tuanh.myvocabulary.applib.MyFolder
import com.myandroid.tuanh.myvocabulary.applib.MyWordPro
import com.myandroid.tuanh.myvocabulary.database.FolderDB
import com.myandroid.tuanh.myvocabulary.database.SQLiteDataController
import com.myandroid.tuanh.myvocabulary.database.WordProDB
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStreamReader
import java.net.MalformedURLException
import java.net.URL

/**
 * Created by Tanh on 7/20/2017.
 */
class AdapterWord(private var activity: Activity, private var list: ArrayList<MyWordPro>) : BaseAdapter() {
    private var dialogX: Dialog = Dialog(activity)
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater: LayoutInflater= activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View= layoutInflater.inflate(R.layout.layout_my_word,null)
        var txtv: Button= view.findViewById(R.id.buttonWord)
        var word = list[p0]
        var buttonInfo = view.findViewById<Button>(R.id.buttonInfo)
        var buttonAll= view.findViewById<Button>(R.id.buttonAll)
        if (word.WebViewFull.equals(""))
            buttonAll.setBackgroundResource(R.drawable.warning)
        buttonAll.setOnClickListener {
            if(!word.WebViewFull.equals("")) {
                var intent: Intent = Intent(this.activity, MyWordActivity::class.java)
                intent.putExtra("id", word.Id)
                activity.startActivity(intent)
            }
            else{
                var alerDialog: AlertDialog.Builder = AlertDialog.Builder(activity)
                alerDialog.setTitle("Từ \"${word.Word}\" chưa có đầy đủ thông tin !")
                alerDialog.setIcon(R.drawable.info)
                alerDialog.setMessage("Bạn muốn cập nhập không ?\n(Dùng dữ liệu trực tuyến)")
                alerDialog.setPositiveButton("Có", DialogInterface.OnClickListener { dialogInterface, i ->
                    update(word)
                    activity.finish()
                })
                alerDialog.setNegativeButton("Không", DialogInterface.OnClickListener { dialogInterface, i ->
                })
                alerDialog.show()
            }
        }
        buttonInfo.setText(word.toString())
        buttonInfo.setOnClickListener {
            txtv.text= word.Word
            word.ListExample
            if(!word.Pronunciation.equals(""))
                txtv.text= word.Word +" ["+word.Pronunciation+"]"
            txtv.visibility= View.VISIBLE
            txtv.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.ssss))
            buttonAll.visibility= View.VISIBLE
            buttonInfo.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.ssss))
            buttonInfo.text = word.getInfo()
            if(!word.Note.equals("")) {
                var buttonNote = view.findViewById<Button>(R.id.buttonNote)
                buttonNote.visibility = View.VISIBLE
                buttonNote.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.ssss))
                buttonNote.text =   "\tNOTE: "+word.Note
            }
            txtv.setOnClickListener {
                var dialog: Dialog = Dialog(activity);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialog.setContentView(R.layout.layout_editword)
                dialog.show()
                var edtWord = dialog.findViewById<EditText>(R.id.editTextWord)
                var edtPron = dialog.findViewById<EditText>(R.id.editTextPron)
                edtPron.setText(word.Pronunciation)
                edtWord.setText(word.Word)
                var listViewInfo = dialog.findViewById<ListView>(R.id.listViewType)
                listViewInfo.adapter = AdapterTpye(activity,word)
                var edtHint= dialog.findViewById<EditText>(R.id.editTextHint)
                edtHint.setText(word.Note)
                var btnSave= dialog.findViewById<Button>(R.id.buttonSave)
                var btnDel= dialog.findViewById<Button>(R.id.buttonDel)
                var btnQuit= dialog.findViewById<Button>(R.id.buttonQuit)
                var spinerF: Spinner = dialog.findViewById(R.id.spinnerFolder)
                var folder: FolderDB = FolderDB(activity)
                var listFolder: ArrayList<MyFolder> = folder.List
                var arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, listFolder)
                var folder_name= ""

                val arrayList = word.ListType
                arrayList.remove(word.VectorTypeWord)
                arrayList.add(word.VectorTypeWord)
                val spinnerM = dialog.findViewById<Spinner>(R.id.spinnerMean)
                val arrayAdapterM = ArrayAdapter(activity, android.R.layout.simple_spinner_item, arrayList)
                arrayAdapterM.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
                spinnerM.adapter = arrayAdapterM
                spinnerM.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                    override fun onItemSelected(adapterView: AdapterView<*>, view: View, i: Int, l: Long) {
                        word.VectorTypeWord = arrayList.get(i)
                    }

                    override fun onNothingSelected(adapterView: AdapterView<*>) {
                        word.VectorTypeWord = arrayList.get(0)
                    }
                }
                arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
                spinerF.adapter = arrayAdapter
                var pos: Int =0
                for(i in listFolder){
                    if(i.Id== word.IdFolder)
                        break
                    pos++;
                }
                spinerF.setSelection(pos)
                spinerF.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
                    override fun onNothingSelected(p0: AdapterView<*>?) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                    }

                    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                        word.IdFolder= listFolder[p2].Id
                        folder_name= listFolder[p2].Name
                    }
                }
                btnQuit.setOnClickListener {
                    dialog.dismiss()
                }
                var alerDialogDel: AlertDialog.Builder = AlertDialog.Builder(activity)
                alerDialogDel.setTitle("Xác nhận xoá")
                alerDialogDel.setIcon(android.R.drawable.ic_delete)
                alerDialogDel.setMessage("Xoá từ:\n $word")
                alerDialogDel.setPositiveButton("Có", DialogInterface.OnClickListener { dialogInterface, i ->
                    createDB()
                    var db = WordProDB(view.context)
                    if(db.deleteWord(word.Id)) {
                        Toast.makeText(view.context, "Xóa thành công", Toast.LENGTH_LONG).show()
                        dialog.dismiss()
                        list.remove(word)
                        notifyDataSetChanged()
                    }else
                        Toast.makeText(view.context,"Sai "+word.Id, Toast.LENGTH_LONG).show()
                })
                alerDialogDel.setNegativeButton("Không", DialogInterface.OnClickListener { dialogInterface, i ->
                })
                btnDel.setOnClickListener {
                    alerDialogDel.show()
                }
                btnSave.setOnClickListener {
                    createDB()
                    var db = WordProDB(view.context)
                    word.Note= edtHint.text.toString()
                    word.Word= edtWord.text.toString()
                    word.Pronunciation= edtPron.text.toString()
                    if(db.updateWord(word)) {
                        Toast.makeText(view.context, "Thay đổi thành công", Toast.LENGTH_LONG).show()
                        if(word.IdFolder!= listFolder[pos].Id) {
                            Toast.makeText(activity,"Từ \"${word.Word}\" đã được chuyển sang thư mục $folder_name !", Toast.LENGTH_LONG).show()
                            if(pos!=0)
                                list.remove(word)
                        }
                        notifyDataSetChanged()
                    }else
                        Toast.makeText(view.context,"Sai "+word.Id, Toast.LENGTH_LONG).show()
                    dialog.hide()
                }
            }
        }
        var anim = AnimationUtils.loadAnimation(activity,R.anim.sacle)
        view.startAnimation(anim)
        return view
    }
    private fun update(myWordPro: MyWordPro):MyWordPro{
        var word = myWordPro
        var pro: ProgressBar = activity.findViewById(R.id.progressBarLoad)
        pro.visibility = View.VISIBLE
        class ReadWeb : AsyncTask<String, Void, String>() {

            override fun doInBackground(vararg strings: String): String? {
                try {
                    val url = URL(strings[0])
                    val urlConnection = url.openConnection()
                    val inputStream = urlConnection.getInputStream()
                    val inputStreamReader = InputStreamReader(inputStream)
                    val bufferedReader = BufferedReader(inputStreamReader)
                    var result = bufferedReader.readLine()
                    while ( result != null) {
                        if (result.contains("class=\"kq\"")) {
                            bufferedReader.close()

                            if (result.contains("class=\"i p10\">Dữ liệu đang được cập nhật")) {
                                return null
                            }
                            if (result.contains("<div class=\"m\"><span> Xem </span>")) {
                                result = result.substring(result.indexOf(".html\">") + 7, result.indexOf("</a> </div></div>"))
                                return "Xem " + "\"" + result + "\""
                            }else
                                return result

                        }
                        result = bufferedReader.readLine()
                    }
                } catch (e: MalformedURLException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                return null
            }

            override fun onPostExecute(s: String?) {
                super.onPostExecute(s)
                var pro: ProgressBar = activity.findViewById(R.id.progressBarLoad)
                pro.visibility = View.GONE
                if (s == null) {
                    Toast.makeText(activity, "Không tìm được", Toast.LENGTH_SHORT).show()
                } else {
                    if (s.contains("Xem")) {
                        Toast.makeText(activity, s, Toast.LENGTH_SHORT).show()
                    } else {
                        var id= word.Id
                        var idFol= word.IdFolder
                        var note= word.Note
                        word= HandlingWeb(word.Word,s).MyWord
                        word.Id= id
                        word.IdFolder= idFol
                        word.Note= note
                        createDB()
                        var db = WordProDB(activity)
                        if(db.updateWord(word)) {
                            Toast.makeText(activity, "Cập nhập thành công !", Toast.LENGTH_SHORT).show()
                        }
                        else
                            Toast.makeText(activity, "Lỗi !", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
        ReadWeb().execute("http://tratu.coviet.vn/hoc-tieng-anh/tu-dien/lac-viet/A-V/${word.Word}.html")
        return word
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