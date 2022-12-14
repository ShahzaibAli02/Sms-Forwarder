package com.sms.smsforwarder.di.core

import android.content.Context
import com.sms.smsforwarder.data.roomdb.AppDatabase
import com.sms.smsforwarder.data.roomdb.DatabaseDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DBModule
{
    @Singleton
    @Provides
    fun providesDataBaseDao(@ApplicationContext context:Context):DatabaseDao
    {
        return AppDatabase.getDatabase(context).databaseDao()
    }

}