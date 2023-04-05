package com.example.ecommerceadminpanel.utils

import com.example.ecommerceadminpanel.data.local.entity.CategoryEntity
import com.example.ecommerceadminpanel.data.local.entity.SubCategoryEntity

interface SubCategoryItemClickListener {
    fun onDeletePressed(subCategoryEntity: SubCategoryEntity)
    fun onEditPressed(subCategoryEntity: SubCategoryEntity)
    fun onViewItemPressed(id:Int)
    fun activateAndDeactivate(id: Int,isActive:Boolean,categoryId:Int)
}