package org.integrational.greetings.domain.service

import org.integrational.greetings.domain.model.Greeting
import org.integrational.greetings.domain.repo.GreetingsRepo
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

@ApplicationScoped
class GreetingsServiceImpl @Inject constructor(private val repo: GreetingsRepo) : GreetingsService {
    override fun add(name: String) = repo.createOrUpdate(Greeting(null, name, "Hello $name"))
    override fun get(name: String) = repo.findByName(name)
    override fun getAll() = repo.findAll()
}