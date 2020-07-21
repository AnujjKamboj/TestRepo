package com.wehoperstest.app.extension

import android.widget.EditText
import androidx.appcompat.widget.AppCompatEditText

fun isEmptyCheck(editText: AppCompatEditText): Boolean {
    return editText.text.toString().trim().isEmpty()
}

fun getTextValue(editText: AppCompatEditText): String {
    return editText.text.toString().trim()
}