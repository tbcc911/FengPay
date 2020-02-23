package com.fzs.comn.tools;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author
 * @version 1.0
 * @date 2019/12/3
 */
public class Util extends com.hzh.frame.util.Util {

    /**
     * 获取2个颜色的中间色
     * @param startColor 开始颜色
     * @param endColor 结束颜色
     * @param weight 颜色段(0-1之间)
     */
    public static String getMiddleColor(String startColor, String endColor,float weight) {
        if (startColor.contains("#") && endColor.contains("#") && startColor.length() == endColor.length() && endColor.length() == 7) {
            String tempColor1 = startColor.replace("#", "");
            String tempColor2 = endColor.replace("#", "");
            
            int rStart=Integer.parseInt(tempColor1.substring(0,2),16);
            int rEnd=Integer.parseInt(tempColor2.substring(0,2),16);
            int rDifference=rEnd-rStart;
            int gStart=Integer.parseInt(tempColor1.substring(2,4),16);
            int gEnd=Integer.parseInt(tempColor2.substring(2,4),16);
            int gDifference=gEnd-gStart;
            int bStart=Integer.parseInt(tempColor1.substring(4,6),16);
            int bEnd=Integer.parseInt(tempColor2.substring(4,6),16);
            int bDifference=bEnd-bStart;
            
            int rCenter= (int) (rStart+rDifference*weight);
            int gCenter= (int) (gStart+gDifference*weight);
            int bCenter= (int) (bStart+bDifference*weight);
            return "#"+String.format("%02x", rCenter)+String.format("%02x", gCenter)+String.format("%02x", bCenter);
        }
        return "";
    }

    /**
     * 生成随机数
     * @param min 最小值
     * @param max 最大值
     * */
    public static double random(double min,double max){
        return Math.random()*(max-min)+min;
    }

    /**
     * 生成随机数(1-max)
     * @param max 最大值
     * */
    public static int random(int max){
        return new Random().nextInt(max)+1;
    }

    /**
     * 生成不重复的随机数
     * @param max 最大长度(随机范围：0 --> (max-1))
     * @param except 需要剔除的数
     * */
    public static int getPrize(int max,int... except){
        //0-5随机
        int prize=new Random().nextInt(max);
        if(except!=null && except.length>0){
            //需要判断重复
            boolean isRepeat = false;//是否重复
            for (int i=0;i<except.length;i++){
                if(prize==except[i]){
                    isRepeat=true;
                    break;
                }
            }
            if(isRepeat){//重复
                return getPrize(max,except);
            }else{//不重复
                return prize;
            }
        }else{
            //不需要判断重复
            return prize;
        }
    }


    private static long lastClickTime;
    public synchronized static boolean isFastClick() {
        long time = System.currentTimeMillis();
        if (time - lastClickTime < 500) {
            return true;
        }
        lastClickTime = time;
        return false;
    }

    /**
     * 判断是否是数字[0-9]并且能被X整除
     * @param x 被整除数
     * @param content 需要判断的数值
     * */
    public static boolean isNumberX(int x,String content) {
        Pattern p = Pattern.compile("[0-9]*");
        Matcher m = p.matcher(content);
        if (m.matches()) {
            if (Integer.parseInt(content) % x == 0) {
                return true;
            }
        }
        return false;
    }

    /**
     * 隐藏内容中间几位 用 *号代替
     * @param content 内容
     * */
    public static String phoneHide(String content){
        if(!Util.isEmpty(content) && content.length()==11){
            return content.replaceAll("(\\d{3})\\d{4}(\\d{4})","$1****$2");
        }else{
            return content;
        }
    }

    /**
     * 隐藏内容中间几位 用 *号代替
     * @param content 内容
     * */
    public static String idCardHide(String content){
        if(!Util.isEmpty(content) && content.length()==18){
            return content.replaceAll("(\\d{4})\\d{10}(\\w{4})","$1*****$2");
        }else{
            return content;
        }
    }
}
