package com.example.mybookshelf

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
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

        //“目录”文件
        val menuEditor=getSharedPreferences("menu", Context.MODE_PRIVATE).edit()
        menuEditor.putInt("sum",2)
        menuEditor.apply()
        //在内存中添加一些书本信息
//        var bookEditor=getSharedPreferences("1", Context.MODE_PRIVATE).edit()
//        bookEditor.putString("title","Kokomi Love Ayaka!")
//        bookEditor.putInt("imageId",R.drawable.book1)
//        bookEditor.putString("author","Tuckahoe")
//        bookEditor.putString("time","2022-11")
//        bookEditor.putString("publisher","ZhuHai Publisher")
//        bookEditor.putString("ISBN","20013289")
//        bookEditor.putString("condition","Reading")
//        bookEditor.putString("position","Default Bookshelf")
//        bookEditor.putString("note","Notes")
//        bookEditor.putString("lable","fiction")
//        bookEditor.putString("link","http://tuckahoes")
//        bookEditor.putInt("index",1)
//        bookEditor.apply()
//        bookEditor=getSharedPreferences("2", Context.MODE_PRIVATE).edit()
//        bookEditor.putString("title","决战!Ayaka!")
//        bookEditor.putInt("imageId",R.drawable.book2)
//        bookEditor.putString("author","Kokomi")
//        bookEditor.putString("time","2020-10")
//        bookEditor.putString("publisher","XiuShan Publisher")
//        bookEditor.putString("ISBN","20013289")
//        bookEditor.putString("condition","Unread")
//        bookEditor.putString("position","Default Bookshelf")
//        bookEditor.putString("note","Notes")
//        bookEditor.putString("lable","fiction")
//        bookEditor.putString("link","http://tuckahoes")
//        bookEditor.putInt("index",2)
//        bookEditor.apply()

        //从目录文件中读取书本数量等信息
        val bookMenu=getSharedPreferences("menu",Context.MODE_PRIVATE)
        val bookSum=bookMenu.getInt("sum",0)

        //初始化书本数据
        repeat(5){
            for(i in 1..bookSum){
                val bookMessage=getSharedPreferences(i.toString(),Context.MODE_PRIVATE)
                val bookTitle=bookMessage.getString("title","Default Title")
                val bookImageId=bookMessage.getInt("imageId",0)
                val bookAuthor=bookMessage.getString("author","Author")
                val bookTime=bookMessage.getString("time","0000-0")
                val bookIndex=bookMessage.getInt("index",-1)
                bookList.add(Book(bookTitle.toString(),bookImageId,bookAuthor.toString(),bookTime.toString(),bookIndex))
            }
        }
        val adapter=BookAdapter(this,R.layout.books_meau,bookList)
        val listView: ListView =findViewById(R.id.listView)
        listView.adapter=adapter
        //主页面书本点击事件
        listView.setOnItemClickListener{parent,view,position,id->
            val intent = Intent(this,Book_Detail::class.java)
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
            val intent =Intent(this,Book_Detail::class.java)
            intent.putExtra("index",0)
            intent.putExtra("bookSum",bookSum)
            startActivity(intent)
        }
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

//书类,属性为书名、图片ID、书的信息（作者等）、出版时间、索引值
class Book(var title:String, var imageId: Int,var author:String, var time:String,val index:Int )
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