package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "UserRedAwardRecordComn")
public class UserRedAwardRecordComn extends Model {
    @Column(name = "lid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String lid;

    @Column(name = "nid")
    private String nid;

    @Column(name = "aderid")
    private String aderid;//广告商ID

    @Column(name="userid")
    private String userid;//用户ID

    @Column(name="nick")
    private String nick;//昵称

    @Column(name="time")
    private String time;//时间

    @Column(name="number")
    private String number;//数量

    @Column(name="level")
    private String level;//等级

    @Column(name="head")
    private String head;//头像

    @Column(name="type")
    private String type;//类型

    @Column(name="usage")
    private String usage;//范围

    @Column(name="nickname")
    private String nickname;//
    @Column(name="winDesc")
    private String winDesc;//
    @Column(name="winDate")
    private String winDate;//

    @Column(name="stateName")
    private String stateName;//领取状态
    @Column(name="stateValue")
    private String stateValue;//领取状态: 0->未领取; 1->已领取

    public String getLid() {
        return lid;
    }

    public void setLid(String lid) {
        this.lid = lid;
    }

    public String getNid() {
        return nid;
    }

    public void setNid(String nid) {
        this.nid = nid;
    }

    public String getAderid() {
        return aderid;
    }

    public void setAderid(String aderid) {
        this.aderid = aderid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNick() {
        return nick;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getHead() {
        return head;
    }

    public void setHead(String head) {
        this.head = head;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUsage() {
        return usage;
    }

    public void setUsage(String usage) {
        this.usage = usage;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getWinDesc() {
        return winDesc;
    }

    public void setWinDesc(String winDesc) {
        this.winDesc = winDesc;
    }

    public String getWinDate() {
        return winDate;
    }

    public void setWinDate(String winDate) {
        this.winDate = winDate;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getStateValue() {
        return stateValue;
    }

    public void setStateValue(String stateValue) {
        this.stateValue = stateValue;
    }
}
