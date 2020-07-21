package com.wehoperstest.app.retrofit

import com.wehoperstest.app.model.change_pass.ChangePasswordEntity
import com.wehoperstest.app.model.login_data.Login
import com.wehoperstest.app.model.login_data.LoginWrapper
import com.wehoperstest.app.model.signup_data.SignUpWrapper
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 *A simple created by Anuj Kamboj.
 *
 */
interface ApiInterface {

    @POST("user/login")
    fun loginApi(@Body auth: Login): Call<LoginWrapper>

    @POST("user/changePassword")
    fun changePasswordApi(@Body auth: ChangePasswordEntity): Call<LoginWrapper>

    @Multipart
    @PUT("user")
    fun register(
        @Part("name") address: RequestBody,
        @Part("area") address_type: RequestBody,
        @Part("phone") city: RequestBody,
        @Part("password") country: RequestBody,
        @Part("state") country_code: RequestBody,
        @Part("city") email: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<SignUpWrapper>

    @Multipart
    @PATCH("user")
    fun update(
        @Part("name") address: RequestBody,
        @Part("area") address_type: RequestBody,
        @Part("phone") city: RequestBody,
        @Part("password") country: RequestBody,
        @Part("state") country_code: RequestBody,
        @Part("city") email: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<SignUpWrapper>

}