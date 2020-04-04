package org.integrational.greetings.domain.service

import org.integrational.greetings.domain.model.Greeting

interface GreetingsService {
    fun add(name: String): Greeting
    fun get(name: String): Greeting?
    fun getAll(): Collection<Greeting>
}
