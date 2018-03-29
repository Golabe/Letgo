package com.golabe.network.retrofit

import com.golabe.app.AppConstant
import com.golabe.network.interceptor.CacheInterceptor
import com.golabe.utils.FileUtils
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList
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


    private fun createOkHttpClient(): OkHttpClient {
        val cacheFolder = FileUtils.getInstance().cacheFolder
        val cache = Cache(cacheFolder, 1024 * 1024 * 100)
        return OkHttpClient.Builder()
                .addInterceptor(HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(CacheInterceptor())
                .cache(cache).addInterceptor(object :Interceptor{
                    override fun intercept(chain: Interceptor.Chain?): Response {

                    }
                })
                .writeTimeout(10, TimeUnit.SECONDS)
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
//                .cookieJar(object : CookieJar {
//                    private val cookiesStore = HashMap<HttpUrl, List<Cookie>>(16)
//
//                    override fun saveFromResponse(url: HttpUrl, cookies: MutableList<Cookie>) {
//                        HttpUrl.parse(url.host())?.let { cookiesStore.put(it, cookies) }
//
//                    }
//
//                    override fun loadForRequest(url: HttpUrl): MutableList<Cookie> {
//
//                        val cookies = cookiesStore[HttpUrl.parse(url.host())] as MutableList<Cookie>
//                        return if (cookies != null) cookies else ArrayList()
//
//                    }
//                })
                .build()
    }
}