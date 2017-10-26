package com.manoelmotoso.views

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import com.manoelmotoso.services.R
import com.manoelmotoso.services.Server
import kotlinx.android.synthetic.main.activity_video_admin.*

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class VideoAdminActivity : Activity() {
    var videosPath: Array<String>? = null

    var currentIndex: Int = 0
    var mMoment:Int=0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_admin)
        videosPath = arrayOf("android.resource://$packageName/raw/movie1", "android.resource://$packageName/raw/movie2")
        Server(this)
    }


    fun playVideoByIndex(index: Int) {
        currentIndex = index
        val uri = Uri.parse(videosPath!![currentIndex])
        fullscreen_video.setVideoURI(uri)
        fullscreen_video.seekTo(0)
        fullscreen_video.start()
    }

}
