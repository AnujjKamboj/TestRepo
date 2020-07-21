package com.wehoperstest.app.activity

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.wehoperstest.app.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initParm()
    }

    private fun initParm() {
        updateTV.setOnClickListener {
            startActivity(Intent(this, UpdateUserActivity::class.java))
        }
        changePasswordTV.setOnClickListener {
            startActivity(Intent(this, ChangePasswordActivity::class.java))
        }
    }
}