package com.example.ecommerceadminpanel.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.ecommerceadminpanel.R
import com.example.ecommerceadminpanel.data.local.entity.CategoryEntity
import com.example.ecommerceadminpanel.databinding.ItemRecordBinding
import com.example.ecommerceadminpanel.presentation.fragment.CategoriesFragmentDirections
import com.example.ecommerceadminpanel.utils.CategoryItemClickListener

class CategoryListAdapter (
    private val categoryItemClickListener: CategoryItemClickListener,
    var data: List<CategoryEntity> = emptyList(),
    val context: Context
) :
    RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder>() {


    class CategoryViewHolder(val itemBinding: ViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(
            ItemRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {

        with(holder) {
            val result= data[position]
            (this.itemBinding as ItemRecordBinding).name.text = result.name
            itemBinding.image.setImageBitmap(result.image)
            if(result.isActivated) {
                itemBinding.activated.text = "ACTIVATED"
                itemBinding.activated.setTextColor(context.resources.getColor(R.color.teal_700))
            }else{
                itemBinding.activated.text = "DEACTIVATED"
                itemBinding.activated.setTextColor(context.resources.getColor(R.color.red))
            }
            itemBinding.openDetails.setOnClickListener {
               categoryItemClickListener.onViewItemPressed(result.categoryId)
            }
            itemBinding.editDelete.setOnClickListener {
                categoryItemClickListener.onDeletePressed(result)
            }
            itemBinding.editDetails.setOnClickListener {
                categoryItemClickListener.onEditPressed(result)
            }
            itemBinding.root.setOnClickListener {
                itemBinding.group.isVisible = true
            }
            itemBinding.activated.setOnClickListener {
                categoryItemClickListener.activateAndDeactivate(result.categoryId,result.isActivated)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(list: List<CategoryEntity>){
         data=list
         notifyDataSetChanged()
    }

    }