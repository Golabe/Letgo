package com.golabe.network.retrofit

import android.text.TextUtils
import com.golabe.common.app.AppConstant
import com.golabe.common.app.AppConstant.Companion.MAX_CACHE_TIME
import com.golabe.common.app.LetgoApplcation
import com.golabe.common.utils.FileUtils
import com.golabe.common.utils.NetworkUtils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetManager private constructor() {


    companion object {
        val instance = NetManager()
    }

    fun <S> createRetrofit(clazz: Class<S>): S {
        Retrofit.Builder()
                .baseUrl(AppConstant.BASE_URL)
                .client(createOkHttpClient())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .let {
                    return it.create(clazz)
                }

    }


    private fun createOkHttpClient(): OkHttpClient =
            OkHttpClient.Builder().apply {
                readTimeout(10, TimeUnit.SECONDS)
                writeTimeout(10, TimeUnit.SECONDS)
                connectTimeout(10, TimeUnit.SECONDS)
                addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))

                addInterceptor { chain ->
                    var request = chain.request()
                    val method = request.method()

                    if (method == "GET") request.newBuilder().header("Cache-Control", " public, max-age=$MAX_CACHE_TIME")

                    var cacheControl = request.cacheControl().toString()
                    if (!NetworkUtils.isNetwork(LetgoApplcation.instance.getContext()) && TextUtils.isEmpty(cacheControl)) {
                        request = request.newBuilder()
                                .cacheControl(CacheControl.FORCE_CACHE)
                                .build()
                    }
                    //如果没有添加注解,则不缓存
                    if (TextUtils.isEmpty(cacheControl) || "no-store".contains(cacheControl)) {
                        //响应头设置成无缓存
                        cacheControl = "no-store"
                    } else if (NetworkUtils.isNetwork(LetgoApplcation.instance.getContext())) {
                        //如果有网络,则将缓存的过期事件,设置为0,获取最新数据
                        cacheControl = "public, max-age=" + 0
                    } else {

                    }
                    val response = chain.proceed(request)

                    return@addInterceptor response.newBuilder()
                            .header("Cache-Control", cacheControl)
                            .removeHeader("Pragma")
                            .build()

                }
                cache(Cache(FileUtils.getInstance().cacheFolder, 1024 * 1024 * 100))

            }.build()
}