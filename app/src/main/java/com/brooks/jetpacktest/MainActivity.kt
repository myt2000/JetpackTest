package com.brooks.jetpacktest

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*

//class MyObserver {
//    fun activityStart() {
//
//    }
//
//    fun activityStop() {
//
//    }
//}
//
//
//class MainActivity  : AppCompatActivity() {
//
//    lateinit var observer: MyObserver
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        observer = MyObserver()
//    }
//
//    override fun onStart() {
//        super.onStart()
//        observer.activityStart()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        observer.activityStop()
//    }
//}

class MainActivity : AppCompatActivity() {

    lateinit var viewModel: MainViewModel
    lateinit var sp: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        lifecycle.addObserver(MyObserver(lifecycle))
        sp = getPreferences(Context.MODE_PRIVATE)
        val countReserved = sp.getInt("count_reserved", 0)

        viewModel = ViewModelProvider(this, MainViewModelFactory(countReserved)).get(MainViewModel::class.java)



//        viewModel = ViewModelProvider(this).get(MainViewModel::class.java)

        plusOneBtn.setOnClickListener {
            viewModel.counter++
            refreshCounter()
        }

        clearBtn.setOnClickListener {
            viewModel.counter = 0
            refreshCounter()
        }
        refreshCounter()
    }

    override fun onPause() {
        super.onPause()
        sp.edit {
            putInt("count_reserved", viewModel.counter)
        }
    }

    private fun refreshCounter() {
        infoText.text = viewModel.counter.toString()
    }
}