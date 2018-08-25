package com.myandroid.tuanh.myvocabulary.database

import Lib.MyDate
import android.content.ContentValues
import android.content.Context
import android.database.SQLException
import com.myandroid.tuanh.myvocabulary.applib.MyFolder
import com.myandroid.tuanh.myvocabulary.applib.MyNote
import java.util.*

/**
 * Created by Tanh on 7/20/2017.
 */
class NoteDB(con : Context) : SQLiteDataController(con) {
    val List: ArrayList<MyNote>
        get() {
            val arrayList = java.util.ArrayList<MyNote>()
            openDataBase()
            val cursor = database.rawQuery("SELECT * FROM MyNote", null)

            if(cursor.count==0) {
                close()
                return arrayList
            }
            while (cursor.moveToNext())
            {
                var note = MyNote(cursor.getInt(0),cursor.getString(1), MyDate(cursor.getString(2)))
                note.IdFolder= cursor.getInt(3)
                arrayList.add(note)
            }
            arrayList.reverse()
            close()
            return arrayList
        }
    fun getListFromFolder(myFolder: MyFolder):ArrayList<MyNote>{
        if(myFolder.Id==0)
            return List
        val array: ArrayList<MyNote> = ArrayList()
        val query = "SELECT * FROM MyNote WHERE idfolder = ${myFolder.Id}"
        openDataBase()
        val cursor = database.rawQuery(query,null)
        if(cursor.count==0) {
            close()
            return array
        }
        while (cursor.moveToNext()){
            var note = MyNote(cursor.getInt(0),cursor.getString(1).toString(), MyDate(cursor.getString(2)))
            note.IdFolder= cursor.getInt(3)
            array.add(note)
        }
        close()
        return array
    }
    fun getListSearch(string: String , idFolder: Int):ArrayList<MyNote>{
        val array: ArrayList<MyNote> = ArrayList()
        var query=""
        if (idFolder!=0)
            query = "SELECT * FROM MyNote WHERE idfolder = $idFolder"
        else
            query= "SELECT * FROM MyNote"
        openDataBase()
        val cursor = database.rawQuery(query,null)
        if(cursor.count==0) {
            close()
            return array
        }
        while (cursor.moveToNext()){
            var n= cursor.getString(1).toString()
            n= n.toLowerCase()
            if (n.contains(string.toLowerCase())){
                var note = MyNote(cursor.getInt(0),cursor.getString(1).toString(), MyDate(cursor.getString(2)))
                note.IdFolder= cursor.getInt(3)
                array.add(note)
            }
        }
        close()
        return array
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
            value.put("id",myNote.Id)
            value.put("note", myNote.Note)
            value.put("day", myNote.Day)
            value.put("idfolder",myNote.IdFolder)
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
    fun deleteFolder(myFolder: MyFolder): Int{         //tra ve so luong loi
        openDataBase()
        var er = 0
        val cursor = database.rawQuery("SELECT * FROM MyNote", null)
        while (cursor.moveToNext()){
            if(cursor.getInt(3)==myFolder.Id)
                if(!deleteNote(cursor.getInt(0)))
                    er ++
        }
        return er
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