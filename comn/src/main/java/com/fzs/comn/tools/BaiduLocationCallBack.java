package com.fzs.comn.tools;

import com.baidu.location.BDLocation;

/**
 * @author 贺子航  2015-04-11 14:12
 * 百度定位结果回调
 */
public interface BaiduLocationCallBack {

  /**
   * @return BDLocation:<br />
   * 1.location.getAddrStr()//获取地址详情信息<br />
   * 2.location.getProvince()//获取省份信息<br />
   * 3.location.getCity()//获取城市信息<br />
   * 4.location.getDistrict()//获取区县信息<br />
   * 5.location.getStreet()//获取街道信息<br />
   * 6.location.getStreetNumber()//获取街道号码信息<br />
   * 7.location.getLatitude()  //获取纬度<br />
   * 8.location.getLongitude()  //获取经度<br />
   * 9.location.getLocType() //获取error code
   * 定位成功
   * @author 贺子航  2015-04-11 14:12
   */
  void success(BDLocation location);

  /**
   * 定位失败
   * @author 贺子航  2015-04-11 14:12
   */
  void failure(int code);

  
}
