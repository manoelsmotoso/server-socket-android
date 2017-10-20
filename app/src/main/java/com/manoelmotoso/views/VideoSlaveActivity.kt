package com.manoelmotoso.views

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import com.manoelmotoso.services.Client
import com.manoelmotoso.services.R
import kotlinx.android.synthetic.main.activity_video_slave.*

class VideoSlaveActivity : Activity() {

    private lateinit var mediaController: MediaController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_slave)

        val uri = Uri.parse("android.resource://$packageName/raw/movie1")

        mediaController = MediaController(this)
        videoSlave.setMediaController(mediaController)

        videoSlave.setVideoURI(uri)
        Client(this)
    }
}
