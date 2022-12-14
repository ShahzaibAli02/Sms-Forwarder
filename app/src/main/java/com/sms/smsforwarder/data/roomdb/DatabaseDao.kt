package com.sms.smsforwarder.data.roomdb

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.sms.smsforwarder.data.model.Contact
import com.sms.smsforwarder.data.model.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface DatabaseDao {

    @Query("SELECT * FROM contact where isIgnore=1")
    fun getIgnoredList(): Flow<List<Contact>>


    @Query("SELECT * FROM contact where isIgnore=0")
    fun getIncludeList(): Flow<List<Contact>>


    @Insert
    suspend fun insertContact(contact: Contact):Long




    @Query("SELECT * FROM message")
    fun getAllMessages(): Flow<List<Message>>

    @Insert
    suspend fun insertMessage(message: Message):Long

    @Delete
    suspend fun delete(contact: Contact)
}