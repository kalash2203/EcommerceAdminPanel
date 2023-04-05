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
import com.example.ecommerceadminpanel.data.local.entity.ProductEntity
import com.example.ecommerceadminpanel.databinding.FragmentProductBinding
import com.example.ecommerceadminpanel.presentation.adapter.ProductListAdapter
import com.example.ecommerceadminpanel.presentation.viewModel.ProductViewModel
import com.example.ecommerceadminpanel.utils.ProductItemClickListener
import com.example.ecommerceadminpanel.utils.collectLatestLifeCycleFlow
import com.example.ecommerceadminpanel.utils.showToast
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ProductFragment : BindingFragment<FragmentProductBinding>(R.layout.fragment_product),ProductItemClickListener {


    private val viewModel: ProductViewModel by viewModels()
    lateinit var recordsListAdapter: ProductListAdapter
    val args: ProductFragmentArgs by navArgs()
    private lateinit var productEntity: ProductEntity


    override fun initViews() {
        viewModel.getAllProduct(args.subCategoryId)

        recordsListAdapter = ProductListAdapter(this, emptyList(),requireContext())
        binding.homeRv.apply {
            adapter = recordsListAdapter
            layoutManager = LinearLayoutManager(requireActivity())
        }

        binding.addBtn.setOnClickListener {
            showDialog()
        }


        collectLatestLifeCycleFlow(viewModel.productList){ list ->
              Log.d("List",list.toString())
            recordsListAdapter.submitData(list.productList)

        }
        collectLatestLifeCycleFlow(viewModel.toast){ message ->
            showToast(message)
        }

    }

    private fun showDialog() {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.add_product_dialog)
        val productTitle = dialog.findViewById(R.id.dialogTitle) as EditText
        val productQuantity = dialog.findViewById(R.id.dialogQuantity) as EditText
        val productDesc = dialog.findViewById(R.id.dialogDescription) as EditText
        val addBtn = dialog.findViewById(R.id.cd_addBtn) as Button
        val noBtn = dialog.findViewById(R.id.cd_close) as ImageView
        val dialogImage = dialog.findViewById(R.id.cdImageView) as ImageView
        if (::productEntity.isInitialized){
            viewModel.imageUri="".toUri()
            productTitle.setText(productEntity.name)
            dialogImage.setImageBitmap(productEntity.image)
            productQuantity.setText(productEntity.quantity.toString())
            productDesc.setText(productEntity.description)
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
            val title = productTitle.text.toString().trim()
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
                productEntity.image
            }
            if (title.length < 3) {
                productTitle.error = "too short"
            }  else {
                if (::productEntity.isInitialized){
                    viewModel.updateProduct(
                        ProductEntity(
                            id = productEntity.id,
                            name = productTitle.text.toString(),
                            image = image,
                            quantity = productQuantity.text.toString().toInt(),
                            description = productDesc.text.toString(),
                            subCategoryId = productEntity.subCategoryId
                        )
                    )
                }else {

                    viewModel.insertProduct(
                        ProductEntity(
                            name = productTitle.text.toString(),
                            description = productDesc.text.toString(),
                            quantity = productQuantity.text.toString().toInt(),
                            image = image,
                            subCategoryId = args.subCategoryId
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


    override fun onDeletePressed(productEntity: ProductEntity) {
        viewModel.deleteProduct(productEntity)
    }

    override fun onEditPressed(productEntity: ProductEntity) {
        this.productEntity = productEntity
        showDialog()
    }

    override fun activateAndDeactivate(id: Int, isActive:Boolean, subCategoryId: Int) {
        changeVisibility(id, isActive,subCategoryId)
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
        viewModel.activateProduct(id,!isActive,categoryId)
    }

    }
