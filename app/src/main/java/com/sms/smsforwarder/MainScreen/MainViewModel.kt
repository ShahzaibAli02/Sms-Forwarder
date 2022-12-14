package com.sms.smsforwarder.MainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.sms.smsforwarder.data.SharedPref
import com.sms.smsforwarder.data.roomdb.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(val mainRepository: MainRepository,val sharedPref: SharedPref):ViewModel()
{


    fun getSwitchVal() = sharedPref.getBool(SharedPref.KEY_ON_OFF_SWITCH,true)
    fun setSwitch(isOn:Boolean) = sharedPref.saveBool(SharedPref.KEY_ON_OFF_SWITCH,isOn)

    fun getForwardingUrl()=sharedPref.getString(SharedPref.KEY_FORWARDING_URL,"")



    fun getAllMessages()= liveData {

        mainRepository.getAllMessages().collect(){
            emit(it)
        }
    }


    fun setForwardingUrl(forwardingUrl:String)=sharedPref.saveString(SharedPref.KEY_FORWARDING_URL,forwardingUrl)


    fun getSelectedSim()=sharedPref.getInt(SharedPref.KEY_SIM_SELECTION,0)

    fun setSelectedSim(selectedSim:Int)=sharedPref.saveInt(SharedPref.KEY_SIM_SELECTION,selectedSim)


    fun getSelectedProtocol()=sharedPref.getInt(SharedPref.KEY_PROTOCOL_SELECTION,0)

    fun setSelectedProtocol(selectedProtocol:Int)=sharedPref.saveInt(SharedPref.KEY_PROTOCOL_SELECTION,selectedProtocol)

}