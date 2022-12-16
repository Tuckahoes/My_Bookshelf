package com.example.mybookshelf

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ContentValues
import android.content.Context
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

        val editor0=getSharedPreferences("times", Context.MODE_PRIVATE).edit()
        editor0.putBoolean("init",true)
        editor0.apply()
        val prefs=getSharedPreferences("times",Context.MODE_PRIVATE)
        val isFirstTime=prefs.getBoolean("isFirstTime",true)
        //首次打开向数据库中添加一些书籍
        if(isFirstTime) {
            //初始化数据库内容
            addBookstoSQLite()
            val editor=getSharedPreferences("times", Context.MODE_PRIVATE).edit()
            editor.putBoolean("isFirstTime",false)
            editor.apply()
        }

        //关联数据库与listView的适配器
        val mSimpleCursorAdapter=SimpleCursorAdapter(
            this,R.layout.books_meau,null,arrayOf("title","imageId","author","time"),
            intArrayOf(R.id.book_title,R.id.book_image,R.id.book_author,R.id.book_time),
            CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER)
        val listView: ListView =findViewById(R.id.listView)
        listView.adapter=mSimpleCursorAdapter
        refreshListView()

        registerForContextMenu(listView)
        //主页面书本点击事件
        listView.setOnItemClickListener{parent,view,position,id->
            val intent = Intent(this,BookDetail::class.java)
            intent.putExtra("index",id.toInt())
            startActivityForResult(intent,1)
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
            startActivityForResult(intent,2)
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

    //刷新数据列表
    fun refreshListView(){
        val dbHelper=MyDatabaseHelper(this,"BookStore.db",1)
        val db=dbHelper.writableDatabase
        val cursor = db.query("Book",null,null,null,null,null,null)
        val listView: ListView =findViewById(R.id.listView)
        val mSimpleCursorAdapter:SimpleCursorAdapter = listView.adapter as SimpleCursorAdapter
        mSimpleCursorAdapter.changeCursor(cursor)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            1 -> if(resultCode== RESULT_OK) {
                //Toast.makeText(this,"data saved",Toast.LENGTH_SHORT).show()
                val returnedData=data?.getIntExtra("dataReturn",0)
                if (returnedData==1) {
                    //Toast.makeText(this,"data saved",Toast.LENGTH_SHORT).show()
                    refreshListView()
                }

            }
        }
    }

    //ContextMenu长按开启上下文菜单，实现删除功能
    override fun onCreateContextMenu(
        menu: ContextMenu?,
        v: View?,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menuInflater.inflate(R.menu.context_menu,menu)
    }
    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when(item.itemId){
            R.id.delete-> {
                val menuInfo=item.menuInfo as AdapterView.AdapterContextMenuInfo
                val theid=(menuInfo.position+1).toString()
                val dbHelper=MyDatabaseHelper(this,"BookStore.db",1)
                val db=dbHelper.writableDatabase
                db.delete("Book","_id= ? ", arrayOf(theid))
                Toast.makeText(this,"deleted successfully",Toast.LENGTH_SHORT).show()
                true
            }
            else -> super.onOptionsItemSelected(item)
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
