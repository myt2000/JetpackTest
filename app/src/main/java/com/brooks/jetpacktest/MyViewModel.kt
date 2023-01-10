package com.brooks.jetpacktest

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel

class MyViewModel: ViewModel() {
    private val refreshLiveData = MutableLiveData<Any?>()

    val refreshResult = Transformations.switchMap(refreshLiveData) {
        Repository.refresh()  // 假设Repository中已经定义了refresh()方法
    }

    fun refresh() {
        refreshLiveData.value = refreshLiveData.value
    }
}