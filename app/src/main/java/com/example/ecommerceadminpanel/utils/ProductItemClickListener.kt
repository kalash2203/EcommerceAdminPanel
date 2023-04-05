package com.example.ecommerceadminpanel.utils

import com.example.ecommerceadminpanel.data.local.entity.CategoryEntity
import com.example.ecommerceadminpanel.data.local.entity.ProductEntity
import com.example.ecommerceadminpanel.data.local.entity.SubCategoryEntity

interface ProductItemClickListener {
    fun onDeletePressed(productEntity: ProductEntity)
    fun onEditPressed(productEntity: ProductEntity)
    fun activateAndDeactivate(id: Int,isActive:Boolean,subCategoryId:Int)
}