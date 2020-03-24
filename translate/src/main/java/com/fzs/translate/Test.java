package com.fzs.translate;

import java.util.regex.Pattern;

/**
 * Created by zy on 2019/3/21.
 */
public class Test {

    public static void main(String[] args) {
        translateAndroidXmlString("Windows_XP通过扫码向你付款0.01元");
    }

    /**
     * 翻译的总入口  需要传递两个路径
     */
    public static void translateAndroidXmlString(String content){
        Pattern patternOne=Pattern.compile("(\\S*)通过扫码向你付款([\\d\\.]+)元");
        if (patternOne.matcher(content).find()) {
            content=content.replaceAll("元","");
            content=content.replaceAll("通过扫码向你付款",",");
            String uname = content.split(",")[0];
            String money = content.split(",")[1];
            System.out.println(money);
        }
    }
}
