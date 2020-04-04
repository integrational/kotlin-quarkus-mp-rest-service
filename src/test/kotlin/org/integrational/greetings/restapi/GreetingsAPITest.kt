package org.integrational.greetings.restapi

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import org.junit.jupiter.api.Test

@QuarkusTest
class GreetingsAPITest {

    @Test
    fun testGetAll() = given()
        .`when`().get("/greetings")
        .then().statusCode(200)
}