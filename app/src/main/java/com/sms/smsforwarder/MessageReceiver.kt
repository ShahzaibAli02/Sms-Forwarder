package com.sms.smsforwarder

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.telephony.PhoneNumberUtils
import android.telephony.SmsMessage
import android.util.Log
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.firebase.auth.FirebaseAuth
import com.sms.smsforwarder.data.SharedPref
import com.sms.smsforwarder.data.model.Message
import com.sms.smsforwarder.data.model.Param
import com.sms.smsforwarder.data.roomdb.AppDatabase
import com.sms.smsforwarder.data.roomdb.DatabaseDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import kotlin.collections.HashMap

class MessageReceiver : BroadcastReceiver()
{

    lateinit var sharedPref: SharedPref
    lateinit var databaseDao: DatabaseDao
    lateinit var context: Context
    override fun onReceive(context: Context, intent: Intent)
    {


        this.context=context
        sharedPref = SharedPref(context)
        databaseDao = AppDatabase.getDatabase(context).databaseDao()
        val data = intent.extras
        val pdus = data!!["pdus"] as Array<Any>?
        var sender = ""
        var Message = ""
        var serviceCenterAddress: String? = ""

        val simSlot = getSimSlot(data)
        for (i in pdus!!.indices)
        {

            val smsMessage = SmsMessage.createFromPdu(pdus[i] as ByteArray)
            sender = getNAOrValue(smsMessage.displayOriginatingAddress)
            serviceCenterAddress = getNAOrValue(smsMessage.serviceCenterAddress)
            Message += smsMessage.messageBody

        }


        //If Its ON
        if (sharedPref.getBool(SharedPref.KEY_ON_OFF_SWITCH, true) && sharedPref.getString(FirebaseAuth.getInstance().currentUser!!.uid, "")!!
                .isNotEmpty()
        )
        {


            val isInWhiteList=checkIsInWhiteList(sender)
            //TODO CHECK PHONE IN WHITELIST
            //TODO CHECK SIM SLOT MATCHES

            val selectedSimSlot=sharedPref.getInt(SharedPref.KEY_SIM_SELECTION,0)

            mLog("SIM SLOT :"+simSlot)
            mLog("SELECTD SIM SLOT :"+selectedSimSlot)

            if(isInWhiteList && (simSlot==-1 ||  selectedSimSlot==2 || (selectedSimSlot==simSlot)))
            {
                val hashMap = HashMap<String, String>()
                hashMap["sms_sender"] = sender
                hashMap["sms_body"] = Message
                hashMap["sim_number"] = simSlot.toString()
                hashMap["sms_source"] = serviceCenterAddress!!


                //simAdditionalParams
                val simAdditionalParams:HashMap<String,String> = getSimAdditionalParams(simSlot)
                hashMap.putAll(simAdditionalParams)


                val message = Message(0, Calendar.getInstance().timeInMillis, sender, Message, 0)




                if (sharedPref.getInt(SharedPref.KEY_PROTOCOL_SELECTION, 0) == 0)
                {
                    makeVolleyPostRequest(context, message, hashMap)
                }
                else
                {
                    makeVolleyGetRequest(context, message, hashMap)
                }
            }
            else
            {
                mLog("SIM SLOT NOT MATCH | NOT IN WHITELIST")
            }



        }
        else
        {
            mLog("Msg not sending")
        }


    }

    private fun getSimAdditionalParams(simSlot: Int): HashMap<String, String>
    {

        val hashMap=kotlin.collections.HashMap<String,String>()


        if(simSlot!=-1)
        {
            var paramsList:List<Param> =  databaseDao.getAllParams(simSlot)
            for(param in paramsList)
            {
                if(param.key.isNotEmpty())
                {
                    hashMap[param.key] = param.value
                }
            }
        }

        return hashMap
    }

    private fun checkIsInWhiteList(sender:String):Boolean
    {

        if(sharedPref.getBool(SharedPref.KEY_INCLUDE_ALL,false))
            return true

        val list=databaseDao.getWhiteListedNumbers();
        if(list.isNotEmpty())
        {

            for(contact in list)
            {
                if(contact.phone.trim().equals(sender.trim(),ignoreCase = true))
                    return true
                if(PhoneNumberUtils.compare(context,contact.phone,sender))
                    return true
            }
        }
        return false;
    }
    private fun mLog(message:String)
    {
        Log.d("MYTAG", message)
    }

    private fun makeVolleyPostRequest(
        context: Context,
        message: Message,
        hashMap: HashMap<String, String>,
    )
    {

        val queue = Volley.newRequestQueue(context)

        //Content-Type: application/octet-stream
        var url =  sharedPref.getString(FirebaseAuth.getInstance().currentUser!!.uid, "")!!

        if (!url.endsWith("/"))
        {
            url += "/"
        }
        val stringRequest = object: StringRequest(Method.POST, url,
            { response ->
            message.status = 1
            CoroutineScope(Dispatchers.IO).launch {
                databaseDao.insertMessage(message)
            }

        }, {
            message.status = 0
            CoroutineScope(Dispatchers.IO).launch {
                databaseDao.insertMessage(message)
            }
        }
        )
        {

            override fun getHeaders(): MutableMap<String, String> {
            val headers = HashMap<String, String>()
            headers["Content-Type"] = "application/octet-stream"
            return headers
             }
            override fun getParams(): Map<String, String> {
                return hashMap
            }
        }

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun makeVolleyGetRequest(context: Context, message: Message, hashMap: HashMap<String, String>, )
    {


        val queue = Volley.newRequestQueue(context)
        val url = getFormatedUrl(hashMap)
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            message.status = 1
            CoroutineScope(Dispatchers.IO).launch {
                databaseDao.insertMessage(message)
            }

        }, {
            message.status = 0
            CoroutineScope(Dispatchers.IO).launch {
                databaseDao.insertMessage(message)
            }
        }
        )

        // Add the request to the RequestQueue.
        queue.add(stringRequest)
    }

    private fun getFormatedUrl(hashMap: HashMap<String, String>): String
    {
        var url = sharedPref.getString(FirebaseAuth.getInstance().currentUser!!.uid, "")!!

        if (!url.endsWith("/"))
        {
            url += "/"
        }


        val stringBuilder: StringBuilder = StringBuilder(url)
        val iterator: MutableIterator<String> = hashMap.keys.iterator()
        var i = 1
        while (iterator.hasNext())
        {
            val key = iterator.next()
            val value = hashMap.get(key)
            if (i == 1)
            {
                stringBuilder.append("?$key=$value")
            } else
            {
                stringBuilder.append("&$key=$value")
            }
            iterator.remove() // avoids a ConcurrentModificationException
            i++
        }
        url = stringBuilder.toString()
        return url
    }



    fun getNAOrValue(str: String?): String
    {
        return str ?: "N/A"
    }

    fun getSimSlot(bundle: Bundle): Int
    {
        var slot = -1
        val keySet: Set<String> = bundle.keySet()
        for (key in keySet)
        {
            when (key)
            {
                "phone" -> slot = bundle.getInt("phone", -1)
                "slot" -> slot = bundle.getInt("slot", -1)
                "simId" -> slot = bundle.getInt("simId", -1)
                "simSlot" -> slot = bundle.getInt("simSlot", -1)
                "slot_id" -> slot = bundle.getInt("slot_id", -1)
                "simnum" -> slot = bundle.getInt("simnum", -1)
                "slotId" -> slot = bundle.getInt("slotId", -1)
                "slotIdx" -> slot = bundle.getInt("slotIdx", -1)
                else -> if (key.lowercase(Locale.getDefault())
                        .contains("slot") or key.lowercase(Locale.getDefault()).contains("sim")
                )
                {
                    try
                    {
                        val value: String = bundle.getString(key, "-1")
                        if ((value == "0") or (value == "1") or (value == "2"))
                        {
                            slot = bundle.getInt(key, -1)
                        }
                    } catch (e: Exception)
                    {
                        e.printStackTrace()
                    }

                }
            }
        }

        Log.d("KKK", slot.toString())

        return slot
    }


}