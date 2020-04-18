package org.integrational.greetings.gender.genderapi

import org.eclipse.microprofile.rest.client.inject.RestClient
import org.integrational.greetings.domain.gender.Gender
import org.integrational.greetings.domain.gender.Gender.*
import org.integrational.greetings.domain.gender.GenderService
import java.util.logging.Logger
import javax.enterprise.context.ApplicationScoped
import javax.inject.Inject

/**
 * Invoked (outbound) [GenderService] service implementation ("driven adapter") that calls [GenderAPI].
 */
@ApplicationScoped
class GenderAPIGenderServiceImpl @Inject constructor(@RestClient private val genderAPI: GenderAPI) : GenderService {
    companion object {
        private val LOG = Logger.getLogger(GenderAPIGenderServiceImpl::class.java.canonicalName)
        private const val MIN_PROBABILITY = 0.66
    }

    override fun genderOfName(name: String): Gender {
        val apiResp = genderAPI.queryByFullName(QueryByFullName.Requ(full_name = name))
            .also { LOG.info("Received Gender API response $it") }
        return with(apiResp) {
            if (!result_found) return UNSPECIFIED
            if (probability != null && probability < MIN_PROBABILITY) UNSPECIFIED
            else when (gender.toLowerCase()) {
                "female" -> FEMALE
                "male" -> MALE
                else -> UNSPECIFIED
            }
        }
    }
}