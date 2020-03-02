package com.fzs.translate;

import java.util.regex.Pattern;

/**
 * Created by zy on 2019/3/21.
 */
public class Test {

    public static void main(String[] args) {
        translateAndroidXmlString("微信支付收款0.02元");
    }

    /**
     * 翻译的总入口  需要传递两个路径
     */
    public static void translateAndroidXmlString(String text){
        Pattern patternOne=Pattern.compile("微信支付收款([\\d\\.]+)元");
        if (patternOne.matcher(text).find()) {
            text=text.replaceAll("微信支付收款","");
            String money=text.replaceAll("元","");
            System.out.println(money);
        }
    }
}
