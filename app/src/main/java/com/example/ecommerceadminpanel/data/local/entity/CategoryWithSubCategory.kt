package com.example.ecommerceadminpanel.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class CategoryWithSubCategory(
    @Embedded val categoryEntity: CategoryEntity,
    @Relation(
        parentColumn = "categoryId",
        entityColumn = "categoryId"

    )
    val subCategoryList: List<SubCategoryEntity>
)
