package org.integrational.greetings.domain.gender

/**
 * Interface to an invoked (outbound) service ("driven port") for determining the gender of people.
 */
interface GenderService {
    fun genderOfName(name: String): Gender
}

enum class Gender {
    FEMALE, MALE, UNSPECIFIED
}
