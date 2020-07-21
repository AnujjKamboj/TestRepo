package com.wehoperstest.app.camera


import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Matrix
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Rational
import android.util.Size
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.Nullable
import androidx.annotation.RequiresApi
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import com.wehoperstest.app.activity.ImageCropperActivity
import com.wehoperstest.app.utils.PermissionCallBack
import kotlinx.android.synthetic.main.dialog_camera.*
import java.io.File
import com.wehoperstest.app.R

/**
 * A simple [Fragment] subclass.
 */
class CameraDialog : DialogBaseFragment(), View.OnClickListener,
    PermissionCallBack.PermissionListener {

    private var isBackCamera: Boolean? = true
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onPermissionCallBack(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    activity!!,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                )
                    .show()
                ActivityCompat.requestPermissions(
                    activity!!,
                    REQUIRED_PERMISSIONS,
                    REQUEST_CODE_PERMISSIONS
                );
            }
        }
    }


    private var imageUri: Uri? = null
    private val REQUEST_CODE_PERMISSIONS = 101
    private val REQUIRED_PERMISSIONS =
        arrayOf("android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE")

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle);

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialog_camera, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
    }

    override fun onStart() {
        super.onStart()
        val dialog = dialog
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog.window?.setLayout(width, height)
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun init() {
        if (allPermissionsGranted()) {
            startCamera() //start camera if permission has been granted by user
        } else {
            ActivityCompat.requestPermissions(
                activity!!,
                REQUIRED_PERMISSIONS,
                REQUEST_CODE_PERMISSIONS
            );
        }
//        selectTV.setOnClickListener(this)
//        cancelTV.setOnClickListener(this)
        cameraRotateIV.setOnClickListener(this)

        PermissionCallBack.getInstance(baseActivity!!).setButtonListener(this)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onClick(v: View?) {
        when (v) {
//            cancelTV -> {
//                selectImageRL.visibility = View.GONE
//                view_finder.visibility = View.VISIBLE
//                imgCapture.visibility = View.VISIBLE
//            }
//            selectTV -> {
//                var intent = Intent(baseActivity, ImageCropperActivity::class.java)
//                intent.putExtra("imageUri", imageUri)
//                startActivity(intent)
//                dismiss()
//            }
            cameraRotateIV -> {
                CameraX.unbindAll()
                if (isBackCamera!!) {
                    isBackCamera = false
                    startFontCamera()
                } else {
                    isBackCamera = true
                    startCamera()
                }
            }
        }
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startCamera() {

        CameraX.unbindAll()

        val aspectRatio = Rational(view_finder!!.getWidth(), view_finder!!.getHeight())
        val screen = Size(view_finder!!.getWidth(), view_finder!!.getHeight()) //size of the screen


        val pConfig = PreviewConfig.Builder()
            .setTargetAspectRatio(aspectRatio)
            .setTargetResolution(screen)
            //.setLensFacing(CameraX.LensFacing.FRONT)
            .build()
        val preview = Preview(pConfig)

        preview.setOnPreviewOutputUpdateListener(
            object : Preview.OnPreviewOutputUpdateListener {
                override fun onUpdated(output: Preview.PreviewOutput) {
                    val parent = view_finder!!.getParent() as ViewGroup
                    parent.removeView(view_finder)
                    parent.addView(view_finder, 0)

                    view_finder!!.setSurfaceTexture(output.getSurfaceTexture())
                    updateTransform()
                }
            })


        val imageCaptureConfig =
            ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(baseActivity!!.getWindowManager().getDefaultDisplay().getRotation())
                .build()
        val imgCap = ImageCapture(imageCaptureConfig)

        imgCapture.setOnClickListener(View.OnClickListener {
            var path =
                android.os.Environment.getExternalStorageDirectory().toString() + "/" + System.currentTimeMillis() + ".png"
            val file = File(path)
            imgCap.takePicture(file, object : ImageCapture.OnImageSavedListener {
                override fun onImageSaved(@NonNull file: File) {

                    imageUri = Uri.fromFile(File(file.absolutePath))
//                    imageIV.setImageURI(imageUri)
//                    selectImageRL.visibility = View.VISIBLE
//                    view_finder.visibility = View.GONE
//                    imgCapture.visibility = View.GONE
                    var intent = Intent(baseActivity, ImageCropperActivity::class.java)
                    intent.putExtra("imageUri", imageUri)
                    startActivity(intent)
                    dismiss()

//                    val msg = "Pic captured at " + file.absolutePath
//                    Toast.makeText(baseActivity!!, msg, Toast.LENGTH_LONG).show()
//                    dismiss()
                }

                override fun onError(@NonNull useCaseError: ImageCapture.UseCaseError, @NonNull message: String, @Nullable cause: Throwable?) {
                    val msg = "Pic capture failed : $message"
                    Toast.makeText(baseActivity!!, msg, Toast.LENGTH_LONG).show()
                    cause?.printStackTrace()
                }
            })
        })

        //bind to lifecycle:
        CameraX.bindToLifecycle(this as LifecycleOwner, preview, imgCap)
    }

    @SuppressLint("RestrictedApi")
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun startFontCamera() {

        CameraX.unbindAll()

        val aspectRatio = Rational(view_finder!!.getWidth(), view_finder!!.getHeight())
        val screen = Size(view_finder!!.getWidth(), view_finder!!.getHeight()) //size of the screen


        val pConfig = PreviewConfig.Builder()
            .setTargetAspectRatio(aspectRatio)
            .setTargetResolution(screen)
            .setLensFacing(CameraX.LensFacing.FRONT)
            .build()
        val preview = Preview(pConfig)

        preview.setOnPreviewOutputUpdateListener(
            object : Preview.OnPreviewOutputUpdateListener {
                override fun onUpdated(output: Preview.PreviewOutput) {
                    val parent = view_finder!!.getParent() as ViewGroup
                    parent.removeView(view_finder)
                    parent.addView(view_finder, 0)

                    view_finder!!.setSurfaceTexture(output.getSurfaceTexture())
                    updateTransform()
                }
            })


        val imageCaptureConfig =
            ImageCaptureConfig.Builder().setCaptureMode(ImageCapture.CaptureMode.MIN_LATENCY)
                .setTargetRotation(baseActivity!!.getWindowManager().getDefaultDisplay().getRotation())
                .build()
        val imgCap = ImageCapture(imageCaptureConfig)

        imgCapture.setOnClickListener(View.OnClickListener {
            var path =
                android.os.Environment.getExternalStorageDirectory().toString() + "/" + System.currentTimeMillis() + ".png"
            val file = File(path)
            imgCap.takePicture(file, object : ImageCapture.OnImageSavedListener {
                override fun onImageSaved(@NonNull file: File) {

                    imageUri = Uri.fromFile(File(file.absolutePath))
//                    imageIV.setImageURI(imageUri)
//                    selectImageRL.visibility = View.VISIBLE
//                    view_finder.visibility = View.GONE
//                    imgCapture.visibility = View.GONE
                    var intent = Intent(baseActivity, ImageCropperActivity::class.java)
                    intent.putExtra("imageUri", imageUri)
                    startActivity(intent)
                    dismiss()

//                    val msg = "Pic captured at " + file.absolutePath
//                    Toast.makeText(baseActivity!!, msg, Toast.LENGTH_LONG).show()
//                    dismiss()
                }

                override fun onError(@NonNull useCaseError: ImageCapture.UseCaseError, @NonNull message: String, @Nullable cause: Throwable?) {
                    val msg = "Pic capture failed : $message"
                    Toast.makeText(baseActivity!!, msg, Toast.LENGTH_LONG).show()
                    cause?.printStackTrace()
                }
            })
        })

        //bind to lifecycle:
        CameraX.bindToLifecycle(this as LifecycleOwner, preview, imgCap)
    }

    private fun updateTransform() {
        val mx = Matrix()
        val w = view_finder!!.getMeasuredWidth().toFloat()
        val h = view_finder!!.getMeasuredHeight().toFloat()

        val cX = w / 2f
        val cY = h / 2f

        val rotationDgr: Int
        val rotation = view_finder!!.getRotation().toInt()

        when (rotation) {
            Surface.ROTATION_0 -> rotationDgr = 0
            Surface.ROTATION_90 -> rotationDgr = 90
            Surface.ROTATION_180 -> rotationDgr = 180
            Surface.ROTATION_270 -> rotationDgr = 270
            else -> return
        }

        mx.postRotate(rotationDgr.toFloat(), cX, cY)
        view_finder!!.setTransform(mx)
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {

        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (allPermissionsGranted()) {
                startCamera()
            } else {
                Toast.makeText(
                    activity!!,
                    "Permissions not granted by the user.",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
    }

    private fun allPermissionsGranted(): Boolean {

        for (permission in REQUIRED_PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    activity!!,
                    permission
                ) !== PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

}
