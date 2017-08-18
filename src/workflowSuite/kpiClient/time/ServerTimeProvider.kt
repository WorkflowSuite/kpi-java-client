package workflowSuite.kpiClient.time

import workflowSuite.kpiClient.bits.BitConverter
import java.io.DataInputStream
import java.net.Socket
import java.time.Instant

class ServerTimeProvider(private val configuration: TimeServerConfiguration) : INtpDataProvider {

    override fun GetNtpData(): NtpData {

        val socket = Socket(configuration.endpoint.host, configuration.endpoint.port)
        val requestTransmission = Instant.now()
        val inputStream = DataInputStream(socket.getInputStream())
        val bytes = ByteArray(16)
        inputStream.read(bytes)
        val responseReception = Instant.now()
        val requestReception = fromOADate(BitConverter.byteArrayToDoubleLE(bytes, 0))
        val responseTransmission = fromOADate(BitConverter.byteArrayToDoubleLE(bytes, 8))

        return NtpData(requestTransmission, requestReception,
                responseTransmission, responseReception)
    }
}