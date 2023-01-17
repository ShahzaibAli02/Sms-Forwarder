package com.sms.smsforwarder.data.roomdb

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.sms.smsforwarder.data.model.Contact
import com.sms.smsforwarder.data.model.Message
import com.sms.smsforwarder.data.model.Param

@Database(entities = [Contact::class, Message::class,Param::class], version = 6)
abstract class AppDatabase : RoomDatabase() {
    abstract fun databaseDao(): DatabaseDao

    companion object {

        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase
        {
            return INSTANCE ?: synchronized(this)
            {
                val instance = Room.databaseBuilder(
                    context,
                    AppDatabase::class.java,
                    "smsforwarder").fallbackToDestructiveMigration().allowMainThreadQueries()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}