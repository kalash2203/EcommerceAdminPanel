package com.example.ecommerceadminpanel.presentation.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommerceadminpanel.data.local.entity.*
import com.example.ecommerceadminpanel.domain.repository.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val _productsList = MutableSharedFlow<SubCategoryWithProduct>()
    val productList = _productsList.asSharedFlow()
    private val _toast = MutableSharedFlow<String>()
    val toast = _toast.asSharedFlow()

    var imageUri: Uri? = null



    fun insertProduct(productEntity: ProductEntity) {
        viewModelScope.launch {
            val isProductInserted = categoryRepository.insertProduct(productEntity)

            if (!isProductInserted) {
                _toast.emit("Something went wrong")
                return@launch
            }
            _toast.emit("Product inserted")

        }
    }

    fun updateProduct(productEntity: ProductEntity) {
        viewModelScope.launch {
            val isProductUpdated = categoryRepository.updateProduct(productEntity)

            if (!isProductUpdated) {
                _toast.emit("Something went wrong")
                return@launch
            }
            _toast.emit("Product updated")
            getAllProduct(productEntity.subCategoryId)

        }
    }

    fun deleteProduct(productEntity: ProductEntity) {
        viewModelScope.launch {
            val isProductDeleted = categoryRepository.deleteProductById(productEntity)

            if (!isProductDeleted) {
                _toast.emit("Something went wrong")
                return@launch
            }
            _toast.emit("Product deleted")
            getAllProduct(productEntity.subCategoryId)

        }
    }
    fun activateProduct(id: Int, isActivated:Boolean, categoryId: Int){
        viewModelScope.launch {
            val isProductUpdated = categoryRepository.activateProduct(id,isActivated)

            if (!isProductUpdated) {
                _toast.emit("Something went wrong")
                return@launch
            }
            _toast.emit("Product updated")
            getAllProduct(categoryId)
        }
    }


    fun getAllProduct(subCategoryId:Int) {
        viewModelScope.launch {
            categoryRepository.getAllProduct(subCategoryId).collectLatest {
                _productsList.emit(  it)
            }
        }
    }
}