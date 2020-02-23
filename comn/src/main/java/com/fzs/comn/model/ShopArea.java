package com.fzs.comn.model;

import java.io.Serializable;

/**
 * @author 贺子航  2015-04-03 09:49
 * @description 商品收货区域
 */
public class ShopArea implements Serializable {

  public String id;//区域ID
  public String delete;//是否删除状态 0:正常 1:删除
  public String areaName;//区域名称
  public String level;//当前区域等级 0:省  1:市 2:区
  public String parentId;//父节点ID
  public String areaCode;//邮编
  public String cityCode;//城市编码  
}
