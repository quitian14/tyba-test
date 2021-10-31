package org.quitian14.tyba.technicaltest.config

import org.apache.http.HeaderElement
import org.apache.http.HeaderElementIterator
import org.apache.http.client.config.RequestConfig
import org.apache.http.conn.ConnectionKeepAliveStrategy
import org.apache.http.impl.client.CloseableHttpClient
import org.apache.http.impl.client.HttpClients
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager
import org.apache.http.message.BasicHeaderElementIterator
import org.apache.http.protocol.HTTP
import org.quitian14.tyba.technicaltest.handlers.RestTemplateResponseErrorHandler
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.http.client.ClientHttpRequestFactory
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory
import org.springframework.scheduling.TaskScheduler
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler
import org.springframework.web.client.RestTemplate

@Configuration
class RestTemplateConfiguration {


    private val requestTimeout: Int = 5000

    private val requestReadTimeout: Int = 5000

    private var workerThreads: Int = 1200

    private val httpPoolMaxConnections: Int = 1000000

    private val httpPoolKeepAliveTime: Long = 20000

    private fun getRestTemplate(connectionTimeOut: Int, readTimeout: Int, connectionRequestTimeout: Int): RestTemplate {

        val restTemplate = RestTemplate(clientHttpRequestFactory(connectionTimeOut, readTimeout, connectionRequestTimeout))
        restTemplate.errorHandler = RestTemplateResponseErrorHandler()

        return restTemplate
    }

    fun clientHttpRequestFactory(connectionTimeOut: Int, readTimeout: Int, connectionRequestTimeout: Int): ClientHttpRequestFactory {

        val factory = HttpComponentsClientHttpRequestFactory()

        factory.httpClient = getHttpClient(connectionTimeOut, readTimeout, connectionRequestTimeout)
        factory.setConnectTimeout(connectionTimeOut)
        factory.setReadTimeout(readTimeout)
        factory.setConnectionRequestTimeout(connectionRequestTimeout)

        return factory
    }

    @Bean
    fun poolingConnectionManager(): PoolingHttpClientConnectionManager {

        val poolingConnectionManager = PoolingHttpClientConnectionManager()
        poolingConnectionManager.maxTotal = httpPoolMaxConnections
        poolingConnectionManager.defaultMaxPerRoute = workerThreads

        return poolingConnectionManager
    }

    @Bean
    fun taskScheduler(): TaskScheduler? {

        val scheduler = ThreadPoolTaskScheduler()
        scheduler.setThreadNamePrefix("idleMonitor")
        scheduler.poolSize = 5

        return scheduler
    }

    fun getHttpClient(connectionTimeOut: Int, readTimeout: Int, connectionRequestTimeout: Int): CloseableHttpClient {

        val requestConfig = RequestConfig.custom().setStaleConnectionCheckEnabled(true)
            .setConnectTimeout(connectionTimeOut)
            .setSocketTimeout(readTimeout)
            .setConnectionRequestTimeout(connectionRequestTimeout)
            .build()

        val connectionKeepAliveStrategy = ConnectionKeepAliveStrategy { response, _ ->
            val it: HeaderElementIterator = BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE))

            while (it.hasNext()) {
                val he: HeaderElement = it.nextElement()
                val param: String = he.name
                val value: String = he.value

                if (param.equals("timeout", ignoreCase = true)) {
                    return@ConnectionKeepAliveStrategy value.toLong() * 1000
                }
            }
            httpPoolKeepAliveTime
        }

        return HttpClients.custom()
            .setDefaultRequestConfig(requestConfig)
            .setConnectionManager(poolingConnectionManager())
            .setKeepAliveStrategy(connectionKeepAliveStrategy)
            .build()
    }

    @Bean
    @Primary
    fun restTemplate() = getRestTemplate(requestTimeout, requestReadTimeout, requestReadTimeout)

    @Bean("twoSecondRestTemplate")
    fun oneSecondRestTemplate(): RestTemplate {

        val timeOut = 1000

        return getRestTemplate(timeOut, timeOut, timeOut)
    }

    @Bean("threeSecondRestTemplate")
    fun twoSecondRestTemplate(): RestTemplate {

        val timeOut = 2000

        return getRestTemplate(timeOut, timeOut, timeOut)
    }
}
