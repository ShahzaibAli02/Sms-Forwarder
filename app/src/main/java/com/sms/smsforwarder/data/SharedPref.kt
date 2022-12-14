package com.sms.smsforwarder.data

import android.content.Context
import android.content.SharedPreferences

class SharedPref(context: Context) {


    companion object{
         var sharedPreferences:SharedPreferences?=null
        val KEY_REMEMBER_ME="REMEMBER_ME"
        val KEY_USER_Profile="User_Profile"
        val KEY_FORWARDING_URL="forwarding_url"
        val KEY_SIM_SELECTION="sim_selection" //0 = SIM1 , 1=SIM2  , 2= BOTH
        val KEY_PROTOCOL_SELECTION="protocol_selection" //0 = POST , 1=GET
        val KEY_ON_OFF_SWITCH="on_off"

    }
    init {
        if(sharedPreferences ==null)
        {
            sharedPreferences =context.getSharedPreferences("com.sms.smsforwarder.data",0)
        }
    }


    fun saveString(key:String,value:String)= sharedPreferences!!.edit().putString(key,value).commit()

    fun getString(key:String,defValue:String="")= sharedPreferences!!.getString(key,defValue)


    fun saveBool(key:String,value:Boolean)= sharedPreferences!!.edit().putBoolean(key,value).commit()

    fun getBool(key:String,defValue:Boolean=false)= sharedPreferences!!.getBoolean(key,defValue)


    fun saveInt(key:String,value:Int)= sharedPreferences!!.edit().putInt(key,value).commit()

    fun getInt(key:String,defValue:Int=0)= sharedPreferences!!.getInt(key,defValue)



}