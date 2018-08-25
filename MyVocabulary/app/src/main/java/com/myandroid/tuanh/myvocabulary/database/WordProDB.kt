package com.myandroid.tuanh.myvocabulary.database

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import com.myandroid.tuanh.myvocabulary.applib.MyFolder
import com.myandroid.tuanh.myvocabulary.applib.MyWordPro
import com.myandroid.tuanh.myvocabulary.applib.VecterTypeWord
import java.lang.Exception

/**
 * Created by Tanh on 7/20/2017.
 */

class WordProDB(con: Context) : SQLiteDataController(con) {
    val List: ArrayList<MyWordPro>
        get() {
            val arrayList = ArrayList<MyWordPro>()
            openDataBase()
            val cursor = database.rawQuery("SELECT * FROM MyWordPro", null)
            var size= cursor.count
            if(cursor.count==0){
                close()
                return arrayList
            }
            while (cursor.moveToPosition(size-1)) {
                val myWord = MyWordPro(cursor.getString(1))
                myWord.Id = cursor.getInt(0)
                myWord.IdFolder = cursor.getInt(2)
                if(cursor.getString(3)!=null)
                    myWord.Note = cursor.getString(3)
                if(cursor.getString(4)!= null)
                    myWord.WebViewFull= cursor.getString(4)
                if(cursor.getString(5)!=null)
                    myWord.VectorTypeWord= getVector(cursor.getString(5))
                arrayList.add(myWord)
                size--
            }
            close()
            return arrayList
        }
    private fun getVector(string: String): VecterTypeWord{
        var array = string.split(")")
        return  VecterTypeWord(array[0].substring(1),array[1])
    }
    fun getWord(id: Int): MyWordPro
    {
        val query = "SELECT * FROM MyWordPro WHERE id = $id"
        openDataBase()
        val cursor = database.rawQuery(query,null)
        cursor.moveToFirst()
        var myWord = MyWordPro(cursor.getString(1))
        myWord.Id = cursor.getInt(0)
        myWord.IdFolder = cursor.getInt(2)
        if(cursor.getString(3)!=null)
            myWord.Note = cursor.getString(3)
        if(cursor.getString(4)!= null)
            myWord.WebViewFull= cursor.getString(4)
        if(!cursor.getString(5).equals(""))
            myWord.VectorTypeWord= getVector(cursor.getString(5))
        return myWord
    }
    fun getListFromFolder(myFolder: MyFolder):ArrayList<MyWordPro>{
        if(myFolder.Id==0)
            return List
        val array: ArrayList<MyWordPro> = ArrayList()
        val query = "SELECT * FROM MyWordPro WHERE idfolder = ${myFolder.Id}"
        openDataBase()
        val cursor = database.rawQuery(query,null)
        if(cursor.count==0){
            close()
            return array
        }
        while (cursor.moveToNext()){
            val myWord = MyWordPro(cursor.getString(1))
            myWord.Id = cursor.getInt(0)
            myWord.IdFolder = cursor.getInt(2)
            if(cursor.getString(3)!=null)
                myWord.Note = cursor.getString(3)
            if(cursor.getString(4)!= null)
                myWord.WebViewFull= cursor.getString(4)
            if(!cursor.getString(5).equals(""))
                myWord.VectorTypeWord= getVector(cursor.getString(5))
            array.add(myWord)
        }
        close()
        return array
    }
    fun getWebView(id: Int):String{
        var str=""
        openDataBase()
        val cursor = database.rawQuery("SELECT * FROM MyWordPro WHERE id = $id", null)
        if (cursor.count==0)
            str= "Lỗi"
        else {
            cursor.moveToFirst()
            str = cursor.getString(6)
        }
        return  str
    }
    fun getListSearch(string: String,idFolder: Int):ArrayList<MyWordPro>{
        val array: ArrayList<MyWordPro> = ArrayList()
        var query=""
        if(idFolder!=0)
            query = "SELECT * FROM MyWordPro WHERE word LIKE '%$string%' AND idfolder = $idFolder"
        else
            query = "SELECT * FROM MyWordPro WHERE word LIKE '%$string%'"
        openDataBase()
        val cursor = database.rawQuery(query,null)
        if(cursor.count==0) {
            close()
            return array
        }
        while (cursor.moveToNext()){val myWord = MyWordPro(cursor.getString(1))
            myWord.Id = cursor.getInt(0)
            myWord.IdFolder = cursor.getInt(2)
            if(cursor.getString(3)!=null)
                myWord.Note = cursor.getString(3)
            if(cursor.getString(4)!= null)
                myWord.WebViewFull= cursor.getString(4)
            if(!cursor.getString(5).equals(""))
                myWord.VectorTypeWord= getVector(cursor.getString(5))
            array.add(myWord)
        }
        close()
        return array
    }
    fun isExisted(myWord: MyWordPro):Boolean{
        val query = "SELECT * FROM MyWordPro WHERE word = '${myWord.Word}'"
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
            val cursor = database.rawQuery("SELECT * FROM MyWordPro", null)
            var n= cursor.count
            close()
            return n
        }
    fun newId(): Int
    {
        openDataBase()
        val cursor = database.rawQuery("SELECT * FROM MyWordPro", null)
        if(cursor.count==0)
            return 0
        cursor.moveToLast()
        return cursor.getInt(0)+1
    }
    fun arrayListTypeToString(array: ArrayList<VecterTypeWord>):String{
        var string: StringBuilder = StringBuilder()
        for( i in array){
            string.append(i.toString()+"/")
        }
        string.deleteCharAt(string.length-1)
        return string.toString()
    }
    fun insertWord(myWord: MyWordPro) : Boolean
    {
        var result= false
        try {
            openDataBase()
            var value = ContentValues()
            value.put("id",myWord.Id)
            value.put("word", myWord.Word)
            value.put("idfolder",myWord.IdFolder)
            value.put("note",myWord.Note)
            value.put("webViewFull",myWord.WebViewFull)
            var n = database.insert("MyWordPro", null, value)
            if (n > 0)
                result = true
        }catch (e: Exception){
            e.printStackTrace()
        }
        finally {
            close()
        }
        return  result
    }
    fun updateWord(myWord: MyWordPro):Boolean{
        var result= false
        try {
            openDataBase()
            var value = ContentValues()
            value.put("word", myWord.Word)
            value.put("idfolder",myWord.IdFolder)
            value.put("note",myWord.Note)
            value.put("webViewFull",myWord.WebViewFull)
            var n = database.update("MyWordPro", value,"id=${myWord.Id}",null)
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
    fun deleteFolder(myFolder: MyFolder): Int{         //tra ve so luong loi
        openDataBase()
        var er = 0
        val cursor = database.rawQuery("SELECT * FROM MyWordPro", null)
        while (cursor.moveToNext()){
            if(cursor.getInt(3)==myFolder.Id)
                if(!deleteWord(cursor.getInt(0)))
                    er ++
        }
        close()
        return er
    }
    fun deleteWord(id: Int): Boolean {
        var result = false
        try {

            openDataBase()
            val rs = database.delete("MyWordPro", "id=" + id, null)
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