package com.sms.smsforwarder.includeIgnoreList

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.sms.smsforwarder.data.model.Contact
import com.sms.smsforwarder.login.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val repository: IncludeIgnoreListRepository):ViewModel()
{



    fun getContacts(isIgnore:Boolean) = liveData {

        repository.getContacts(isIgnore).collect()
        {
            emit(it)
        }

    }

    fun insertContact(contact: Contact) = liveData<Boolean> {
        emit(repository.insertContact(contact)>0)
    }

    fun delete(contact: Contact) =viewModelScope.launch {
        repository.delete(contact)
    }

}