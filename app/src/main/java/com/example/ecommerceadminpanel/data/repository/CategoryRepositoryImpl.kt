package com.example.ecommerceadminpanel.data.repository

import com.example.ecommerceadminpanel.data.local.RecordDatabase
import com.example.ecommerceadminpanel.data.local.entity.*
import com.example.ecommerceadminpanel.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val db: RecordDatabase
) : CategoryRepository {
    override suspend fun getAllCategories(): Flow<List<CategoryEntity>> {
        return db.getCategories().getCategories()
    }

    override suspend fun insertCategory(category: CategoryEntity): Boolean {
        return try {
            db.getCategories().insertCategory(category)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun deleteCategoryById(category: CategoryEntity): Boolean {
        return try {
            db.getCategories().deleteCategory(category = category )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun insertProduct(product: ProductEntity): Boolean {
        return try {
            db.getCategories().insertProduct(product)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getAllProduct(subCategoryId: Int): Flow<SubCategoryWithProduct> {
        return db.getCategories().getProductsWithSubCategory(subCategoryId)
    }

    override suspend fun deleteProductById(product: ProductEntity): Boolean {
        return try {
            db.getCategories().deleteProduct(product )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun updateProduct(product: ProductEntity): Boolean {
        return try {
            db.getCategories().updateProduct(product)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun updateCategory(category: CategoryEntity): Boolean {
        return try {
            db.getCategories().updateCategory(category)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun insertSubCategory(category: SubCategoryEntity): Boolean {
        return try {
            db.getCategories().insertSubCategory(category)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun getAllSubCategories(categoryId:Int): Flow<CategoryWithSubCategory> {
        return db.getCategories().getSubCategoryWithCategory(categoryId)
    }

    override suspend fun deleteSubCategoryById(category: SubCategoryEntity): Boolean {
        return try {
            db.getCategories().deleteSubCategory(category )
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun updateSubCategory(category: SubCategoryEntity): Boolean {
        return try {
            db.getCategories().updateSubCategory(category)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun activateCategory(id: Int, isActivated: Boolean): Boolean {
        return try {
            db.getCategories().activateCategory(id,isActivated)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun activateSubCategory(id: Int, isActivated: Boolean): Boolean {
        return try {
            db.getCategories().activateSubCategory(id,isActivated)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun activateProduct(id: Int, isActivated: Boolean): Boolean {
        return try {
            db.getCategories().activateProduct(id,isActivated)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}