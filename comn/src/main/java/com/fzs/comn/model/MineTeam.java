package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 我的代理
 * */
@Table(name = "MineTeam")
public class MineTeam extends Model {
	@Column(name = "lid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String lid;
	@Column
	private String addTime;//时间
	@Column
	private String nc;//昵称
	@Column
	private String icon;//头像
	@Column
	private String name;//名称

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getAddTime() {
        return addTime;
    }

    public void setAddTime(String addTime) {
        this.addTime = addTime;
    }

    public String getNc() {
        return nc;
    }

    public void setNc(String nc) {
        this.nc = nc;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
