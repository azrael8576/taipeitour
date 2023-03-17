package com.alex.cathaybk_recruit_android.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.alex.cathaybk_recruit_android.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
    }

    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch {
            // 模擬登入連線時間
            delay(LOGIN_TIME)
            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
            finish()
        }
    }

    companion object {
        const val LOGIN_TIME = 3000L
    }
}
