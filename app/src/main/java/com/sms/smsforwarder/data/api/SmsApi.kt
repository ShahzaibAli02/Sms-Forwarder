package com.sms.smsforwarder.data.api



import retrofit2.Response
import retrofit2.http.*

interface SmsApi {

    @FormUrlEncoded
    @POST("drivers/register")
    suspend fun send(@Field("country_code") country_code:String,@Field("mobile") mobile:String): Response<String>




}