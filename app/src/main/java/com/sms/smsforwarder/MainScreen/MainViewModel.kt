package com.sms.smsforwarder.MainScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.sms.smsforwarder.data.SharedPref
import com.sms.smsforwarder.data.model.Param
import com.sms.smsforwarder.data.roomdb.DatabaseDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(val mainRepository: MainRepository,val sharedPref: SharedPref):ViewModel()
{


    fun getSwitchVal() = sharedPref.getBool(SharedPref.KEY_ON_OFF_SWITCH,true)




    fun getIncludeAllSwitchVal() = sharedPref.getBool(SharedPref.KEY_INCLUDE_ALL,false)

    fun setIncludeAllSwitchVal(isOn:Boolean) = sharedPref.saveBool(SharedPref.KEY_INCLUDE_ALL,isOn)


    fun setSwitch(isOn:Boolean) = sharedPref.saveBool(SharedPref.KEY_ON_OFF_SWITCH,isOn)

    fun getForwardingUrl()=  sharedPref.getString(FirebaseAuth.getInstance().currentUser!!.uid, "")




        fun getSim1Params()= liveData {

            emit(mainRepository.getSim1Params())
        }
        fun getSim2Params()= liveData {

            emit(mainRepository.getSim2Params())
        }


        fun getAllMessages()= liveData {

            mainRepository.getAllMessages().collect(){
                emit(it)
            }
        }


        fun setForwardingUrl(forwardingUrl:String)=sharedPref.saveString(FirebaseAuth.getInstance().currentUser!!.uid,forwardingUrl)


        fun getSelectedSim()=sharedPref.getInt(SharedPref.KEY_SIM_SELECTION,0)

        fun setSelectedSim(selectedSim:Int)=sharedPref.saveInt(SharedPref.KEY_SIM_SELECTION,selectedSim)


        fun getSelectedProtocol()=sharedPref.getInt(SharedPref.KEY_PROTOCOL_SELECTION,0)

        fun setSelectedProtocol(selectedProtocol:Int)=sharedPref.saveInt(SharedPref.KEY_PROTOCOL_SELECTION,selectedProtocol)
        fun saveParam(param: Param) = viewModelScope.launch{
            mainRepository.saveParam(param)
        }
        fun deleteParam(param: Param) = viewModelScope.launch{
            mainRepository.deleteParam(param)
        }


}