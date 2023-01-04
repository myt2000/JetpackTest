package com.brooks.jetpacktest

import androidx.lifecycle.ViewModel

class MainViewModel(countReserved: Int): ViewModel() {
    var counter = countReserved
}