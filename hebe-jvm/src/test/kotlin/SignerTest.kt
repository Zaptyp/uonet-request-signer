package io.github.wulkanowy.signer.hebe

import org.junit.Assert.assertEquals
import org.junit.Test
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class SignerTest {

    private val fullUrl = "/powiatwulkanowy/123456/api/mobile/register/hebe?lastSyncDate=null"
    private val keyId = "7eba57e1ddba1c249d097a9ff1c9ccdd45351a6a"
    private val privatePem = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDCbF5Tt176EpB4cX5U+PZE0XytjJ9ABDZFaBFDkaexbkuNeuLOaARjQEOlUoBmpZQXxAF8HlYqeTvPiTcnSfQIS6EdqpICuQNdwvy6CHFAe2imkbbB0aHPsGep6zH8ZxHbssazkTCnGy0j2ZtGT2/iy1GEvc/p2bOkCVcR1H1GqFp+/XpfaMwi2SRCwc67K8Fu8TjSDKNvcRT9nuenGoPA1CWoOiOCxhQA6gnB8LULPel6TVDxeBVdYor/z2GxFe/m0pa7XAKzveuUDhH8k8NlNG65MjvZhgy9iFs+eBUq7lCZ0nuIsDzjnUrLSl4ciYKj9d94qrUyF8L8D9Rl+0WlAgMBAAECggEAQ6jg3rNmyxIg0rl0ZG/LjEF26RKR7P5KQLcpouESga3HfzHvsjMCq+OWZvciFhazRd4BQkdwZxGPnfa7ieGzmhtvs1pDu8zU/hE4UClV+EG6NpVpC2Q/sn5KZRijaZoY3eMGQUFatBzCBcLZxYspfbyR3ucLbu9DE+foNB1Fh4u9RCDj3bClTsqPcNBIaLMpYr3f/bM1fFbS9LrJ7AXZQtGg/2MH58WsvV67SiYAQqGCzld/Jp74gmod4Ii0w2XWZ7OeixdF2xr1j7TK0dUUlrrOrb1cgOWSOEXyy3RX/iF7R8uuLXiRfo1URh6VNPoOtrC6fHCrCp1iRBo08qOk4QKBgQDxqLrWA7gKcTr2yQeGfETXOAYi0xqbNj5A9eVC0XngxnFuwWc5zyg3Ps3c0UK2qTSSFv4SoeEHQM+U0+9LjYzIRSUH7zy4zBrBlLtTQCysSuuZ9QfgO55b3/QEYkyx6Hz/z/gg53jKHjsUKIftGMwJ6C1M2svbBNYCsWrUuYcsbQKBgQDN9gkVDABIeWUtKDHyB5HGcVbsg7Ji7GhMjdFA0GB+9kR0doKNctrzxKn65BI2uTWg+mxaw5V+UeJOIaeFsv2uClYJYn1F55VT7NIx3CLFv6zFRSiMSKz2W+NkwGjQqR7D3DeEyalpjeQeMdpHZg27LMbdVkzy/cK8EM9ZQlRLGQKBgQCpB2wn5dIE+85Sb6pj1ugP4Y/pK9+gUQCaT2RcqEingCY3Ye/h75QhkDxOB9CyEwhCZvKv9aqAeES5xMPMBOZD7plIQ34lhB3y6SVdxbV5ja3dshYgMZNCkBMOPfOHPSaxh7X2zfEe7qZEI1Vv8bhF9bA54ZBVUbyfhZlD0cFKwQKBgQC9BnXHb0BDQ8br7twH+ZJ8wkC4yRXLXJVMzUujZJtrarHhAXNIRoVU/MXUkcV1m/3wRGV119M4IAbHFnQdbO0N8kaMTmwS4DxYzh0LzbHMM+JpGtPgDENRx3unWD/aYZzuvQnnQP3O9n7Kh46BwNQRWUMamL3+tY8n83WZwhqC4QKBgBTUzHy0sEEZ3hYgwU2ygbzC0vPladw2KqtKy+0LdHtx5pqE4/pvhVMpRRTNBDiAvb5lZmMB/B3CzoiMQOwczuus8Xsx7bEci28DzQ+g2zt0/bC2Xl+992Ass5PP5NtOrP/9QiTNgoFSCrVnZnNzQqpjCrFsjfOD2fiuFLCD6zi6"
    private val body = "{}"

    @Test
    fun `values should match`() {
        val headers = getSignatureHeaders(keyId, privatePem, body, fullUrl, ZonedDateTime.parse("2021-02-18T16:36:53Z"))
        val signature = "keyId=\"7eba57e1ddba1c249d097a9ff1c9ccdd45351a6a," +
                "headers=\"vCanonicalUrl Digest vDate\"," +
                "algorithm=\"sha256\"," +
                "signature=Base64(sha256withrsa(JLdhYWv05+f7KTstWsCgt0rU2QQpA+jZM6VaVKFYe0Q4hrKKVq5/ZPB3ttBJ0RwD+MGX2mePkYm3BiLdCOqoZfAyylpHCnnQ4lbFOX45sMogQQoFbhmIQF1ZwVxRKrn/lbrd+VsLsInXWn74CMNIOz55p/WrwV3J+w5g1FpYSRM/LVzQMXvcRRXx6WSmfo/qd1H6TH33EVU+fPTw3lhGpAPDl+clZyUDyfWrmxaRvJ/ag2LNdtGVNXDQfyMO7diOJQtqUnWsJoMh5iNFApd9nHgB1Fkmb62aDwGDGtDz65IA7ArdjNW56IXeogQyp+Vv/icap3/ujdQl5zjXssL4Sg==))"

        assertEquals("SHA-256=RBNvo1WzZ4oRRq0W9+hknpT7T8If536DEMBg9hyq/4o=", headers["Digest"])
        assertEquals("api%2fmobile%2fregister%2fhebe%3flastsyncdate%3dnull", headers["vCanonicalUrl"])
        assertEquals("Thu, 18 Feb 2021 16:36:53 GMT", headers["vDate"])
        assertEquals(signature, headers["Signature"])
    }

    @Test
    fun `values should match when no body`() {
        val headers = getSignatureHeaders(keyId, privatePem, null, fullUrl, ZonedDateTime.parse("2021-02-18T16:36:53Z"))
        val signature = "keyId=\"7eba57e1ddba1c249d097a9ff1c9ccdd45351a6a," +
                "headers=\"vCanonicalUrl vDate\"," +
                "algorithm=\"sha256\"," +
                "signature=Base64(sha256withrsa(HF8AbXOjH8CBu+xfviwSXz46GwuRDQL47Bb6rIumdEZ0l6sUF7oMakhFVPBc3G43sNSSIZsxTgdBu0GB+66OHj1ce6/E5WTUi01/chs22mqTFfGbkqQ2eebmmSciW1qGiYy/0IfwidTVJXJa4EYWjsM3538WMvaeoOuqAKOwrOGgQG0qmuR8ZUKIit3R0BbQTTBlV5jX2MVTmTTo2Knx5q9Hz5PTPdyzuKYGD5mXRhx42WK6EzSzxzbdY8+s3esCCEEAeXQS+Bps0nF+h8xFXwc+QHgPjlGVX2HE/EEw+Kp6AW3ub8QjvbvO+mkNU/JaFQBOvRvCy0heMjOTt66QxA==))"

        assertEquals(null, headers["Digest"])
        assertEquals("api%2fmobile%2fregister%2fhebe%3flastsyncdate%3dnull", headers["vCanonicalUrl"])
        assertEquals("Thu, 18 Feb 2021 16:36:53 GMT", headers["vDate"])
        assertEquals(signature, headers["Signature"])
    }
}
