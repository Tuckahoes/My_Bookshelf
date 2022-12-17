package com.example.mybookshelf

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text
import java.io.File

class BookDetail : AppCompatActivity() {
    @SuppressLint("Range")
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
        //Toast.makeText(this,bookIndex.toString(),Toast.LENGTH_SHORT).show()
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
        val bookLabel=findViewById<TextView>(R.id.bookLabel)
        val bookLink=findViewById<TextView>(R.id.bookLink)
        //index!=0,展示选中的书本信息
        val dbHelper=MyDatabaseHelper(this,"BookStore.db",1)
        if(bookIndex != 0) {
            //从数据库读取被点击的书本信息
            val db =dbHelper.writableDatabase
            val thisId=bookIndex.toString()
            val cursor=db.query("Book",null,"_id = ?",arrayOf(thisId),null,null,null,null)
            //赋值
            cursor.moveToFirst()
            bookImage.setImageResource(cursor.getInt(cursor.getColumnIndex("imageId")))
            bookTitle.text=cursor.getString(cursor.getColumnIndex("title"))
            bookAuthor.text=cursor.getString(cursor.getColumnIndex("author"))
            bookPubDate.text=cursor.getString(cursor.getColumnIndex("time"))
            bookPublisher.text=cursor.getString(cursor.getColumnIndex("publisher"))
            bookISBN.text=cursor.getString(cursor.getColumnIndex("ISBN"))
            bookCondition.text=cursor.getString(cursor.getColumnIndex("condition"))
            bookPosition.text=cursor.getString(cursor.getColumnIndex("position"))
            bookNote.text=cursor.getString(cursor.getColumnIndex("note"))
            bookLabel.text=cursor.getString(cursor.getColumnIndex("label"))
            bookLink.text=cursor.getString(cursor.getColumnIndex("link"))
            cursor.close()
        }

        //save按钮点击事件
        val save=findViewById<TextView>(R.id.save)
        save.setOnClickListener {
            val db=dbHelper.writableDatabase
            if(bookIndex!=0) {
                //index!=0,更新数据
                val updateMsg=ContentValues()
                updateMsg.put("title",bookTitle.text.toString())
                updateMsg.put("author",bookAuthor.text.toString())
                updateMsg.put("time",bookPubDate.text.toString())
                updateMsg.put("publisher",bookPublisher.text.toString())
                updateMsg.put("ISBN",bookISBN.text.toString())
                updateMsg.put("condition",bookCondition.text.toString())
                updateMsg.put("position",bookPosition.text.toString())
                updateMsg.put("note",bookNote.text.toString())
                updateMsg.put("label",bookLabel.text.toString())
                updateMsg.put("link",bookLink.text.toString())
                //updateMsg.put("imageId",bookImage.id)
                val thisId=bookIndex.toString()
                db.update("Book",updateMsg,"_id = ?", arrayOf(thisId))
            }
            else {
                //index=0,新增书本
                val addBook=ContentValues().apply {
                    put("title",bookTitle.text.toString())
                    put("imageId",R.drawable.defaultbook)
                    put("author",bookAuthor.text.toString())
                    put("time",bookPubDate.text.toString())
                    put("publisher",bookPublisher.text.toString())
                    put("ISBN",bookISBN.text.toString())
                    put("condition",bookCondition.text.toString())
                    put("position",bookPosition.text.toString())
                    put("note",bookNote.text.toString())
                    put("label",bookLabel.text.toString())
                    put("link",bookLink.text.toString())
                }
                db.insert("Book",null,addBook)
            }
            //Toast.makeText(this,"data saved",Toast.LENGTH_SHORT).show()
            val intent = Intent()
            intent.putExtra("dataReturn",1)
            setResult(RESULT_OK,intent)
            finish()
        }
    }
}