package com.myandroid.tuanh.myvocabulary.gameofvoca

import Lib.RandomArray
import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.SharedPreferences
import android.view.View
import android.view.Window
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.Toast
import com.myandroid.tuanh.myvocabulary.R
import com.myandroid.tuanh.myvocabulary.applib.MyWord
import com.myandroid.tuanh.myvocabulary.applib.MyWordPro
import java.util.*
import kotlin.collections.ArrayList

/**
* Created by Tanh on 7/15/2017.
*/
class CorrectCharacters(private var activity: Activity, array: ArrayList<Int>, arrayChoose: ArrayList<Int>, arrayWord: ArrayList<MyWord>) {
    private var arrayId: ArrayList<Int> = array // id button : các ô chữ cái
    private var word: MyWord= MyWord() // từ đc chọn để chơi
    private var idWord: Int= 0 // id cua word trong dataword
    private var dataWord: ArrayList<MyWord> = arrayWord
    private var arrayIdChoose: ArrayList<Int> = arrayChoose
    private var arrayIdShow: ArrayList<Int> = ArrayList() // danh sach id cac o button da dc show len man hinh
    private var ques: String= String()
    private var numTrue: Int=0
    private var numFalse = 10
    private var point= 0

    @SuppressLint("SetTextI18n")
    fun createButton(){ // khởi tạo các ô chữ hiện lên màn hình de doan
        val buttonT: Button= activity.findViewById(R.id.buttonTurn)
        buttonT.text="10"
        val buttonPoint: Button= activity.findViewById(R.id.buttonPoint)
        val buttonHint: Button= activity.findViewById(R.id.buttonHint)
        buttonHint.text= "Gợi ý"
        buttonHint.setOnClickListener {
            buttonHint.text=getHint(word.toMyWordPro())
            Toast.makeText(activity,"Bạn bị trừ đi 5 điểm",Toast.LENGTH_LONG).show()
            point-= 5
            buttonPoint.text= point.toString()
            buttonHint.setOnClickListener {  }
        }
        idWord= Random().nextInt(dataWord.size)
        word= dataWord[idWord]
        var szWord= word.Word.length // kich thuoc chu
        var tp=""
        when(word.TypeOfWord)
        {
            "noun"-> tp="danh từ"
            "verb"-> tp="động từ"
            "adj"-> tp="tính từ"
            else-> tp="trạng từ"
        }
        ques= "Đây là $tp tiếng anh có $szWord chữ cái"
        val buttonques: Button= activity.findViewById(R.id.buttonQues)
        buttonques.text= ques
        if(szWord<= 10){
            if(szWord%2!=0){ // cân bằng ô chữ
                val button: Button = activity.findViewById(arrayId[9])
                button.visibility= View.GONE
            }
            val arrayBetween : ArrayList<Int> = ArrayBetween(10, szWord).array() // tra ve mang nam giua tinh tu 1
            for (i in arrayBetween){
                val button: Button = activity.findViewById(arrayId[i-1]) // i-1 vi tinh tu 0
                button.text= "" // tra lai o rong
                button.visibility= View.VISIBLE
                arrayIdShow.add(arrayId[i-1])
                val layout2: LinearLayout = activity.findViewById(R.id.layoutword2);
                layout2.visibility= View.GONE
            }
        }else{
            if(szWord%2!=0){ // cân bằng ô chữ
                var button: Button = activity.findViewById(arrayId[19])
                button.visibility= View.GONE
            }
            for (i in 1..10){
                var button: Button = activity.findViewById(arrayId[i-1]) // i-1 vi tinh tu 0
                button.visibility= View.VISIBLE
                button.text= "" // tra lai o rong
                arrayIdShow.add(arrayId[i-1])
                var layout2: LinearLayout = activity.findViewById(R.id.layoutword2);
                layout2.visibility= View.VISIBLE
            }
            var arrayBetween : ArrayList<Int> = ArrayBetween(10, szWord - 10).array() // tra ve mang nam giua tinh tu 1
            for (i in arrayBetween){
                var button: Button = activity.findViewById(arrayId[i+10-1]) // i+10-1 vi tinh tu 0 , + 10 vi n bat dau tu button thu 10
                button.visibility= View.VISIBLE
                button.text= "" // tra lai o rong
                arrayIdShow.add(arrayId[i+10-1])
            }
        }
    }
    fun getHint(word: MyWordPro):String{
        var ques=""
        var listExam = word.getExample()
        var example= listExam[Random().nextInt(listExam.size)]
        while(!example.Str.contains(" ${word.Word} "))
            example= listExam[Random().nextInt(listExam.size)]
        var list = example.Str.split(word.Word)
        var j=0
        for (i in list){
            ques+=i
            if(j!=list.size-1)
                ques+="____"
            j++
        }
        return "\"$ques\"\n(${example.Meaning})"
    }
    @SuppressLint("SetTextI18n", "ApplySharedPref")
    fun createButtonChoose() : Int{
        var poi = 0
        var charArray: CharArray="QWERTYUIOPASDFGHJKLZXCVBNM".toCharArray()
        var j=0
        for (i in arrayIdChoose){
            var button: Button= activity.findViewById(i)
            button.animation= AnimationUtils.loadAnimation(activity,R.anim.sacle)
            button.visibility= View.VISIBLE // tra la tat ca nut bi an
            button.text= charArray[j].toString()
            button.setOnClickListener {
                var button2: Button= activity.findViewById(R.id.buttonNoti)
                button2.text= checkQuess(button.text.toString()[0])
                button2.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.ssss))
                button.visibility= View.INVISIBLE
                ///
                if (numTrue==word.Word.length){
                    gameExtra()
                }
                if(numFalse==0){
                    var dialog = Dialog(activity)
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
                    dialog.setContentView(R.layout.dialog_cc_lose)
                    var buttonAns:Button= dialog.findViewById(R.id.buttonAns)

                    var sharedPreferences: SharedPreferences= activity.getSharedPreferences("pointgame",0);
                    var old= sharedPreferences.getInt("point",0)
                    buttonAns.text=" Bạn có được $point điểm\nĐáp án là:\n $word"
                    poi= point
                    if(poi> old) {
                        buttonAns.text = " Bạn có được $point điểm\nĐáp án là:\n $word\nBạn có điểm cao mới"
                        val editor = sharedPreferences.edit()
                        editor.putInt("point", point)
                        editor.commit()
                    }
                    point= 0
                    var buttonPoint: Button= activity.findViewById(R.id.buttonPoint)
                    buttonPoint.text= point.toString()
                    runAgain()
                    dialog.show()
                    var buttonNew: Button= dialog.findViewById(R.id.buttonNewGame)
                    var buttonQuit: Button= dialog.findViewById(R.id.buttonQuit)
                    buttonNew.setOnClickListener {

                        dialog.hide()
                    }
                    buttonQuit.setOnClickListener {
                        dialog.dismiss()
                        activity.finish()
                    }
                }
            }
            j++
        }
        return poi
    }// khơi tạo button chọn chữ cái
    @SuppressLint("SetTextI18n")
    private fun checkQuess(char: Char):String{
        var wordToUpperCase:String = word.Word.toUpperCase()
        var j= 0
        for(i in 0 until wordToUpperCase.length){
            if (wordToUpperCase[i].equals(char)){
                var button: Button= activity.findViewById(arrayIdShow[i])
                button.startAnimation(AnimationUtils.loadAnimation(activity,R.anim.sacle))
                button.text= char.toString()
                j++
            }
        }
        numTrue+= j
        if(j== 0) {
            numFalse--
            val buttonT: Button= activity.findViewById(R.id.buttonTurn)
            buttonT.text= numFalse.toString()
            return "Rất tiếc! Không có chữ cái $char nào"
        }
        else
            return "Chúc mừng bạn! Có $j chữ cái $char "
    }// chuyền vào chữ cái muốn đoán va kiem tra va tra ve thong
    @SuppressLint("SetTextI18n")
    private fun gameExtra() // game bổ sung - Đoán nghĩa , chuyền cao list id Radiobutton
    {
        var dialog :Dialog= Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_cc)
        var arrayId: ArrayList<Int> = ArrayList()
        var buttonques: Button= dialog.findViewById(R.id.buttonQues2)
        buttonques.text= "Nghĩa tiếng việt của từ ${word.Word}: "
        ///
        arrayId.add(R.id.radioButtonA)
        arrayId.add(R.id.radioButtonB)
        arrayId.add(R.id.radioButtonC)
        arrayId.add(R.id.radioButtonD)
        ///
        var idList = RandomArray(dataWord.size-1, 4).rdArray() // lấy ra 4 số ngẫu nhiên
        var id = idList[Random().nextInt(4)] // lấy ra id ngẫu nhiên trong 4 id lấy đc ở trên - đây là id word thay the
        var j = 0
        if(idList.contains(idWord)) // kiem tra su ton tai cua idWord trong idList
            for (i in idList)
            {
                var radio: RadioButton = dialog.findViewById(arrayId[j])
                radio.text= dataWord[i].Meaning
                radio.setOnClickListener {
                    checkGameExtra(i)
                    dialog.dismiss()
                }
                j++
            }
        else
            for (i in idList) {
                if (i != id) {
                    var radio: RadioButton = dialog.findViewById(arrayId[j])
                    radio.text = dataWord[i].Meaning
                    radio.setOnClickListener {
                        checkGameExtra(i)
                        dialog.dismiss()
                    }
                } else {
                    var radio: RadioButton = dialog.findViewById(arrayId[j])
                    radio.text = word.Meaning
                    radio.setOnClickListener {
                        checkGameExtra(idWord)
                        dialog.dismiss()
                    }
                }
                j++
            }
        dialog.show()
    }
    @SuppressLint("SetTextI18n")
    private fun runAgain() // chay game moi
    {
        numTrue= 0
        numFalse= 10
        arrayIdShow.clear()
        for (i in arrayId)
            activity.findViewById<Button>(i).visibility= View.INVISIBLE // tra lai tat ca cac o bi ẩn
        var button2: Button= activity.findViewById(R.id.buttonNoti)
        button2.text="Chọn chữ cái bạn muốn đoán"
        createButton()
        createButtonChoose()
    }
    private fun checkGameExtra(iChoose: Int)
    {
        var buttonPoint: Button= activity.findViewById(R.id.buttonPoint)
        var dialog : AlertDialog.Builder= AlertDialog.Builder(activity)
        dialog.setIcon(R.drawable.dict)
        dialog.setPositiveButton("Tiếp tục", DialogInterface.OnClickListener{ dialogInterface: DialogInterface, i: Int ->
            runAgain()
        })
        if(iChoose== idWord) {
            Toast.makeText(activity, "Đúng !", Toast.LENGTH_SHORT).show()
            point+= 10
            dialog.setTitle("Đúng rồi")
            dialog.setMessage("Đáp án là: "+ word.toString()+"\nBạn có thêm 10 điểm")
        }
        else {
            Toast.makeText(activity, "Sai !", Toast.LENGTH_SHORT).show()
            dialog.setTitle("Sai rồi")
            point +=5
            dialog.setMessage("Đáp án là: "+ word.toString()+"\nBạn có thêm 5 điểm")
        }
        buttonPoint.text= point.toString()
        dialog.show()
    }
}