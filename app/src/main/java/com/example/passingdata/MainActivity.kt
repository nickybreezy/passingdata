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
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private lateinit var accelerometer: Sensor
    private lateinit var platform: View
    private lateinit var player: ImageView
    private var playerX: Float = 0f
    private val handler = Handler()
    private val fallDelay = 5000L // 5 seconds
    var playerOutOfBounds = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        platform = findViewById(R.id.platform)
        player = findViewById(R.id.player)

        // Position the player on the platform
        val platformCenterX = platform.width / 2
        val playerInitialX = platformCenterX - (player.width / 2)
        player.x = playerInitialX.toFloat()
        player.y = platform.y - player.height.toFloat()
        println("start: Player's coordinates: x=${player.x}, y=${player.y}")
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
        player.y = platform.y - player.height.toFloat()
        println("Platform Y: ${platform.y}")
        println("Player Y: ${player.y}")

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
        playerX = playerInitialX.toFloat()
        player.x = playerX
        player.y = platform.y - player.height.toFloat()
    }


    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                val x = it.values[0]
                val y = it.values[1]
                val z = it.values[2]
                // Calculate roll angle
                val roll = Math.atan2(y.toDouble(), z.toDouble())

                // Rotate the ball image based on the roll angle
                player.rotation = Math.toDegrees(roll).toFloat()
                val tilt = -it.values[0]
                playerX += tilt
                player.x = playerX
                val platformLeft = platform.x
                val platformRight = platform.x + platform.width
                val playerLeft = player.x
                val playerRight = player.x + player.width
                if (playerLeft < platformLeft - player.width / 2) {
                    playerX = platformLeft - player.width / 2
                    player.x = playerX
                    player.y += z * 9.81f
                } else if (playerRight > platformRight + player.width / 2) {
                    playerX = platformRight - player.width / 2
                    player.x = playerX
                    player.y += z * 9.81f
                }
//
                val screenHeight = resources.displayMetrics.heightPixels
                if (player.y + player.height > screenHeight) {
                    // The player has fallen off the platform
                    resetPlayerPosition()
                    playerOutOfBounds = false
                }
            }
        }
    }

        private fun monitorPlayerPosition() {
            player.viewTreeObserver.addOnGlobalLayoutListener {
                val playerCenterX = player.x + player.width / 2
                val platformCenterX = platform.x + platform.width / 2

                if (playerCenterX < platformCenterX - player.width / 4 ||
                    playerCenterX > platformCenterX + platform.width / 4
                ) {
                    // The player has moved too far from the center of the platform
                    resetPlayerPosition()
                }
            }
        }

        fun putPlayerBackOnPlatform(view: View) {
            resetPlayerPosition()
            monitorPlayerPosition()
        }
    }


