package com.golabe.network.interceptor

import com.golabe.common.utils.FileUtils
import okhttp3.Cache
import okhttp3.Interceptor
import okhttp3.Response

class CacheInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val cachePath = FileUtils.getInstance().cacheFolder
        val cache = Cache(cachePath, 1024 * 1024 * 100)

        val request = chain.request()

        val maxTime = 24 * 60 * 60
        val response = chain.proceed(request)

        val newResponse = response.newBuilder()

                .build()


    }
}