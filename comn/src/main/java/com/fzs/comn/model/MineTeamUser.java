package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "MineTeamUser") //消息
public class MineTeamUser extends Model {


    @Column(name = "avatarUrl")
    private String avatarUrl;
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "phone")
    private String phone;
    @Column(name = "publishCount")
    private String publishCount;
    @Column(name = "registerTime")
    private String registerTime;
    @Column(name = "shareCount")
    private String shareCount;
    @Column(name = "memberId")
    private String memberId;
    @Column(name = "shareEffectiveCount")
    private String shareEffectiveCount;
    @Column(name = "teamCount")
    private String teamCount;
    @Column(name = "teamEffectiveCount")
    private String teamEffectiveCount;
    @Column(name = "createTime")
    private String createTime;
    @Column(name = "isEffective")
    private int isEffective;//是否有效
    @Column(name = "incomeScale")
    private String incomeScale;//代理收益比例


    public String getAvatarUrl() {
        return avatarUrl;
    }

    public MineTeamUser setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
        return this;
    }

    public String getNickname() {
        return nickname;
    }

    public MineTeamUser setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getPhone() {
        return phone;
    }

    public MineTeamUser setPhone(String phone) {
        this.phone = phone;
        return this;
    }

    public String getPublishCount() {
        return publishCount;
    }

    public MineTeamUser setPublishCount(String publishCount) {
        this.publishCount = publishCount;
        return this;
    }

    public String getRegisterTime() {
        return registerTime;
    }

    public MineTeamUser setRegisterTime(String registerTime) {
        this.registerTime = registerTime;
        return this;
    }

    public String getShareCount() {
        return shareCount;
    }

    public MineTeamUser setShareCount(String shareCount) {
        this.shareCount = shareCount;
        return this;
    }

    public String getMemberId() {
        return memberId;
    }

    public MineTeamUser setMemberId(String memberId) {
        this.memberId = memberId;
        return this;
    }

    public String getShareEffectiveCount() {
        return shareEffectiveCount;
    }

    public MineTeamUser setShareEffectiveCount(String shareEffectiveCount) {
        this.shareEffectiveCount = shareEffectiveCount;
        return this;
    }

    public String getTeamCount() {
        return teamCount;
    }

    public MineTeamUser setTeamCount(String teamCount) {
        this.teamCount = teamCount;
        return this;
    }

    public String getTeamEffectiveCount() {
        return teamEffectiveCount;
    }

    public MineTeamUser setTeamEffectiveCount(String teamEffectiveCount) {
        this.teamEffectiveCount = teamEffectiveCount;
        return this;
    }

    public String getCreateTime() {
        return createTime;
    }

    public MineTeamUser setCreateTime(String createTime) {
        this.createTime = createTime;
        return this;
    }

    public boolean getIsEffective() {
        return isEffective==1;
    }

    public void setIsEffective(boolean isEffective) {
        if(isEffective){
            this.isEffective = 1;
        }else{
            this.isEffective = 0;
        }
    }

    public String getIncomeScale() {
        return incomeScale;
    }

    public void setIncomeScale(String incomeScale) {
        this.incomeScale = incomeScale;
    }
}
