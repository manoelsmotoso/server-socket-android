package com.manoelmotoso.services

import com.manoelmotoso.helpers.Basic.setInterval
import com.manoelmotoso.helpers.NetworkHelper
import com.manoelmotoso.views.VideoAdminActivity
import kotlinx.android.synthetic.main.activity_video_admin.*
import org.json.JSONObject
import java.io.*
import java.net.ServerSocket
import java.net.Socket
import java.nio.Buffer
import android.system.Os.socket
import java.net.UnknownHostException


class Server(internal var activity: VideoAdminActivity) {
    internal lateinit var serverSocket: ServerSocket

    internal var message = ""

    init {
        val socketServerThread = Thread(SocketServerThread())
        socketServerThread.start()
    }



    private inner class SocketServerThread : Thread() {


        override fun run() = try {
            serverSocket = ServerSocket(socketServerPORT)
            activity.runOnUiThread {
                val ipAddress = NetworkHelper().ipAddress
                activity.tvIpAddress.text= "IP:$ipAddress PORTA:$socketServerPORT"
            }

            while (true) {
                val socket = serverSocket!!.accept()

                activity.runOnUiThread {
                    activity.playVideoByIndex(0)
                }

                SocketServerReplyThread(socket).run()

            }
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }

    private inner class SocketServerReplyThread internal constructor(private val hostThreadSocket: Socket) : Thread() {

        override fun run() = try {
            val outputStream: OutputStream = hostThreadSocket.getOutputStream()
            val printStream = PrintStream(outputStream)

            val jsonObject = JSONObject()
            activity.runOnUiThread {
                // set interval
                 setInterval({
                    jsonObject.put("moment", activity.fullscreen_video.currentPosition)
                    jsonObject.put("index", activity.currentIndex)
                    printStream.print(jsonObject)
                }, 2000)
            }


        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            message += "Something wrong! " + e.toString() + "\n"
            try {
                serverSocket.close()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }

        }

    }

    private inner class SocketServerCeceivingThread internal constructor(private val socket: Socket) : Thread() {
        override fun run() = try
        {
            val byteArrayOutputStream = ByteArrayOutputStream(1024)
            val buffer = ByteArray(1024)
            val bytesRead: Int
            val inputStream = socket.getInputStream()

        } catch (e: UnknownHostException)
        {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }
    }
    companion object {
        internal val socketServerPORT = 8080
    }
}
