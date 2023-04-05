package com.example.ecommerceadminpanel.data.local.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = "product")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val id:Int=0,
    val name:String,
    val image: Bitmap,
    val quantity:Int=0,
    val description : String,
    val isActivated : Boolean=true,
    val subCategoryId : Int
)

