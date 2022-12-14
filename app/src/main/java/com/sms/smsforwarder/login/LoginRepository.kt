package com.sms.smsforwarder.login

import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.sms.smsforwarder.Util
import com.sms.smsforwarder.data.Resource
import com.sms.smsforwarder.data.api.SmsApi
import com.sms.smsforwarder.data.model.Login
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


class LoginRepository @Inject constructor ()
{

     suspend fun login( email:String, pass:String): Resource<AuthResult>
    {
        return try {

            val firebaseAuth=FirebaseAuth.getInstance()
            val firebaseUser= firebaseAuth
                .signInWithEmailAndPassword(email,pass)
                .await()
            Util.responseToResource(firebaseUser)
        } catch (e:Exception) {
            Util.handleError(e)
        }
    }

}