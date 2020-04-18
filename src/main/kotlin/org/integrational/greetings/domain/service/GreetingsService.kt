package org.integrational.greetings.domain.service

import org.integrational.greetings.domain.model.Greeting

/**
 * Interface of the main exposed (inbound) domain service ("driving port") provided by this app.
 */
interface GreetingsService {
    fun add(name: String): Greeting
    fun get(name: String): Greeting?
    fun getAll(): Collection<Greeting>
}
