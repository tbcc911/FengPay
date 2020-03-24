package com.fzs.comn.tools;

import android.Manifest;
import android.app.Activity;
import android.os.Build;
import android.util.Log;

import com.activeandroid.query.Select;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.fzs.comn.R;
import com.fzs.comn.model.User;
import com.hzh.frame.comn.callback.HttpCallBack;
import com.hzh.frame.core.BaseSP;
import com.hzh.frame.core.HttpFrame.BaseHttp;
import com.hzh.frame.util.PowerUtil;
import com.hzh.frame.widget.toast.BaseToast;

import org.json.JSONObject;

public class BaiduLocation {
  private Activity mActivity;
  private LocationClient mLocationClient = null;
  private BaiduLocationCallBack callBack;
  // 返回的定位结果是百度经纬度:默认值gcj02(国标),这里是因为我们后台用的百度加密坐标,为了距离换算方便所以我们这里也用的百度加密坐标
  private String coorType = "bd09ll";
  public static final int REQUEST_CODE=100;
  public static final String[] PERMISSIONS_CONTACT = new String[]{Manifest.permission.ACCESS_FINE_LOCATION} ;
  
  public BaiduLocation(Activity activity) {
      mActivity=activity;
      if (Build.VERSION.SDK_INT>=23){
          showContacts();
      }else{
          startLocation();
      }
  }

  public BaiduLocation(Activity activity, BaiduLocationCallBack cb) {
      mActivity=activity;
      callBack = cb;
      if (Build.VERSION.SDK_INT>=23){
          showContacts();
      }else{
          startLocation();
      }
  }

  public void showContacts(){
      String[] permissions=new String[]{Manifest.permission.ACCESS_FINE_LOCATION};
      if(!PowerUtil.selectApply(mActivity,permissions)){
          PowerUtil.apply(mActivity,100,permissions);
      }else{
          startLocation();
      }
  }
  
  /**
   * @description 开始百度定位
   * @author 贺子航  2015-04-22 16:07
   */
  public void startLocation() {
    mLocationClient = new LocationClient(mActivity); // 声明LocationClient类
    mLocationClient.registerLocationListener(new MyLocationListener()); // 注册监听函数
    setoption();
    mLocationClient.start();
    if (mLocationClient != null && mLocationClient.isStarted()) {
      mLocationClient.requestLocation();
    }
  }

  public void setoption() {
    LocationClientOption option = new LocationClientOption();
    option.setLocationMode(LocationClientOption.LocationMode.Battery_Saving);// 设置定位模式
    option.setCoorType(coorType);// 返回的定位结果是百度经纬度:默认值gcj02(国标),这里是因为我们后台用的百度加密坐标,为了距离换算方便所以我们这里也用的百度加密坐标
    option.setScanSpan(2000);// 设置发起定位请求的间隔时间为5000ms
    option.setIsNeedAddress(true);// 返回的定位结果包含地址信息
    option.setNeedDeviceDirect(true);// 返回的定位结果包含手机机头的方向
    mLocationClient.setLocOption(option);
  }

  /**
   * @description 定位结果回调
   * @author 贺子航  2015-04-22 16:07
   */
  class MyLocationListener extends BDAbstractLocationListener {
    public void onReceiveLocation(BDLocation location) {
      try {
        if (location == null) {
          Log.e("百度定位","------百度定位失败------");
        } else {
            if(62==location.getLocType() || 63==location.getLocType() || 67==location.getLocType() || (161<location.getLocType() && location.getLocType()<168)){
                Log.e("百度定位","------定位失败,请检查是否打开定位权限------");
                BaseSP.getInstance().put("lat","0");
                BaseSP.getInstance().put("lng","0");
                BaseSP.getInstance().put("LocatCityName","");
                if (callBack != null) {
                    callBack.failure(location.getLocType());
                }
                BaseToast.getInstance().setMsg(R.id.content,"获取用户定位权限失败,请手动开启").show();
//                new XDialog1Button(mActivity, "获取定位权限失败,请手动开启定位权限", new XDialog1Button.XDialog1ButtonCallBack() {
//                    @Override
//                    public void confirm() {
//                        Intent localIntent = new Intent();
//                        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        if (Build.VERSION.SDK_INT >= 9) {
//                            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//                            localIntent.setData(Uri.fromParts("package", AndroidUtil.getPackageName(), null));
//                        } else if (Build.VERSION.SDK_INT <= 8) {
//                            localIntent.setAction(Intent.ACTION_VIEW);
//                            localIntent.setClassName("com.android.settings","com.android.settings.InstalledAppDetails");
//                            localIntent.putExtra("com.android.settings.ApplicationPkgName", AndroidUtil.getPackageName());
//                        }
//                        mActivity.startActivity(localIntent);
//                    }
//                }).show();
            }else{
                Log.e("百度定位","------百度定位成功------>Lat:"+location.getLatitude()+";Lng"+location.getLongitude());
                BaseSP.getInstance().put("lat",location.getLatitude()+"");
                BaseSP.getInstance().put("lng",location.getLongitude()+"");
                BaseSP.getInstance().put("LocatCityName",location.getCity()+"");
//                updServerUserArea(location);
                if (callBack != null) {
                    callBack.success(location);
                }
            }
            mLocationClient.stop();
        }
      } catch (Exception e) {
        e.printStackTrace();
        Log.e("百度定位","------数据处理异常------");
      }
    }
  }
  public void updServerUserArea(BDLocation location) throws Exception {
	User user=new Select().from(User.class).executeSingle();
	if(!Util.isEmpty(user)){
		//保证用户存在
		JSONObject params=new JSONObject();
	    params.put("CITY", location.getCityCode());
	    params.put("LNG", location.getLongitude());
	    params.put("LAT", location.getLatitude());
	    BaseHttp.getInstance().writeGreen("2017", params, new HttpCallBack(){
			@Override
			public void onSuccess(JSONObject response) {
				if("1".equals(response.optString("result"))){
				    JSONObject data=response.optJSONObject("data");
					if("1".equals(data.optString("code"))){
						Log.e("百度定位","------修改服务器用户地理信息成功------");
					}else{
		            	Log.e("百度定位","------修改服务器用户地理信息失败------");
					}
				}
			}
			@Override
			public void onFail() {
	            Log.e("百度定位","------修改服务器用户地理信息失败------");
			}
	    });
	}
  }
  
}
