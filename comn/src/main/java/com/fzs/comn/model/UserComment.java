package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "UserComment")
public class UserComment extends Model{
	@Column(name = "mid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String mid;// 消息ID
	@Column(name = "userHead")
	private String userHead;// 评论头像
	@Column(name = "userNickname")
	private String userNickname;// 评论昵称
	@Column(name = "userContent")
	private String userContent;// 评论内容
	@Column(name = "image1")
	private String image1;// 评论图像1
    @Column(name = "image2")
    private String image2;// 评论图像2
    @Column(name = "image3")
    private String image3;// 评论图像3
    @Column(name = "imageNumber")
    private String imageNumber;// 图片数量
	public String getMid() {
		return mid;
	}
	public void setMid(String mid) {
		this.mid = mid;
	}
    public String getUserHead() {
        return userHead;
    }
    public void setUserHead(String userHead) {
        this.userHead = userHead;
    }
    public String getUserNickname() {
        return userNickname;
    }
    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
    public String getUserContent() {
        return userContent;
    }
    public void setUserContent(String userContent) {
        this.userContent = userContent;
    }
    public String getImage1() {
        return image1;
    }
    public void setImage1(String image1) {
        this.image1 = image1;
    }
    public String getImage2() {
        return image2;
    }
    public void setImage2(String image2) {
        this.image2 = image2;
    }
    public String getImage3() {
        return image3;
    }
    public void setImage3(String image3) {
        this.image3 = image3;
    }
    public String getImageNumber() {
        return imageNumber;
    }
    public void setImageNumber(String imageNumber) {
        this.imageNumber = imageNumber;
    }
	
}
