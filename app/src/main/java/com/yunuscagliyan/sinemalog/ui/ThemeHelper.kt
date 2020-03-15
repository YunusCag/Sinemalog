package com.yunuscagliyan.sinemalog.ui

import androidx.appcompat.app.AppCompatDelegate

object ThemeHelper {
    const val LIGHT_MODE="light"
    const val DARK_MODE="dark"

    fun applyTheme(theme:String){
        val mode=when(theme){
            LIGHT_MODE->AppCompatDelegate.MODE_NIGHT_NO
            DARK_MODE->AppCompatDelegate.MODE_NIGHT_YES
            else->{
                AppCompatDelegate.MODE_NIGHT_NO
            }
        }
        AppCompatDelegate.setDefaultNightMode(mode)
    }
}