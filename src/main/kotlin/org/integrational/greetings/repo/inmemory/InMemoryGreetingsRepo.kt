package org.integrational.greetings.repo.inmemory

import org.integrational.greetings.domain.model.Greeting
import org.integrational.greetings.domain.repo.DuplicateNameException
import org.integrational.greetings.domain.repo.GreetingsRepo
import java.util.concurrent.atomic.AtomicLong
import javax.enterprise.context.ApplicationScoped

/**
 * [GreetingsRepo] repository implementation ("driven adapter") that persists [Greeting]s in memory.
 */
@ApplicationScoped
class InMemoryGreetingsRepo : GreetingsRepo {
    companion object {
        private const val WORLD_NAME = "World"
        private val WORLD_KEY = nameKey(WORLD_NAME)

        private fun nameKey(name: String?): String = name?.toLowerCase() ?: WORLD_KEY
        private fun greeting(entity: GreetingEntity) = Greeting(entity.id, entity.name, entity.message)
    }

    private val lastId = AtomicLong() // sequence generating primary key
    private val entityById = mutableMapOf<Long, GreetingEntity>() // primary repo
    private val idByNameKey = mutableMapOf<String, Long>() // index for unique case-insensitive key 'name'

    init {
        create(Greeting(null, WORLD_NAME, "Hello $WORLD_NAME"))
    }

    override fun create(greeting: Greeting): Greeting = synchronized(this) {
        if (greeting.id != null) throw IllegalArgumentException("id of Greeting to create must be null")

        // no other entity must have that name
        findByName(greeting.name)?.let { throw DuplicateNameException(greeting.name) }

        // use new id
        return store(greeting.copy(id = lastId.incrementAndGet()))
    }

    override fun update(greeting: Greeting): Greeting = synchronized(this) {
        if (greeting.id == null) throw IllegalArgumentException("id of Greeting to update must not be null")

        // no other entity must have that name, unless it's the entity being updated
        findByName(greeting.name)?.takeIf { it.id != greeting.id }?.let { throw DuplicateNameException(greeting.name) }

        // disassociate the entity's old name from its id
        read(greeting.id)?.let { idByNameKey -= nameKey(it.name) }

        // use existing id
        return store(greeting)
    }

    private fun store(gr: Greeting): Greeting = synchronized(this) {
        if (gr.id == null) throw IllegalArgumentException("id of Greeting to store must not be null")

        val ge = GreetingEntity(gr.id, gr.name, gr.message)
        entityById[ge.id] = ge
        idByNameKey[nameKey(ge.name)] = ge.id
        return gr
    }

    override fun read(id: Long) = entityById[id]?.let { greeting(it) }

    override fun delete(id: Long) = synchronized(this) {
        read(id)?.let {
            // found
            entityById -= id
            idByNameKey -= nameKey(it.name)
            true
        } ?: false // not found
    }

    override fun findByName(name: String) = entityById[idByNameKey[nameKey(name)]]?.let { greeting(it) }

    override fun findAll() = entityById.values.map { greeting(it) }
}

private data class GreetingEntity(val id: Long, val name: String, val message: String)
