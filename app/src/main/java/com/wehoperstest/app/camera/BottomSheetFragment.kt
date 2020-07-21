package com.wehoperstest.app.camera

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.wehoperstest.app.R
import com.wehoperstest.app.activity.ImageCropperActivity
import com.wehoperstest.app.utils.GalleryImageCallBack
import com.wehoperstest.app.utils.PermissionCallBack
import kotlinx.android.synthetic.main.dialog_select_option.*


class BottomSheetFragment : BaseDialogFragment(), View.OnClickListener,
    GalleryImageCallBack.GalleryImageListener, PermissionCallBack.PermissionListener {


    private var outputfileUri: Uri? = null

    override fun onClick(v: View?) {
        when (v) {
            cameraTV -> {
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    var cameraDialog = CameraDialog()
                    cameraDialog.show(baseActivity!!.supportFragmentManager, "cameraTag")
                    dismiss()
                } else {
                    Toast.makeText(
                        baseActivity,
                        "Your device not supported this camera",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            galleryTV -> {
                requestGalleryPermission()
                dismiss()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.dialog_select_option, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    private fun init() {
        galleryTV.setOnClickListener(this)
        cameraTV.setOnClickListener(this)
        selectLL.setOnClickListener(this)

        GalleryImageCallBack.getInstance(baseActivity!!).setGalleryImageListener(this)

    }

    fun requestGalleryPermission() {
        PermissionCallBack.getInstance(baseActivity!!).setButtonListener(this)
        if (ContextCompat.checkSelfPermission(
                baseActivity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
            || ContextCompat.checkSelfPermission(
                baseActivity!!,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                baseActivity!!, arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ), 101
            )
        } else {
            startGalleryIntent()
        }
    }

    fun startGalleryIntent() {
        val galleryIntent = Intent(Intent.ACTION_PICK)
        galleryIntent.type = "image/*"
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        baseActivity!!.startActivityForResult(
            Intent.createChooser(galleryIntent, "Select Picture"),
            102
        )
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 102) {
                if (data != null) {
                    outputfileUri = data.data
                    var intent = Intent(baseActivity, ImageCropperActivity::class.java)
                    intent.putExtra("imageUri", outputfileUri)
                    baseActivity!!.startActivity(intent)
                    dismiss()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> if (ContextCompat.checkSelfPermission(
                    baseActivity!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    baseActivity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startGalleryIntent()
            }
        }
    }

    override fun onGalleryImageCallBack(requestCode: Int, resultCode: Int, data: Intent?) {
//        if (resultCode == Activity.RESULT_OK) {
//            if (requestCode == AppConstants.SELECT_FILE) {
        if (data != null) {
            outputfileUri = data.data
            var intent = Intent(baseActivity, ImageCropperActivity::class.java)
            intent.putExtra("imageUri", outputfileUri)
            baseActivity!!.startActivity(intent)
            dismiss()
        }
//            }
//        }
    }

    override fun onPermissionCallBack(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            101 -> if (ContextCompat.checkSelfPermission(
                    baseActivity!!,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(
                    baseActivity!!,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                startGalleryIntent()
            }
        }
    }


}