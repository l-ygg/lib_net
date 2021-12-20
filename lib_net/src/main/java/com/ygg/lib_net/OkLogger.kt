package com.ygg.lib_net

import android.util.Log

object OkLogger {
    private var isLogEnable = true
    private var tag = "retrofit"
    fun debug(isEnable: Boolean) {
        debug(tag, isEnable)
    }

    fun debug(logTag: String, isEnable: Boolean) {
        tag = logTag
        isLogEnable = isEnable
    }

    fun v(msg: String?) {
        v(tag, msg)
    }

    fun v(tag: String?, msg: String?) {
        if (isLogEnable) msg?.let { Log.v(tag, it) }
    }

    fun d(msg: String?) {
        d(tag, msg)
    }

    fun d(tag: String?, msg: String?) {
        if (isLogEnable) msg?.let { Log.d(tag, it) }
    }

    fun i(msg: String?) {
        i(tag, msg)
    }

    fun i(tag: String?, msg: String?) {
        if (isLogEnable) msg?.let { Log.i(tag, it) }
    }

    fun w(msg: String?) {
        w(tag, msg)
    }

    fun w(tag: String?, msg: String?) {
        if (isLogEnable) msg?.let { Log.w(tag, it) }
    }

    fun e(msg: String?) {
        e(tag, msg)
    }

    fun e(tag: String?, msg: String?) {
        if (isLogEnable) msg?.let { Log.e(tag, it) }
    }

    @JvmStatic
    fun printStackTrace(t: Throwable?) {
        if (isLogEnable && t != null) t.printStackTrace()
    }
}