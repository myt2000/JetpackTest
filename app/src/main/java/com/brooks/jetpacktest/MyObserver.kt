package com.brooks.jetpacktest

import android.util.Log
import androidx.lifecycle.*


class MyObserver : LifecycleEventObserver {
    private fun activityStart() {
        Log.d("MyObserver", "activityStart")
    }

    private fun activityStop() {
        Log.d("MyObserver", "activityStop")
    }

    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
        if (event == Lifecycle.Event.ON_START) {
            activityStart()
        } else if (event == Lifecycle.Event.ON_STOP) {
            activityStop()
        }
        Log.d("MyObserver", "onStateChanged: source : $source, event: $event")
    }
}