package com.sms.smsforwarder

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.sms.smsforwarder.login.LoginActivity

class MainActivity : AppCompatActivity()
{
    override fun onStart()
    {
        super.onStart()
        startService(Intent(this, SmsService::class.java))
    }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Handler().postDelayed(Runnable {
            finish()
            startActivity(Intent(this,LoginActivity::class.java))

        },2000)
    }
}