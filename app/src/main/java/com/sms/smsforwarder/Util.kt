package com.sms.smsforwarder

import android.app.AlertDialog
import android.content.Context
import android.widget.ProgressBar
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave

import com.sms.smsforwarder.data.Resource
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Response
import java.io.File

class Util {


    companion object {


        fun getProgressDialog(context: Context): AlertDialog {
            val builder = AlertDialog.Builder(context)
            val progressBar = ProgressBar(context)
            val doubleBounce: Sprite = Wave()
            doubleBounce.color = context.resources.getColor(R.color.purple_200)
            progressBar.indeterminateDrawable = doubleBounce
            builder.setView(progressBar)
            builder.setCancelable(false)
            val alertDialog = builder.create()
            alertDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
            return alertDialog
        }


        public fun <T> responseToResource(response: T): Resource<T> {

          return  response?.let {

                return Resource.Success(it)
            } ?: return Resource.Error("Email or password is incorrect")
        }

        public fun <T> responseToResource(response: Response<T>): Resource<T> {
            if(response.isSuccessful)
            {
                response.body()?.let {

                    return Resource.Success(it)
                }
            }
            return  Resource.Error(response.message())
        }

        fun <T> handleError(e: Exception): Resource<T> {

            if (e is java.net.ConnectException)
            {
                return Resource.Error("Check your internet")
            }
            if (e is java.net.SocketTimeoutException)
            {
                return Resource.Error("Check your internet")
            }
            e.printStackTrace()
            return Resource.Error(e.message.toString())
        }

    }
}