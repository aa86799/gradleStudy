package com.stone.gradlestudy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_test).setOnClickListener {
            Log.i("TAG", "onCreate: xxxx----")
//            Log.i("TAG", "onCreate: stone----")
//            HookClick.click()
        }
    }
}