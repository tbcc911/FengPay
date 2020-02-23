package com.fzs.comn.model;


import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "ShopMainBanner")
public class ShopMainBanner extends Model {
    @Column(name = "lid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String lid;
    
    @Column(name = "url")
	private String url;
    
    @Column(name = "value")
	private String value;
    
    @Column(name = "type")
    private Integer type;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
