package com.manoelmotoso.services

import android.widget.Toast
import com.manoelmotoso.helpers.Basic.setInterval
import com.manoelmotoso.views.VideoSlaveActivity
import kotlinx.android.synthetic.main.activity_video_admin.*
import kotlinx.android.synthetic.main.activity_video_slave.*
import org.json.JSONObject
import java.io.InputStreamReader
import java.io.OutputStream
import java.io.PrintStream
import java.net.Socket
import java.net.UnknownHostException


/**
 * Created by manoel on 10/20/17.
 */

internal class Client(internal val activity: VideoSlaveActivity, private val url: String, private val port: Int) {

    private lateinit var socket: Socket

    private var response: String = ""

    init {
        val socketClientThread = Thread(SocketClientThread())
        socketClientThread.start()
    }

    inner class SocketClientThread : Thread() {


        override fun run() = try {

            socket = Socket(url, port)

            val jsonObject = JSONObject()

            var outputStream: OutputStream = socket.getOutputStream()
            var printStream = PrintStream(outputStream)


            activity.runOnUiThread {
                activity.playVideoByIndex(0)
                // set interval
                setInterval({
                    if (socket.isConnected) {
                        jsonObject.put("moment", activity.videoSlave.currentPosition)
                        jsonObject.put("index", activity.currentIndex)
                        printStream.print(jsonObject)
                    }
                }, 2000)
            }


        } catch (e: UnknownHostException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            response = "UnknownHostException: " + e.toString()
        }

    }

}

