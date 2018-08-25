package com.myandroid.tuanh.myvocabulary.adapter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.*
import com.myandroid.tuanh.myvocabulary.applib.MyWordPro
import com.myandroid.tuanh.myvocabulary.applib.VecterTypeWord
import com.myandroid.tuanh.myvocabulary.R
import com.myandroid.tuanh.myvocabulary.database.SQLiteDataController
import java.io.IOException

/**
 * Created by Tanh on 7/20/2017.
 */
class AdapterTpye(private var activity: Activity,private var word: MyWordPro) : BaseAdapter() {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        var list = word.ListType
        val layoutInflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.layout_type,null)
        var vectorType = list[p0]
        var buttonType = view.findViewById<Button>(R.id.buttonType)
        buttonType.text= vectorType.toStringMax()
        var buttonDel = view.findViewById<Button>(R.id.buttonDel)
        buttonDel.setOnClickListener {
            list.remove(vectorType)
            Toast.makeText(activity, "Xoá thành công", Toast.LENGTH_SHORT).show()
            word.ListType= list
            notifyDataSetChanged()
        }
        buttonType.setOnClickListener {
            var dialogType: Dialog = Dialog(activity);
            dialogType.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialogType.setContentView(R.layout.layout_addtype)
            var buttonW = dialogType.findViewById<Button>(R.id.buttonWord)
            buttonW.text= word.Word
            var spinerType = dialogType.findViewById<Spinner>(R.id.spinnerType)
            var list= ArrayList<String>();
            list.add("noun");
            list.add("verb");
            list.add("adj");
            list.add("adv");
            list.add("preposition")
            list.add("pronouns")
            var arrayAdapter = ArrayAdapter(activity, android.R.layout.simple_spinner_item, list)
            arrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_single_choice)
            spinerType.adapter = arrayAdapter
            var j=0
            for (i in list)
            {
                if(i.equals(vectorType.TypeOfWord)) {
                    spinerType.setSelection(j)
                    break
                }
                j++
            }
            var edtMean = dialogType.findViewById<EditText>(R.id.editTextMean)
            edtMean.setText(vectorType.Meaning)
            var buttonGo = dialogType.findViewById<Button>(R.id.buttonGo)
            dialogType.show()
            buttonGo.setOnClickListener {
                if(!edtMean.text.toString().equals("")) {
                    vectorType = VecterTypeWord(list[spinerType.selectedItemPosition], edtMean.text.toString())
                    word.ListType[p0]= vectorType
                    notifyDataSetChanged()
                    Toast.makeText(activity, "Đã lưu", Toast.LENGTH_SHORT).show()
                    dialogType.dismiss()
                }
                else
                    Toast.makeText(activity, "Không được để trống nghĩa", Toast.LENGTH_SHORT).show()
            }
        }
        var anim = AnimationUtils.loadAnimation(activity, R.anim.sacle)
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
        return word.ListType.size
    }

}