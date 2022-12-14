package com.sms.smsforwarder

import android.app.AlertDialog
import android.app.Dialog
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ParseException
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.sms.smsforwarder.data.SharedPref


open class BaseActivity : AppCompatActivity() {


    
    lateinit var alertDialog: AlertDialog
    lateinit var sharedPref: SharedPref
    fun showProgressBar()
    {
        if(::alertDialog.isInitialized && !alertDialog.isShowing && isDestroying().not())
        {
            alertDialog.show()
        }
    }

    fun showMessageDialog(title:String="", message:String,
                          positiveButtonMsg:String="Yes",
                          negativeButtonMsg:String="No",
                          positiveButtonColor: Int=R.color.color_accepted,
                          negativeButtonColor: Int=R.color.grey,
                          showPositiveButton:Boolean=true,
                          showNegativeButton:Boolean=true,
                          isCancelable:Boolean=true,
                          messageListener: MessageListener
    ): Dialog
    {
        val dialog= Dialog(this)
        dialog.setContentView(R.layout.lyt_dialog_message)
        dialog.setCancelable(isCancelable)
        val window: Window =dialog.window!!
        window.setGravity(Gravity.BOTTOM)
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        window.setLayout(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        val txtTitle=dialog.findViewById<TextView>(R.id.txtTitle)
        val txtMessage=dialog.findViewById<TextView>(R.id.txtMessage)
        val btnPositive: Button =dialog.findViewById(R.id.btnPositive)
        val btnNegative: Button =dialog.findViewById(R.id.btnNegative)


        btnPositive.visibility=when(showPositiveButton){
            true->View.VISIBLE
            false->View.GONE

        }
        btnNegative.visibility=when(showNegativeButton){
            true->View.VISIBLE
            false->View.GONE

        }
        txtTitle.text=title
        txtMessage.text=message
        btnPositive.backgroundTintList= ColorStateList.valueOf(resources.getColor(positiveButtonColor))
        btnNegative.backgroundTintList= ColorStateList.valueOf(resources.getColor(negativeButtonColor))
        btnPositive.text=positiveButtonMsg
        btnNegative.text=negativeButtonMsg

        btnPositive.setOnClickListener {
            dialog.dismiss()
            messageListener.onSelected(true)
        }
        btnNegative.setOnClickListener {
            dialog.dismiss()
            messageListener.onSelected(false)
        }

        dialog.show();
        return dialog
    }


    fun checkIsEmpty(vararg editTexts:EditText,showErrorMessage:Boolean = true):Boolean
    {
        for (et in editTexts)
        {
            if(et.text.isEmpty())
            {
                if(showErrorMessage)
                {
                    et.error="Required Field"
                    et.requestFocus()
                }
                return true
            }
        }
        return false
    }
    fun hideProgressBar()
    {
        if(::alertDialog.isInitialized && alertDialog.isShowing && isDestroying().not())
        {
            alertDialog.dismiss()
        }
    }
    fun showMessage(message:String?)
    {
        message?.let {
            Toast.makeText(this,it,Toast.LENGTH_LONG).show()
        }

    }
    fun isDestroying():Boolean
    {
        return isDestroyed || isFinishing || isChangingConfigurations
    }
    fun showViews(vararg views:View)
    {
        views.iterator().forEach {
            it.visibility=View.VISIBLE
        }
    }


    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedPref=SharedPref(this)
        alertDialog=Util.getProgressDialog(this)
    }
}