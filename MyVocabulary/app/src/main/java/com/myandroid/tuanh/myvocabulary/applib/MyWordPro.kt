@file:Suppress("UNREACHABLE_CODE")

package com.myandroid.tuanh.myvocabulary.applib

import android.util.Log
import java.util.*

/**
* Created by Tanh on 8/23/2017.
*/
class MyWordPro(word: String) {

    private var id = 0
    private var word="" // từ
    private var pron=""
    private var arrayListType: ArrayList<VecterTypeWord> = ArrayList() // list các cặp loại từ và nghĩa ,ghi chú
    private var idFolder: Int = 0
    private var note=""
    private var webViewFull=""
    private var vectorTypeWord: VecterTypeWord= VecterTypeWord() // Nghiã chính
    var  VectorTypeWord: VecterTypeWord
        get() {return vectorTypeWord}
        set(value) {
            vectorTypeWord= value
        }
    val ListExample: ArrayList<ExampleWord>
        get() {
            return getExample()
        }
    var Id: Int
        get() {
            return id
        }
        set(value) {
            id= value
        }
    var Word: String
        get() {return word}
        set(value) {
            word= value.toLowerCase().trim()
        }
    var IdFolder: Int
        get() = idFolder
        set(value) {
            idFolder= value
        }
    var ListType: ArrayList<VecterTypeWord>
        get() {return arrayListType}
        set(value) {
            arrayListType= value
        }
    var Pronunciation :String
        get() {return pron}
        set(value) {
            if (!value.equals("")&&value[0]=='[')
                pron= value.substring(1,value.length-2)
            else
                pron= value
        }
    var Note: String
        get() {
            return note
        }
        set(value) {
            note= value
        }
    var WebViewFull: String
        get() {
            return webViewFull
        }
        set(value) {
            webViewFull= value
            if(!webViewFull.equals(""))
                getMyWordFromWeb()
        }
    fun addType(typeWord: VecterTypeWord){
        typeWord.Word= word
        arrayListType.add(typeWord)
    }
    fun showWeb():String{
        var classWF1="<style>\n" +
                "div.ub {\n" +
                //"background-color:black;\n" +
                "font-size:35px;"+
                "font-weight: bold;"+
                "color:black;\n" +
                "margin:10px;"+
                "}\n" +
                "</style>"
        var classM="<style>\n" +
                "div.m {\n" +
                "font-size:25px;"+
                "color:red;\n" +
                "font-style: italic;"+
                "margin-top:10px;"+
                "margin-left:20px;"+
                "margin-bottom:5px;"+
                "}\n" +
                "</style>"
        var classE="<style>\n" +
                "div.e {\n" +
                "color:blue;\n" +
                "margin-left:30px;"+
                "}\n" +
                "</style>"
        var classEm="<style>\n" +
                "div.em {\n" +
                "color:black;\n" +
                "margin-left:35px;"+
                "font-style: italic;"+
                "}\n" +
                "</style>"
        return "$classWF1$classM$classE$classEm<big><big>$webViewFull</big></big>"
    }
    override fun toString(): String {
        var str= word+" "
            if (pron != "")
                str += "[$pron] "
            when (vectorTypeWord.TypeOfWord) {
                "noun" -> str += "(n)"
                "verb" -> str += "(v)"
                "pronouns" -> str += "(pro)"
                "preposition" -> str += "(pre)"
                "adjective" -> str += "(adj)"
                "adverb" -> str += "(adv)"
                "interjections" -> str += "(int)"
                "conjunctions" -> str += "(con)"
                else-> str+= "("+vectorTypeWord.TypeOfWord+")"
            }
            str += ": ${vectorTypeWord.Meaning}"
        return  str
    }
    fun getInfo():String{
        var str= ""
        for (i in arrayListType){
            str+= "*"+i.toStringMax()
            if(!i.equals(arrayListType.get(arrayListType.size-1)))
                str+="\n"
        }
        return str
    }
    fun toMyWord(): MyWord{
        var myW = MyWord(word,pron,vectorTypeWord.Meaning,WebViewFull)
        myW.Pronunciation= vectorTypeWord.TypeOfWord;
        return myW
    }
    private fun getMyWordFromWeb() {
        var arrayLists = webViewFull.split("</div>")
        var str = arrayLists[0]
        str = str.substring(str.indexOf(">")+2, str.indexOf("]"))
        pron = str
        var arrayVetor : ArrayList<VecterTypeWord> = ArrayList()
        var type= ""
        var mean= ""
        var h=0;
        for (i in arrayLists) {
            if (i.contains("class=\"ub\"")) {
                if(i.contains("tính từ"))
                    type = "adjective"
                if(i.contains("danh từ"))
                    type = "noun"
                if(i.contains("động từ"))
                    type = "verb"
                if(i.contains("phó từ"))
                    type = "adverb"
                if(i.contains("đại từ"))
                    type = "pronouns"
                if(i.contains("giới từ"))
                    type = "preposition"
                if(i.contains("liên từ"))
                    type = "conjunctions"
                if(i.contains("thán từ"))
                    type = "interjections"
                //////////////// Xử lí loại từ
                var j=h+1
                if (j<arrayLists.size) {
                    if (arrayLists[j].contains("class=\"m\"")) {
                        mean = arrayLists[j]
                        if(!mean.contains(";"))
                            mean = mean.substring(mean.indexOf(">") + 1)
                        else
                            mean = mean.substring(mean.indexOf(">") + 1,mean.indexOf(";"))
                        var vector = VecterTypeWord(type, mean)
                        vector.Word = word
                        arrayVetor.add(vector)
                    }
                }
            }
            h++
        }
        arrayListType= arrayVetor
    }
    fun getExample():ArrayList<ExampleWord>{
        var arrayLists = webViewFull.split("</div>")
        var listExample= java.util.ArrayList<ExampleWord>()
        var j=0
        for (i in arrayLists){
            if (i.contains("class=\"e\"")){
                var strE = i
                strE = strE.substring(strE.indexOf(">") + 1)
                var strEm = arrayLists[j+1]
                strEm = strEm.substring(strEm.indexOf(">") + 1)
                var example = ExampleWord(word, strE, strEm)
                listExample.add(example)
                Log.d("EXAM", example.toString())
            }
            j++
        }
        return listExample
    }
    fun getListMyWordFromListPro(arrayList: ArrayList<MyWordPro>):ArrayList<MyWord> {
        var array: ArrayList<MyWord> = ArrayList()
        for (i in arrayList){
            array.add(i.toMyWord())
        }
        return array
    }
    init {
        this.word = word.toLowerCase().trim()
    }
}