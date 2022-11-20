package com.example.mybookshelf

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

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
        //接收书本index
        val bookIndex:Int=intent.getIntExtra("index",0)
        //获取控件
        val bookImage=findViewById<ImageView>(R.id.bookImage)
        val bookTitle=findViewById<TextView>(R.id.bookTitle)
        val bookAuthor=findViewById<TextView>(R.id.bookAuthor)
        val bookPubDate=findViewById<TextView>(R.id.bookPubDate)
        val bookPublisher=findViewById<TextView>(R.id.bookPublisher)
        val bookISBN=findViewById<TextView>(R.id.bookISBN)
        val bookCondition=findViewById<TextView>(R.id.bookCondition)
        val bookPosition=findViewById<TextView>(R.id.bookPosition)
        val bookNote=findViewById<TextView>(R.id.bookNote)
        val bookLable=findViewById<TextView>(R.id.bookLable)
        val bookLink=findViewById<TextView>(R.id.bookLink)
        if(bookIndex != 0) {
            //赋值
            val bookDetail=getSharedPreferences(bookIndex.toString(),Context.MODE_PRIVATE)
            bookImage.setImageResource(bookDetail.getInt("imageId",-2))
            bookTitle.text=bookDetail.getString("title","Default Title")
            bookAuthor.text=bookDetail.getString("author","Default Author")
            bookPubDate.text=bookDetail.getString("time","0000-0")
            bookPublisher.text=bookDetail.getString("publisher","Unknown")
            bookISBN.text=bookDetail.getString("ISBN","00000000")
            bookCondition.text=bookDetail.getString("condition","Unknown")
            bookPosition.text=bookDetail.getString("position","Unknown")
            bookNote.text=bookDetail.getString("note","none")
            bookLable.text=bookDetail.getString("lable","Unset")
            bookLink.text=bookDetail.getString("link","Unknown")
        }

        //save按钮点击事件
        val save=findViewById<TextView>(R.id.save)
        save.setOnClickListener {
            var theBookIndex=0
            theBookIndex = if(bookIndex != 0) {
                bookIndex
            } else {
                val bookSum=intent.getIntExtra("bookSum",0)
                val menuEditor=getSharedPreferences("menu", Context.MODE_PRIVATE).edit()
                menuEditor.putInt("sum",bookSum+1)//总数+1
                menuEditor.apply()
                bookSum+1
            }
            val bookEditor=getSharedPreferences(theBookIndex.toString(),Context.MODE_PRIVATE).edit()
            bookEditor.putString("title", bookTitle.text.toString())
            bookEditor.putString("author",bookAuthor.text.toString())
            bookEditor.putString("time",bookPubDate.text.toString())
            bookEditor.putString("publisher",bookPublisher.text.toString())
            bookEditor.putString("ISBN",bookISBN.text.toString())
            bookEditor.putString("condition",bookCondition.text.toString())
            bookEditor.putString("position",bookPosition.text.toString())
            bookEditor.putString("note",bookNote.text.toString())
            bookEditor.putString("lable",bookLable.text.toString())
            bookEditor.putString("link",bookLink.text.toString())
            //bookEditor.putInt("imageId",bookImage.id)
            bookEditor.apply()
            Toast.makeText(this,"data saved",Toast.LENGTH_SHORT).show()
        }
    }
}