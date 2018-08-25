package com.myandroid.tuanh.myvocabulary.database
import Lib.MyDate
import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import com.myandroid.tuanh.myvocabulary.applib.MyFolder
import java.sql.SQLException

/**
 * Created by Tanh on 8/11/2017.
 */
class FolderDB (con : Context) : SQLiteDataController(con) {
    val List: ArrayList<MyFolder>
        @SuppressLint("Recycle")
        get() {
            val arrayList = ArrayList<MyFolder>()
            openDataBase()
            val cursor = database.rawQuery("SELECT * FROM MyFolder", null)
            if(cursor.count==0){
                close()
                return arrayList
            }
            while (cursor.moveToNext()) {
                var folder = MyFolder(cursor.getString(1))
                folder.Id = cursor.getInt(0)
                folder.Day = MyDate(cursor.getString(2).toString())
                arrayList.add(folder)
            }
            close()
            return arrayList
        }
    val Count: Int
        @SuppressLint("Recycle")
        get() {
            openDataBase()
            val cursor = database.rawQuery("SELECT * FROM MyFolder", null)
            var n = cursor.count
            close()
            return n
        }
    fun isExisted(myFolder: MyFolder):Boolean{
        val query = "SELECT * FROM MyFolder"
        openDataBase()
        val cursor = database.rawQuery(query,null)
        while (cursor.moveToNext()){
            var name= cursor.getString(1).toLowerCase()
            if(name.equals(myFolder.Name.toLowerCase())) {
                close()
                return true
            }
        }
        close()
        return false;
    }
    @SuppressLint("Recycle")
    fun getFolder(id : Int): MyFolder{
        openDataBase()
        val cursor = database.rawQuery("SELECT * FROM MyFolder", null)
        while (cursor.moveToNext()){
            if(cursor.getInt(0)==id){
                var folder = MyFolder(cursor.getString(1))
                folder.Id = cursor.getInt(0)
                folder.Day = MyDate(cursor.getString(2).toString())
                close()
                return folder
            }
        }
        close()
        return MyFolder("Tất cả")
    }
    @SuppressLint("Recycle")
    fun newId(): Int {
        openDataBase()
        val cursor = database.rawQuery("SELECT * FROM MyFolder", null)
        if (cursor.count == 0)
            return 0
        cursor.moveToLast()
        return cursor.getInt(0) + 1
    }
    @SuppressLint("Recycle")
    fun insertFolder(myFolder: MyFolder): Boolean {
        var result = false
        try {
            openDataBase()
            var value = ContentValues()
            value.put("id",myFolder.Id)
            value.put("name", myFolder.Name)
            value.put("day", myFolder.Day.toString())
            var n = database.insert("MyFolder", null, value)
            if (n > 0)
                result = true
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            close()
        }
        return result
    }

    fun updateFolder(myFolder: MyFolder): Boolean {
        var result = false
        try {
            openDataBase()
            var value = ContentValues()
            value.put("name", myFolder.Name)
            var n = database.update("MyFolder", value, "id=${myFolder.Id}", null)
            if (n > 0)
                result = true
        } catch (e: SQLException) {
            e.printStackTrace()
        } finally {
            close()
        }
        return result
    }

    fun deleteFolder(id: Int): Boolean {
        var result = false
        try {

            openDataBase()
            val rs = database.delete("MyFolder", "id=" + id, null)
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