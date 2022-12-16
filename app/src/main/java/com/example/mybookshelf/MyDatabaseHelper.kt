package com.example.mybookshelf

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast

class MyDatabaseHelper(val context:Context,name:String,version:Int): SQLiteOpenHelper(context,name,null,version){
    //创建图书的数据库表
    private val createBook="create table Book ("+
            "_id integer primary key autoincrement,"+
            "title text,"+
            "imageId integer,"+
            "author text,"+
            "time text,"+
            "publisher text,"+
            "ISBN text,"+
            "condition text,"+
            "position text,"+
            "note text,"+
            "label text,"+
            "link text)"

    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(createBook)
        Toast.makeText(context,"Create succeeded",Toast.LENGTH_SHORT).show()
    }

    //数据库升级
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
//        db.execSQL("drop table if exists Book")
//        onCreate(db)
    }
}