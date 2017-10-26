package com.manoelmotoso.services

import android.widget.Toast
import com.manoelmotoso.views.VideoSlaveActivity
import kotlinx.android.synthetic.main.activity_video_slave.*
import java.io.BufferedReader
import java.io.InputStreamReader
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

            val echoSocket = Socket(url, port)
            val buffer = BufferedReader(InputStreamReader(echoSocket.getInputStream()))
            alertMsg("After buffer")
            var readText = buffer.readText()
            alertMsg("After readText")

            activity.runOnUiThread {
                activity.tvResponse!!.text = "" + readText
            }


        } catch (e: UnknownHostException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            response = "UnknownHostException: " + e.toString()
        }


    }

    fun alertMsg(msg: String) {
        activity.runOnUiThread {
            Toast.makeText(activity.applicationContext, msg, Toast.LENGTH_SHORT).show()
        }
    }


}


/*
*
socket = Socket(url, port)
val jsonObject = JSONObject()
var outputStream: OutputStream = socket.getOutputStream()
var printStream = PrintStream(outputStream)

// set interval
setInterval({
    if (socket.isConnected) {
        jsonObject.put("moment", activity.videoSlave.currentPosition)
        jsonObject.put("index", activity.currentIndex)
        printStream.print(jsonObject)
    }
}, 2000)
*/