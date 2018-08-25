package Lib

import java.util.Random
import kotlin.collections.ArrayList

/**
 * Created by Tanh on 7/14/2017.
 */
// lấy n số ngẫu nhiên trong tập số [0,max) (n<max)
class RandomArray {
    private var max: Int= 10
    private var maxHin: Int= 10 // max ảo để giảm dần ko làm thay đổi max maxHIn= max
    private var n: Int= 4
    private var listMax: IntArray= IntArray(10)
    private var listN: ArrayList<Int> = ArrayList() //danh sách n số random
    constructor(max: Int,n:Int) {
        this.max = max
        maxHin= max
        this.n = n
        listMax= IntArray(max)
    }
    private fun del(x: Int){
        for (i in x until maxHin-1)
            listMax[i]=listMax[i+1]
        maxHin--
    } //xoa vị trí x trong listMax
    private fun run() {
        listN.clear()
        for (i in 0 until max)
            listMax[i]=i
        maxHin= max // mục đính bảo tồn giá trị max
        var i= 0
        var rd= Random()
        while (i< n) {
            var x= rd.nextInt(maxHin)
            listN.add(listMax[x])
            i++
            del(x)
        }
    }
    fun rdArray():ArrayList<Int>
    {
        run()
        return listN
    }
}