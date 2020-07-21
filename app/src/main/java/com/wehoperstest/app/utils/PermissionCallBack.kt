package com.wehoperstest.app.utils

import android.content.Context

class PermissionCallBack private constructor(private val context: Context)  //private constructor.
{
    companion object {
    private var listnerr: PermissionListener? = null
    private var buttonCallBack: PermissionCallBack? = null

    fun getInstance(context: Context): PermissionCallBack {
        if (buttonCallBack == null) { //if there is no instance available... create new one
            buttonCallBack = PermissionCallBack(context)
        }
        return buttonCallBack!!
    }
}

    fun onPermissionListener(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (listnerr != null) {
            listnerr!!.onPermissionCallBack(requestCode, permissions, grantResults)
        }
    }

    fun setButtonListener(listner: PermissionListener) {
        listnerr = listner
    }

    //    requestCode: Int,
    //    permissions: Array<out String>,
    //    grantResults: IntArray
    interface PermissionListener {
        fun onPermissionCallBack(
            requestCode: Int,
            permissions: Array<out String>,
            grantResults: IntArray
        )
    }


}