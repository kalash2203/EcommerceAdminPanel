package com.example.ecommerceadminpanel.presentation.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceadminpanel.data.local.entity.CategoryWithSubCategory
import com.example.ecommerceadminpanel.data.local.entity.SubCategoryEntity
import com.example.ecommerceadminpanel.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SubCategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _subCategoryList = MutableSharedFlow<CategoryWithSubCategory>()
    val subCategoryList = _subCategoryList.asSharedFlow()
    private val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    var imageUri: Uri? = null




    fun insertSubCategory(categoryEntity: SubCategoryEntity) {
        viewModelScope.launch {
            val isSubCategoryInserted = categoryRepository.insertSubCategory(categoryEntity)

            if (!isSubCategoryInserted) {
                _toast.emit("Something went wrong")
                return@launch
            }
            _toast.emit("Sub Category inserted")

        }
    }
    fun updateSubCategory(subCategoryEntity: SubCategoryEntity) {
        viewModelScope.launch {
            val isSubCategoryUpdated = categoryRepository.updateSubCategory(subCategoryEntity)

            if (!isSubCategoryUpdated) {
                _toast.emit("Something went wrong")
                return@launch
            }
            _toast.emit("Sub Category updated")
            getAllSubCategories(subCategoryEntity.categoryId)

        }
    }

    fun deleteSubCategory(subCategoryEntity: SubCategoryEntity) {
        viewModelScope.launch {
            val isSubCategoryDeleted = categoryRepository.deleteSubCategoryById(subCategoryEntity)

            if (!isSubCategoryDeleted) {
                _toast.emit("Something went wrong")
                return@launch
            }
            _toast.emit("Sub Category deleted")
           getAllSubCategories(subCategoryEntity.categoryId)

        }
    }
    fun activateSubCategory(id: Int, isActivated:Boolean, categoryId: Int){
        viewModelScope.launch {
            val isSubCategoryUpdated = categoryRepository.activateSubCategory(id,isActivated)

            if (!isSubCategoryUpdated) {
                _toast.emit("Something went wrong")
                return@launch
            }
            _toast.emit("Sub Category updated")
           getAllSubCategories(categoryId)
        }
    }

     fun getAllSubCategories(categoryId:Int) {
        viewModelScope.launch {
            categoryRepository.getAllSubCategories(categoryId).collectLatest {
                _subCategoryList.emit(  it)
            }
        }
    }
}