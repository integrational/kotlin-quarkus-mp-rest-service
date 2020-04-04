package org.integrational.greetings.restapi

import org.integrational.greetings.domain.model.Greeting
import javax.ws.rs.*
import javax.ws.rs.core.MediaType.APPLICATION_JSON

@Path("/greetings")
@Produces(APPLICATION_JSON)
interface GreetingsAPI {
    @POST
    @Consumes(APPLICATION_JSON)
    fun add(toAdd: GreetingToAdd): Greeting

    @GET
    @Path("/{name}")
    fun getByName(@PathParam("name") name: String): Greeting?

    @GET
    fun getAll(): Collection<Greeting>
}

data class GreetingToAdd(val name: String)

data class ErrorResponse(val message: String)