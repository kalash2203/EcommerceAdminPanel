package com.example.ecommerceadminpanel.domain.repository

import com.example.ecommerceadminpanel.data.local.entity.*
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    suspend fun insertCategory(category: CategoryEntity) : Boolean
    suspend fun getAllCategories() : Flow<List<CategoryEntity>>
    suspend fun deleteCategoryById(category: CategoryEntity) : Boolean
    suspend fun updateCategory(category: CategoryEntity) : Boolean
    suspend fun activateCategory(id: Int,isActivated:Boolean) : Boolean

    suspend fun insertSubCategory(category: SubCategoryEntity) : Boolean
    suspend fun getAllSubCategories(categoryId:Int) : Flow<CategoryWithSubCategory>
    suspend fun deleteSubCategoryById(category: SubCategoryEntity) : Boolean
    suspend fun updateSubCategory(category: SubCategoryEntity) : Boolean
    suspend fun activateSubCategory(id: Int,isActivated:Boolean) : Boolean


    suspend fun insertProduct(product:ProductEntity) : Boolean
    suspend fun getAllProduct(subCategoryId:Int) : Flow<SubCategoryWithProduct>
    suspend fun deleteProductById(product: ProductEntity) : Boolean
    suspend fun updateProduct(product: ProductEntity) : Boolean
    suspend fun activateProduct(id: Int,isActivated:Boolean) : Boolean



}