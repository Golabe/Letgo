package com.golabe.common.app

import android.app.Application
import android.content.Context

class LetgoApplcation private constructor() : Application() {
    private lateinit var mContext: Context

    companion object {
        val instance = LetgoApplcation()
    }

    override fun onCreate() {
        super.onCreate()
        mContext = this
    }

    fun getContext(): Context = mContext
}