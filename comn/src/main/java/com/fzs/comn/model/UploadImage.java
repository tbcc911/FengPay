package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

import java.io.Serializable;

@Table(name = "UploadImage")
public class UploadImage extends Model implements Serializable{

    @Column(name = "nid")
    private String nid;// 网络ID
	@Column(name = "name")
	private String name;// 标题
    @Column(name = "uri")
    private String uri;// 地址

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }
}
