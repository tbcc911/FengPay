package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "Address")
public class Address extends Model{
	@Column(name = "lid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String lid;
	@Column(name = "nid")
	private String nid;
	@Column(name = "name")
	private String name;
	@Column(name = "phone")
	private String phone;
	@Column(name = "address")
	private String address;
	@Column(name = "addressInfo")
	private String addressInfo;
	@Column(name = "province")
	private String province;
	@Column(name = "provinceID")
	private String provinceID;
	@Column(name = "city")
	private String city;
	@Column(name = "cityID")
	private String cityID;
	@Column(name = "area")
	private String area;
	@Column(name = "areaID")
	private String areaID;
	@Column(name = "userID")
	private String userID;
    @Column(name = "isDefault")
    private boolean isDefault;
    @Column(name = "region")
    private String region;
    public void setRegion(String region) {
        this.name = region;
    }
    public String getRegion() {
        return region;
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
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getAddressInfo() {
		return addressInfo;
	}
	public void setAddressInfo(String addressInfo) {
		this.addressInfo = addressInfo;
	}
	public String getProvince() {
		return province;
	}
	public void setProvince(String province) {
		this.province = province;
	}
	public String getProvinceID() {
		return provinceID;
	}
	public void setProvinceID(String provinceID) {
		this.provinceID = provinceID;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCityID() {
		return cityID;
	}
	public void setCityID(String cityID) {
		this.cityID = cityID;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getAreaID() {
		return areaID;
	}
	public void setAreaID(String areaID) {
		this.areaID = areaID;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }
}
