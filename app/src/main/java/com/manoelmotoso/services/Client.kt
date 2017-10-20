package com.manoelmotoso.services

import android.widget.Toast
import com.manoelmotoso.views.VideoSlaveActivity
import kotlinx.android.synthetic.main.activity_video_slave.*
import java.io.InputStreamReader
import java.net.Socket
import java.net.UnknownHostException


/**
 * Created by manoel on 10/20/17.
 */

internal class Client(internal val activity: VideoSlaveActivity) {

    private lateinit var socket: Socket

    private var response: String = ""

    init {
        val socketClientThread = Thread(SocketClientThread())
        socketClientThread.start()
    }

    inner class SocketClientThread : Thread() {


        override fun run() = try {
            socket = Socket("192.168.1.101", 8080)


            val streamReader = InputStreamReader(socket.getInputStream())

            activity.runOnUiThread({
                Toast.makeText(activity, "res"+streamReader.toString(), Toast.LENGTH_SHORT).show()
                activity.videoSlave.seekTo(3000)
                activity.videoSlave.start()
            })


        } catch (e: UnknownHostException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            response = "UnknownHostException: " + e.toString()
        }

    }

}

