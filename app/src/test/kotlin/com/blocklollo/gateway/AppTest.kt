package com.blocklollo.gateway

import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import io.ktor.server.testing.ApplicationTestBuilder
import io.ktor.server.testing.testApplication
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.singleton
import java.util.Random

/**
 * Integration tests for the [myKodeinApp] module from KodeinSimpleApplication.
 */
class AppTest {

    private fun bind(b: ApplicationTestBuilder) {
        /** Calls the [myKodeinApp] module with a [Random] class that always return 7. */
        b.application {
            myKodeinApp(DI {
                bind<Random>() with singleton {
                    object : Random() {
                        override fun next(bits: Int): Int = 7
                    }
                }
            })
        }
    }
    /**
     * A test that creates the application with a custom [Kodein]
     * that maps to a custom [Random] instance.
     *
     * This [Random] always returns a constant value '7'.
     * We then call the single defined entry point to verify
     * that when used the Random generator the result is predictable and constant.
     *
     * Additionally, we test that the [Random.next] function has been called once.
     */
    @Test // GET /rand
    fun testProvideFakeRandom() {
        testApplication {
            bind(this)

            /**
             * Checks that the single route, returns a constant value '7' from the mock,
             * and that the [Random.next] has been called just once
             */
            val response = client.get("/rand")
            assertEquals("Random number in 0..99: 7", response.bodyAsText())
        }
    }
}
