package org.integrational.greetings.restapi

import io.quarkus.test.common.http.TestHTTPResource
import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.eclipse.microprofile.rest.client.RestClientBuilder
import org.hamcrest.CoreMatchers.containsString
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.api.Test
import java.net.URI

/**
 * Tests the REST API exposed by this app.
 */
@QuarkusTest
@Tags(Tag("integration"), Tag("restapi"))
class GreetingsAPITest {

    @TestHTTPResource("/")
    private lateinit var baseURI: URI

    private lateinit var api: GreetingsAPI

    @BeforeEach
    fun init() {
        if (!this::api.isInitialized) {
            api = RestClientBuilder.newBuilder().baseUri(baseURI).build(GreetingsAPI::class.java)
        }
    }

    @Test
    fun openAPIDefPublished() = given()
        .`when`().get("/openapi")
        .then().statusCode(200).body(containsString("openapi"))

    @Test
    fun getAll() = assert(api.getAll().isNotEmpty())
}