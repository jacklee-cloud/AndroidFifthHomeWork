package com.example.fifthhomework;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import org.litepal.LitePal;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {
      private EditText editText;
      private String content;
      private int  position;
      public static final String NOTE_CONTENT="NOTE_CONTENT";
      public static final String NOTE_ID="NOTE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);
        Toolbar toolbar=(Toolbar)findViewById(R.id.toolbar);
        editText=(EditText)findViewById(R.id.edit_content);
        setSupportActionBar(toolbar);
         Intent intent=getIntent();
      content=intent.getStringExtra(NOTE_CONTENT);
     position=intent.getIntExtra(NOTE_ID,0);
        editText.setText(content);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.toolbar,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.clear:
                AlertDialog.Builder dialog=MainActivity.getAlertDialogBuilder(this,"清除文本","是否清除文本?");
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editText.setText("");
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                break;
            case R.id.save:
                AlertDialog.Builder dialog_save=MainActivity.getAlertDialogBuilder(this,"保存","确定要保存笔记吗？");
                dialog_save.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //获取到已储存的所有笔记
                       ArrayList<Content> contents=(ArrayList<Content>)LitePal.findAll(Content.class);
                       //得到当前笔记
                        Content ct=contents.get(position);
                        //更新内容
                        ct.setContent(editText.getText().toString());
                        //保存
                        ct.save();
                      // Log.e(NOTE_CONTENT,"position is "+position);
                        Toast.makeText(NoteActivity.this,"保存成功！",Toast.LENGTH_SHORT).show();
                    }
                });
                dialog_save.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog_save.show();

            break;

        }
        return true;
    }

}
