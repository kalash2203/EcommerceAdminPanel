package com.example.ecommerceadminpanel.presentation.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.ecommerceadminpanel.R
import com.example.ecommerceadminpanel.data.local.entity.CategoryEntity
import com.example.ecommerceadminpanel.data.local.entity.CategoryWithSubCategory
import com.example.ecommerceadminpanel.data.local.entity.ProductEntity
import com.example.ecommerceadminpanel.data.local.entity.SubCategoryEntity
import com.example.ecommerceadminpanel.databinding.ItemProductBinding
import com.example.ecommerceadminpanel.databinding.ItemRecordBinding
import com.example.ecommerceadminpanel.utils.ProductItemClickListener
import com.example.ecommerceadminpanel.utils.SubCategoryItemClickListener

class ProductListAdapter (
    private val productItemClickListener: ProductItemClickListener,
    var data: List<ProductEntity> = emptyList(),
    val  context: Context
) :
    RecyclerView.Adapter<ProductListAdapter.ProductViewHolder>() {


    class ProductViewHolder(val itemBinding: ViewBinding) :
        RecyclerView.ViewHolder(itemBinding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        return ProductViewHolder(
            ItemProductBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {

        with(holder) {
            val result= data[position]
            (this.itemBinding as ItemProductBinding).name.text = result.name
            itemBinding.image.setImageBitmap(result.image)
            itemBinding.quantity.text = "${result.quantity} ${if (result.quantity>1) "Pieces" else "Piece"}"
            itemBinding.desc.text = result.description
            if(result.isActivated) {
                itemBinding.activated.text = "ACTIVATED"
                itemBinding.activated.setTextColor(context.resources.getColor(R.color.teal_700))
            }else{
                itemBinding.activated.text = "DEACTIVATED"
                itemBinding.activated.setTextColor(context.resources.getColor(R.color.red))
            }
            itemBinding.editDelete.setOnClickListener {
                productItemClickListener.onDeletePressed(result)
            }
            itemBinding.editDetails.setOnClickListener {
                productItemClickListener.onEditPressed(result)
            }
            itemBinding.root.setOnClickListener {
                itemBinding.group.isVisible = true
            }
            itemBinding.activated.setOnClickListener {
                productItemClickListener.activateAndDeactivate(result.id,result.isActivated,result.subCategoryId)
            }
        }

    }

    override fun getItemCount(): Int {
        return data.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun submitData(list: List<ProductEntity>){
         data=list
         notifyDataSetChanged()
    }

    }