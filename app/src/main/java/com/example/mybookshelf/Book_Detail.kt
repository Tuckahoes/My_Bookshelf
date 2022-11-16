package com.example.mybookshelf

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView

class Book_Detail : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_detail)
        supportActionBar?.hide()
        //返回按钮
        val back=findViewById<ImageView>(R.id.back)
        back.setOnClickListener{
            finish()
        }
    }
}