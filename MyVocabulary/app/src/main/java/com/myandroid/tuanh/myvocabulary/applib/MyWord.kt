package com.myandroid.tuanh.myvocabulary.applib

/**
 * Created by Tanh on 7/14/2017.
 */
class MyWord{
    private var id: Int= 0
    private var word: String= "hello"
    private var pronunciation: String= ""
    private var typeOfWord: String="v"
    private var meaning: String= "xin chào"
    private var note: String= "similar to \"Hi\""
    private var idFolder: Int = 0
    var Id: Int
        get() {
            return id
        }
        set(value) {
            id= value
        }
    var Word: String
        get() {
            return word
        }
        set(value) {
            word= value.toLowerCase().trim()
        }
    var Pronunciation: String
        get() {
            return pronunciation
        }
        set(value) {
            pronunciation= value
        }
    var TypeOfWord: String
        get() {
            return typeOfWord
        }
        set(value) {
            typeOfWord= value.toLowerCase()
        }
    var Meaning: String
        get() {
            return meaning
        }
        set(value) {
            meaning= value.toLowerCase().trim()
        }
    var Note: String
        get() {
            return note
        }
        set(value) {
            note= value
        }
    var IdFolder: Int
        get() = idFolder
        set(value) {
            idFolder= value
        }
    constructor()
    constructor(word: String, pronunciation: String, meaning: String,note: String){
        Word= word.toLowerCase().trim()
        Pronunciation= pronunciation
        Meaning= meaning.toLowerCase().trim()
        Note= note.toLowerCase().trim()
    }
    override fun toString(): String {
        var str= word
        if(pronunciation!="")
            str+= "/$pronunciation/"
        if (typeOfWord!="") {
            when(typeOfWord){
                "noun"-> str+=" (n) "
                "verb"-> str+=" (v) "
                else-> str+=" ($typeOfWord )"
            }
        }
        str+=": $meaning"
        return str
    }// hàm similar để dùng cho việc tìm kiếm từ AV và VA trong ứng dụng
    fun similarE(englishWord: String): Boolean {
        return word.contains(englishWord)
    }// so với word - gần giống nhau về mặt kí tự eg: hello và hell , hi và his , h và home (tập con)
    fun similarV(vietnameseWord: String): Boolean {
        return meaning.contains(vietnameseWord)
    }// so với meaning - gần giống nhau về mặt kí tự eg: nhà và nh(tập con)

    override fun equals(other: Any?): Boolean {
        other as MyWord
        if(other.Word.compareTo(Word,ignoreCase = true)== 0) // kt giống nhau ko phân biệt chữ HOA
            return true
        return false
    }
    fun toMyWordPro(): MyWordPro{
        var word = MyWordPro(word)
        word.WebViewFull=note
        return word
    }
}