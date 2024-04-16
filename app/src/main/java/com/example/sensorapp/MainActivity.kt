package com.example.sensorapp
import android.os.Bundle
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.sensorapp.ui.theme.SensorappTheme
class MainActivity : ComponentActivity(), SensorEventListener {
    private lateinit var sensorManager: SensorManager
    private var accelerometer: Sensor? = null

    private var azimuth = mutableStateOf(0f)
    private var pitch = mutableStateOf(0f)
    private var roll = mutableStateOf(0f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

        setContent {
            SensorappTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OrientationDisplay(azimuth.value, pitch.value, roll.value)
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        accelerometer?.also { acc ->
            sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_UI)
        }
    }

    override fun onPause() {
        super.onPause()
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        // Handle sensor accuracy changes here
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            if (it.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                azimuth.value = it.values[0]
                pitch.value = it.values[1]
                roll.value = it.values[2]
            }
        }
    }

    @Composable
    fun OrientationDisplay(azimuth: Float, pitch: Float, roll: Float) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(text = "Azimuth: $azimuth")
            Text(text = "Pitch: $pitch")
            Text(text = "Roll: $roll")
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun DefaultPreview() {
        SensorappTheme {
            OrientationDisplay(azimuth.value, pitch.value, roll.value)
        }
    }
}
