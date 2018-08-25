package Lib

import java.util.*

/**
 * Created by Tanh on 7/14/2017.
 */
class MyDate {
    private var day: Int= 0
    private var month: Int= 0
    private var year: Int= 0
    constructor()
    {
        var cal= Calendar.getInstance()
        day= cal.get(Calendar.DAY_OF_MONTH)
        month= cal.get(Calendar.MONTH)+1
        year= cal.get(Calendar.YEAR)
    }
    constructor(string: String)
    {
        var list= string.split("/")
        day= list[0].toInt()
        month= list[1].toInt()
        year= list[2].toInt()
        println(toString());
    }
    constructor(month: Int, day: Int, year: Int)
    {
        this.month= month
        this.day= day
        this.year= year
    }
    override fun toString(): String {
        return "$day/$month/$year"
    }
}