package com.example.fypintelidea.core

import android.util.Log
import com.example.fypintelidea.core.Constants

object ConnectavoLogger {

    private val TAG = Constants.LOGGER_TAG
    private val Logs_Enabled = true

    private fun buildMsg(msg: String): String {

        val buffer = StringBuilder()
        val stackTraceElement = Thread.currentThread().stackTrace[4]

        buffer.append("[ ")
        buffer.append(Thread.currentThread().name)
        buffer.append(": ")
        buffer.append(stackTraceElement.fileName)
        buffer.append(": ")
        buffer.append(stackTraceElement.lineNumber)
        buffer.append(": ")
        buffer.append(stackTraceElement.methodName)
        buffer.append("() ] ===> ")
        buffer.append(msg)

        return buffer.toString()
    }

    fun v(msg: String) {
        if (Logs_Enabled) {
            Log.v(TAG, buildMsg(msg))
        }
    }

    fun d(msg: String) {
        if (Logs_Enabled) {
            Log.d(TAG, buildMsg(msg))
        }
    }

    fun i(msg: String) {
        if (Logs_Enabled) {
            Log.i(TAG, buildMsg(msg))
        }
    }

    fun w(msg: String) {
        if (Logs_Enabled) {
            Log.w(TAG, buildMsg(msg))
        }
    }

    fun w(msg: String, e: Exception) {
        if (Logs_Enabled) {
            Log.w(TAG, buildMsg(msg), e)
        }
    }

    fun e(msg: String) {
        if (Logs_Enabled) {
            Log.e(TAG, buildMsg(msg))
        }
    }

    fun e(msg: String, e: Exception) {
        if (Logs_Enabled) {
            Log.e(TAG, buildMsg(msg), e)
        }
    }

}