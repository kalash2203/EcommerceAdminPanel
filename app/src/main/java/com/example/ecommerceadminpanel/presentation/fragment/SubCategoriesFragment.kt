package com.example.ecommerceadminpanel.presentation.fragment

import android.app.Activity
import android.app.Dialog
import android.graphics.Bitmap
import android.provider.MediaStore
import android.util.Log
import android.view.Window
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.ecommerceadminpanel.R
import com.example.ecommerceadminpanel.data.local.entity.SubCategoryEntity
import com.example.ecommerceadminpanel.databinding.FragmentSubCategoriesBinding
import com.example.ecommerceadminpanel.presentation.adapter.SubCategoryListAdapter
import com.example.ecommerceadminpanel.presentation.viewModel.SubCategoryViewModel
import com.example.ecommerceadminpanel.utils.SubCategoryItemClickListener
import com.example.ecommerceadminpanel.utils.collectLatestLifeCycleFlow
import com.example.ecommerceadminpanel.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SubCategoriesFragment : BindingFragment<FragmentSubCategoriesBinding>(R.layout.fragment_sub_categories),SubCategoryItemClickListener {


    private val viewModel: SubCategoryViewModel by viewModels()
    private lateinit var recordsListAdapter: SubCategoryListAdapter
    private val args: SubCategoriesFragmentArgs by navArgs()
    private lateinit var subCategoryEntity: SubCategoryEntity


    override fun initViews() {

        viewModel.getAllSubCategories(args.categoryId)

        recordsListAdapter = SubCategoryListAdapter(this, emptyList(),requireContext())
        binding.homeRv.apply {
            adapter = recordsListAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        binding.addBtn.setOnClickListener {
            showDialog()
        }


        collectLatestLifeCycleFlow(viewModel.subCategoryList){ list ->
              Log.d("List",list.toString())
              recordsListAdapter.submitData(list.subCategoryList)
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
        if (::subCategoryEntity.isInitialized){
            viewModel.imageUri="".toUri()
            categoryTitle.setText(subCategoryEntity.name)
            dialogImage.setImageBitmap(subCategoryEntity.image)
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
                subCategoryEntity.image
            }
            if (title.length < 3) {
                categoryTitle.error = "too short"
            }  else {
                if (::subCategoryEntity.isInitialized){
                    viewModel.updateSubCategory(
                        SubCategoryEntity(
                            categoryId = subCategoryEntity.categoryId,
                            name = categoryTitle.text.toString(),
                            image = image,
                            subCategoryId = subCategoryEntity.subCategoryId
                        )
                    )
                }else {

                    viewModel.insertSubCategory(
                        SubCategoryEntity(
                            name = categoryTitle.text.toString(),
                            image = image,
                            categoryId = args.categoryId
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

    override fun onDeletePressed(subCategoryEntity: SubCategoryEntity) {
        viewModel.deleteSubCategory(subCategoryEntity)
    }

    override fun onEditPressed(subCategoryEntity: SubCategoryEntity) {
        this.subCategoryEntity = subCategoryEntity
        showDialog()
    }

    override fun onViewItemPressed(id: Int) {
        findNavController().navigate(
            SubCategoriesFragmentDirections.actionSubCategoriesFragmentToProductFragment(id)
        )
    }

    override fun activateAndDeactivate(id: Int,isActive:Boolean,categoryId: Int) {
        changeVisibility(id, isActive,categoryId)
    }

    private fun changeVisibility(id:Int,isActive: Boolean,categoryId: Int) {
        val alertDialog: AlertDialog.Builder = AlertDialog.Builder(requireContext())
        alertDialog.setTitle("Activate/Deactivate")
        alertDialog.setMessage("Are You Sure You Want To ${if (!isActive) "Activate" else "Deactivate"}")
        alertDialog.setNegativeButton(
            "Cancel"
        ) { _, _ ->
            showToast("Cancelled")
        }
        alertDialog.setPositiveButton("Yes"){ _,_->
            updateCategory(id, isActive,categoryId)
        }
        val alert: AlertDialog = alertDialog.create()
        alert.setCanceledOnTouchOutside(false)
        alert.show()
    }

    private fun updateCategory(id: Int, isActive: Boolean,categoryId:Int){
        viewModel.activateSubCategory(id,!isActive,categoryId)
    }


}
