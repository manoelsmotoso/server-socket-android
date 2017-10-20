package com.manoelmotoso.services

import com.manoelmotoso.helpers.Basic.setInterval
import com.manoelmotoso.views.VideoAdminActivity
import org.json.JSONObject
import java.io.IOException
import java.io.OutputStream
import java.io.PrintStream
import java.net.NetworkInterface
import java.net.ServerSocket
import java.net.Socket
import java.net.SocketException

class Server(internal var activity: VideoAdminActivity) {
    internal lateinit var serverSocket: ServerSocket
    internal var message = ""

    val port: Int
        get() = socketServerPORT

    // TODO Auto-generated catch block
    val ipAddress: String
        get() {
            var ip = ""
            try {
                val enumNetworkInterfaces = NetworkInterface
                        .getNetworkInterfaces()
                while (enumNetworkInterfaces.hasMoreElements()) {
                    val networkInterface = enumNetworkInterfaces.nextElement()
                    val enumInetAddress = networkInterface.inetAddresses
                    while (enumInetAddress.hasMoreElements()) {
                        val inetAddress = enumInetAddress.nextElement()

                        if (inetAddress.isSiteLocalAddress) {
                            ip += "Server running at : " + inetAddress.hostAddress
                        }
                    }
                }

            } catch (e: SocketException) {
                e.printStackTrace()
                ip += "Something Wrong! " + e.toString() + "\n"
            }

            return ip
        }

    init {
        val socketServerThread = Thread(SocketServerThread())
        socketServerThread.start()
    }

    fun onDestroy() {

        try {
            serverSocket!!.close()
        } catch (e: IOException) {
            // TODO Auto-generated catch block
            e.printStackTrace()
        }


    }

    private inner class SocketServerThread : Thread() {


        override fun run() = try {
            serverSocket = ServerSocket(socketServerPORT)
            activity.runOnUiThread { activity.tvIpAddress.text = "IP:$ipAddress PORTA:$socketServerPORT" }

            while (true) {
                val socket = serverSocket!!.accept()

                activity.runOnUiThread {
                    activity.videoView.start()
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
                    jsonObject.put("moment", activity.videoView.currentPosition)
                    jsonObject.put("index", 0)
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

    companion object {
        internal val socketServerPORT = 8080
    }
}
