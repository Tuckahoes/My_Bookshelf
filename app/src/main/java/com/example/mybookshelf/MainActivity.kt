package com.example.mybookshelf

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import android.widget.*
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
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
        //初始化书本数据
        initBook()
        val adapter=BookAdapter(this,R.layout.books_meau,bookList)
        val listView: ListView =findViewById(R.id.listView)
        listView.adapter=adapter
            //主页面书本点击事件
        listView.setOnItemClickListener{parent,view,position,id->
            val intent = Intent(this,Book_Detail::class.java)
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
        //悬浮按钮点击事件
        val fab=findViewById<FloatingActionButton>(R.id.fab)
        fab.setOnClickListener {
            Toast.makeText(this,"FAB clicked",Toast.LENGTH_SHORT).show()
        }
    }

    //加载布局文件
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.toolbar,menu)
        return true
    }

    //加入菜单项点击事件
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val drawerLayout =findViewById<DrawerLayout>(R.id.drawerLayout)
        when (item.itemId) {
            android.R.id.home -> drawerLayout.openDrawer(GravityCompat.START)
            R.id.settings -> Toast.makeText(this,"settings",Toast.LENGTH_SHORT).show()
            R.id.others -> Toast.makeText(this,"others",Toast.LENGTH_SHORT).show()
        }
        return true
    }



    private fun initBook() {
        repeat(5) {
            bookList.add(Book("book1's title",R.drawable.book1,"author1","2020-1"))
            bookList.add(Book("book2's title",R.drawable.book2,"author2","2020-2"))
        }
    }
}

//书类,属性为书名、图片ID、书的信息（作者等）、出版时间
class Book(var title:String, var imageId: Int,var message:String, var time:String )

//适配器
class BookAdapter(activity:Activity,val resourceId:Int ,data:List<Book>):
        ArrayAdapter<Book>(activity,resourceId,data) {
    inner class ViewHolder(val bookImage:ImageView,val bookTitle:TextView)
            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view:View
                val viewHolder:ViewHolder
                if(convertView==null){
                    view = LayoutInflater.from(context).inflate(resourceId,parent,false)
                    val bookImage:ImageView=view.findViewById(R.id.book_image)
                    val bookTitle: TextView=view.findViewById(R.id.book_title)
                    viewHolder=ViewHolder(bookImage,bookTitle)
                    view.tag=viewHolder
                }else{
                    view = convertView
                    viewHolder=view.tag as ViewHolder
                }
                val book = getItem(position)
                if(book!=null) {
                    viewHolder.bookImage.setImageResource(book.imageId)
                    viewHolder.bookTitle.text=book.title
                }
            return view
            }
        }