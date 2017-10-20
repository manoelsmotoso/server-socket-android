package com.manoelmotoso.helpers

import android.os.Handler
import java.util.*

/**
 * Created by manoel on 10/19/17.
 */

internal object Basic {

    // setTimeout, setInterval
    interface TaskHandle {
        fun invalidate()
    }

    fun setTimeout(r: Runnable, delay: Long): TaskHandle {
        val h = Handler()
        h.postDelayed(r, delay)
        return object : TaskHandle {
            override fun invalidate() {
                h.removeCallbacks(r)
            }
        }
    }

    fun setInterval(r: () -> Unit, interval: Long): TaskHandle {
        val t = Timer()
        val h = Handler()
        t.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                h.post(r)
            }
        }, interval, interval)  // Unlike JavaScript, in Java the initial call is immediate, so we put interval instead.
        return object : TaskHandle {
            override fun invalidate() {
                t.cancel()
                t.purge()
            }
        }
    }
}