package com.example.mybookshelf

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import android.widget.*
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.navigation.NavigationView

class MainActivity : AppCompatActivity() {

    private val bookList=ArrayList<Book>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //设置toolbar
        val toolbar=findViewById<Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.let {
            it.setDisplayHomeAsUpEnabled(true)
            it.setHomeAsUpIndicator(R.drawable.ic_nav_menu)
        }

        //初始化数据库内容
        addBookstoSQLite()
        //从数据表中读取数据并存入到bookList中
        initbookList()

        val adapter=BookAdapter(this,R.layout.books_meau,bookList)
        val listView: ListView =findViewById(R.id.listView)
        listView.adapter=adapter
        //主页面书本点击事件
        listView.setOnItemClickListener{parent,view,position,id->
            val intent = Intent(this,BookDetail::class.java)
            val clickedBook = bookList[position]
            intent.putExtra("index",clickedBook.index)
            startActivity(intent)
        }

        //抽屉菜单点击事件
        val navView=findViewById<NavigationView>(R.id.navView)
        val drawerLayout=findViewById<DrawerLayout>(R.id.drawerLayout)
        navView.setCheckedItem(R.id.nav_books)//设为默认选中
        navView.setNavigationItemSelectedListener {
            //逻辑代码
            drawerLayout.closeDrawers()
            true
        }
        //点击悬浮按钮添加书本
        val fab=findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            val intent =Intent(this,BookDetail::class.java)
            intent.putExtra("index",0)
            startActivity(intent)
        }

    }
    //创建数据库Book表的“子表”BookStore,并加入一些信息
    private fun addBookstoSQLite() {
        val dbHelper=MyDatabaseHelper(this,"BookStore.db",1)
        val db =dbHelper.writableDatabase
        val bookMsg=ContentValues().apply {
            put("title","转生成为雷电将军然后天下无敌！")
            put("imageId",R.drawable.book2)
            put("author","Tuckahoe")
            put("time","2002-1")
            put("publisher","Tivato")
            put("ISBN","22223333")
            put("condition","Reading")
            put("position","under the bed")
            put("note","nothing")
            put("label","unset")
            put("link","unknown")
        }
        val bookMsg2=ContentValues().apply {
            put("title","恋与Kokomi")
            put("imageId",R.drawable.book1)
            put("author","Tuckahoe")
            put("time","2022-12")
            put("publisher","Tivato")
            put("ISBN","23333333")
            put("condition","Reading")
            put("position","table")
            put("note","nothing")
            put("label","unset")
            put("link","unknown")
        }
        repeat(4) {
            db.insert("Book",null,bookMsg)
            db.insert("Book",null,bookMsg2)
        }
    }
    //将数据库中数据读取到bookList中
    @SuppressLint("Range")
    private fun initbookList() {
        val dbHelper=MyDatabaseHelper(this,"BookStore.db",1)
        val db=dbHelper.writableDatabase
        val cursor = db.query("Book",null,null,null,null,null,null)
        if (cursor.moveToFirst()) {
            do {
                val index=cursor.getInt(cursor.getColumnIndex("id"))
                val title=cursor.getString(cursor.getColumnIndex("title"))
                val imageId=cursor.getInt(cursor.getColumnIndex("imageId"))
                val author=cursor.getString(cursor.getColumnIndex("author"))
                val time=cursor.getString(cursor.getColumnIndex("time"))
                bookList.add(Book(title,imageId,author,time,index))
            }while(cursor.moveToNext())
        }
        cursor.close()
    }

    //加载抽屉菜单栏布局文件
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }
    //菜单项点击事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val drawerLayout =findViewById<DrawerLayout>(R.id.drawerLayout)
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
            R.id.settings -> Toast.makeText(this,"settings",Toast.LENGTH_SHORT).show()
            R.id.others -> Toast.makeText(this,"others",Toast.LENGTH_SHORT).show()
        }
        return true
    }
}

//书类,属性为书名、图片ID、书的信息（作者等）、出版时间
class Book(var title:String, var imageId: Int,var author:String, var time:String,var index:Int )
//适配器
class BookAdapter(activity:Activity,val resourceId:Int ,data:List<Book>):
        ArrayAdapter<Book>(activity,resourceId,data) {
    inner class ViewHolder(val bookImage:ImageView,val bookTitle:TextView,val bookAuthor:TextView,val bookTime:TextView)
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view:View
                val viewHolder:ViewHolder
                if(convertView==null){
                    view = LayoutInflater.from(context).inflate(resourceId,parent,false)
                    val bookImage:ImageView=view.findViewById(R.id.book_image)
                    val bookTitle: TextView=view.findViewById(R.id.book_title)
                    val bookAuthor:TextView=view.findViewById(R.id.book_author)
                    val bookTime:TextView=view.findViewById(R.id.book_time)
                    viewHolder=ViewHolder(bookImage,bookTitle,bookAuthor,bookTime)
                    view.tag=viewHolder
                }else{
                    view = convertView
                    viewHolder=view.tag as ViewHolder
                }
                val book = getItem(position)
                if(book!=null) {
                    viewHolder.bookImage.setImageResource(book.imageId)
                    viewHolder.bookTitle.text=book.title
                    viewHolder.bookAuthor.text=book.author
                    viewHolder.bookTime.text=book.time
                }
            return view
            }
        }