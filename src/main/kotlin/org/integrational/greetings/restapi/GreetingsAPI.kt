package org.integrational.greetings.restapi

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition
import org.eclipse.microprofile.openapi.annotations.Operation
import org.eclipse.microprofile.openapi.annotations.info.Contact
import org.eclipse.microprofile.openapi.annotations.info.Info
import javax.ws.rs.*
import javax.ws.rs.core.Application
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@OpenAPIDefinition(
    info = Info(
        title = "Greetings API",
        version = "0.0.1",
        contact = Contact(name = "Gerald Loeffler", url = "http://integrational.eu")
    )
)
class API : Application()

@Path("/greetings")
@Produces(APPLICATION_JSON)
interface GreetingsAPI {
    @Operation(
        summary = "Add Greeting",
        description = "Add a Greeting from the representation in the request body, where the given name must be unique."
    )
    @POST
    @Consumes(APPLICATION_JSON)
    fun add(toAdd: GreetingToAdd): Greeting

    @Operation(
        summary = "Get Greeting by name",
        description = "Get the Greeting with the given name, or none."
    )
    @GET
    @Path("/{name}")
    fun getByName(@PathParam("name") name: String): Greeting?

    @Operation(
        summary = "Get all Greetings",
        description = "Get all Greetings."
    )
    @GET
    fun getAll(): Collection<Greeting>
}

sealed class GreetingCore {
    abstract val name: String
}

data class GreetingToAdd(override val name: String) : GreetingCore()
data class Greeting(override val name: String, val message: String) : GreetingCore()

data class ErrorResponse(val message: String)