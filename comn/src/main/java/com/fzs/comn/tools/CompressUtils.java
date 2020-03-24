package com.fzs.comn.tools;

import android.graphics.Bitmap;

public class CompressUtils{

    static {
        System.loadLibrary("native-lib");
    }
    
    public static native int compressBitmap(Bitmap bitmap, int quality, byte[] fileNameBytes,
                                            boolean optimize);

}