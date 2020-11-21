package com.example.fifthhomework;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

/*
* 待解决的问题：更新每条笔记中的内容，如何在原有的基础上进行更新，而不是新建一个实例？
*               从记事本页面回退到主活动后，如何刷新页面？
*
* */


public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private List<Content> contentList=new ArrayList<>();
    private RecyclerView recyclerView;
    private FloatingActionButton addNote;
    private MyAdapter adapter;
    private SwipeRefreshLayout swipeRefreshLayout;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        addNote=(FloatingActionButton)findViewById(R.id.add_note);
        swipeRefreshLayout=(SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.colorPrimary));
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshNotes();
            }
        });
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        Toolbar toolbar_main=(Toolbar)findViewById(R.id.toolbar_main);
        initView();
        setSupportActionBar(toolbar_main);
        recyclerView.setLayoutManager(layoutManager);
         adapter=new MyAdapter(contentList);
        recyclerView.setAdapter(adapter);
        addNote.setOnClickListener(this);
    }

    @Override
    public void onClick(View view){
        AlertDialog.Builder dialog=getAlertDialogBuilder(this,"添加笔记","是否添加笔记?");
        dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    adapter.addItem(contentList.size(),new Content());
            }
        });
        dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        dialog.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar_main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.clear_all:
                AlertDialog.Builder dialog=getAlertDialogBuilder(this,"清空笔记","你确定要清空笔记吗？");
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        LitePal.deleteAll(Content.class);
                        contentList.clear();
                        adapter.notifyDataSetChanged();
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;


        }
        return true;
    }




    private void initView(){
        //从数据库中取出已存储的内容并初始化页面
        contentList= LitePal.findAll(Content.class);
    }

    //对Dialog的相关设置进行封装
    //但由于监听里面的逻辑代码不一样，所以没办法把设置监听也封装进来
    public static AlertDialog.Builder getAlertDialogBuilder(Context context,String title,String message){
         AlertDialog.Builder dialog=new AlertDialog.Builder(context);
         dialog.setTitle(title);
         dialog.setMessage(message);
         dialog.setCancelable(false);
         return dialog;

    }
    private void refreshNotes(){
        initView();
        adapter=new MyAdapter(contentList);
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        swipeRefreshLayout.setRefreshing(false);
    }

}
