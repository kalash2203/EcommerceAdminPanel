package com.example.ecommerceadminpanel.data.local.entity

import androidx.room.Embedded
import androidx.room.Relation

data class SubCategoryWithProduct(

@Embedded
val subCategoryEntity: SubCategoryEntity,
@Relation(
    parentColumn = "subCategoryId",
    entityColumn = "subCategoryId"

)
val productList: List<ProductEntity>

)
