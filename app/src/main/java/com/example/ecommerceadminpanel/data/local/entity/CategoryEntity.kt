package com.example.ecommerceadminpanel.data.local.entity

import android.graphics.Bitmap
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey(autoGenerate = true)
    val categoryId:Int=0,
    val name:String,
    val image:Bitmap,
    val isActivated : Boolean=true,
)

