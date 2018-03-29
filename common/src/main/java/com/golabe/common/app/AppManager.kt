package com.golabe.common.app

import android.app.Activity
import java.util.*

class AppManager private constructor() {
    private val activityStack = Stack<Activity>()

    companion object {
        val instance = AppManager()
    }

    fun add(act: Activity) {
        activityStack.add(act)
    }

    fun remove(act: Activity) {
        activityStack.remove(act)
    }

    fun currentAct(): Activity = activityStack.lastElement()
    fun removeAll() {
        for (act in activityStack) {
            act.finish()

        }
        activityStack.clear()
    }

}