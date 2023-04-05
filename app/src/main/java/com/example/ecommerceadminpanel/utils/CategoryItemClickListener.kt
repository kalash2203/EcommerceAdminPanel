package com.example.ecommerceadminpanel.utils

import com.example.ecommerceadminpanel.data.local.entity.CategoryEntity
import com.example.ecommerceadminpanel.data.local.entity.SubCategoryEntity

interface CategoryItemClickListener {
    fun onDeletePressed(categoryEntity: CategoryEntity)
    fun onEditPressed(categoryEntity: CategoryEntity)
    fun onViewItemPressed(id:Int)
    fun activateAndDeactivate(id: Int,isActive:Boolean)
}