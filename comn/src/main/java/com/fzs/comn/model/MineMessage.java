package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "MineMessage") //消息
public class MineMessage extends Model {


    @Column(name = "nid")
    private String nid;
    @Column(name = "title")
    private String title;
    @Column(name = "content")
    private String content;
    @Column(name = "createTime")
    private String createTime;
    @Column(name = "messageCategoryName") //消息类型
    private String messageCategoryName;

    public String getNid() {
        return nid;
    }

    public MineMessage setNid(String nid) {
        this.nid = nid;
        return this;
    }

    public String getTitle() {
        return title;
    }

    public MineMessage setTitle(String title) {
        this.title = title;
        return this;
    }

    public String getContent() {
        return content;
    }

    public MineMessage setContent(String content) {
        this.content = content;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public MineMessage setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public String getMessageCategoryName() {
        return messageCategoryName;
    }

    public MineMessage setMessageCategoryName(String messageCategoryName) {
        this.messageCategoryName = messageCategoryName;
        return this;
    }
}
