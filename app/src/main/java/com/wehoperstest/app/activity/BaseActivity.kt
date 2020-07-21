package com.wehoperstest.app.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.wehoperstest.app.utils.PrefStore

open class BaseActivity : AppCompatActivity() {
    lateinit var store: PrefStore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        store = PrefStore(this)
    }
}