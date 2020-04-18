package org.integrational.greetings

import io.quarkus.test.junit.QuarkusTest
import io.restassured.RestAssured.given
import io.restassured.http.ContentType.JSON
import org.hamcrest.CoreMatchers.containsString
import org.hamcrest.CoreMatchers.notNullValue
import org.junit.jupiter.api.Tag
import org.junit.jupiter.api.Tags
import org.junit.jupiter.api.Test
import java.util.*

/**
 * Tests HTTP endpoints exposed by this app which are not defined in its REST API definition.
 * These are endpoints for health, metrics, and the retrieval of the OpenAPI definition.
 */
class TODOGreetingsHTTPTODO {

    private fun getThen(path: String, status: Int = 200) = given().`when`().get(path).then().statusCode(status)
    private fun postThen(name: String, status: Int = 201) = given().`when`()
        .contentType(JSON).body("""{"name":"$name"}""").post("/greetings")
        .then().statusCode(status)

    // FIXME
    fun testGetNonExistentAddGetExistent() {
        val name = UUID.randomUUID().toString()
        val path = "/greetings/$name"
        getThen(path, 204)
        postThen(name).header("Location", containsString(path))
        getThen(path).body(notNullValue())
    }

    // FIXME
    fun testAddDuplicateAlsoCaseInsensitive() {
        val name1 = UUID.randomUUID().toString().toUpperCase()
        val name2 = name1.toLowerCase()
        val path1 = "/greetings/$name1"
        val path2 = "/greetings/$name2"
        getThen(path1, 204)
        getThen(path2, 204)
        postThen(name1).header("Location", containsString(path1))
        getThen(path1).body(notNullValue())
        getThen(path2).body(notNullValue())
        postThen(name1, 409)
        getThen(path1).body(notNullValue())
        getThen(path2).body(notNullValue())
        postThen(name2, 409)
        getThen(path1).body(notNullValue())
        getThen(path2).body(notNullValue())
    }
}