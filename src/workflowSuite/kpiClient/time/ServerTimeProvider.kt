package workflowSuite.kpiClient.time

import java.net.Socket
import java.time.Instant

class ServerTimeProvider(private val configuration: TimeServerConfiguration) : INtpDataProdiver {
    override fun GetNtpData(): NtpData {

        val socket = Socket(configuration.endpoint.host, configuration.endpoint.port)
        val requestTransmission = Instant.now()
        val inputStream = socket.getInputStream()
        val bytes = ByteArray(16)
        inputStream.read(bytes)
        val responseReception = Instant.now()
        return NtpData(requestTransmission, fromOADate(bytes, 0),
                fromOADate(bytes, 8), responseReception)
    }
}