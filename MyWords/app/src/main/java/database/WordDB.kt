package database

import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import applib.MyWord
import java.lang.Exception
import java.util.*

/**
 * Created by Tanh on 7/20/2017.
 */

class WordDB(con: Context) : SQLiteDataController(con) {
    val List: ArrayList<MyWord>
        get() {
            val arrayList = ArrayList<MyWord>()
            openDataBase()
            val cursor = database.rawQuery("SELECT * FROM MyWord", null)
            var size= cursor.count
            while (cursor.moveToPosition(size-1)) {
                val myWord = MyWord()
                myWord.Word= cursor.getString(1).toString()
                if(cursor.getString(2)== null)
                    myWord.Pronunciation= ""
                else
                    myWord.Pronunciation= cursor.getString(2).toString()
                myWord.Meaning=cursor.getString(3).toString()
                if(cursor.getString(4)== null)
                    myWord.Note= ""
                else
                    myWord.Note= cursor.getString(4).toString()
                if(cursor.getString(5)== null)
                    myWord.TypeOfWord= ""
                else
                    myWord.TypeOfWord= cursor.getString(5).toString()
                myWord.Id= cursor.getInt(0)
                myWord.IdFolder= cursor.getInt(6)
                arrayList.add(myWord)
                size--
            }
            return arrayList
        }
    val Count: Int
        get() {
            openDataBase()
            val cursor = database.rawQuery("SELECT * FROM MyWord", null)
            var n= cursor.count
            close()
            return n
        }
    fun newId(): Int
    {
        openDataBase()
        val cursor = database.rawQuery("SELECT * FROM MyWord", null)
        if(cursor.count==0)
            return 0
        cursor.moveToLast()
        return cursor.getInt(0)
    }
    fun insertWord(myWord: MyWord) : Boolean
    {
        var result= false
        try {
            openDataBase()
            var value = ContentValues()
            value.put("word", myWord.Word)
            value.put("pronun", myWord.Pronunciation)
            value.put("mean", myWord.Meaning)
            value.put("note", myWord.Note)
            value.put("type",myWord.TypeOfWord)
            value.put("idfolder",myWord.IdFolder)
            var n = database.insert("MyWord", null, value)
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
    fun updateWord(myWord: MyWord):Boolean{
        var result= false
        try {
            openDataBase()
            var value = ContentValues()
            value.put("word", myWord.Word)
            value.put("pronun", myWord.Pronunciation)
            value.put("mean", myWord.Meaning)
            value.put("note", myWord.Note)
            value.put("type",myWord.TypeOfWord)
            value.put("idfolder",myWord.IdFolder)
            var n = database.update("MyWord", value,"id=${myWord.Id}",null)
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

    fun deleteWord(id: Int): Boolean {
        var result = false
        try {

            openDataBase()
            val rs = database.delete("MyWord", "id=" + id, null)
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
