package com.brooks.jetpacktest

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.content.edit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.BackoffPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import kotlinx.android.synthetic.main.activity_main.*
import java.util.concurrent.TimeUnit
import kotlin.concurrent.thread


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
            viewModel.plusOne()
        }

        clearBtn.setOnClickListener {
            viewModel.clear()
        }

        getUserBtn.setOnClickListener {
            val userId = (0..10000).random().toString()
            viewModel.getUser(userId)
        }
        viewModel.user.observe(this, Observer {
                user -> infoText.text = user.firstName
        })

        viewModel.counter.observe(this, Observer { count ->
            infoText.text = count.toString()
        })

        val userDao = AppDatabase.getDatabase(this).userDao()
        val user1 = User("Tom", "Brady", 40)
        val user2 = User("Tom", "Hanks", 63)

        addDataBtn.setOnClickListener {
            thread {
                user1.id = userDao.insertUser(user1)
                user2.id = userDao.insertUser(user2)
            }
        }

        updateDataBtn.setOnClickListener {
            thread {
                user1.age = 42
                userDao.updateUser(user1)
            }
        }

        deleteDataBtn.setOnClickListener {
            thread {
                userDao.deleteUserByLastName("Hanks")
            }
        }

        queryDataBtn.setOnClickListener {
            thread {
                for (user in userDao.loadAllUsers()) {
                    Log.d("MainActivity", user.toString())
                }
                var path = getDatabasePath("app_database").getAbsolutePath();
                Log.d("数据库路径", "$path")
            }
        }

        doWorkBtn.setOnClickListener {
            val request = OneTimeWorkRequest.Builder(SimpleWorker::class.java).build()
            WorkManager.getInstance(this).enqueue(request)

            val request2 = OneTimeWorkRequest.Builder(SimpleWorker::class.java)
                .setInitialDelay(5, TimeUnit.MINUTES)
                .addTag("simple")
                .setBackoffCriteria(BackoffPolicy.LINEAR, 10, TimeUnit.SECONDS)
                .build()

//            WorkManager.getInstance(this).cancelAllWorkByTag("simple")
//            WorkManager.getInstance(this).cancelWorkById(request2.id)
//            WorkManager.getInstance(this).cancelAllWork()

            WorkManager.getInstance(this)
                .getWorkInfoByIdLiveData(request2.id)
                .observe(this) { workInfo ->
                    if (workInfo.state == WorkInfo.State.SUCCEEDED) {
                        Log.d("MainActivity", "do work succeeded")
                    } else if (workInfo.state == WorkInfo.State.FAILED) {
                        Log.d("MainActivity", "do work failed")
                    }
                }
        }
    }

    override fun onPause() {
        super.onPause()
        sp.edit {
            putInt("count_reserved", viewModel.counter.value?:0)
        }
    }
}