package com.sms.smsforwarder.di.core

import android.content.Context
import com.sms.smsforwarder.data.SharedPref
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class SharedPrefModule
{

    @Singleton
    @Provides
    fun providesSharedPref(@ApplicationContext context: Context): SharedPref
    {

        return SharedPref(context)
    }
}