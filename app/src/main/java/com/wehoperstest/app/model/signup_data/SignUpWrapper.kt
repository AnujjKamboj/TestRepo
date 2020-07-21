package com.wehoperstest.app.model.signup_data

import com.wehoperstest.app.model.signup_data.Data

data class SignUpWrapper(
    val `data`: Data,
    val message: Any,
    val success: Boolean
)