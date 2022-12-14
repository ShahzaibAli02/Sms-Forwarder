package com.sms.smsforwarder.MainScreen

import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.sms.smsforwarder.BaseActivity
import com.sms.smsforwarder.MessageListener
import com.sms.smsforwarder.R
import com.sms.smsforwarder.adapters.MessageAdapter
import com.sms.smsforwarder.data.model.Message
import com.sms.smsforwarder.databinding.ActivityMainScreenBinding
import com.sms.smsforwarder.includeIgnoreList.IncludeIgnoreListActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.jar.Manifest


@AndroidEntryPoint
class MainScreenActivity : BaseActivity()
{

    private lateinit var binding:ActivityMainScreenBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MessageAdapter
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)


        adapter= MessageAdapter()
        viewModel=ViewModelProvider(this)[MainViewModel::class.java]


        binding.btnIncludeList.setOnClickListener { openIncludeIgnoreListActivity(false) }
        binding.btnIgnoreList.setOnClickListener {  openIncludeIgnoreListActivity(true)  }



        binding.switchOnOff.isChecked=viewModel.getSwitchVal()
        binding.recyclerViewMessages.adapter=adapter

       //binding.inputForwardingUrl.editText?.setText(viewModel.getForwardingUrl())



        binding.btnSave.setOnClickListener {

            val forwardingUrl:String=binding.inputForwardingUrl.editText?.text.toString()
            if(forwardingUrl.isEmpty())
            {
                binding.inputForwardingUrl.error="Enter url"
                return@setOnClickListener
            }
            viewModel.setForwardingUrl(forwardingUrl)
            showMessage("Saved")

        }

        binding.switchOnOff.setOnCheckedChangeListener { compoundButton, isChecked ->
            viewModel.setSwitch(isChecked)
        }



        viewModel.getAllMessages().observe(this,::readAllMessages)
        handleProtocolSelection()
        handleSimSelection()

        if(ActivityCompat.checkSelfPermission(this,android.Manifest.permission.READ_SMS)!=PackageManager.PERMISSION_GRANTED)
        {
            requestPermission()
        }
    }

    private fun readAllMessages(messages: List<Message>)
    {
        adapter.setList(messages)
    }

    private fun requestPermission()
    {
        requestPermissionLauncher.launch(arrayOf(android.Manifest.permission.READ_SMS, android.Manifest.permission.RECEIVE_SMS))
    }

    val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()
        ) {

            if(it.isEmpty() || (it.isNotEmpty() && it[android.Manifest.permission.READ_SMS] !=true) )
            {
                showMessageDialog("Permission Required","Read sms permission is required for this to be functional. Please give read sms permission", positiveButtonMsg = "Grant Permission", negativeButtonMsg ="Cancel" , messageListener = object:MessageListener{
                    override fun onSelected(b: Boolean)
                    {
                        if(b)
                        {
                           requestPermission()
                        }
                    }
                })
            }

        }


    private fun handleProtocolSelection()
    {
        when (viewModel.getSelectedProtocol())
        {
            0 ->
            {
                binding.radioPost.isChecked = true
            }
            1 ->
            {
                binding.radioGet.isChecked = true
            }

        }

        binding.radioProtocolSelection.setOnCheckedChangeListener { radioGroup, i ->

            var selectedProtocol = 0
            when (i)
            {
                R.id.radioPost ->
                {
                    selectedProtocol = 0
                }
                R.id.radioGet ->
                {
                    selectedProtocol = 1
                }

            }

            viewModel.setSelectedProtocol(selectedProtocol)
        }
    }
    private fun handleSimSelection()
    {
        when (viewModel.getSelectedSim())
        {
            0 ->
            {
                binding.radioSim1.isChecked = true
            }
            1 ->
            {
                binding.radioSim2.isChecked = true
            }
            2 ->
            {
                binding.radioBoth.isChecked = true
            }
        }

        binding.radioGroupSimSelection.setOnCheckedChangeListener { radioGroup, i ->

            var selectedSim = 0
            when (i)
            {
                R.id.radioSim1 ->
                {
                    selectedSim = 0
                }
                R.id.radioSim2 ->
                {
                    selectedSim = 1
                }
                R.id.radioBoth ->
                {
                    selectedSim = 2
                }
            }

            viewModel.setSelectedSim(selectedSim)
        }
    }

    private fun openIncludeIgnoreListActivity(isIgnore: Boolean)
    {

        startActivity(Intent(this,IncludeIgnoreListActivity::class.java).putExtra(IncludeIgnoreListActivity.EXTRA_IGNORE,isIgnore))
    }
}