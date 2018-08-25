package gameofvoca

import Lib.RandomArray
import applib.MyWord
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
    constructor(wordList: ArrayList<MyWord>)
    {
        this.wordList= wordList
        length= wordList.size
    }
    fun run():Vector<String>// trả về danh sach casc ddasp an va caau hoi, khởi tạo game
    {
        type= Random().nextInt(2)
        idList = RandomArray(length, 4).rdArray() // lấy ra 4 số ngẫu nhiên trong length số
        id = Random().nextInt(4) // lấy ra id ngẫu nhiên lam dap an (0,1,2,3)
        var vector: Vector<String> = Vector()
        var wordA: MyWord = wordList[idList[0]]
        var wordB: MyWord = wordList[idList[1]]
        var wordC: MyWord = wordList[idList[2]]
        var wordD: MyWord = wordList[idList[3]]
        // Khởi tạo câu hỏi
        if(type==0) {
            vector.add("Từ tiếng anh có nghĩa \"${wordList[idList[id]].Meaning}\" là ?")
            vector.add(wordA.Word)
            vector.add(wordB.Word)
            vector.add(wordC.Word)
            vector.add(wordD.Word)

        }else{
            vector.add("Nghĩa tiếng việt của từ \"${wordList[idList[id]].Word}\" là: ")
            vector.add(wordA.Meaning)
            vector.add(wordB.Meaning)
            vector.add(wordC.Meaning)
            vector.add(wordD.Meaning)
        }
        return vector
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
}