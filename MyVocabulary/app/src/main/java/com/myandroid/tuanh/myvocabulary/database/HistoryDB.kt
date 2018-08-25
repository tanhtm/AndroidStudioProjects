package com.myandroid.tuanh.myvocabulary.database

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import com.myandroid.tuanh.myvocabulary.applib.MyWordPro
import java.lang.Exception

/**
 * Created by Tanh on 7/20/2017.
 */

class HistoryDB(con: Context) : SQLiteDataController(con) {
    val List: ArrayList<MyWordPro>
        get() {
            val arrayList = ArrayList<MyWordPro>()
            openDataBase()
            val cursor = database.rawQuery("SELECT * FROM HistoryTranslate", null)
            var size= cursor.count
            if(cursor.count==0){
                close()
                return arrayList
            }
            while (cursor.moveToPosition(size-1)) {
                val myWord = MyWordPro(cursor.getString(1))
                myWord.Id = cursor.getInt(0)
                myWord.WebViewFull= cursor.getString(2)
                myWord.Note= cursor.getString(3)
                arrayList.add(myWord)
                size--
            }
            close()
            return arrayList
        }
    fun getHistory(id: Int): MyWordPro{
        var query=""
        query = "SELECT * FROM HistoryTranslate WHERE id = $id"
        openDataBase()
        val cursor = database.rawQuery(query,null)
        if(cursor.count==0) {
            close()
            return MyWordPro("")
        }
        var myWord = MyWordPro("")
        cursor.moveToFirst()
        myWord.Id = cursor.getInt(0)
        myWord.Word=cursor.getString(1)
        if(cursor.getString(2)!= null)
            myWord.WebViewFull= cursor.getString(2)
        myWord.Note= cursor.getString(3)
        close()
        return myWord
    }
    fun getListSearch(string: String):ArrayList<MyWordPro>{
        val array: ArrayList<MyWordPro> = ArrayList()
        var query=""
        query = "SELECT * FROM HistoryTranslate WHERE word LIKE '%$string%'"
        if (string.equals(""))
            return List
        openDataBase()
        val cursor = database.rawQuery(query,null)
        if(cursor.count==0) {
            close()
            return array
        }
        while (cursor.moveToNext()){val myWord = MyWordPro(cursor.getString(1))
            myWord.Id = cursor.getInt(0)
            if(cursor.getString(2)!= null)
                myWord.WebViewFull= cursor.getString(2)
            myWord.Note= cursor.getString(3)
            array.add(myWord)
        }
        close()
        return array
    }
    fun isExisted(myWord: MyWordPro):Boolean{
        val query = "SELECT * FROM HistoryTranslate WHERE word = '${myWord.Word}'"
        openDataBase()
        val cursor = database.rawQuery(query,null)
        if (cursor.count!=0) {
            close()
            return true
        }
        close()
        return false;
    }
    val Count: Int
        get() {
            openDataBase()
            val cursor = database.rawQuery("SELECT * FROM HistoryTranslate", null)
            var n= cursor.count
            close()
            return n
        }
    fun newId(): Int
    {
        openDataBase()
        val cursor = database.rawQuery("SELECT * FROM HistoryTranslate", null)
        if(cursor.count==0)
            return 0
        cursor.moveToLast()
        return cursor.getInt(0)+1
    }
    fun insertHistory(myWord: MyWordPro) : Boolean
    {
        var result= false
        try {
            openDataBase()
            var value = ContentValues()
            value.put("id",myWord.Id)
            value.put("word", myWord.Word)
            value.put("webInfo",myWord.WebViewFull)
            value.put("note",myWord.Note)
            var n = database.insert("HistoryTranslate", null, value)
            if (n >= 0)
                result = true
        }catch (e: Exception){
            e.printStackTrace()
        }
        finally {
            close()
        }
        return  result
    }
    fun updateHistory(myWord: MyWordPro):Boolean{
        var result= false
        try {
            openDataBase()
            var value = ContentValues()
            value.put("id",myWord.Id)
            value.put("word", myWord.Word)
            value.put("webInfo",myWord.WebViewFull)
            value.put("note",myWord.Note)
            var n = database.insert("HistoryTranslate", null, value)
            if (n > 0)
                result = true
        }catch (e: SQLException){
            e.printStackTrace()
        }
        finally {
            close()
        }
        return  result
    }
    fun deleteHistory(id: Int): Boolean {
        var result = false
        try {

            openDataBase()
            val rs = database.delete("HistoryTranslate", "id=" + id, null)
            if (rs > 0) {
                result = true
            }
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            close()
        }
        return result
    }
}