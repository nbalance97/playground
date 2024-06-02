package Client1

import org.junit.jupiter.api.Test

class ClientTest {

    @Test
    fun connect() {
        val client = EchoClient()

        client.start()
    }
}
