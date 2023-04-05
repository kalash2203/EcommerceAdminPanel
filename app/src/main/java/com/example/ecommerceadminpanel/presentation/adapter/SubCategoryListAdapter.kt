package com.example.ecommerceadminpanel.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.ecommerceadminpanel.R
import com.example.ecommerceadminpanel.data.local.entity.CategoryEntity
import com.example.ecommerceadminpanel.data.local.entity.CategoryWithSubCategory
import com.example.ecommerceadminpanel.data.local.entity.SubCategoryEntity
import com.example.ecommerceadminpanel.databinding.ItemRecordBinding
import com.example.ecommerceadminpanel.presentation.fragment.CategoriesFragmentDirections
import com.example.ecommerceadminpanel.presentation.fragment.SubCategoriesFragmentDirections
import com.example.ecommerceadminpanel.utils.SubCategoryItemClickListener

class SubCategoryListAdapter (
    private val subCategoryItemClickListener: SubCategoryItemClickListener,
    var data: List<SubCategoryEntity> = emptyList(),
    val  context: Context
) :
    RecyclerView.Adapter<SubCategoryListAdapter.SubCategoryViewHolder>() {


    class SubCategoryViewHolder(val itemBinding: ViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubCategoryViewHolder {
        return SubCategoryViewHolder(
            ItemRecordBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: SubCategoryViewHolder, position: Int) {

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
                subCategoryItemClickListener.onViewItemPressed(result.subCategoryId)
            }
            itemBinding.editDelete.setOnClickListener {
                subCategoryItemClickListener.onDeletePressed(result)
            }
            itemBinding.editDetails.setOnClickListener {
                subCategoryItemClickListener.onEditPressed(result)
            }
            itemBinding.root.setOnClickListener {
                itemBinding.group.isVisible = true
            }
            itemBinding.activated.setOnClickListener {
                subCategoryItemClickListener.activateAndDeactivate(result.subCategoryId,result.isActivated,result.categoryId)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(list: List<SubCategoryEntity>){
         data=list
         notifyDataSetChanged()
    }

    }