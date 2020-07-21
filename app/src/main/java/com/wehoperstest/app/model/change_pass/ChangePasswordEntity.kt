package com.wehoperstest.app.model.change_pass

data class ChangePasswordEntity(
    val oldPassword: String,
    val newPassword: String
)