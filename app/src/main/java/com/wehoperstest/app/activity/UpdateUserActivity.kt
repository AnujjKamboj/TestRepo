package com.wehoperstest.app.activity

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.wehoperstest.app.R
import com.wehoperstest.app.camera.BottomSheetFragment
import com.wehoperstest.app.extension.getTextValue
import com.wehoperstest.app.extension.isEmptyCheck
import com.wehoperstest.app.model.login_data.UserData
import com.wehoperstest.app.model.signup_data.SignUpWrapper
import com.wehoperstest.app.retrofit.ApiInterface
import com.wehoperstest.app.retrofit.ServiceGenerator
import com.wehoperstest.app.utils.BottomSheetImageCallBack
import com.wehoperstest.app.utils.FileUtils
import com.wehoperstest.app.utils.PermissionCallBack
import kotlinx.android.synthetic.main.activity_sign_up.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class UpdateUserActivity : BaseActivity(), BottomSheetImageCallBack.ImageCallBackListener {
    var file: File? = null
    var uri: Uri? = null

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_user)
        init()
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun init() {
        val userData: UserData = store.getObject("userData", UserData::class.java)!!
        setData(userData)
        BottomSheetImageCallBack.getInstance(this).setImageCallBackListener(this)

        selectImageBT.setOnClickListener {
            val bottomSheetDialogFragment = BottomSheetFragment()
            bottomSheetDialogFragment.show(supportFragmentManager, "BottomSheet")
        }
        submitBT.setOnClickListener {
            hitRegisterApi()
        }
    }

    private fun setData(userData: UserData) {
        nameET.setText(userData.name)
        areaET.setText(userData.area)
        phoneNumberET.setText(userData.phone)
        stateET.setText(userData.state)
        cityET.setText(userData.city)
        Glide.with(this).load(userData.image).into(profileIV)
    }

    private fun isValidParms(): Boolean {
        if (isEmptyCheck(nameET)) {
            showMessage("Please enter your Name")
        } else if (isEmptyCheck(areaET)) {
            showMessage("Please enter your Area")
        } else if (isEmptyCheck(phoneNumberET)) {
            showMessage("Please enter your Phone number")
        } else if (isEmptyCheck(passwordET)) {
            showMessage("Please enter your Password")
        } else if (uri == null) {
            showMessage("Please select your profile Image")
        } else {
            return true
        }

        return false
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == 102) {
                if (data != null) {
                    uri = data.data
                    var intent = Intent(this, ImageCropperActivity::class.java)
                    intent.putExtra("imageUri", uri)
                    startActivity(intent)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        PermissionCallBack.getInstance(this)
            .onPermissionListener(requestCode, permissions, grantResults)
    }

    override fun onImageCallBackCallBack(file: File?, filePath: String?, fileUri: Uri?) {
        uri = fileUri
        profileIV.setImageURI(fileUri)
    }

    private fun hitRegisterApi() {
        val call = register()
        call.enqueue(object : Callback<SignUpWrapper> {
            override fun onResponse(
                call: Call<SignUpWrapper>?,
                response: Response<SignUpWrapper>?
            ) {
                var signUpWrapper = response?.body()
                if (response!!.isSuccessful) {
                    startActivity(Intent(this@UpdateUserActivity, LoginActivity::class.java))
                } else {
                    showMessage(response.message())
                }
            }

            override fun onFailure(call: Call<SignUpWrapper>?, t: Throwable?) {
                showMessage(t!!.localizedMessage)
            }
        })
    }

    private fun register(): Call<SignUpWrapper> {
        val apiInterface = ServiceGenerator.createService(
            ApiInterface::class.java,
            store.getString("token", ""),
            ""
        )

        var image: MultipartBody.Part? = null
        if (uri != null) {
            val file: File = FileUtils.getFile(this!!, uri)
            val requestFile1 =
                RequestBody.create(MediaType.parse("image/*"), file)
            image =
                MultipartBody.Part.createFormData("image", file.name, requestFile1)
        }

        val name: RequestBody = RequestBody.create(MultipartBody.FORM, getTextValue(nameET))
        val area: RequestBody = RequestBody.create(MultipartBody.FORM, getTextValue(areaET))
        val password: RequestBody = RequestBody.create(MultipartBody.FORM, getTextValue(passwordET))
        val phone: RequestBody = RequestBody.create(MultipartBody.FORM, getTextValue(phoneNumberET))
        val state: RequestBody = RequestBody.create(MultipartBody.FORM, getTextValue(stateET) ?: "")
        val city: RequestBody = RequestBody.create(MultipartBody.FORM, getTextValue(cityET) ?: "")

        return apiInterface.update(
            name,
            area,
            phone,
            password,
            state,
            city,
            image
        )
    }
}