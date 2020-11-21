package com.example.fifthhomework;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import org.litepal.LitePal;

import java.util.ArrayList;
import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder>{
    private static final String TAG="MyAdapter";
    private Context myContext;
    private List<Content> myList;
    public MyAdapter(List<Content> list){
        myList=list;
    }
    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView content;
        public ViewHolder(View view){
            super(view);
            cardView=(CardView)view;
            content=(TextView)view.findViewById(R.id.note_content);
        }

    }
   @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType){
        if (myContext==null)
            myContext=parent.getContext();
        View view= LayoutInflater.from(myContext).inflate(R.layout.note_item,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final int position=holder.getAdapterPosition();
                AlertDialog.Builder dialog=MainActivity.getAlertDialogBuilder(myContext,"删除笔记","你确定要删除这篇笔记吗?");
                dialog.setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        removeItem(position);
                    }
                });
                dialog.setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                dialog.show();
                return true;//表示长按事件已被处理，不需要再调用onClick
            }
        });
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position=holder.getAdapterPosition();
                //从数据库中拿到所有数据
                ArrayList<Content> contents=(ArrayList<Content>) LitePal.findAll(Content.class);
                //从所有数据中拿到对应的数据
                Content content=contents.get(position);
                Log.e(TAG,"content is"+content.getContent());
                Intent intent=new Intent(myContext,NoteActivity.class);
                intent.putExtra(NoteActivity.NOTE_CONTENT,content.getContent());
                intent.putExtra(NoteActivity.NOTE_ID,position);
                myContext.startActivity(intent);
            }
        });
        return holder;
   }
   @Override
   public void onBindViewHolder(ViewHolder viewHolder,int position){
        Content content=myList.get(position);
        viewHolder.content.setText(content.getContent());
   }
   @Override
   public int getItemCount(){
        return myList.size();
   }

   //移除item的函数
    public void removeItem(int position){
        Content content=myList.get(position);
        //删除数据库中的数据
        content.delete();
        //删除视图
        myList.remove(position);
        notifyItemRemoved(position);
        //刷新视图
        notifyDataSetChanged();
    }
    //添加item函数
    public void addItem(int position,Content content){
       // Log.e(TAG,"This is "+position);
        myList.add(position,content);
        content.setNoteId(position);
        //保存到数据库中
        content.save();
        notifyItemInserted(position);
    }

}
