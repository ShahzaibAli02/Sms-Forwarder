package com.sms.smsforwarder.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.auth.FirebaseAuth
import com.sms.smsforwarder.BaseActivity
import com.sms.smsforwarder.MainScreen.MainScreenActivity
import com.sms.smsforwarder.data.Resource
import com.sms.smsforwarder.data.SharedPref
import com.sms.smsforwarder.databinding.ActivityLoginBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BaseActivity()
{


    private lateinit var viewModel:LoginViewModel
    private lateinit var binding:ActivityLoginBinding


    override fun onStart()
    {
        super.onStart()
        if(sharedPref.getBool(SharedPref.KEY_REMEMBER_ME) && FirebaseAuth.getInstance().currentUser!=null)
        {
            finish()
            startActivity(Intent(this,MainScreenActivity::class.java))
        }
    }
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)

        viewModel=ViewModelProvider(this)[LoginViewModel::class.java]
        binding=ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)





        binding.btnLogin.setOnClickListener {
            viewModel.email=binding.inputLayoutEmail.editText!!.text.toString()
            viewModel.pass=binding.inputLayoutPassword.editText!!.text.toString()

            viewModel.isValid=true
            if(viewModel.email.isEmpty())
            {
                binding.inputLayoutEmail.error="Required Field"
                viewModel.isValid=false
            }

            if(viewModel.pass.isEmpty())
            {
                binding.inputLayoutPassword.error="Required Field"
                viewModel.isValid=false
            }

            if(viewModel.isValid)
            {
                observeResult()
            }
        }
    }

    private fun observeResult()
    {





        viewModel.login().observe(this) { resource ->

            if(isDestroying()) return@observe
            when (resource)
            {
                is Resource.Loading ->
                {
                    showProgressBar()
                }
                is Resource.Success ->
                {
                    hideProgressBar()
                    sharedPref.saveBool(SharedPref.KEY_REMEMBER_ME,true)
                    showMessage("Success")
                    startActivity(Intent(this,MainScreenActivity::class.java))
                }
                is Resource.Error ->
                {
                    hideProgressBar()
                    showMessage(resource.message)
                }
            }



        }
    }


}