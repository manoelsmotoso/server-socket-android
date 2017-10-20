package com.manoelmotoso.helpers

import java.net.NetworkInterface
import java.net.SocketException

/**
 * Created by manoel_motoso on 20/10/17.
 */

class NetworkHelper{

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
}
