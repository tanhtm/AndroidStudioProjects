package libmanagement

import applib.MyWord

/**
 * Created by Tanh on 7/16/2017.
 */
class WordManagement{
    private var listWord: ArrayList<MyWord> = arrayListOf()
    var List: ArrayList<MyWord>
        get() {
            return listWord
        }
        set(value) {
            listWord= value
        }
    constructor()
    fun addWord(word: MyWord){
        if(existed(word))
            println("Từ đã tồn tại")
        else {
            //listWord.add(word)
            listWord.add(0,word)
            println("Thêm thành công")
        }
    }
    fun addListWord(list: ArrayList<MyWord>){
        listWord= list
    } // them ca 1 danh sach tu
    fun length() :Int{
        return listWord.size
    }
    fun get(i :Int):MyWord
    {
        return listWord[i-1]
    }
    fun delWord(word: MyWord)
    {
        listWord.remove(word)
        print("Xoá thành công từ $word")
    }
    fun editWord(word: MyWord)
    {
        var new= listWord.get(word.Id-1)
        new= word
        print("Sửa thàn công $word\n")
    }
    public fun existed(word: MyWord) :Boolean{
        if(listWord.contains(word))
            return true
        return false
    } // ktra xem  từ đã tồn tại chưa
    override fun toString(): String {
        var str="Hiện thư viện có ${length()} từ.\n"
        var i= 1
        for(word in listWord )
            str+="${i++}: $word\n"
        return str
    }
    fun search(str: String):ArrayList<MyWord>
    {
        var array: ArrayList<MyWord> = ArrayList()
        var word: MyWord
        for (word in listWord)
        {
            if(word.similarE(str))
                array.add(word)
        }
        return array
    }
}