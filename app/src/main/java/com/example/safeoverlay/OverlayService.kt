package com.example.safeoverlay

import android.app.Notification
import android.app.NotificationChannel
import android.app.Service
import android.content.Intent
import android.graphics.PixelFormat
import android.os.Build
import android.os.Handler
import android.os.IBinder
import android.os.Looper
import android.view.Gravity
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class OverlayService : Service() {
    private lateinit var windowManager: WindowManager
    private var panel: LinearLayout? = null
    private var clockView: TextView? = null
    private val handler = Handler(Looper.getMainLooper())
    private val clock = SimpleDateFormat("HH:mm:ss", Locale.getDefault())

    private val tick = object : Runnable {
        override fun run() {
            clockView?.text = "Время: ${clock.format(Date())}"
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate() {
        super.onCreate()
        createNotification()
        windowManager = getSystemService(WINDOW_SERVICE) as WindowManager
        showPanel()
    }

    private fun createNotification() {
        val channelId = "safe_overlay"
        if (Build.VERSION.SDK_INT >= 26) {
            val channel = NotificationChannel(
                channelId, "Safe overlay", android.app.NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(android.app.NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        val builder = if (Build.VERSION.SDK_INT >= 26) {
            Notification.Builder(this, channelId)
        } else {
            @Suppress("DEPRECATION")
            Notification.Builder(this)
        }
        val notification = builder
            .setContentTitle("Safe Game Overlay")
            .setContentText("Плавающая панель активна")
            .setSmallIcon(android.R.drawable.ic_menu_info_details)
            .build()

        if (Build.VERSION.SDK_INT >= 34) {
            startForeground(
                7,
                notification,
                android.content.pm.ServiceInfo.FOREGROUND_SERVICE_TYPE_SPECIAL_USE
            )
        } else {
            startForeground(7, notification)
        }
    }

    @Suppress("DEPRECATION")
    private fun showPanel() {
        val container = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(18, 14, 18, 14)
            setBackgroundColor(0xEE20242B.toInt())
        }

        clockView = TextView(this).apply {
            text = "Время: --:--:--"
            textSize = 16f
            setTextColor(0xFFFFFFFF.toInt())
        }
        container.addView(clockView)

        val note = EditText(this).apply {
            hint = "Личная заметка"
            minLines = 2
            setTextColor(0xFFFFFFFF.toInt())
            setHintTextColor(0xFFBBBBBB.toInt())
        }
        container.addView(note)

        container.addView(Button(this).apply {
            text = "Закрыть панель"
            setOnClickListener { stopSelf() }
        })

        val windowType = if (Build.VERSION.SDK_INT >= 26) {
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        } else {
            WindowManager.LayoutParams.TYPE_PHONE
        }

        val params = WindowManager.LayoutParams(
            WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT,
            windowType,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.END
            x = 24
            y = 120
        }

        panel = container
        windowManager.addView(container, params)
        handler.post(tick)
    }

    override fun onDestroy() {
        handler.removeCallbacks(tick)
        panel?.let {
            try { windowManager.removeView(it) } catch (_: Exception) { }
        }
        panel = null
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
