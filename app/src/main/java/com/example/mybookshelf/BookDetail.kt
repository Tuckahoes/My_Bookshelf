package com.example.mybookshelf

import android.annotation.SuppressLint
import android.content.Context
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
        if(bookIndex != 0) {
            //从数据库读取被点击的书本信息
            val dbHelper=MyDatabaseHelper(this,"BookStore.db",1)
            val db =dbHelper.writableDatabase
            //where语句有问题
            val cursor=db.query("Book",null,"id"+"=?",arrayOf("1"),null,null,null,null)
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

        fun deleteFile(fileIndex:Int) {
            val fileName=fileIndex.toString()
            val file = File("/data/data/com.example.mybookshelf/shared_prefs/1.xml")
            file.delete()
        }
        //有BUG，姑且注释！！！！
        //save按钮点击事件
//        val save=findViewById<TextView>(R.id.save)
//        save.setOnClickListener {
//            var theBookIndex=0
//            theBookIndex = if(bookIndex != 0) {
//                val bookSum=intent.getIntExtra("bookSum",0)
//                val menuEditor=getSharedPreferences("menu", Context.MODE_PRIVATE).edit()
//                menuEditor.putInt("sum",bookSum-1)//总数+1
//                menuEditor.apply()
//                bookIndex
//            } else {
//                val bookSum=intent.getIntExtra("bookSum",0)
//                val menuEditor=getSharedPreferences("menu", Context.MODE_PRIVATE).edit()
//                menuEditor.putInt("sum",bookSum+1)//总数+1
//                menuEditor.apply()
//                bookSum+1
//            }
//
//            val bookEditor=getSharedPreferences(theBookIndex.toString(),Context.MODE_PRIVATE).edit()
//            bookEditor.putString("title", bookTitle.text.toString())
//            bookEditor.putString("author",bookAuthor.text.toString())
//            bookEditor.putString("time",bookPubDate.text.toString())
//            bookEditor.putString("publisher",bookPublisher.text.toString())
//            bookEditor.putString("ISBN",bookISBN.text.toString())
//            bookEditor.putString("condition",bookCondition.text.toString())
//            bookEditor.putString("position",bookPosition.text.toString())
//            bookEditor.putString("note",bookNote.text.toString())
//            bookEditor.putString("lable",bookLable.text.toString())
//            bookEditor.putString("link",bookLink.text.toString())
//            bookEditor.putInt("imageId",bookImage.id)
//            bookEditor.putInt("index",theBookIndex)
////            如果是添加书籍，一定要加上索引
//            if(bookIndex == 0) {
//                bookEditor.putInt("imageId",2131165395)
//                bookEditor.putInt("index",theBookIndex)
//            }
//            bookEditor.apply()
//            deleteFile(theBookIndex)
//            Toast.makeText(this,"data saved",Toast.LENGTH_SHORT).show()
//        }
    }
}