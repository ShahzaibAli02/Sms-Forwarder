package com.sms.smsforwarder.MainScreen

import com.sms.smsforwarder.data.roomdb.DatabaseDao
import javax.inject.Inject

class MainRepository @Inject constructor(val databaseDao: DatabaseDao)
{

    fun getAllMessages() = databaseDao.getAllMessages()

}