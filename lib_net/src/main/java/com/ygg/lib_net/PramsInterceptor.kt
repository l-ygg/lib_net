package com.ygg.lib_net

import android.text.TextUtils
import okhttp3.*

/**
 * Copyright (C) 2021 重庆呼我出行网络科技有限公司
 * 版权所有
 * <p>
 * 功能描述：添加全局参数
 * <p>
 * <p>
 * 作者：lengyang 2021/12/20
 * <p>
 * 修改人：
 * 修改描述：
 * 修改日期
 */
class PramsInterceptor(
    private val contents: Map<String, String>?
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val requestBuilder = original.newBuilder()

        if (contents != null && contents.isNotEmpty()) {
            if (original.body() is FormBody) {
                val newFormBody = FormBody.Builder()
                val oldFormBody = original.body() as FormBody?
                for (i in 0 until oldFormBody!!.size()) {
                    newFormBody.addEncoded(
                        oldFormBody!!.encodedName(i),
                        oldFormBody!!.encodedValue(i)
                    )
                }
                val keys = contents.keys
                for (headerKey in keys) {
                    if (original.body() is FormBody) {
                        newFormBody.add(headerKey, contents[headerKey])
                        requestBuilder.method(original.method(), newFormBody.build())
                    }
                }
            }else if(original.body() is MultipartBody){
                val newFormBody = MultipartBody.Builder()
                // 默认是multipart/mixed，大坑【主要是我们php后台接收时头信息要求严格】
                // 默认是multipart/mixed，大坑【主要是我们php后台接收时头信息要求严格】
                newFormBody.setType(MediaType.parse("multipart/form-data"))
                val oldFormBody = original.body() as MultipartBody?
                for (i in 0 until oldFormBody!!.size()) {
                    newFormBody.addPart(oldFormBody.part(i))
                }
                val keys = contents.keys
                for (headerKey in keys) {
                    if (original.body() is FormBody) {
                        newFormBody.addFormDataPart(headerKey, contents[headerKey])
                        requestBuilder.method(original.method(), newFormBody.build())
                    }
                }
            } else if (TextUtils.equals(original.method(), "POST")) {
                val newFormBody = FormBody.Builder()
                val keys = contents.keys
                for (headerKey in keys) {
                    if (original.body() is FormBody) {
                        newFormBody.add(headerKey, contents[headerKey])
                        requestBuilder.method(original.method(), newFormBody.build())
                    }
                }
            }
        }

        val request = requestBuilder.build()
        return chain.proceed(request)
    }
}