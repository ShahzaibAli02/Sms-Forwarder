package com.sms.smsforwarder.adapters

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.sms.smsforwarder.data.model.Contact
import java.util.ArrayList
import android.view.ViewGroup
import android.view.LayoutInflater
import com.sms.smsforwarder.databinding.ItemIncludeIgnoreBinding

class ContactsAdapter(val backColor:Int) : RecyclerView.Adapter<ContactsAdapter.ViewHolder>()
{
    private var listData: MutableList<Contact>
    private lateinit var onClick: (Contact) -> Unit
    init
    {
        listData = ArrayList()
    }

    fun setDeleteListener(onClick:(Contact) -> Unit)
    {
        this.onClick=onClick
    }
    fun setList(newList: List<Contact>)
    {
        listData.clear()
        listData.addAll(newList)

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder
    {
        val binding = ItemIncludeIgnoreBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int)
    {
        val contact = listData[i]
        holder.binding.apply {
            parent.setCardBackgroundColor(backColor)

            txtName.text=contact.name
            txtNumber.text=contact.phone
            imgDelete.setOnClickListener {
                if(::onClick.isInitialized)
                         onClick(contact)
            }
        }
    }

    override fun getItemCount(): Int
    {
        return listData.size
    }

    class ViewHolder(var binding: ItemIncludeIgnoreBinding) : RecyclerView.ViewHolder(binding.root)
}