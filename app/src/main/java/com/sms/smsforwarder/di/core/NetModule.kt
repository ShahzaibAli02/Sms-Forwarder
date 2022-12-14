package com.sms.smsforwarder.di.core

import com.sms.smsforwarder.data.api.SmsApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetModule()
{


    @Singleton
    @Provides
    fun getRetrofit():Retrofit
    {

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://eoki578g332geky.m.pipedream.net/")
            .build()
    }
    @Singleton
    @Provides
    fun getSmsApi(retrofit: Retrofit): SmsApi = retrofit.create(SmsApi::class.java)
}