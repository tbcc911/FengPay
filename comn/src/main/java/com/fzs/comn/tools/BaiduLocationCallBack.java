package com.fzs.comn.tools;

import com.baidu.location.BDLocation;

public interface BaiduLocationCallBack {

  void success(BDLocation location);
  void failure(int code);

  
}
