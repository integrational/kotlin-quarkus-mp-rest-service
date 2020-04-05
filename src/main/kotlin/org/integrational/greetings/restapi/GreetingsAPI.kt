package org.integrational.greetings.restapi

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.info.Contact
import org.eclipse.microprofile.openapi.annotations.info.Info
import org.eclipse.microprofile.openapi.annotations.media.Schema
import javax.ws.rs.*
import javax.ws.rs.core.Application
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@OpenAPIDefinition(
    info = Info(
        title = "Greetings API",
        description = "Manage Greetings to people identified by their unique name.",
        version = "0.0.1",
        contact = Contact(name = "Gerald Loeffler", url = "http://integrational.eu")
    )
)
class API : Application()

@Path("/greetings")
@Produces(APPLICATION_JSON)
interface GreetingsAPI {
    @Operation(
        summary = "Add a Greeting",
        description = "Add a Greeting from the representation in the request body, where the given name must be unique, returning the complete added Greeting."
    )
    @POST
    @Consumes(APPLICATION_JSON)
    fun add(toAdd: GreetingToAdd): Greeting

    @Operation(
        summary = "Get a Greeting by name",
        description = "Get the Greeting with the given name, if any, where the name is case-insensitive."
    )
    @GET
    @Path("/{name}")
    fun getByName(@PathParam("name") name: String): Greeting?

    @Operation(
        summary = "Get all Greetings",
        description = "Get all Greetings, in no particular order."
    )
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

data class ErrorResponse(val message: String)