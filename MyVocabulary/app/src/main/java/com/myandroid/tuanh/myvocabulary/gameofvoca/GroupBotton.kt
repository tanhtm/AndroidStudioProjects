package com.myandroid.tuanh.myvocabulary.gameofvoca

import android.app.Activity
import android.graphics.Color
import android.view.animation.AnimationUtils
import android.widget.Button
import com.myandroid.tuanh.myvocabulary.R

/**
 * Created by Tanh on 9/9/2017.
 */
abstract class GroupBotton {
    private  var activity: Activity = Activity()
    private  var listId = ArrayList<Int>()
    private  var COLOR = Color.RED
    private  var idIsSelect= 0
    private  var TEXT_SIZE: Float = Float.NaN
    constructor()
    constructor(activity: Activity, listId: ArrayList<Int>) {
        this.activity = activity
        this.listId = listId
        TEXT_SIZE= activity.findViewById<Button>(listId[0]).textSize
        onClick()
    }
    abstract fun setOnClick(button: Button)
    private  fun onClick(){
        for (i in listId)
        {
            var button = activity.findViewById<Button>(i)
            var anim = AnimationUtils.loadAnimation(activity, R.anim.sacle)
            button.startAnimation(anim)
            button.setOnClickListener {
                if (idIsSelect!=0) {
                    var buttonOld = activity.findViewById<Button>(idIsSelect)
                    buttonOld.setTextColor(Color.parseColor("#111111"))
                    buttonOld.textSize= TEXT_SIZE
                }
                button.setTextColor(COLOR)
                button.textSize= button.textSize + 3
                idIsSelect= button.id
                setOnClick(button)
            }
        }
    }
    fun clearSelect(){
        if (idIsSelect!=0) {
            var buttonOld = activity.findViewById<Button>(idIsSelect)
            buttonOld.setTextColor(Color.parseColor("#111111"))
            buttonOld.textSize= TEXT_SIZE
        }
    }
    fun clearOnClick(){
        for(i in listId){
            var button = activity.findViewById<Button>(i)
            button.setOnClickListener {  }
        }
    }
    fun isSelect():Boolean{
        if(idIsSelect==0)
            return false
        return true
    }
}