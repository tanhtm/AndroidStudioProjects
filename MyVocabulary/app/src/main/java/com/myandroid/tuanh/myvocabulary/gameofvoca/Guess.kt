package com.myandroid.tuanh.myvocabulary.gameofvoca

import Lib.RandomArray
import com.myandroid.tuanh.myvocabulary.applib.MyWord
import com.myandroid.tuanh.myvocabulary.applib.MyWordPro
import java.util.*
import kotlin.collections.ArrayList

/**
 * Created by Tanh on 7/25/2017.
 */
class Guess {
    //private var point: Int = 0 // số từ đã đoán đúng
    private var wordList: ArrayList<MyWord> = ArrayList() // danh sách từ có trong cơ sở dữ liệu
    private var length: Int = 0 // số lượng từ trong wordList
    private var idList: ArrayList<Int> = ArrayList() //danh sách 4 id đc chọn random để chơi game
    private var id: Int = 0 //id của đc chọn để là đáp án . đc chọn ngẫu nhiên trong 4 số trong idList
    private var iChoose: Int= 99
    private var type: Int = 0 // type=0 là chơi đoán nghĩa TViệt , type=1 là đoán từ T.Anh
    private var strAns =""
    val Type : Int
        get() = type
    constructor(wordList: ArrayList<MyWord>)
    {
        this.wordList= wordList
        length= wordList.size
    }
    fun run():Vector<String>// trả về danh sach casc ddasp an va caau hoi, khởi tạo game
    {
        type= Random().nextInt(4)
        idList = RandomArray(length, 4).rdArray() // lấy ra 4 số ngẫu nhiên trong length số
        id = Random().nextInt(4) // lấy ra id ngẫu nhiên lam dap an (0,1,2,3)
        var vector: Vector<String> = Vector()
        var wordA: MyWord = wordList[idList[0]]
        var wordB: MyWord = wordList[idList[1]]
        var wordC: MyWord = wordList[idList[2]]
        var wordD: MyWord = wordList[idList[3]]
        // Khởi tạo câu hỏi
        if(type==0) {
            vector.add("[Đoán nghĩa]\n\t\t\"${wordList[idList[id]].Meaning}\"")
            vector.add(wordA.Word)
            vector.add(wordB.Word)
            vector.add(wordC.Word)
            vector.add(wordD.Word)


        }
        if(type==1){
            vector.add("[Đoán nghĩa]\n\t\t\"${wordList[idList[id]].Word}\"")
            vector.add(wordA.Meaning)
            vector.add(wordB.Meaning)
            vector.add(wordC.Meaning)
            vector.add(wordD.Meaning)
        }
        if (type==2||type==3){
            var myPro= wordList[idList[id]].toMyWordPro()
            vector.add(getQuesType2(myPro))
            vector.add(wordA.Word)
            vector.add(wordB.Word)
            vector.add(wordC.Word)
            vector.add(wordD.Word)
        }
        return vector
    }
    fun getQuesType2(word: MyWordPro):String{
        var ques=""
        var listExam = word.getExample()
        var example= listExam[Random().nextInt(listExam.size)]
        while(!example.Str.contains(" ${word.Word} "))
            example= listExam[Random().nextInt(listExam.size)]
        var list = example.Str.split(" ${word.Word} ")
        var j=0
        for (i in list){
            ques+=i
            if(j!=list.size-1)
                ques+=" ____ "
            j++
        }
        return "[Hoàn thiện câu]\n\t\" $ques \"  (${example.Meaning})"
    }
    fun setAns(idAns: Int){
        iChoose= idAns
    }
    fun check() :Boolean
    {
        if(iChoose==id)
            return true
        return false
    }
    fun getAns(): Int
    {
        return id
    }

}