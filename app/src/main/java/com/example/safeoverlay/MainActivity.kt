package com.example.safeoverlay

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView

class MainActivity : Activity() {
    private val requestOverlay = 1001

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(28, 28, 28, 28)
        }
        layout.addView(TextView(this).apply {
            text = "Safe Game Overlay\n\nПлавающая панель с часами и заметкой. Не читает память игры, экран или данные других приложений."
            textSize = 18f
            gravity = Gravity.CENTER
        })

        layout.addView(Button(this).apply {
            text = "Включить панель"
            setOnClickListener {
                if (Settings.canDrawOverlays(this@MainActivity)) {
                    startOverlay()
                } else {
                    val intent = Intent(
                        Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                        Uri.parse("package:$packageName")
                    )
                    startActivityForResult(intent, requestOverlay)
                }
            }
        })

        layout.addView(Button(this).apply {
            text = "Остановить панель"
            setOnClickListener {
                stopService(Intent(this@MainActivity, OverlayService::class.java))
            }
        })
        setContentView(layout)
    }

    @Deprecated("Deprecated in Android API, retained for broad compatibility")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestOverlay && Settings.canDrawOverlays(this)) startOverlay()
    }

    private fun startOverlay() {
        val intent = Intent(this, OverlayService::class.java)
        if (Build.VERSION.SDK_INT >= 26) startForegroundService(intent) else startService(intent)
    }
}
