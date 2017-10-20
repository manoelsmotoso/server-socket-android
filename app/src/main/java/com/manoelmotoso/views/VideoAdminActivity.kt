package com.manoelmotoso.views

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.widget.MediaController
import android.widget.TextView
import android.widget.VideoView
import com.manoelmotoso.services.R
import com.manoelmotoso.services.Server


/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class VideoAdminActivity : Activity() {

    private lateinit var mediaController: MediaController

    lateinit var videoView: VideoView
    lateinit var tvIpAddress: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_admin)

        tvIpAddress = findViewById(R.id.tvIpAddress) as TextView
        videoView = findViewById(R.id.fullscreen_video) as VideoView

        val uri = Uri.parse("android.resource://$packageName/raw/movie1")

        mediaController = MediaController(this)
        videoView.setMediaController(mediaController)

        videoView.setVideoURI(uri)
        Server(this)
    }




}
