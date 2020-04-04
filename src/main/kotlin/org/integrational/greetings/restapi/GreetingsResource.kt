package org.integrational.greetings.restapi

import org.integrational.greetings.domain.service.GreetingsService
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject
import org.integrational.greetings.domain.model.Greeting as DomainGreeting

@ApplicationScoped
class GreetingsResource @Inject constructor(private val svc: GreetingsService) : GreetingsAPI {
    override fun add(toAdd: GreetingToAdd) = fromDomain(svc.add(toAdd.name))
    override fun getByName(name: String) = svc.get(name)?.let { fromDomain(it) }
    override fun getAll() = svc.getAll().map { fromDomain(it) }

    companion object {
        private fun fromDomain(dg: DomainGreeting) = Greeting(dg.name, dg.message)
    }

//    @ExceptionHandler
//    fun handleDuplicateName(ex: DuplicateNameException): ResponseEntity<ErrorResponse> =
//        ResponseEntity.badRequest().body(ErrorResponse("Duplicate name '${ex.name}'"))
}