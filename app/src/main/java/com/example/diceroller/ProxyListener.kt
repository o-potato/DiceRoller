package com.example.diceroller

import android.util.Log
import android.view.View

abstract class ProxyClickListener : View.OnClickListener{
    override fun onClick(v: View?) {
        if (v != null) {
            doOnClick(v)
            printLog(v)
        }
    }

    private fun printLog(v: View) {
        val info = TrackInfo(v, "CLICK")
        Log.i("Track", info.viewName + " " + info.action)
    }

    abstract fun doOnClick(v: View)
}

abstract class ProxyLongClickListener : View.OnLongClickListener{
    override fun onLongClick(v: View?): Boolean {
        if (v != null){
            doOnLongClick(v)
            printLog(v)
        }
        return true
    }

    private fun printLog(v: View) {
        val info = TrackInfo(v, "LONG_CLICK")
        Log.i("Track", info.viewName + " " + info.action)
    }

    abstract fun doOnLongClick(v: View)
}

class TrackInfo(v: View, a: String){
    val viewName: String = v.id.toString()
    val action: String = a
}