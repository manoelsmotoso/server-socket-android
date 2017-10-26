package com.manoelmotoso.views

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.os.StrictMode
import android.widget.EditText
import android.widget.MediaController
import com.manoelmotoso.helpers.Basic
import com.manoelmotoso.services.Client
import com.manoelmotoso.services.R
import kotlinx.android.synthetic.main.activity_video_admin.*
import kotlinx.android.synthetic.main.activity_video_slave.*
import org.json.JSONObject
import java.io.BufferedWriter
import java.io.IOException
import java.io.OutputStreamWriter
import java.io.PrintWriter
import java.net.InetAddress
import java.net.Socket
import java.net.UnknownHostException

class VideoSlaveActivity : Activity() {

    var currentIndex: Int = 0
    private var socket: Socket? = null

    private var SERVERPORT = 8080
    private var SERVER_IP = "10.11.1.62"

    private lateinit var videosPath: Array<String>

    private lateinit var mInterval: Basic.TaskHandle

    private lateinit var mThread: Thread

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_video_slave)

        val policy = StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        videosPath = arrayOf("android.resource://$packageName/raw/movie1", "android.resource://$packageName/raw/movie2")

        playVideoByIndex(0)
        edIpServer.setText(SERVER_IP)
        edPortServer.setText(SERVERPORT.toString())

        videoSlave.setOnCompletionListener {
            if (currentIndex == 1)
                playVideoByIndex(0)
            if(currentIndex == 0){
                playVideoByIndex(1)
            }


        }

        btnSetUrl.setOnClickListener({
            SERVER_IP = edIpServer.text.toString()
            SERVERPORT = Integer.parseInt(edPortServer.text.toString())
            mThread = Thread(ClientThread())
            mThread.start()

            mInterval = Basic.setInterval({
                var jsonObject = JSONObject()

                try {
                    val str = videoSlave.currentPosition

                    jsonObject.put("moment", str)
                    jsonObject.put("index", currentIndex)

                    val out = PrintWriter(BufferedWriter(OutputStreamWriter(socket!!.getOutputStream())), true)
                    out.println(jsonObject)

                } catch (e: Exception) {
                    mInterval.invalidate()
                    e.printStackTrace()
                }

            }, 500)

        })


    }

    fun playVideoByIndex(index: Int) {
        currentIndex = index
        val uri = Uri.parse(videosPath[currentIndex])
        videoSlave.setVideoURI(uri)
        videoSlave.seekTo(0)
        videoSlave.start()

    }

    override fun onDestroy() {
        super.onDestroy()
        mThread.stop()
        mThread.destroy()
        mInterval.invalidate()
        socket!!.close()
    }

    internal inner class ClientThread : Runnable {

        override fun run() {

            try {
                val serverAddr = InetAddress.getByName(SERVER_IP)

                socket = Socket(serverAddr, SERVERPORT)

            } catch (e1: UnknownHostException) {
                e1.printStackTrace()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }

        }
    }

}
