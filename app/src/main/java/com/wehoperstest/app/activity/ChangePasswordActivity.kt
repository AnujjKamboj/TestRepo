package com.wehoperstest.app.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import com.wehoperstest.app.R
import com.wehoperstest.app.extension.isEmptyCheck
import com.wehoperstest.app.model.change_pass.ChangePasswordEntity
import com.wehoperstest.app.model.login_data.LoginWrapper
import com.wehoperstest.app.retrofit.ApiInterface
import com.wehoperstest.app.retrofit.ServiceGenerator
import kotlinx.android.synthetic.main.activity_change_password.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChangePasswordActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_change_password)
        initParm()
    }

    private fun initParm() {
        submitBT.setOnClickListener {
            if (isValidParms())
                hitChangePasswordApi()
        }
    }

    private fun isValidParms(): Boolean {
        if (isEmptyCheck(oldPasswordET)) {
            showMessage("Please enter old Password")
        } else if (isEmptyCheck(newPasswordET)) {
            showMessage("Please enter new Password")
        } else {
            return true
        }
        return false
    }

    private fun hitChangePasswordApi() {
        val apiInterface = ServiceGenerator.createService(
            ApiInterface::class.java,
            store.getString("token", ""),
            ""
        )
        val call = apiInterface.changePasswordApi(
            ChangePasswordEntity(
                oldPasswordET.text.toString().trim(), newPasswordET.text.toString().trim()
            )
        )
        call.enqueue(object : Callback<LoginWrapper> {
            override fun onResponse(
                call: Call<LoginWrapper>?,
                response: Response<LoginWrapper>?
            ) {
                if (response!!.isSuccessful) {
                    val jsonArray = response.body()?.data?.items
                    store.saveString("token", jsonArray!![0].toString())
                    val userData = jsonArray[1]
                    store.save("userData", userData)
                    startActivity(Intent(this@ChangePasswordActivity, MainActivity::class.java))
                } else {
                    showMessage(response.message())
                }
            }

            override fun onFailure(call: Call<LoginWrapper>?, t: Throwable?) {
                showMessage(t!!.localizedMessage)
            }
        })

    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }
}