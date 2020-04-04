package org.integrational.greetings.restapi

import org.integrational.greetings.domain.service.GreetingsService
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class GreetingsResource(@Inject private val svc: GreetingsService) : GreetingsAPI {
    override fun add(toAdd: GreetingToAdd) = svc.add(toAdd.name)
    override fun getByName(name: String) = svc.get(name)
    override fun getAll() = svc.getAll()

//    @ExceptionHandler
//    fun handleDuplicateName(ex: DuplicateNameException): ResponseEntity<ErrorResponse> =
//        ResponseEntity.badRequest().body(ErrorResponse("Duplicate name '${ex.name}'"))
}