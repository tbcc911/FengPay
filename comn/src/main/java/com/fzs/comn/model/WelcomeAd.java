package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "WelcomeAd")
public class WelcomeAd extends Model{
	@Column(name = "onClickUrl")
	private String onClickUrl;// 点击URL
	@Column(name = "url")
	private String url;// 图片URL
	@Column(name = "localUrl")
	private String localUrl;// 本地图片URL
	@Column(name = "isDown")
	private int isDown;// 是否成功下载到本地(1:成功下载到本地;0:未下载到本地)
	public String getOnClickUrl() {
		return onClickUrl;
	}
	public void setOnClickUrl(String onClickUrl) {
		this.onClickUrl = onClickUrl;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getLocalUrl() {
		return localUrl;
	}
	public void setLocalUrl(String localUrl) {
		this.localUrl = localUrl;
	}
	public int getIsDown() {
		return isDown;
	}
	public void setIsDown(int isDown) {
		this.isDown = isDown;
	}
}
