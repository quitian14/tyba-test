package org.quitian14.tyba.technicaltest.handlers

import org.quitian14.tyba.technicaltest.exceptions.BusinessException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpMethod
import org.springframework.http.client.ClientHttpResponse
import org.springframework.stereotype.Component
import org.springframework.web.client.ResponseErrorHandler
import java.io.IOException
import java.net.URI
import kotlin.jvm.Throws

@Component
class RestTemplateResponseErrorHandler : ResponseErrorHandler {

    private val logger = LoggerFactory.getLogger(this.javaClass)

    @Throws(IOException::class)
    override fun hasError(httpResponse: ClientHttpResponse): Boolean {
        return httpResponse.statusCode.is4xxClientError || httpResponse.statusCode.is5xxServerError
    }

    @Throws(IOException::class, BusinessException::class)
    override fun handleError(httpResponse: ClientHttpResponse) {
        logger.error(" Error ms CODE: ${httpResponse.rawStatusCode}  TEXT: ${httpResponse.statusText}")
    }

    @Throws(IOException::class, BusinessException::class)
    override fun handleError(url: URI, method: HttpMethod, response: ClientHttpResponse) {
        logger.error(" Error ms URL: ${url.toURL()} CODE: ${response.rawStatusCode}  TEXT: ${response.statusText}")
    }
}
