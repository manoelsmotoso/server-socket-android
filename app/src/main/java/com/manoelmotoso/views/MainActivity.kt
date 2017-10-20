package com.manoelmotoso.views

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.manoelmotoso.services.R
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnNote8.setOnClickListener({
            startActivity(Intent(this, VideoAdminActivity::class.java))
        })

        btnTabE.setOnClickListener({
            startActivity(Intent(this, VideoSlaveActivity::class.java))
        })
    }

}