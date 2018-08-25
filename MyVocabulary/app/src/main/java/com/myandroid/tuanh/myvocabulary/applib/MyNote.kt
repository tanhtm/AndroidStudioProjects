package com.myandroid.tuanh.myvocabulary.applib

import Lib.MyDate

/**
 * Created by Tanh on 7/14/2017.
 */
class MyNote {
    private var id: Int= 0
    private var note: String= "(to) make sb do sth - chủ động\nsb be made to do sth - bị động"
    private var day: MyDate= MyDate()
    private var idFolder: Int = 0
    var Note: String
        get() = note
        set(value) {
            note= value
        }
    var Id: Int
        get() {
            return id
        }
        set(value) {
            id= value
        }
    var Day: String
        get() {
            return day.toString()
        }
        set(value) {
            day= MyDate(value)
        }
    var IdFolder: Int
        get() = idFolder
        set(value) {
            idFolder= value
        }
    constructor()
    constructor(id: Int,note: String,day: MyDate){
        this.id= id
        this.note= note
        this.day= day
    } // dùng khi lấy dữ liệu từ cơ sở
    constructor(note: String) {
        this.note=note
        day= MyDate()
    } // dùng khi khởi tạo giá trị mới
    override fun toString(): String {
        return "\tNum: $id,day: $day.\n\t$note."
    }
    fun keyword(key:String): Boolean{
        return note.contains(key)
    } // kiểm tra xem key có nằm trong note ko // Dùng cho việc tìm note bằng từ khoá
}