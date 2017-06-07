package com.bnsantos.offline

import android.content.Context
import android.content.SharedPreferences

open class Preferences (context: Context) {
    val USER_ID = "USER_ID"
    val mSharedPreferences : SharedPreferences = context.getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)

    open var userId: String
        get() = mSharedPreferences.getString(USER_ID, "---------")
        set(value) = mSharedPreferences.edit().putString(USER_ID, value).apply()
}