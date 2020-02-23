package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Profit")
public class Profit extends Model{
	@Column(name = "userid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String userid;//用户ID
	@Column
	private double today;//今日收益
	@Column
	private double yesterday;//昨日收益
	@Column
	private String EARN_1;//广告
	@Column
	private String EARN_2;//邀请
	@Column
	private String EARN_3;//分享
	@Column
	private String EARN_4;//下载
	@Column
	private String EARN_5;//电话
	@Column
	private String EARN_6;//网页
	@Column
	private String EARN_8;//广告费收益
	@Column(name = "EARN_9")
	private String EARN_9;//邀请
	@Column(name = "INVITE_MONEY")
	private String INVITE_MONEY;//邀请用户看广告
	
	
	public String getEARN_8() {
		return EARN_8;
	}
	public void setEARN_8(String eARN_8) {
		EARN_8 = eARN_8;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public double getToday() {
		return today;
	}
	public void setToday(double today) {
		this.today = today;
	}
	public double getYesterday() {
		return yesterday;
	}
	public void setYesterday(double yesterday) {
		this.yesterday = yesterday;
	}
	public String getEARN_1() {
		if("NaN".equals(EARN_1)){
			return "0";
		}
		return EARN_1;
	}
	public void setEARN_1(String eARN_1) {
		EARN_1 = eARN_1;
	}
	public String getEARN_2() {
		if("NaN".equals(EARN_2)){
			return "0";
		}
		return EARN_2;
	}
	public void setEARN_2(String eARN_2) {
		EARN_2 = eARN_2;
	}
	public String getEARN_3() {
		if("NaN".equals(EARN_3)){
			return "0";
		}
		return EARN_3;
	}
	public void setEARN_3(String eARN_3) {
		EARN_3 = eARN_3;
	}
	public String getEARN_4() {
		if("NaN".equals(EARN_4)){
			return "0";
		}
		return EARN_4;
	}
	public void setEARN_4(String eARN_4) {
		
		EARN_4 = eARN_4;
	}
	public String getEARN_5() {
		if("NaN".equals(EARN_5)){
			return "0";
		}
		return EARN_5;
	}
	public void setEARN_5(String eARN_5) {
		EARN_5 = eARN_5;
	}
	public String getEARN_6() {
		if("NaN".equals(EARN_6)){
			return "0";
		}
		return EARN_6;
	}
	public void setEARN_6(String eARN_6) {
		EARN_6 = eARN_6;
	}
	public String getEARN_9() {
		return EARN_9;
	}
	public void setEARN_9(String eARN_9) {
		EARN_9 = eARN_9;
	}
	public String getINVITE_MONEY() {
		return INVITE_MONEY;
	}
	public void setINVITE_MONEY(String iNVITE_MONEY) {
		INVITE_MONEY = iNVITE_MONEY;
	}
	
}
