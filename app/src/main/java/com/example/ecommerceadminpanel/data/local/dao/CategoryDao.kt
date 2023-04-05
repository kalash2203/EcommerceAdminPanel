package com.example.ecommerceadminpanel.data.local.dao

import androidx.room.*
import com.example.ecommerceadminpanel.data.local.entity.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: CategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSubCategory(subCategory: SubCategoryEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(productEntity: ProductEntity)

    @Query("SELECT * FROM category")
    fun getCategories(): Flow<List<CategoryEntity>>

    @Transaction
    @Query("SELECT * FROM category WHERE categoryId = :categoryId")
     fun getSubCategoryWithCategory(categoryId: Int): Flow<CategoryWithSubCategory>

    @Transaction
    @Query("SELECT * FROM subCategory WHERE subCategoryId = :subCategoryId")
     fun getProductsWithSubCategory(subCategoryId: Int): Flow<SubCategoryWithProduct>

    @Delete
    suspend fun deleteCategory(category: CategoryEntity)

    @Delete
    suspend fun deleteSubCategory(subCategory: SubCategoryEntity)

    @Delete
    suspend fun deleteProduct(productEntity: ProductEntity)

    @Update
    suspend fun updateCategory(category: CategoryEntity)

    @Update
    suspend fun updateSubCategory(subCategory: SubCategoryEntity)

    @Update
    suspend fun updateProduct(productEntity: ProductEntity)

    @Query("UPDATE category SET isActivated = :isActivated WHERE categoryId = :categoryId")
    suspend fun activateCategory(categoryId: Int,isActivated:Boolean)

    @Query("UPDATE subCategory SET isActivated = :isActivated WHERE subCategoryId = :subCategoryId")
    suspend fun activateSubCategory(subCategoryId: Int,isActivated:Boolean)

    @Query("UPDATE product SET isActivated = :isActivated WHERE id = :id")
    suspend fun activateProduct(id: Int,isActivated:Boolean)

}