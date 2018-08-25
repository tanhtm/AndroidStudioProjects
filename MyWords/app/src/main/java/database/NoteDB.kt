package database

import Lib.MyDate
import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import applib.MyNote
import java.util.*

/**
 * Created by Tanh on 7/20/2017.
 */
class NoteDB(con : Context) :SQLiteDataController(con) {
    val List: ArrayList<MyNote>
        get() {
            val arrayList = java.util.ArrayList<MyNote>()
            openDataBase()
            val cursor = database.rawQuery("SELECT * FROM MyNote", null)
            while (cursor.moveToNext())
            {
                var note = MyNote(cursor.getInt(0),cursor.getString(1), MyDate(cursor.getString(2)))
                //note.IdFolder= cursor.getInt(3)
                arrayList.add(note)
            }
            arrayList.reverse()
            return arrayList
        }
    val Count: Int
        get() {
            openDataBase()
            val cursor = database.rawQuery("SELECT * FROM MyNote", null)
            var n= cursor.count
            close()
            return n
        }
    fun newId():Int
    {
        openDataBase()
        val cursor = database.rawQuery("SELECT * FROM MyNote", null)
        if(cursor.count==0)
            return 0
        cursor.moveToLast()
        return cursor.getInt(0)+1
    }
    fun insertNote(myNote: MyNote) : Boolean
    {
        var result= false
        try {
            openDataBase()
            var value = ContentValues()
            value.put("note", myNote.Note)
            value.put("day", myNote.Day)
            var n = database.insert("MyNote", null, value)
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
    fun updateNote(myNote: MyNote):Boolean{
        var result= false
        try {
            openDataBase()
            var value = ContentValues()
            value.put("note", myNote.Note)
            var n = database.update("MyNote", value,"id=${myNote.Id}",null)
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
    fun deleteNote(id: Int): Boolean {
        var result = false
        try {

            openDataBase()
            val rs = database.delete("MyNote", "id=" + id, null)
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