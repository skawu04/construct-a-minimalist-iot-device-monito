Kotlin
interface IoTDevice {
    val id: String
    val name: String
    val status: DeviceStatus
}

enum class DeviceStatus {
    ONLINE,
    OFFLINE,
    ALERT
}

data class IoTDeviceMonitor(
    val devices: List<IoTDevice>,
    val updateInterval: Long
)

interface IoTDeviceMonitorAPI {
    fun getDevices(): List<IoTDevice>
    fun getDevice(id: String): IoTDevice?
    fun updateDeviceStatus(id: String, status: DeviceStatus)
    fun startMonitoring()
    fun stopMonitoring()
}

class IoTDeviceMonitorImpl : IoTDeviceMonitorAPI {
    private val devices: MutableList<IoTDevice> = mutableListOf()
    private val thread: Thread

    init {
        thread = Thread {
            while (true) {
                // Simulate device updates
                devices.forEach { device ->
                    if (device.status == DeviceStatus.ONLINE) {
                        device.status = DeviceStatus.ALERT
                    } else {
                        device.status = DeviceStatus.ONLINE
                    }
                }
                // Simulate update interval
                Thread.sleep(5000)
            }
        }
    }

    override fun getDevices(): List<IoTDevice> = devices

    override fun getDevice(id: String): IoTDevice? = devices.find { it.id == id }

    override fun updateDeviceStatus(id: String, status: DeviceStatus) {
        devices.find { it.id == id }?.status = status
    }

    override fun startMonitoring() {
        thread.start()
    }

    override fun stopMonitoring() {
        thread.interrupt()
    }
}

object IoTDeviceMonitorFactory {
    fun create/devices(updateInterval: Long): IoTDeviceMonitorAPI {
        val devices = listOf(
            IoTDevice("device1", "Device 1", DeviceStatus.ONLINE),
            IoTDevice("device2", "Device 2", DeviceStatus.OFFLINE),
            IoTDevice("device3", "Device 3", DeviceStatus.ALERT)
        )
        return IoTDeviceMonitorImpl(devices, updateInterval)
    }
}