package com.fzs.comn.tools;
public class TextTools {

    public static String getPrizeName(int index){
            if(index==0){
                return "草莓";
            } else
            if(index==1){
                return "西瓜";
            } else
            if(index==2){
                return "柠檬";
            } else
            if(index==3){
                return "樱桃";
            } else
            if(index==4){
                return "橘子";
            } else
            if(index==5){
                return "葡萄";
            }else{
                return "错误";
            }
    }

}
