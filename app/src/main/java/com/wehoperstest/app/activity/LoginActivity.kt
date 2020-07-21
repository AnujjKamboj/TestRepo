package com.wehoperstest.app.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.wehoperstest.app.R
import com.wehoperstest.app.extension.isEmptyCheck
import com.wehoperstest.app.model.login_data.Login
import com.wehoperstest.app.model.login_data.LoginWrapper
import com.wehoperstest.app.retrofit.ApiInterface
import com.wehoperstest.app.retrofit.ServiceGenerator
import com.wehoperstest.app.utils.PrefStore
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initi()
    }

    private fun initi() {
        loginBT.setOnClickListener {
            if (isValidParms()) {
                hitLoginApi()
            }
        }
    }

    private fun isValidParms(): Boolean {
        if (isEmptyCheck(phoneNumberET)) {
            showMessage("Please enter your Phone number")
        } else if (isEmptyCheck(passwordET)) {
            showMessage("Please enter your Password")
        } else {
            return true
        }
        return false
    }

    private fun showMessage(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }

    private fun hitLoginApi() {
        val apiInterface = ServiceGenerator.createService(
            ApiInterface::class.java,
            "",
            ""
        )
        val call = apiInterface.loginApi(
            Login(
                passwordET.text.toString().trim(), phoneNumberET.text.toString().trim()
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
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    showMessage(response.message())
                }
            }

            override fun onFailure(call: Call<LoginWrapper>?, t: Throwable?) {
                showMessage(t!!.localizedMessage)
            }
        })

    }

}