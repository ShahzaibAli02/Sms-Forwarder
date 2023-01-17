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
import com.sms.smsforwarder.adapters.ParamsAdapter
import com.sms.smsforwarder.data.model.Message
import com.sms.smsforwarder.data.model.Param
import com.sms.smsforwarder.data.roomdb.AppDatabase
import com.sms.smsforwarder.databinding.ActivityMainScreenBinding
import com.sms.smsforwarder.includeIgnoreList.IncludeIgnoreListActivity
import dagger.hilt.android.AndroidEntryPoint
import java.util.jar.Manifest
import kotlin.random.Random


@AndroidEntryPoint
class MainScreenActivity : BaseActivity()
{

    private lateinit var binding:ActivityMainScreenBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: MessageAdapter
    private lateinit var sim1ParamAdapter:ParamsAdapter
    private lateinit var sim2ParamAdapter:ParamsAdapter
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityMainScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)



        adapter= MessageAdapter()
        sim1ParamAdapter= ParamsAdapter(0)
        sim2ParamAdapter= ParamsAdapter(1)

        binding.recyclerViewSim1Param.adapter=sim1ParamAdapter

        binding.recyclerViewSim2Param.adapter=sim2ParamAdapter

        viewModel=ViewModelProvider(this)[MainViewModel::class.java]




        binding.btnIncludeList.setOnClickListener { openIncludeIgnoreListActivity(false) }
        binding.btnIgnoreList.setOnClickListener {  openIncludeIgnoreListActivity(true)  }



        binding.switchIncludeAll.isChecked=viewModel.getIncludeAllSwitchVal()
        binding.switchOnOff.isChecked=viewModel.getSwitchVal()
        binding.recyclerViewMessages.adapter=adapter

        binding.inputForwardingUrl.editText?.setText(viewModel.getForwardingUrl())



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


        binding.switchIncludeAll.setOnCheckedChangeListener { compoundButton, isChecked ->

            if(isChecked)
            {
                showMessageDialog(message = "Turning on this button means app will forward all messages from all senders", positiveButtonMsg = "Turn On", negativeButtonMsg = "Don't Turn On", messageListener = object:MessageListener{
                    override fun onSelected(b: Boolean)
                    {
                        //DO NOTHING
                        if(!b)
                        {
                            binding.switchIncludeAll.isChecked=false
                        }
                        viewModel.setIncludeAllSwitchVal(b)

                    }

                })
            }
            else
            {
                viewModel.setIncludeAllSwitchVal(false)
            }
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


        sim1ParamAdapter.onDelete=::deleteParam
        sim1ParamAdapter.onSave=::saveParam


        sim2ParamAdapter.onDelete=::deleteParam
        sim2ParamAdapter.onSave=::saveParam


        viewModel.getSim1Params().observe(this)
        {
            sim1ParamAdapter.setList(it)
        }
        viewModel.getSim2Params().observe(this)
        {
            sim2ParamAdapter.setList(it)
        }
    }


    private fun saveParam(param: Param)
    {
        viewModel.saveParam(param)
    }
    private fun deleteParam(param: Param)
    {
        viewModel.deleteParam(param)
        showMessage("Deleted...")
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