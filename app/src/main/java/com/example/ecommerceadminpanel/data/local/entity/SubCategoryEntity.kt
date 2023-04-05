package com.example.ecommerceadminpanel.data.local.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "subCategory")
data class SubCategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val subCategoryId:Int=0,
    val name:String,
    val image: Bitmap,
    val isActivated : Boolean=true,
    val categoryId:Int

)
