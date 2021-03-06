package org.integrational.greetings.restapi

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.headers.Header
import org.eclipse.microprofile.openapi.annotations.info.Contact
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.media.Content
import org.eclipse.microprofile.openapi.annotations.media.Schema
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses
import java.net.URI
import javax.ws.rs.*
import javax.ws.rs.core.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON
import kotlin.reflect.jvm.javaMethod

@OpenAPIDefinition(
    info = Info(
        title = "Greetings API",
        description = "Manage Greetings to people identified by their unique name",
        version = "0.0.1",
        contact = Contact(name = "Gerald Loeffler", url = "http://integrational.eu")
    )
)
class API : Application()

/**
 * Exposed (inbound) REST API of this app.
 */
@Path("/greetings")
@Produces(APPLICATION_JSON)
interface GreetingsAPI {
    @Operation(
        summary = "Add a Greeting",
        description = "Add a Greeting from the representation in the request body, where the given name must be unique"
    )
    @APIResponses(
        APIResponse(
            responseCode = "201", description = "Greeting was added",
            headers = [Header(
                name = "Location", description = "Absolute URI of the added Greeting",
                schema = Schema(implementation = URI::class)
            )]
        ),
        APIResponse(
            responseCode = "409", description = "Greeting was not added because name was not unique",
            content = [Content(schema = Schema(implementation = ErrorResponse::class))]
        )
    )
    @POST
    @Consumes(APPLICATION_JSON)
    fun add(toAdd: GreetingToAdd, @Context uriInfo: UriInfo): Response

    @Operation(
        summary = "Get a Greeting by name",
        description = "Get the Greeting with the given name, if any, where the name is case-insensitive"
    )
    @APIResponses(
        APIResponse(
            responseCode = "200", description = "OK",
            content = [Content(schema = Schema(implementation = Greeting::class))]
        ),
        APIResponse(responseCode = "204", description = "There is no Greeting with the given name")
    )
    @GET
    @Path("/{name}")
    fun getByName(@PathParam("name") name: String): Greeting?

    /**
     * Return the URI of the Greeting with the given name, assuming such a Greeting exists.
     * This is the URI [getByName] is mapped to.
     */
    fun uriForName(name: String): URI =
        UriBuilder.fromResource(GreetingsAPI::class.java).path(::getByName.javaMethod).build(name)

    @Operation(summary = "Get all Greetings", description = "Get all Greetings, in no particular order")
    @GET
    fun getAll(): Collection<Greeting>
}

sealed class GreetingCore {
    abstract val name: String
}

@Schema(description = "Representation of a new Greeting to add")
data class GreetingToAdd(
    @get:Schema(description = "Name of the person to greet", example = "Isaac Newton")
    override val name: String
) : GreetingCore()

@Schema(description = "Representation of a complete Greeting")
data class Greeting(
    @get:Schema(description = "Name of the person to greet", example = "Isaac Newton")
    override val name: String,
    @get:Schema(description = "Message to greet the person with", example = "Hello Isaac Newton!")
    val message: String
) : GreetingCore()

@Schema(description = "Response when an error has occurred")
data class ErrorResponse(
    @get:Schema(description = "Explanation of the error", example = "Duplicate name 'Isaac Newton'")
    val message: String
)