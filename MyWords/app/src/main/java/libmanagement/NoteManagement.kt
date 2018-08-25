package libmanagement

import applib.MyNote
import applib.MyWord

/**
 * Created by Tanh on 7/16/2017.
 */
class NoteManagement {
    private var listNote: ArrayList<MyNote> = arrayListOf()
    var List: ArrayList<MyNote>
        get() {
            return listNote
        }
        set(value) {
            listNote= value
        }
    constructor()
    fun addNote(note: MyNote){
            listNote.add(0,note)
            println("Thêm thành công")
    }
    fun addListNote(list: ArrayList<MyNote>){
        listNote= list
    } // them ca 1 danh sach tu
    fun length() :Int{
        return listNote.size
    }
    fun get(i :Int): MyNote
    {
        return listNote[i-1]
    }
    fun delNote(note: MyNote)
    {
        listNote.remove(note)
        print("Xoá thành công $note")
    }
    fun editNote(note: MyNote)
    {
        var new= listNote.get(note.Id-1)
        new= note // cơ chế Alias
        print("Sửa thàn công $new\n")
    }
    private fun existed(node: MyNote) :Boolean{
        if(listNote.contains(node))
            return true
        return false
    } // ktra xem  từ đã tồn tại chưa
    override fun toString(): String {
        var str="Hiện thư viện có ${length()} note.\n"
        var i= 1
        for(word in listNote )
            str+="${i++}: $word\n"
        return str
    }
    fun search(str: String):ArrayList<MyNote>
    {
        var array: ArrayList<MyNote> = ArrayList()
        var note: MyWord
        for (note in listNote)
        {
            if(note.keyword(str))
                array.add(note)
        }
        return array
    }
}