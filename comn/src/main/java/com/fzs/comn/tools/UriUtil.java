package com.fzs.comn.tools;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore.Images.ImageColumns;

public class UriUtil {
	
	/**
	 * 获取Uri的绝对路径
	 **/
	public static String getRealFilePath( final Context context, final Uri uri ) {  
	    if ( null == uri ) return null;  
	    final String scheme = uri.getScheme();  
	    String data = null;  
	    if ( scheme == null )  
	        data = uri.getPath();  
	    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {  
	        data = uri.getPath();  
	    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {  
	        Cursor cursor = context.getContentResolver().query( uri, new String[] { ImageColumns.DATA }, null, null, null );  
	        if ( null != cursor ) {  
	            if ( cursor.moveToFirst() ) {  
	                int index = cursor.getColumnIndex( ImageColumns.DATA );  
	                if ( index > -1 ) {  
	                    data = cursor.getString( index );  
	                }  
	            }  
	            cursor.close();  
	        }  
	    }  
	    return data;  
	}  
}
