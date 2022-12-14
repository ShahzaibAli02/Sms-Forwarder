package com.sms.smsforwarder.includeIgnoreList

import com.sms.smsforwarder.data.model.Contact
import com.sms.smsforwarder.data.roomdb.DatabaseDao
import javax.inject.Inject

class IncludeIgnoreListRepository @Inject constructor(val dao :DatabaseDao)
{

    fun getContacts(isIgnore:Boolean) = if(isIgnore) dao.getIgnoredList() else dao.getIncludeList()
    suspend fun insertContact(contact: Contact): Long
    {
       return dao.insertContact(contact)
    }

    suspend fun delete(contact: Contact)
    {
        dao.delete(contact)
    }


}