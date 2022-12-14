package com.sms.smsforwarder.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sms.smsforwarder.data.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepo: LoginRepository):ViewModel()
{


    public var email:String=""
    public  var pass:String=""
    public  var isValid:Boolean=false

    fun login()= liveData {
        emit(Resource.Loading("Authenticating.."))
        val login=loginRepo.login(email,pass)
        emit(login)
    }
}