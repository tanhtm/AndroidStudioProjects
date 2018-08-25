package adapter

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.TextView
import applib.MyFolder
import com.myandroid.tuanh.mywords.R
import java.util.*

/**
 * Created by Tanh on 8/11/2017.
 */
class AdapterFolder (private var activity: Activity, private var list: ArrayList<MyFolder>) : BaseAdapter() {
    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val layoutInflater: LayoutInflater = activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val view: View= layoutInflater.inflate(R.layout.layout_myfolder,null)
        var folder: MyFolder = list[p0]
        var buttonName = view.findViewById<Button>(R.id.buttonName)
        buttonName.text= folder.Name
        var textView = view.findViewById<TextView>(R.id.textViewInfo)
        textView.text= folder.toString()
        return  view
    }
    override fun getItem(p0: Int): Any? {
        return null
    }

    override fun getItemId(p0: Int): Long {
        return 0
    }
    override fun getCount(): Int {
        return list.size
    }
}