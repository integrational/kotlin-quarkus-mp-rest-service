package org.integrational.greetings.restapi

import org.integrational.greetings.domain.repo.DuplicateNameException
import org.integrational.greetings.domain.service.GreetingsService
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import javax.ws.rs.core.Response
import javax.ws.rs.core.UriInfo
import javax.ws.rs.ext.ExceptionMapper
import javax.ws.rs.ext.Provider
import org.integrational.greetings.domain.model.Greeting as DomainGreeting

@ApplicationScoped
class GreetingsResource @Inject constructor(private val svc: GreetingsService) : GreetingsAPI {
    override fun add(toAdd: GreetingToAdd, uriInfo: UriInfo) =
        svc.add(toAdd.name).let { Response.created(uriOfAdded(it, uriInfo)).build() }

    override fun getByName(name: String) = svc.get(name)?.let { fromDomain(it) }
    override fun getAll() = svc.getAll().map { fromDomain(it) }

    companion object {
        private fun uriOfAdded(dg: DomainGreeting, ui: UriInfo) = ui.absolutePathBuilder.path(dg.name).build()
        private fun fromDomain(dg: DomainGreeting) = Greeting(dg.name, dg.message)
    }
}

@Provider
class DuplicateNameExceptionMapper : ExceptionMapper<DuplicateNameException> {
    override fun toResponse(ex: DuplicateNameException): Response =
        Response.status(Response.Status.CONFLICT).entity(ErrorResponse("Duplicate name '${ex.name}'")).build()
}