package com.example.ecommerceadminpanel.presentation.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceadminpanel.data.local.entity.CategoryEntity
import com.example.ecommerceadminpanel.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CategoryViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _categoryList = MutableStateFlow<List<CategoryEntity>>(emptyList())
    val categoryList = _categoryList.asStateFlow()
    private val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    var imageUri: Uri? = null

    init {
        getAllCategories()
    }


    fun updateCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            val isCategoryUpdated = categoryRepository.updateCategory(categoryEntity)

            if (!isCategoryUpdated) {
                _toast.emit("Something went wrong")
                return@launch
            }
            _toast.emit("Category updated")
            getAllCategories()

        }
    }

    fun deleteCategory(categoryEntity: CategoryEntity) {
        viewModelScope.launch {
            val isCategoryDeleted = categoryRepository.deleteCategoryById(categoryEntity)

            if (!isCategoryDeleted) {
                _toast.emit("Something went wrong")
                return@launch
            }
            _toast.emit("Category deleted")
            getAllCategories()

        }
    }

    fun insertCategory(categoryEntity: CategoryEntity){
        viewModelScope.launch {
            val isCategoryInserted = categoryRepository.insertCategory(categoryEntity)

            if (!isCategoryInserted) {
                _toast.emit("Something went wrong")
                return@launch
            }
            _toast.emit("Category inserted")

        }
    }


    fun activateCategory(id: Int,isActivated:Boolean){
        viewModelScope.launch {
            val isCategoryUpdated = categoryRepository.activateCategory(id,isActivated)

            if (!isCategoryUpdated) {
                _toast.emit("Something went wrong")
                return@launch
            }
            _toast.emit("Category updated")
            getAllCategories()
        }
    }

    private fun getAllCategories() {
        viewModelScope.launch {
            categoryRepository.getAllCategories().collectLatest {
                _categoryList.value = it
            }
        }
    }
}