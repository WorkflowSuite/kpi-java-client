import java.time.Instant

data class NtpData(val requestTransmission: Instant, val requestReception: Instant,
                   val responseTransmission: Instant, val responseReception: Instant)