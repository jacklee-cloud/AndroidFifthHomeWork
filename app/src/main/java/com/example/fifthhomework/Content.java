package com.example.fifthhomework;

import android.content.Context;

import org.litepal.crud.LitePalSupport;

public class Content extends LitePalSupport {
    private int noteId;
    private String content="(新建笔记)";//默认内容
    public Content(){}

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public Content(String content){
        this.content=content;
    }

    public Content(String content, int noteId){
        this.content=content;
        this.noteId=noteId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
