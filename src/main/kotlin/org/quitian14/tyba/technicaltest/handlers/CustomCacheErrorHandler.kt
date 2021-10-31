package org.quitian14.tyba.technicaltest.handlers

import org.slf4j.LoggerFactory
import org.springframework.cache.Cache
import org.springframework.cache.interceptor.CacheErrorHandler

class CustomCacheErrorHandler : CacheErrorHandler {

    private val log = LoggerFactory.getLogger(this.javaClass)

    private val baseLog = "[CustomCacheErrorHandler]"
    private val exceptionDetails = "exception details:"

    override fun handleCacheGetError(exception: java.lang.RuntimeException, cache: Cache, key: Any) {
        log.error("$baseLog Problem getting key: $key | $exceptionDetails ${exception.message}")
    }

    override fun handleCacheClearError(exception: java.lang.RuntimeException, cache: Cache) {
        log.error("$baseLog Problem clearing redis server | $exceptionDetails ${exception.message}")
    }

    override fun handleCachePutError(exception: java.lang.RuntimeException, cache: Cache, key: Any, value: Any?) {
        log.error("$baseLog Problem putting up key: $key | $exceptionDetails ${exception.message}")
    }

    override fun handleCacheEvictError(exception: java.lang.RuntimeException, cache: Cache, key: Any) {
        log.error("$baseLog Problem evicting key: | $key $exceptionDetails ${exception.message}")
    }
}
