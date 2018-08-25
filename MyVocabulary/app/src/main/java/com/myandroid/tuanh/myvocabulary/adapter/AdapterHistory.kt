package com.myandroid.tuanh.myvocabulary.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import com.myandroid.tuanh.myvocabulary.R
import com.myandroid.tuanh.myvocabulary.ResultActivity
import com.myandroid.tuanh.myvocabulary.applib.MyWordPro
import com.myandroid.tuanh.myvocabulary.database.HistoryDB
import com.myandroid.tuanh.myvocabulary.database.SQLiteDataController
import java.io.IOException

/**
 * Created by Tanh on 7/20/2017.
 */
class AdapterHistory(private var activity: Activity, private var list: ArrayList<MyWordPro>) : BaseAdapter() {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View = layoutInflater.inflate(R.layout.layout_history,null)
        var word = list[p0]
        createView(view,word)
        return view
    }

    private fun createView(view: View, word: MyWordPro){
        var textViewWord = view.findViewById<TextView>(R.id.textViewWord)
        if(word.Note== "V-A")
            textViewWord.text = word.Word+" ("+ word.Note+")"
        else
            textViewWord.text = word.Word
        textViewWord.setOnClickListener {
            var intent = Intent(activity,ResultActivity::class.java)
            intent.putExtra("id",word.Id)
            activity.startActivity(intent)
        }
        var buttonDel = view.findViewById<Button>(R.id.buttonDel)
        buttonDel.setOnClickListener {
            createDB()
            var hisDB= HistoryDB(activity)
            if (hisDB.deleteHistory(word.Id)) {
                list.remove(word)
                notifyDataSetChanged()
            }
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