package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "MineCollection")
public class MineCollection extends Model{
	@Column(name = "lid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
    private String lid;
    @Column(name = "nid")
    private String nid;
    @Column(name = "memberId")
    private Long memberId;
    @Column(name = "memberNickname")
    private String memberNickname;
    @Column(name = "memberIcon")
    private String memberIcon;
    @Column(name = "productId")
    private String productId; 
    @Column(name = "productName")
    private String productName;
    @Column(name = "productPic")
    private String productPic;
    @Column(name = "productSubTitle")
    private String productSubTitle;
    @Column(name = "productPrice")
    private String productPrice;
    @Column(name = "storeLogoImage")
    private String storeLogoImage;
    public String getStoreLogoImage() {
        return storeLogoImage;
    }

    public void setStoreLogoImage(String storeLogoImage) {
        this.storeLogoImage = storeLogoImage;
    }

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

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public String getMemberNickname() {
        return memberNickname;
    }

    public void setMemberNickname(String memberNickname) {
        this.memberNickname = memberNickname;
    }

    public String getMemberIcon() {
        return memberIcon;
    }

    public void setMemberIcon(String memberIcon) {
        this.memberIcon = memberIcon;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPic() {
        return productPic;
    }

    public void setProductPic(String productPic) {
        this.productPic = productPic;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductSubTitle() {
        return productSubTitle;
    }

    public void setProductSubTitle(String productSubTitle) {
        this.productSubTitle = productSubTitle;
    }
}
