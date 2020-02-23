package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

/**
 * 购物车商品model
 * */
@Table(name = "CartGoods", id = "_id")
public class CartGoods extends Model{
	@Column(name = "cartId")//购物车ID
	private String cartId;
	
	@Column(name = "goodsId")
	private String goodsId;
	@Column(name = "goodsIcon")//商品图标
	private String goodsIcon;
	@Column(name = "goodsName")//商品名称
	private String goodsName;
	@Column(name = "goodsAttrIds")//选择商品属性ID
	private String goodsAttrIds;
	@Column(name = "goodsAttrNames")//选择商品属性名称
	private String goodsAttrNames;
	@Column(name = "goodsPrice")//商品价格
	private double goodsPrice;
	@Column(name = "goodsStock")//商品库存
	private long goodsStock;
	@Column(name = "goodsNumber")//兑换数量
	private long goodsNumber;
	@Column(name = "goodsState")//商品状态
	private int goodsState;
	@Column(name = "goodsUseIntegralSet")//商品使用积分设置
	private int goodsUseIntegralSet;
	@Column(name = "goodsUseIntegralValue")//商品使用积分值
	private long goodsUseIntegralValue;
	@Column(name = "goodsBuyType")//购买类型
	private int goodsBuyType;
	private String goodsBuyTypeStr;
	
	@Column(name = "storeSCID")//购物车店铺ID
	private String storeSCID;
	@Column(name = "storeID")//店铺ID
	private String storeID;
	@Column(name = "storePrice")//店铺价格
	private double storePrice;
	@Column(name = "storeName")//店铺名称
	private String storeName;
	@Column(name = "storeUpdateTime")//店铺更新时间
	private long storeUpdateTime;
	@Column(name = "storeTtotalPrice")//店铺总金额
	private double storeTtotalPrice;
	@Column(name = "storeNumber")//店铺总数量
	private long storeNumber;
	
	@Column(name = "cartGoodsState")//购物车商品是否选中 0:未选中  1:已选中
	private int cartGoodsState;
	@Column(name = "createTime")//创建时间
	private long createTime;
	@Column(name = "userId")//用户ID
	private String userId;
    @Column(name = "dBi")//D币
    private double dBi;
	
	public String getStoreID() {
		return storeID;
	}
	public void setStoreID(String storeID) {
		this.storeID = storeID;
	}
	public String getGoodsId() {
		return goodsId;
	}
	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}
	public String getGoodsIcon() {
		return goodsIcon;
	}
	public void setGoodsIcon(String goodsIcon) {
		this.goodsIcon = goodsIcon;
	}
	public String getGoodsName() {
		return goodsName;
	}
	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}
	public String getGoodsAttrIds() {
		return goodsAttrIds;
	}
	public void setGoodsAttrIds(String goodsAttrIds) {
		this.goodsAttrIds = goodsAttrIds;
	}
	public String getGoodsAttrNames() {
		return goodsAttrNames;
	}
	public void setGoodsAttrNames(String goodsAttrNames) {
		this.goodsAttrNames = goodsAttrNames;
	}
	public double getGoodsPrice() {
		return goodsPrice;
	}
	public void setGoodsPrice(double goodsPrice) {
		this.goodsPrice = goodsPrice;
	}
	public double getStorePrice() {
		return storePrice;
	}
	public void setStorePrice(double storePrice) {
		this.storePrice = storePrice;
	}
	public long getGoodsStock() {
		return goodsStock;
	}
	public void setGoodsStock(long goodsStock) {
		this.goodsStock = goodsStock;
	}
	public long getGoodsNumber() {
		return goodsNumber;
	}
	public void setGoodsNumber(long goodsNumber) {
		this.goodsNumber = goodsNumber;
	}
	public int getCartGoodsState() {
		return cartGoodsState;
	}
	public void setCartGoodsState(int cartGoodsState) {
		this.cartGoodsState = cartGoodsState;
	}
	public long getCreateTime() {
		return createTime;
	}
	public void setCreateTime(long createTime) {
		this.createTime = createTime;
	}
	public long getGoodsState() {
		return goodsState;
	}
	public void setGoodsState(int goodsState) {
		this.goodsState = goodsState;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getCartId() {
		return cartId;
	}
	public void setCartId(String cartId) {
		this.cartId = cartId;
	}
	public String getStoreName() {
		return storeName;
	}
	public void setStoreName(String storeName) {
		this.storeName = storeName;
	}
	public String getStoreSCID() {
		return storeSCID;
	}
	public void setStoreSCID(String storeSCID) {
		this.storeSCID = storeSCID;
	}
	public long getStoreUpdateTime() {
		return storeUpdateTime;
	}
	public void setStoreUpdateTime(long storeUpdateTime) {
		this.storeUpdateTime = storeUpdateTime;
	}
	public double getStoreTtotalPrice() {
		return storeTtotalPrice;
	}
	public void setStoreTtotalPrice(double storeTtotalPrice) {
		this.storeTtotalPrice = storeTtotalPrice;
	}
	public long getStoreNumber() {
		return storeNumber;
	}
	public void setStoreNumber(long storeNumber) {
		this.storeNumber = storeNumber;
	}
	public int getGoodsUseIntegralSet() {
		return goodsUseIntegralSet;
	}
	public void setGoodsUseIntegralSet(int goodsUseIntegralSet) {
		this.goodsUseIntegralSet = goodsUseIntegralSet;
	}
	public long getGoodsUseIntegralValue() {
		return goodsUseIntegralValue;
	}
	public void setGoodsUseIntegralValue(long goodsUseIntegralValue) {
		this.goodsUseIntegralValue = goodsUseIntegralValue;
	}
	public int getGoodsBuyType() {
		return goodsBuyType;
	}
	public void setGoodsBuyType(int goodsBuyType) {
		this.goodsBuyType = goodsBuyType;
	}
	public String getGoodsBuyTypeStr() {
		return goodsBuyTypeStr;
	}
	public void setGoodsBuyTypeStr(String goodsBuyTypeStr) {
		this.goodsBuyTypeStr = goodsBuyTypeStr;
	}

    public double getdBi() {
        return dBi;
    }

    public void setdBi(double dBi) {
        this.dBi = dBi;
    }
}
