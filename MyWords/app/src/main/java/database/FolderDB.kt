package database
import Lib.MyDate
import android.content.ContentValues
import android.content.Context
import applib.MyFolder
import java.sql.SQLException

/**
 * Created by Tanh on 8/11/2017.
 */
class FolderDB (con : Context) :SQLiteDataController(con) {
    val List: ArrayList<MyFolder>
        get() {
            val arrayList = java.util.ArrayList<MyFolder>()
            openDataBase()
            val cursor = database.rawQuery("SELECT * FROM MyFolder", null)
            while (cursor.moveToNext()) {
                var folder = MyFolder(cursor.getString(1))
                folder.Id = cursor.getInt(0)
                folder.Day = MyDate(cursor.getString(2).toString())
                arrayList.add(folder)
            }
            arrayList.reverse()
            return arrayList
        }
    val Count: Int
        get() {
            openDataBase()
            val cursor = database.rawQuery("SELECT * FROM MyFolder", null)
            var n = cursor.count
            close()
            return n
        }

    fun newId(): Int {
        openDataBase()
        val cursor = database.rawQuery("SELECT * FROM MyFolder", null)
        if (cursor.count == 0)
            return 0
        cursor.moveToLast()
        return cursor.getInt(0) + 1
    }

    fun insertFolder(myFolder: MyFolder): Boolean {
        var result = false
        try {
            openDataBase()
            var value = ContentValues()
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