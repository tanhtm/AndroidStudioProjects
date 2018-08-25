package com.myandroid.tuanh.myvocabulary.applib

import Lib.MyDate

/**
 * Created by Tanh on 8/10/2017.
 */
class MyFolder { //các thư mục
    private var name: String ="My Folder"
    private var id: Int= 0
    private var day: MyDate = MyDate()
    private var listWord: ArrayList<MyWord> = ArrayList()
    private var listNote: ArrayList<MyNote> = ArrayList()
    var ListWord: ArrayList<MyWord>
        get() {
           return listWord
        }
        set(value) {
            listWord= value
        }
    var ListNote: ArrayList<MyNote>
        get() {
            return listNote
        }
        set(value) {
            listNote= value
        }
    var Id: Int
        get() {
            return id
        }
        set(value) {
            id = value
        }
    var Day: MyDate
        get() {return day}
        set(value){
            day= value
        }
    var  Name: String
        get() = name
        set(value) {
            name= value
        }
    constructor(name: String)
    {
        this.name= name
    }
    fun addWord(myWord: MyWord){
        listWord.add(myWord)
    }
    fun addNote(myNote: MyNote){
        listNote.add(myNote)
    }

    override fun equals(other: Any?): Boolean {
        other as MyFolder
        if(name == other.Name)
            return true
        else
            return false
    }

    override fun toString(): String {
        return name
    }
}