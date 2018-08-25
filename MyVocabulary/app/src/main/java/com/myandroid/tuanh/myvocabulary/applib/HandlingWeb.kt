package com.myandroid.tuanh.myvocabulary.applib

import android.util.Log

/**
 * Created by Tanh on 8/28/2017.
 */
class HandlingWeb {
    private var  myWord: MyWordPro = MyWordPro("")
    private var strWeb="" // chuỗi web nguyên bản copy trên html

    var MyWord: MyWordPro
        get() {return myWord}
        set(value) {myWord= value}
    constructor(word: String, strWeb: String) {
        myWord.Word=word
        this.strWeb = strWeb
        setWebToWord()
    }
    private fun  setWebToWord(){ // lọc web loại bỏ chi tiết thừa rồi set vào MyWord
        var string = strWeb
        var str=""
        var j=""
        var arrayList = string.split("</div>")
        for (i in arrayList){
            if(checkClass(i)){
                if (i.contains("<a href"))
                    j= removeLinkWeb(i)
                else
                    j=i
                if (i.contains("<span>"))
                    j= removeSpan(j)
                str += j + "</div>\n"
            }
        }
        Log.d("SETWEB",str)
        myWord.WebViewFull= str
    }
    fun removeSpan(str: String):String{
        var string =""
        var listString= str.split("<span>")
        for (i in listString){
            string += i
        }
        listString= string.split("</span>")
        string=""
        for (i in listString){
            string += i
        }
        Log.d("Web",string)
        return string
    }
    fun checkClass(string: String):Boolean{
        if (string.contains("<div id=\"partofspeech_100\"><div class=\"ub\""))
            return false
        if(string.contains("class=\"p5l fl cB\"")||string.contains("class=\"ub\"")||string.contains("class=\"m\"")||string.contains("class=\"e\"")||string.contains("class=\"em\""))
            return true
        return false
    }
    fun removeLinkWeb(str:String): String{
        var string = str
        while (string.contains("href=\"/hoc-tieng-anh/tu-dien/lac-viet/")) {
            var string1 = string.substring(string.indexOf("<a href=\"/hoc-tieng-anh/tu-dien/lac-viet/"), string.indexOf(".html\">") + 7)
            var array = string.split(string1)
            string=""
            for (i in array){
                string+= i
            }
        }
        while (string.contains("</a>")) {
            var array = string.split("</a>")
            string = ""
            for (i in array) {
                string += i
            }
        }
        return  string
    }
}