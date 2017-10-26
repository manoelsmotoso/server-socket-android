package com.manoelmotoso.services

import android.os.Handler
import android.widget.Toast
import com.manoelmotoso.helpers.Basic
import com.manoelmotoso.helpers.Basic.setInterval
import com.manoelmotoso.helpers.NetworkHelper
import com.manoelmotoso.views.VideoAdminActivity
import kotlinx.android.synthetic.main.activity_video_admin.*
import org.json.JSONObject
import java.io.*
import java.net.ServerSocket
import java.net.Socket


class Server(internal var activity: VideoAdminActivity) {
    internal lateinit var serverSocket: ServerSocket
    var updateConversationHandler: Handler? = null
    internal var message = ""

    init {
        val socketServerThread = Thread(SocketServerThread())
        socketServerThread.start()
        updateConversationHandler = Handler()
    }


    private inner class SocketServerThread : Thread() {


        override fun run() = try {
            serverSocket = ServerSocket(socketServerPORT)
            activity.runOnUiThread {
                val ipAddress = NetworkHelper().ipAddress
                activity.tvIpAddress.text = "IP:$ipAddress PORTA:$socketServerPORT"
            }

            while (true) {
                val socket = serverSocket!!.accept()

                activity.runOnUiThread {
                    activity.playVideoByIndex(0)
                }

//                SocketServerReplyThread(socket).run()
                val commThread = CommunicationThread(socket)
                Thread(commThread).start()

            }
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }

    }

    private inner class SocketServerReplyThread internal constructor(private val hostThreadSocket: Socket) : Thread() {

        lateinit var mInterval: Basic.TaskHandle

        override fun run() = try {
            val outputStream: OutputStream = hostThreadSocket.getOutputStream()
            var printStream = PrintStream(outputStream)

            val jsonObject = JSONObject()
            activity.runOnUiThread {
                // set interval
                mInterval = setInterval({
                    printStream = PrintStream(outputStream)
                    jsonObject.put("moment", activity.fullscreen_video.currentPosition)
                    jsonObject.put("index", activity.currentIndex)
                    printStream.print(jsonObject)
                    printStream.close()
                }, 2000)
            }


        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
            message += "Something wrong! " + e.toString() + "\n"
            try {
                mInterval.invalidate();
                serverSocket.close()
            } catch (e1: IOException) {
                e1.printStackTrace()
            }

        }

    }

    internal inner class CommunicationThread(private val clientSocket: Socket) : Runnable {

        private var input: BufferedReader? = null

        init {

            try {

                this.input = BufferedReader(InputStreamReader(this.clientSocket.getInputStream()))

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }

        override fun run() {

            while (!Thread.currentThread().isInterrupted) {

                try {

                    val read = input!!.readLine()

                    updateConversationHandler!!.post(updateUIThread(read))

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }

    }

    internal inner class updateUIThread(private val msg: String) : Runnable {

        override fun run() {
            var jsonObj = JSONObject(msg)
            val moment = jsonObj.getInt("moment")
            val index = jsonObj.getInt("index")
            if (activity.currentIndex != index)
                activity.playVideoByIndex(index)

            activity.tvMsgFromClient.text = msg
        }
    }

    companion object {
        internal val socketServerPORT = 8080
    }
}
