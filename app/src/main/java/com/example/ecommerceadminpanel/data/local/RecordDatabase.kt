package com.example.ecommerceadminpanel.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.ecommerceadminpanel.data.local.convertors.BitmapConvertor
import com.example.ecommerceadminpanel.data.local.dao.CategoryDao
import com.example.ecommerceadminpanel.data.local.entity.CategoryEntity
import com.example.ecommerceadminpanel.data.local.entity.ProductEntity
import com.example.ecommerceadminpanel.data.local.entity.SubCategoryEntity

@Database(entities = [CategoryEntity::class,SubCategoryEntity::class,ProductEntity::class], version = 1)
@TypeConverters(BitmapConvertor::class)
abstract class RecordDatabase  : RoomDatabase(){
    abstract fun getCategories(): CategoryDao
}