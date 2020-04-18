package org.integrational.greetings.gender.genderapi

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import org.eclipse.microprofile.config.ConfigProvider
import org.eclipse.microprofile.rest.client.annotation.ClientHeaderParam
import org.eclipse.microprofile.rest.client.inject.RegisterRestClient
import javax.ws.rs.Consumes
import javax.ws.rs.POST
import javax.ws.rs.Path
import javax.ws.rs.Produces
import javax.ws.rs.core.MediaType.APPLICATION_JSON

/**
 * Represents an API client's view of a sub-set of [Gender API](https://gender-api.com/en/api-docs/v2)
 * following the "MicroProfile Rest Client" specification.
 * Only the sub-set of Gender API's functionality needed for implementing
 * [org.integrational.greetings.domain.gender.GenderService] is captured here.
 */
@RegisterRestClient(baseUri = "https://gender-api.com", configKey = "genderAPI")
@Path("/v2")
@Consumes(APPLICATION_JSON)
@Produces(APPLICATION_JSON)
@ClientHeaderParam(name = "Authorization", value = ["{authHeader}"])
interface GenderAPI {

    @JvmDefault
    fun authHeader() = "Bearer ${Config.token}"

    /**
     * Represents [query-by-full-name](https://gender-api.com/en/api-docs/v2/query-by-full-name).
     */
    @POST
    @Path("/gender")
    fun queryByFullName(requ: QueryByFullName.Requ): QueryByFullName.Resp
}

/**
 * Helper to return config values for [GenderAPI] which can't be injected.
 */
private object Config {
    private fun string(n: String): String = ConfigProvider.getConfig().getValue("genderAPI.$n", String::class.java)

    val token = string("token")
}

class QueryByFullName {
    @JsonIgnoreProperties(ignoreUnknown = true) // Jackson not JSON-B
    data class Requ(
        val full_name: String? = null,
        val country: String? = null,
        val locale: String? = null,
        val ip: String? = null,
        val id: String? = null
    )

    @JsonIgnoreProperties(ignoreUnknown = true) // Jackson not JSON-B
    data class Resp(
        val input: Map<String, Any>, // treat generically because underdocumented
        val details: Details?,
        val result_found: Boolean,
        val first_name: String?, // is returned but not documented
        val full_name: String?,
        val probability: Double?,
        val gender: String
    )

    @JsonIgnoreProperties(ignoreUnknown = true) // Jackson not JSON-B
    data class Details(
        val credits_used: Int?,
        val samples: Int?,
        val country: String?,
        val first_name_sanitized: String?, // is returned but not documented
        val full_name_sanitized: String?,
        val duration: String?
    )
}