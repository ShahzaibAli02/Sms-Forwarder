package com.sms.smsforwarder.adapters

import android.content.Context
import android.graphics.Color
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.sms.smsforwarder.data.model.Contact
import java.util.ArrayList
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.core.widget.doAfterTextChanged
import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.sms.smsforwarder.data.model.Message
import com.sms.smsforwarder.data.model.Param
import com.sms.smsforwarder.R
import com.sms.smsforwarder.data.roomdb.DatabaseDao
import com.sms.smsforwarder.databinding.ItemIncludeIgnoreBinding
import com.sms.smsforwarder.databinding.ItemMessageBinding
import com.sms.smsforwarder.databinding.ItemParamsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Runnable
import kotlinx.coroutines.launch
import java.util.logging.Handler
import kotlin.random.Random

class ParamsAdapter constructor(val sim:Int) : RecyclerView.Adapter<ParamsAdapter.ViewHolder>()
{
    private var listData: MutableList<Param> = ArrayList()
     lateinit var onSave: (Param) -> Unit
    lateinit var onDelete: (Param) -> Unit

    enum class PARAM_UPDATE{
        KEY,VALUE
    }

    fun setList(newList: List<Param>)
    {
        listData.clear()
        listData.addAll(newList)

        if(listData.isEmpty())
        {
            listData.add(Param(Random.nextInt(),"","",sim))
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder
    {
        val binding = ItemParamsBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int)
    {

        val param = listData[i]
        holder.keyTextChangeListener?.initParam(param)
        holder.valTextChangeListener?.initParam(param)
        holder.binding.apply {

            val isLast=(i==listData.size-1)

            imgAdd.setImageResource(if(isLast) R.drawable.ic_baseline_add_circle_outline_24 else R.drawable.ic_baseline_cancel_24)


            inputKey.editText?.setText(param.key)
            inpuValue.editText?.setText(param.value)

           // inputKey.editText?.addTextChangedListener(TextChangeHandler(param,PARAM_UPDATE.KEY))
            //inpuValue.editText?.addTextChangedListener(TextChangeHandler(param,PARAM_UPDATE.VALUE))



            imgAdd.setOnClickListener {

                val isLast=listData.size==i+1
                if(isLast)
                {
                    listData.add(Param(Random.nextInt(),"","",sim))
                }
                else
                {
                    if(::onDelete.isInitialized)
                    {
                        Log.d("MYTAG -> DELETE", param.toString())
                        onDelete(param)
                    }
                    listData.removeAt(i)
                }
                notifyDataSetChanged()


            }
        }
    }


    inner class  TextChangeHandler(var paramupdate: PARAM_UPDATE):TextWatcher
    {

        lateinit var param: Param
        val handler=android.os.Handler()
        lateinit var runnable: java.lang.Runnable

        fun initParam(param: Param)
        {
            this.param=param
        }
        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
        {
        }

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int)
        {

        }

        override fun afterTextChanged(p0: Editable?)
        {

            if(::param.isInitialized)
            {
                if(paramupdate==PARAM_UPDATE.KEY)
                {
                    param.key=p0?.toString()?:""
                }
                if(paramupdate==PARAM_UPDATE.VALUE)
                {
                    param.value=p0?.toString()?:""
                }
                if(::runnable.isInitialized)
                {
                    handler.removeCallbacks(runnable)
                }
                else
                {
                    runnable= java.lang.Runnable {
                        if(::onSave.isInitialized)
                        {
                            Log.d("MYTAG -> SAVE", param.toString())
                            onSave(param)
                        }
                    }
                }

                handler.postDelayed(runnable,2000)
            }


        }

    }

    override fun getItemCount(): Int
    {
        return listData.size
    }

    inner class ViewHolder(var binding: ItemParamsBinding,var keyTextChangeListener:TextChangeHandler?=null,var valTextChangeListener:TextChangeHandler?=null) : RecyclerView.ViewHolder(binding.root)
    {

        init
        {

            keyTextChangeListener=TextChangeHandler(PARAM_UPDATE.KEY)
            valTextChangeListener=TextChangeHandler(PARAM_UPDATE.VALUE)

            binding.inpuValue.editText?.addTextChangedListener(valTextChangeListener)
            binding.inputKey.editText?.addTextChangedListener(keyTextChangeListener)

        }
    }
}