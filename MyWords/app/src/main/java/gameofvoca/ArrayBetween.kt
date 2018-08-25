package gameofvoca

/**
 * Created by Tanh on 7/27/2017.
 */
class ArrayBetween(private var m: Int, private var n: Int) { //{ Bai toán tìm n số ở giữa m số [1..m] vidu n=6 m=[1..10] => n={3,4,5,6,7,8}}trả về vị trí đầu tiên của n
    fun firstPos(): Int
    {
        var fp: Int= 0
        var num: Int= (m-n)/2
        fp= 1+ num
        return fp
    }
    fun array(): ArrayList<Int>
    {
        var array : ArrayList<Int> = ArrayList()
        var fp= firstPos()
        array.add(fp)
        var i= 1
        while (i != n)
        {
            array.add(fp+ i)
            i++
        }
        return array
    }
}