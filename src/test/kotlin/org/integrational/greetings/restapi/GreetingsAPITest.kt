package org.integrational.greetings.restapi

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType.JSON
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Test
import java.util.*

@QuarkusTest
class GreetingsAPITest {

    private fun getThen(path: String, status: Int = 200) = given().`when`().get(path).then().statusCode(status)

    @Test
    fun openAPIDefPublished() = getThen("/openapi").body(containsString("openapi"))

    @Test
    fun healthPublished() = getThen("/health").body(containsString("UP"))

    @Test
    fun metricsPublished() = getThen("/metrics").body(containsString("base_jvm_uptime_seconds"))

    @Test
    fun testGetAll() = getThen("/greetings").body(notNullValue())

    @Test
    fun testGetAll2() = getThen("/greetings/").body(notNullValue())

    @Test
    fun testGetNonExistentAddGetExistent() {
        val name = UUID.randomUUID().toString()
        val path = "/greetings/$name"
        getThen(path, 204)
        given().`when`()
            .contentType(JSON).body("""{"name":"$name"}""").post("/greetings")
            .then()
            .statusCode(201)
            .header("Location", containsString(path))
        getThen(path).body(notNullValue())
    }
}