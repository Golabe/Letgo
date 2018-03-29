package com.golabe.common.mvp.ui.activity

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.golabe.common.app.AppManager

abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutResId())
        AppManager.instance.add(this)
        initView()
        fetchData()
    }

    abstract fun getLayoutResId(): Int
    abstract fun initView()
    abstract fun fetchData()

    override fun onDestroy() {
        super.onDestroy()
        AppManager.instance.remove(this)
    }
}