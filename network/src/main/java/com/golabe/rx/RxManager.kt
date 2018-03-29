package com.golabe.rx

import android.content.Context
import rx.Observable
import rx.Subscriber
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers

class RxManager private constructor() {
    companion object {
        val instance = RxManager()
    }

    fun <T> doSubcriber(ctx:Context,ob: Observable<T>, sb: Subscriber<T>): Subscription =
            ob

                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(sb)
}