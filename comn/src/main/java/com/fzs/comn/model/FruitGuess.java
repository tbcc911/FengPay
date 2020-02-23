package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "FruitGuess") //水果机
public class FruitGuess extends Model {
    
    
    @Column(name = "nickname")
    private String nickname;
    @Column(name = "winDate")
    private String winDate;
    @Column(name = "winDesc")
    private String winDesc;
    

    public String getNickname() {
        return nickname;
    }

    public FruitGuess setNickname(String nickname) {
        this.nickname = nickname;
        return this;
    }

    public String getWinDate() {
        return winDate;
    }

    public FruitGuess setWinDate(String winDate) {
        this.winDate = winDate;
        return this;
    }

    public String getWinDesc() {
        return winDesc;
    }

    public FruitGuess setWinDesc(String winDesc) {
        this.winDesc = winDesc;
        return this;
    }
    
}
