package com.sms.smsforwarder.MainScreen

import com.sms.smsforwarder.data.model.Param
import com.sms.smsforwarder.data.roomdb.DatabaseDao
import javax.inject.Inject

class MainRepository @Inject constructor(val databaseDao: DatabaseDao)
{

    fun getAllMessages() = databaseDao.getAllMessages()


    fun getSim1Params() = databaseDao.getAllParams(0)


    fun getSim2Params() = databaseDao.getAllParams(1)

    suspend  fun saveParam(param: Param)
    {
        databaseDao.insertParam(param)
    }
    suspend  fun deleteParam(param: Param)
    {
        databaseDao.deleteParam(param)
    }

}