package com.fzs.comn.tools;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.fzs.comn.R;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.hzh.frame.util.AndroidUtil;

public class ImageUtil extends com.hzh.frame.util.ImageUtil {


    /**
     * 生成二维码
     * @param url 二维码链接
     * @param context 上下文
     * @param userName 用户名称
     * @param isAder 是否广告代理商
     * @return
     */
    public static Bitmap create2DCode(String url,Context context,String userName,String phone,boolean isAder) {
        try {
            //底纹图片
            Bitmap bottomBitmap;
            // 生成二维码
            BitMatrix matrix = null;
            if(isAder){
                bottomBitmap = decodeResource(context.getResources(), R.mipmap.code2_yes_ader);
                matrix = new MultiFormatWriter().encode(url,BarcodeFormat.QR_CODE, bottomBitmap.getWidth()/3, bottomBitmap.getWidth()/3);
            }else{
                bottomBitmap = decodeResource(context.getResources(), R.mipmap.code2_no_ader);
                matrix = new MultiFormatWriter().encode(url,BarcodeFormat.QR_CODE, bottomBitmap.getWidth(), bottomBitmap.getHeight());
            }
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            // 二维矩阵转为一维像素数组,也就是一直横着排了
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    }
                    //					else{  
                    //						//白色填充背景
                    //	                    pixels[y * width + x] = 0xffffffff;  
                    //		            } 
                }
            }
            //二维码图片
            Bitmap topBitmap = Bitmap.createBitmap(width, height,Bitmap.Config.ARGB_4444);
            // 通过像素数组生成bitmap,具体参考api
            topBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //最终合成二维码图片
            return mergeBitmap(topBitmap,bottomBitmap,userName,phone,isAder);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 偷红包分享图片
     * @param context 当前Activity
     * @param url 分享地址URL
     * @param nick 昵称
     * @param head 头像
     * @param content 内容
     * @param inviteCode 邀请码
     * */
    public static Bitmap stealRedShareImage(Context context,String url,String nick,Bitmap head,String content,String inviteCode){
        try {
            // 生成二维码
            BitMatrix matrix = new MultiFormatWriter().encode(url,BarcodeFormat.QR_CODE, AndroidUtil.getWindowWith()/2, AndroidUtil.getWindowWith()/2);
            // 二维矩阵转为一维像素数组,也就是一直横着排了
            int[] pixels = new int[matrix.getWidth() * matrix.getHeight()];
            for (int y = 0; y < matrix.getHeight(); y++) {
                for (int x = 0; x < matrix.getWidth(); x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * matrix.getWidth() + x] = 0xff000000;
                    }
                }
            }
            //二维码图片
            Bitmap codeBitmap = Bitmap.createBitmap(matrix.getWidth(), matrix.getHeight(),Bitmap.Config.ARGB_4444);
            // 通过像素数组生成bitmap,具体参考api
            codeBitmap.setPixels(pixels, 0, matrix.getWidth(), 0, 0, matrix.getWidth(), matrix.getHeight());
            //最终合成二维码图片
            return stealRedShareImageCompose(context,codeBitmap,nick,head,content,inviteCode);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成二维码
     * @param url 二维码链接
     * @return
     */
    public static Bitmap create2DCodePure(String url,int widthT,int heightT) {
        try {
            // 生成二维码
            BitMatrix matrix = matrix = new MultiFormatWriter().encode(url,BarcodeFormat.QR_CODE, widthT, heightT);
            int width = matrix.getWidth();
            int height = matrix.getHeight();
            // 二维矩阵转为一维像素数组,也就是一直横着排了
            int[] pixels = new int[width * height];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    }
                    else{
                        //白色填充背景
                        //	                    pixels[y * width + x] = 0xffffffff;  
                    }
                }
            }
            //二维码图片
            Bitmap topBitmap = Bitmap.createBitmap(width, height, Config.ARGB_4444);
            // 通过像素数组生成bitmap,具体参考api
            topBitmap.setPixels(pixels, 0, width, 0, 0, width, height);
            //最终合成二维码图片
            return topBitmap;
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 合成图片
     * @param context 当前Activity
     * @param codeBitmap 二维码
     * @param nick 昵称
     * @param head 头像
     * @param content 内容
     */
    private static Bitmap stealRedShareImageCompose(Context context,Bitmap codeBitmap,String nick,Bitmap head,String content,String inviteCode) {
        if(Util.isEmpty(head)){
            head=decodeResource(context.getResources(), R.drawable.base_image_default);
        }
        //背景图片比例 (576:1026)
        Bitmap bgBitmap = decodeResource(context.getResources(), R.mipmap.ui_user_stealred_share_icon);
        Bitmap canvasBitmap = Bitmap.createBitmap(AndroidUtil.getWindowWith()/10*9, AndroidUtil.getWindowWith()/10*9*1026/576,bgBitmap.getConfig());
        Canvas canvas = new Canvas(canvasBitmap);
        //字体宽度(自定义)
        int fontWidth=canvasBitmap.getHeight()/45;
        /**************************************************背景************************************************************/
        Rect bgSrc = new Rect();// 图片显示大小(可绘画图片的某一些地方，而不是全部图片)
        Rect bgDst = new Rect();// 图片在canvas上显示的位置及大小
        bgSrc.left = 0;
        bgSrc.top = 0;
        bgSrc.right = bgSrc.left + bgBitmap.getWidth();
        bgSrc.bottom = bgSrc.top + bgBitmap.getHeight();
        bgDst.left = 0;
        bgDst.top = 0;
        bgDst.right = bgDst.left + canvasBitmap.getWidth();
        bgDst.bottom = bgDst.top + canvasBitmap.getHeight();
        canvas.drawBitmap(bgBitmap, bgSrc, bgDst, null);
        /**************************************************头像************************************************************/
        Rect headSrc = new Rect();// 图片显示大小(可绘画图片的某一些地方，而不是全部图片)
        Rect headDst = new Rect();// 图片在canvas上显示的位置及大小
        headSrc.left = 0;
        headSrc.top = 0;
        headSrc.right = headSrc.left + head.getWidth();
        headSrc.bottom = headSrc.top + head.getHeight();
        headDst.left = canvasBitmap.getWidth()/20;
        headDst.top = canvasBitmap.getWidth()/20;
        headDst.right = headDst.left + canvasBitmap.getWidth()/6 ;
        headDst.bottom = headDst.top + canvasBitmap.getWidth()/6 ;
        canvas.drawBitmap(head, headSrc, headDst, null);
        /**************************************************内容背景************************************************************/
        Bitmap contentBitmap = decodeResource(context.getResources(), R.mipmap.ui_user_stealred_share_content);
        Rect contentSrc = new Rect();// 图片显示大小(可绘画图片的某一些地方，而不是全部图片)
        Rect contentDst = new Rect();// 图片在canvas上显示的位置及大小
        contentSrc.left = 0;
        contentSrc.top = 0;
        contentSrc.right = contentBitmap.getWidth();
        contentSrc.bottom = contentBitmap.getHeight();
        contentDst.left = headDst.left;
        contentDst.top = headDst.bottom + headDst.top/2;
        contentDst.right = contentDst.left + (int)(content.length()*(canvasBitmap.getWidth()/100.0*4.3));
        contentDst.bottom = contentDst.top + (int)(canvasBitmap.getHeight()/100.0*6);
        canvas.drawBitmap(contentBitmap, contentSrc, contentDst, null);
        /**************************************************邀请码背景************************************************************/
        Bitmap inviteBitmap = decodeResource(context.getResources(), R.mipmap.ui_user_stealred_share_invite);
        Rect inviteSrc = new Rect();// 图片显示大小(可绘画图片的某一些地方，而不是全部图片)
        Rect inviteDst = new Rect();// 图片在canvas上显示的位置及大小
        inviteSrc.left = 0;
        inviteSrc.top = 0;
        inviteSrc.right = inviteSrc.left + inviteBitmap.getWidth();
        inviteSrc.bottom = inviteSrc.top + inviteBitmap.getHeight();
        inviteDst.left = canvasBitmap.getWidth()/2-(int)((inviteCode.length()*(canvasBitmap.getWidth()/100.0*3.5))/2);
        inviteDst.top = canvasBitmap.getHeight()-canvasBitmap.getHeight()*257/858-(int)((canvasBitmap.getHeight()/100.0*6)/2);
        inviteDst.right = inviteDst.left + (int)(inviteCode.length()*(canvasBitmap.getWidth()/100.0*3.5));
        inviteDst.bottom = inviteDst.top + (int)(canvasBitmap.getHeight()/100.0*6);
        canvas.drawBitmap(inviteBitmap, inviteSrc, inviteDst, null);
        /**************************************************二维码************************************************************/
        //看到的二维码大小并非实际的大小 二维码Bitmap有一个透明的较大的外边框所以这里即使设置二维码大小为背景图片宽度的1/2实际显示的二维嘛大小较1/2要小一些
        Rect codeSrc = new Rect();// 图片显示大小(可绘画图片的某一些地方，而不是全部图片)
        Rect codeDst = new Rect();// 图片在canvas上显示的位置及大小
        codeSrc.left = 0;
        codeSrc.top = 0;
        codeSrc.right = codeSrc.left + codeBitmap.getWidth();
        codeSrc.bottom = codeSrc.top + codeBitmap.getHeight();
        codeDst.left = canvasBitmap.getWidth()/2-(int)((canvasBitmap.getWidth()/100.0*47)/2);
        //背景图片下边白色区域占总图片比例(257:858)
        codeDst.top = canvasBitmap.getHeight()-canvasBitmap.getHeight()*257/858;
        codeDst.right = codeDst.left + (int)(canvasBitmap.getWidth()/100.0*47);
        codeDst.bottom = codeDst.top + (int)(canvasBitmap.getWidth()/100.0*47);
        canvas.drawBitmap(codeBitmap, codeSrc, codeDst, null);
        /**************************************************昵称************************************************************/
        //Paint.ANTI_ALIAS_FLAG:这个标志位意指抗锯齿的
        Paint p=new Paint(Paint.ANTI_ALIAS_FLAG);
        p.setColor(Color.WHITE);
        p.setTextSize(canvasBitmap.getHeight()/40);
        int nickLeft=headDst.right+headDst.left;
        int nickTop=headDst.top+canvasBitmap.getWidth()/6/5*2;
        canvas.drawText(nick, nickLeft, nickTop, p);
        /**************************************************内容************************************************************/
        int contentLeft=contentDst.left+(int)((contentDst.right-contentDst.left)/100.0*4.5);
        int contentTop=contentDst.top+(int)((contentDst.bottom-contentDst.top)/100.0*70);
        canvas.drawText(content, contentLeft, contentTop, p);
        /**************************************************邀请码************************************************************/
        p.setColor(Color.parseColor("#50300e"));
        int inviteLeft=inviteDst.left+(int)((inviteDst.right-inviteDst.left)/100.0*7);
        int inviteTop=inviteDst.top+(int)((inviteDst.bottom-inviteDst.top)/100.0*65);
        canvas.drawText(inviteCode, inviteLeft, inviteTop, p);
        return canvasBitmap;
    }

}