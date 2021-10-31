package org.quitian14.tyba.technicaltest.utils

import com.fasterxml.jackson.databind.ObjectMapper
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate

@Component
class Requester {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Autowired
    lateinit var oneSecondRestTemplate: RestTemplate

    fun <T> execute(url: String, method: HttpMethod, requestEntity: Any? = null, responseEntity: Class<T>, headers: HashMap<String, String>? = null, injectedRestTemplate: RestTemplate = oneSecondRestTemplate): ResponseEntity<T> {

        val request = HttpEntity(requestEntity, getHeaders(headers))

        val response: ResponseEntity<T> = try {
            injectedRestTemplate.exchange(url, method, request, responseEntity)
        } catch (exception: Exception) {
            logger.error("Url {},  Error message {},  with request {}  with method {}", url, exception.message, objectMapper.writeValueAsString(request), method.toString())
            ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR)
        }

        if (response.statusCode.is4xxClientError) {
            logger.error("Url {},  Error 4xx response {},  with request {}  with method {}", url, objectMapper.writeValueAsString(response.body), objectMapper.writeValueAsString(request), method.toString())
        }

        if (response.statusCode.is5xxServerError) {
            logger.error("Url {},  Error 5xx response {},  with request {}  with method {}", url, objectMapper.writeValueAsString(response.body), objectMapper.writeValueAsString(request), method.toString())
        }

        return response
    }

    fun getHeaders(headersMap: HashMap<String, String>?): HttpHeaders {
        val headers = when (headersMap) {
            null -> getDefaultHeaders()
            else -> headersMap
        }

        val requestHeaders = HttpHeaders()
        for (header in headers) {
            requestHeaders.set(header.key, header.value)
        }

        return requestHeaders
    }

    fun getDefaultHeaders(): HashMap<String, String> {
        val defaultHeaders: HashMap<String, String> = HashMap()
        defaultHeaders[HttpHeaders.CONTENT_TYPE] = MediaType.APPLICATION_JSON_VALUE

        return defaultHeaders
    }
}
