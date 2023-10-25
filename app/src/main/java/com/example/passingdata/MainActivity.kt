package com.example.passingdata

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.view.ViewTreeObserver
import android.widget.Toast

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var platform: View
    private lateinit var player: View
    private var playerX: Float = 0f
    private val handler = Handler()
    private val fallDelay = 5000L // 5 seconds

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        platform = findViewById(R.id.platform)
        player = findViewById(R.id.player)

        // Position the player on the platform
        val platformCenterX = platform.width / 2
        val playerInitialX = platformCenterX - (player.width / 2)
        player.x = playerInitialX.toFloat()

        // Start monitoring the player's position
        monitorPlayerPosition()
    }

    override fun onResume() {
        super.onResume()
        val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME)

        // Position the player on the platform
        val platformCenterX = platform.width / 2
        val playerInitialX = platformCenterX - (player.width / 2)
        player.x = playerInitialX.toFloat()

        // Start monitoring the player's position
        monitorPlayerPosition()
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Not needed for this example
    }

    private fun resetPlayerPosition() {
        val platformCenterX = platform.x + platform.width / 2
        val playerInitialX = platformCenterX - (player.width / 2)
        playerX = playerInitialX
        player.x = playerX
    }


    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val tilt = -it.values[0]
                playerX += tilt
                player.x = playerX

                val platformLeft = platform.x
                val platformRight = platform.x + platform.width
                val playerLeft = player.x
                val playerRight = player.x + player.width

                if (playerLeft < platformLeft) {

                    playerX = platformLeft
                    player.x = playerX
                } else if (playerRight > platformRight) {

                    playerX = platformRight - player.width
                    player.x = playerX
                }
            }
        }
    }


    private fun monitorPlayerPosition() {
        player.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val playerCenterX = player.x + player.width / 2
                val platformCenterX = platform.x + platform.width / 2

                if (playerCenterX < platformCenterX - player.width / 4 ||
                    playerCenterX > platformCenterX + platform.width / 4
                ) {
                    // The player has moved too far from the center of the platform
                    resetPlayerPosition()
                }
            }
        })
    }

    fun putPlayerBackOnPlatform(view: View) {
        resetPlayerPosition()
        monitorPlayerPosition()
    }
}
