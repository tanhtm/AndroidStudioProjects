package com.myandroid.tuanh.myvocabulary.applib

/**
 * Created by Tanh on 8/23/2017.
 */
class VecterTypeWord {
    private var word=""
    private var type: String =""
    private var mean: String =""
    private  var example: ExampleWord = ExampleWord(word)
    var Example: ExampleWord
        get() {return  example}
        set(value) {example= value }
    var Word: String
        get() {return word}
        set(value) {word= value}
    var TypeOfWord: String
        get() {
            return type
        }
        set(value) {
            type= value.toLowerCase()
        }
    var Meaning: String
        get() {
            return mean
        }
        set(value) {
            mean= value.toLowerCase().trim()
        }
    constructor()
    constructor(type: String, mean: String) {
        this.type = type.toLowerCase().trim()
        this.mean = mean.toLowerCase().trim()
    }

    override fun toString(): String {
        var str= ""
        when(type){
            "noun"-> str+="(n)"
            "verb"-> str+="(v)"
            "pronouns"-> str+= "(pro)"
            "preposition"-> str+="(pre)"
            "adjective"-> str+="(adj)"
            "adverb"-> str+="(adv)"
            "interjections"-> str+="(int)"
            "conjunctions"-> str+="(con)"
        }
        str+=mean
        return str
    }
    fun toStringMax(): String{
        var str= ""
        when(type){
            "noun"-> str+="(n)"
            "verb"-> str+="(v)"
            "pronouns"-> str+= "(pro)"
            "preposition"-> str+="(pre)"
            "adjective"-> str+="(adj)"
            "adverb"-> str+="(adv)"
            "interjections"-> str+="(int)"
            "conjunctions"-> str+="(con)"
        }
        str+=" $mean"
        return  str
    }
}