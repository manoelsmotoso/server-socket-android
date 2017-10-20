package com.manoelmotoso.views

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import com.manoelmotoso.services.Client
import com.manoelmotoso.services.R
import kotlinx.android.synthetic.main.activity_video_admin.*
import kotlinx.android.synthetic.main.activity_video_slave.*

class VideoSlaveActivity : Activity() {

    var currentIndex: Int = 0

    private lateinit var videosPath: Array<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_slave)

        videosPath = arrayOf("android.resource://$packageName/raw/movie1", "android.resource://$packageName/raw/movie2")
        val uri = Uri.parse("android.resource://$packageName/raw/movie1")


        videoSlave.setVideoURI(uri)

        btnSetUrl.setOnClickListener({
            var url = edIpServer.text.toString()
            val port = Integer.parseInt(edPortServer.text.toString())
            Client(this, url, port)

        })

    }

    fun playVideoByIndex(index: Int) {
        fullscreen_video.seekTo(0)
        currentIndex = index
        val uri = Uri.parse(videosPath[currentIndex])
        fullscreen_video.setVideoURI(uri)
        fullscreen_video.start()
    }

}
