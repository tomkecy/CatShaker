package cybulski.tomasz.funnycats

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager


class ShakeHandler : SensorEventListener {

    private var sManager: SensorManager? = null
    private var accSensor: Sensor? = null

    private val gravity = FloatArray(3)

    private var counter: Int = 0
    private var firstMoveTime: Long = 0
    private var shakeListener: ShakeListener? = null

    fun setListener(listener: ShakeListener) {
        this.shakeListener = listener
    }

    fun initialize(context: Context) {
        sManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        accSensor = sManager!!.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        registerListener()
    }

    fun registerListener() {
        sManager!!.registerListener(this, accSensor, SensorManager.SENSOR_DELAY_NORMAL)
    }

    fun unregisterListener() {
        sManager!!.unregisterListener(this)
    }

    override fun onSensorChanged(sensorEvent: SensorEvent) {
        val maxAcc = calcMaxAcceleration(sensorEvent)
        if (maxAcc >= MOV_THRESHOLD) {
            if (counter == 0) {
                counter++
                firstMoveTime = System.currentTimeMillis()
            } else {
                val now = System.currentTimeMillis()
                if (now - firstMoveTime < SHAKE_WINDOW_TIME_INTERVAL)
                    counter++
                else {
                    counter = 0
                    return
                }

                if (counter >= MOV_COUNTS)
                    if (shakeListener != null)
                        shakeListener!!.onShake()
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, i: Int) {}

    private fun calcMaxAcceleration(event: SensorEvent): Float {
        gravity[0] = calcGravityForce(event.values[0], 0)
        gravity[1] = calcGravityForce(event.values[1], 1)
        gravity[2] = calcGravityForce(event.values[2], 2)

        val accX = event.values[0] - gravity[0]
        val accY = event.values[1] - gravity[1]
        val accZ = event.values[2] - gravity[2]

        val max1 = Math.max(accX, accY)
        return Math.max(max1, accZ)
    }

    // Low pass filter
    private fun calcGravityForce(currentVal: Float, index: Int): Float {
        return ALPHA * gravity[index] + (1 - ALPHA) * currentVal
    }

    interface ShakeListener {
        fun onShake()
    }

    companion object {
        private val MOV_COUNTS = 4
        private val MOV_THRESHOLD = 4
        private val ALPHA = 0.8f
        private val SHAKE_WINDOW_TIME_INTERVAL = 2000 // milliseconds
    }
}