package com.example.nycschools.view

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nycopendata.R
import com.example.nycschools.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

//Simple Splash screen animation that display the company's logo
class SplashScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)
        iv_logo.alpha=0f
        iv_logo.animate().setDuration(3500).alpha(1f).withEndAction {
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in,android.R.anim.fade_out)
            finish()
        }
    }
}