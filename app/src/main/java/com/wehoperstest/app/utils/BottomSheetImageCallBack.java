package com.wehoperstest.app.utils;

import android.content.Context;
import android.net.Uri;

import java.io.File;

public class BottomSheetImageCallBack {
    private static ImageCallBackListener listner;
    private Context context;
    private static BottomSheetImageCallBack buttonCallBack;

    private BottomSheetImageCallBack(Context context) {
        this.context = context;
    }  //private constructor.

    public static BottomSheetImageCallBack getInstance(Context context) {
        if (buttonCallBack == null) { //if there is no instance available... create new one
            buttonCallBack = new BottomSheetImageCallBack(context);
        }
        return buttonCallBack;
    }

    public void onImageCallBackListener(File file, String filePath, Uri uri) {
        if (listner != null) {
            listner.onImageCallBackCallBack(file, filePath, uri);
        }
    }

    public void setImageCallBackListener(ImageCallBackListener listner) {
        this.listner = listner;
    }

    public interface ImageCallBackListener {
        public void onImageCallBackCallBack(File file, String filePath, Uri uri);
    }
}