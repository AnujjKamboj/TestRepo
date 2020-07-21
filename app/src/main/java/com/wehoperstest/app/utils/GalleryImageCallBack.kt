package com.wehoperstest.app.utils

import android.content.Context
import android.content.Intent

import java.util.ArrayList

class GalleryImageCallBack private constructor(private val context: Context)  //private constructor.
{
    companion object {
    private var listnerr: GalleryImageListener? = null
    private var buttonCallBack: GalleryImageCallBack? = null

    fun getInstance(context: Context): GalleryImageCallBack {
        if (buttonCallBack == null) { //if there is no instance available... create new one
            buttonCallBack = GalleryImageCallBack(context)
        }
        return buttonCallBack!!
    }
}

    fun onGalleryImageListener(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        if (listnerr != null) {
            listnerr!!.onGalleryImageCallBack(requestCode, resultCode, data)
        }
    }

    fun setGalleryImageListener(listner: GalleryImageListener) {
        listnerr = listner
    }

    //    requestCode: Int,
    //    permissions: Array<out String>,
    //    grantResults: IntArray
    interface GalleryImageListener {
        fun onGalleryImageCallBack(
            requestCode: Int, resultCode: Int, data: Intent?
        )
    }


}