package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Ader")
public class Ader extends Model {
	@Column
	private String aderid;//广告商ID
	@Column(name = "userid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String userid;//用户ID
	@Column
	private String mainad;//主广告ID
	@Column
	private int code;//广告商状态 1正常，2审核中,-1待申请或者停用
	@Column
	private boolean isServiceMoney;//是否缴纳服务费
	@Column
	private double money;//余额
	@Column
	private int ad_num;//广告数量
	@Column
	private int nownum;//今日点击量
	@Column
	private int yestnum;//昨日点击量
	@Column
	private int nowoknum;//今日有效点击
	@Column
	private int yestoknum;//昨日有效点击
	@Column(name = "grade")
	private String grade;//当前用户等级
	@Column(name = "gradeSum")
	private int gradeSum;//下一等级总需升级人数
	@Column(name = "gradeShen")
	private int gradeShen;//下一等级还需升级人数
	@Column(name = "realnameState")
	private int realnameState;//实名认证状态
	@Column(name = "svip")
	private int svip;//SVIP等级
	@Column(name = "svipAdd")
	private int svipAdd;
	
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
	public String getMainad() {
		return mainad;
	}
	public void setMainad(String mainad) {
		this.mainad = mainad;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public double getMoney() {
		return money;
	}
	public void setMoney(double money) {
		this.money = money;
	}
	public int getAdnum() {
		return ad_num;
	}
	public void setAdnum(int adnum) {
		this.ad_num = adnum;
	}
	public int getNownum() {
		return nownum;
	}
	public void setNownum(int nownum) {
		this.nownum = nownum;
	}
	public int getYestnum() {
		return yestnum;
	}
	public void setYestnum(int yestnum) {
		this.yestnum = yestnum;
	}
	public int getNowoknum() {
		return nowoknum;
	}
	public void setNowoknum(int nowoknum) {
		this.nowoknum = nowoknum;
	}
	public int getYestoknum() {
		return yestoknum;
	}
	public void setYestoknum(int yestoknum) {
		this.yestoknum = yestoknum;
	}
	public boolean isServiceMoney() {
		return isServiceMoney;
	}
	public void setServiceMoney(boolean isServiceMoney) {
		this.isServiceMoney = isServiceMoney;
	}
	public String getGrade() {
		return grade;
	}
	public void setGrade(String grade) {
		this.grade = grade;
	}
	public int getGradeSum() {
		return gradeSum;
	}
	public void setGradeSum(int gradeSum) {
		this.gradeSum = gradeSum;
	}
	public int getGradeShen() {
		return gradeShen;
	}
	public void setGradeShen(int gradeShen) {
		this.gradeShen = gradeShen;
	}
	public int getRealnameState() {
		return realnameState;
	}
	public void setRealnameState(int realnameState) {
		this.realnameState = realnameState;
	}
	public int getSvip() {
		return svip;
	}
	public void setSvip(int svip) {
		this.svip = svip;
	}
	public int getSvipAdd() {
		return svipAdd;
	}
	public void setSvipAdd(int svipAdd) {
		this.svipAdd = svipAdd;
	}
	
}
