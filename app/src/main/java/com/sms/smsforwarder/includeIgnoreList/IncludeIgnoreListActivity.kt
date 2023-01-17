package com.sms.smsforwarder.includeIgnoreList

import android.app.Dialog
import android.graphics.Color
import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.sms.smsforwarder.BaseActivity
import com.sms.smsforwarder.MessageListener
import com.sms.smsforwarder.R
import com.sms.smsforwarder.adapters.ContactsAdapter
import com.sms.smsforwarder.data.model.Contact
import com.sms.smsforwarder.databinding.ActivityIncludeIgnoreListBinding
import com.sms.smsforwarder.databinding.LytDialogAddBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect


@AndroidEntryPoint
class IncludeIgnoreListActivity : BaseActivity()
{


    companion object{
        val EXTRA_IGNORE="ignore"
    }
    private lateinit var binding:ActivityIncludeIgnoreListBinding
    private lateinit var viewModel:ListViewModel
    private lateinit var adapter: ContactsAdapter
    private  var isIgnore:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding=ActivityIncludeIgnoreListBinding.inflate(layoutInflater)
        setContentView(binding.root)


        isIgnore= intent.extras!!.getBoolean(EXTRA_IGNORE,false)
        viewModel=ViewModelProvider(this)[ListViewModel::class.java]
        adapter= ContactsAdapter(if(isIgnore) getColor(R.color.color_ignore) else getColor(R.color.color_include))
        binding.recyclerView.adapter=adapter



        binding.txtHeaderTitle.text=if(isIgnore) "Ignore List" else "Include List"


        adapter.setDeleteListener(::onClickDelete)
        binding.imgBack.setOnClickListener { onBackPressed() }
       // binding.fabAdd.setOnClickListener {showAddDialog()}





        viewModel.getContacts(isIgnore).observe(this){

            binding.lytEmpty.parent.visibility= if(it.isEmpty()) View.VISIBLE else View.GONE
            binding.recyclerView.visibility= if(it.isEmpty()) View.GONE else View.VISIBLE
            adapter.setList(it)
        }
    }

    private fun showAddDialog()
    {
        val dialog=Dialog(this)
        val binding=LytDialogAddBinding.inflate(layoutInflater,null,false)
        dialog.setContentView(binding.root)
        dialog.window?.setLayout(MATCH_PARENT,WRAP_CONTENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog.show()


        binding.imgCancel.setOnClickListener { dialog.dismiss() }
        binding.btnSave.setOnClickListener {


            val name:String=binding.inputName.editText?.text.toString()
            val number:String=binding.inputNumber.editText?.text.toString()

            if(number.isEmpty())
            {
                binding.inputNumber.error="Required field"
                binding.inputNumber.requestFocus()
                return@setOnClickListener
            }
            viewModel.insertContact(Contact(0, name, number, isIgnore)).observe(this)
             {

                if(it)
                {
                    dialog.dismiss()
                    showMessage("Saved Successfully")
                }
                 else
                {
                    showMessage("Failed to insert")
                }

             }


        }

    }

    private fun onClickDelete(contact: Contact)
    {


        showMessageDialog(message = "Are you sure to delete ?" , positiveButtonColor = R.color.red, messageListener = object : MessageListener
        {
            override fun onSelected(b: Boolean)
            {
                if(b)
                {
                    viewModel.delete(contact)
                    showMessage("Deleted")
                }
            }
        })


    }
    override fun onBackPressed()
    {
        super.onBackPressed()
        finish()
    }
}


