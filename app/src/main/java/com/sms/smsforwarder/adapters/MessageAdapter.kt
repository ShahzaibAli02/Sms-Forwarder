package com.sms.smsforwarder.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import androidx.recyclerview.widget.RecyclerView
import com.sms.smsforwarder.data.model.Contact
import java.util.ArrayList
import android.view.ViewGroup
import android.view.LayoutInflater
import com.sms.smsforwarder.data.model.Message
import com.sms.smsforwarder.databinding.ItemIncludeIgnoreBinding
import com.sms.smsforwarder.databinding.ItemMessageBinding

class MessageAdapter : RecyclerView.Adapter<MessageAdapter.ViewHolder>()
{
    private var listData: MutableList<Message> = ArrayList()
    private lateinit var onClick: (Message) -> Unit

    fun setDeleteListener(onClick:(Message) -> Unit)
    {
        this.onClick=onClick
    }
    fun setList(newList: List<Message>)
    {
        listData.clear()
        listData.addAll(newList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder
    {
        val binding = ItemMessageBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int)
    {
        val message = listData[i]
        holder.binding.apply {

            txtMessage.text=message.message
            txtSender.text=message.sender
            txtMessage.setOnClickListener {
                AlertDialog.Builder(holder.itemView.context)
                    .setTitle("Message")
                    .setMessage(message.message)
                    .setPositiveButton("OK",null)
                    .show()


            }
            txtStatus.text=if(message.status==1) "Sent" else "Failed"
            txtStatus.setTextColor(if(message.status==1) Color.GREEN else Color.RED)
        }
    }

    override fun getItemCount(): Int
    {
        return listData.size
    }

    class ViewHolder(var binding: ItemMessageBinding) : RecyclerView.ViewHolder(binding.root)
}