package com.example.salwano1

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class VerifiedActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verified)

        supportActionBar?.title = "Following"
        }
}