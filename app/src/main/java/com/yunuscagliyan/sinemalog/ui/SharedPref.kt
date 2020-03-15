package com.yunuscagliyan.sinemalog.ui

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color

class SharedPref(context: Context) {
    private lateinit var mSharedPreferences:SharedPreferences
    companion object{
        const val SHAREDPREFRENCE_FILE_NAME="sinemalog"
        const val THEME_STATE_KEY="DARK_MODE"
        const val THEME_COLOR_KEY="THEME_COLOR"
        const val STATUS_BAR_COLOR="STATUS_BAR_COLOR"
        const val THEME_POSITION="THEME_POSITION"
        const val FIRST_TIME_OPENING="FIRST_TIME_OPENING"
    }
    init {
        mSharedPreferences=context.getSharedPreferences(
            SHAREDPREFRENCE_FILE_NAME,Context.MODE_PRIVATE
        )

    }
    fun setNightModeState(state:Boolean){
        val  editor=mSharedPreferences.edit()
        editor.putBoolean(THEME_STATE_KEY,state)
        editor.commit()
    }
    fun loadNightModeState():Boolean{
        var state=mSharedPreferences.getBoolean(
            THEME_STATE_KEY,false
        )
        return state
    }
    fun setToolBarColor(themeColor:Int){
        val editor=mSharedPreferences.edit()
        editor.putInt(THEME_COLOR_KEY,themeColor)
        editor.commit()
    }
    fun loadToolBarColor():Int{
        var toolbarColor=mSharedPreferences.getInt(
            THEME_COLOR_KEY, Color.parseColor("#1976D2")
        )
        return toolbarColor
    }
    fun setStatusBarColor(themeColor:Int){
        val editor=mSharedPreferences.edit()
        editor.putInt(STATUS_BAR_COLOR,themeColor)
        editor.commit()
    }
    fun loadStatusBarColor():Int{
        var themeColor=mSharedPreferences.getInt(
            STATUS_BAR_COLOR, Color.parseColor("#0D47A1")
        )
        return themeColor
    }
    fun setThemePosition(positon:Int){
        val editor=mSharedPreferences.edit()
        editor.putInt(THEME_POSITION,positon)
        editor.commit()
    }
    fun loadThemePosition():Int{
        var positon=mSharedPreferences.getInt(
            THEME_POSITION, Color.parseColor("#303F9F")
        )
        return positon
    }
    fun setFirstTime(firstTime:Boolean){
        val editor=mSharedPreferences.edit()
        editor.putBoolean(FIRST_TIME_OPENING,firstTime)
        editor.commit()
    }
    fun loadFirstTime():Boolean{
        var firstTime=mSharedPreferences.getBoolean(
            FIRST_TIME_OPENING, true
        )
        return firstTime
    }
}