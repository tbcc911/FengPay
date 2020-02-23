package com.fzs.comn.model;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;

@Table(name = "MineOrderExchange")
public class MineOrderExchange extends Model {
	@Column(name = "orderid", unique = true, onUniqueConflict = Column.ConflictAction.REPLACE)
	private String orderid;// 订单编号
	@Column(name = "order_id")
	private String order_id;//订单编号
	@Column(name = "image")
	private String image;// 图片URL
	@Column(name = "order_status")
	private String order_status;// 订单状态
	@Column(name = "name")
	private String name;// 商品名称
	@Column(name = "shopname")
	private String shopname;// 商店名称
	
	@Column(name = "state")
	private String state;// 订单状态名称
    @Column(name = "price")
    private String price;// 单价
	@Column(name = "totalPrice")
	private String totalPrice;// 总价
	@Column(name = "count")
	private String count;//数量
	@Column(name = "company_name")
	private String company_name;//物流公司名称
	@Column(name = "company_mark")
	private String company_mark;//物流公司编码
	@Column(name = "courierCode")
	private String courierCode; //运单编号
    @Column(name = "createTime")
    private String createTime; //下单时间
    @Column(name = "payTime")
    private String payTime; //支付时间
    @Column(name = "freight")
    private String freight; //运费
    @Column(name = "payIntegral")
    private String payIntegral; //已经支付积分
	
	public String getOrder_id() {
		return order_id;
	}
	public void setOrder_id(String order_id) {
		this.order_id = order_id;
	}
	public String getShopname() {
		return shopname;
	}
	public void setShopname(String shopname) {
		this.shopname = shopname;
	}
	public String getCompany_name() {
		return company_name;
	}
	public void setCompany_name(String company_name) {
		this.company_name = company_name;
	}
	public String getCompany_mark() {
		return company_mark;
	}
	public void setCompany_mark(String company_mark) {
		this.company_mark = company_mark;
	}
	public String getCourierCode() {
		return courierCode;
	}
	public void setCourierCode(String courierCode) {
		this.courierCode = courierCode;
	}
	public String getOrder_status() {
		return order_status;
	}
	public void setOrder_status(String order_status) {
		this.order_status = order_status;
	}
	public String getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(String totalPrice) {
		this.totalPrice = totalPrice;
	}
	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getPayTime() {
        return payTime;
    }

    public void setPayTime(String payTime) {
        this.payTime = payTime;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getFreight() {
        return freight;
    }

    public void setFreight(String freight) {
        this.freight = freight;
    }

    public String getPayIntegral() {
        return payIntegral;
    }

    public void setPayIntegral(String payIntegral) {
        this.payIntegral = payIntegral;
    }
}
