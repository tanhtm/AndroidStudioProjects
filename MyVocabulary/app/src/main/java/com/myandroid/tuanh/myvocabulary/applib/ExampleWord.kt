package com.myandroid.tuanh.myvocabulary.applib

/**
 * Created by Tanh on 9/7/2017.
 */
class ExampleWord {
    private  var word: String= "good"
    private  var string: String ="a good friend"
    private  var meanning: String ="một người bạn tốt"
    val Str: String
        get() {return  string}
    val Meaning: String
        get() = meanning
    constructor(word: String){
        this.word= word
    }
    constructor(word: String, string: String, meanning: String) {
        this.word = word
        this.string = string
        this.meanning = meanning
    }

    override fun toString(): String {
        return "$string:$meanning"
    }
    fun toString(str: String):String{
        return "$string ($meanning)"
    }
}