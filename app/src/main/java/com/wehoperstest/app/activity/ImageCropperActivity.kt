package com.wehoperstest.app.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.theartofdev.edmodo.cropper.CropImage
import com.wehoperstest.app.R
import com.wehoperstest.app.utils.BottomSheetImageCallBack
import com.wehoperstest.app.utils.GalleryImageCallBack


class ImageCropperActivity : AppCompatActivity(), GalleryImageCallBack.GalleryImageListener {

    private lateinit var imageUri: Uri

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_cropper)
        getImageUriIntent()
    }

    private fun getImageUriIntent() {
        if (intent != null) {
            imageUri = intent.extras?.get("imageUri") as Uri
        }
        CropImage.activity(imageUri)
            .start(this)

        GalleryImageCallBack.getInstance(this).setGalleryImageListener(this)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            val result = CropImage.getActivityResult(data)
            if (resultCode == Activity.RESULT_OK) {
                val resultUri = result.uri
                BottomSheetImageCallBack.getInstance(this)
                    .onImageCallBackListener(null, null, resultUri)
                finish()
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                val error = result.error
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onGalleryImageCallBack(requestCode: Int, resultCode: Int, data: Intent?) {

    }
}
