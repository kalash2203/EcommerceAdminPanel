package com.example.ecommerceadminpanel.presentation.fragment

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceadminpanel.R
import com.example.ecommerceadminpanel.data.local.entity.CategoryEntity
import com.example.ecommerceadminpanel.databinding.FragmentCategoriesBinding
import com.example.ecommerceadminpanel.presentation.adapter.CategoryListAdapter
import com.example.ecommerceadminpanel.presentation.viewModel.CategoryViewModel
import com.example.ecommerceadminpanel.utils.CategoryItemClickListener
import com.example.ecommerceadminpanel.utils.collectLatestLifeCycleFlow
import com.example.ecommerceadminpanel.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CategoriesFragment : BindingFragment<FragmentCategoriesBinding>(R.layout.fragment_categories) ,CategoryItemClickListener{


    private val viewModel: CategoryViewModel by viewModels()
    private lateinit var categoryListAdapter: CategoryListAdapter
    private lateinit var category:CategoryEntity


    override fun initViews() {

        categoryListAdapter = CategoryListAdapter(this, emptyList(),requireContext())
        binding.homeRv.apply {
            adapter = categoryListAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        binding.addBtn.setOnClickListener {
            showDialog()
        }


        collectLatestLifeCycleFlow(viewModel.categoryList){ list ->
              Log.d("List",list.toString())
              categoryListAdapter.submitData(list)
        }
        collectLatestLifeCycleFlow(viewModel.toast){ message ->
            showToast(message)
        }

    }

    private fun showDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.add_record_dialog)
        val categoryTitle = dialog.findViewById(R.id.dialogTitle) as EditText
        val addBtn = dialog.findViewById(R.id.cd_addBtn) as Button
        val noBtn = dialog.findViewById(R.id.cd_close) as ImageView
        val dialogImage = dialog.findViewById(R.id.cdImageView) as ImageView
        if (::category.isInitialized){
            viewModel.imageUri="".toUri()
            categoryTitle.setText(category.name)
            dialogImage.setImageBitmap(category.image)
        }
        noBtn.setOnClickListener { dialog.dismiss() }
        dialog.show()

        dialogImage.setOnClickListener {
            com.github.dhaval2404.imagepicker.ImagePicker.with(this)
                .compress(1024)         //Final image size will be less than 1 MB(Optional)
                .maxResultSize(
                    1080,
                    720
                )  //Final image resolution will be less than 1080 x 1080(Optional)
                .createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                    dialogImage.setImageURI(viewModel.imageUri)
                }


        }
        addBtn.setOnClickListener {
            val title = categoryTitle.text.toString().trim()
            if (viewModel.imageUri==null) {
                showToast("Please Select Image")
                return@setOnClickListener
            }
            val image: Bitmap = if (viewModel.imageUri!="".toUri()) {
                MediaStore.Images.Media.getBitmap(
                    requireActivity().contentResolver,
                    viewModel.imageUri
                )
            }else{
                category.image
            }

            if (title.length < 3) {
                categoryTitle.error = "too short"
            }  else {
                if (::category.isInitialized){
                    viewModel.updateCategory(
                        CategoryEntity(
                            categoryId = category.categoryId,
                            name = categoryTitle.text.toString(),
                            image = image
                        )
                    )
                }else {

                    viewModel.insertCategory(
                        CategoryEntity(
                            name = categoryTitle.text.toString(),
                            image = image
                        )
                    )
                }
                dialog.dismiss()
            }

        }


    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data

            when (resultCode) {
                Activity.RESULT_OK -> {
                    //Image Uri will not be null for RESULT_OK
                    val fileUri = data?.data!!
                    viewModel.imageUri = fileUri

                }
                com.github.dhaval2404.imagepicker.ImagePicker.RESULT_ERROR -> {
                    showToast(com.github.dhaval2404.imagepicker.ImagePicker.getError(data))
                }
                else -> {
                    showToast("Task Cancelled")
                }
            }
        }

    override fun onDeletePressed(categoryEntity: CategoryEntity) {
    viewModel.deleteCategory(categoryEntity)
    }

    override fun onEditPressed(categoryEntity: CategoryEntity) {
        category = categoryEntity
       showDialog()
    }

    override fun onViewItemPressed(id: Int) {
         findNavController().navigate(
                    CategoriesFragmentDirections.actionCategoriesFragmentToSubCategoriesFragment(id)
                )
    }

    override fun activateAndDeactivate(id: Int,isActive:Boolean) {
              changeVisibility(id, isActive)
    }

    private fun changeVisibility(id:Int,isActive: Boolean) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Activate/Deactivate")
        alertDialog.setMessage("Are You Sure You Want To ${if (!isActive) "Activate" else "Deactivate"}")
        alertDialog.setNegativeButton(
            "Cancel"
        ) { _, _ ->
            showToast("Cancelled")
        }
        alertDialog.setPositiveButton("Yes"){ _,_->
            updateCategory(id, isActive)
        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    private fun updateCategory(id: Int, isActive: Boolean){
        viewModel.activateCategory(id,!isActive)
    }

}
