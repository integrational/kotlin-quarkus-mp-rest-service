package org.integrational.greetings

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.api.Test

/**
 * Tests HTTP monitoring endpoints exposed by this app for health and metrics.
 */
@QuarkusTest
@Tags(Tag("integration"), Tag("monitoring"))
class MonitoringEndpointsTest {
    private fun getThen(path: String, status: Int = 200) = given().`when`().get(path).then().statusCode(status)

    @Test
    fun healthPublished() = getThen("/health").body(containsString("UP"))

    @Test
    fun metricsPublished() = getThen("/metrics").body(containsString("base_jvm_uptime_seconds"))
}