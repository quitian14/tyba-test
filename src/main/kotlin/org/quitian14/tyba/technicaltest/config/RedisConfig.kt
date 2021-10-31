package org.quitian14.tyba.technicaltest.config

import org.quitian14.tyba.technicaltest.constants.RedisTtl.DAY
import org.quitian14.tyba.technicaltest.constants.RedisTtl.HOUR
import org.quitian14.tyba.technicaltest.constants.RedisTtl.MINUTE
import org.quitian14.tyba.technicaltest.handlers.CustomCacheErrorHandler
import org.quitian14.tyba.technicaltest.utils.CacheKeyGeneratorUtil
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.CacheManager
import org.springframework.cache.annotation.CachingConfigurerSupport
import org.springframework.cache.annotation.EnableCaching
import org.springframework.cache.interceptor.CacheErrorHandler
import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.data.redis.cache.RedisCacheConfiguration
import org.springframework.data.redis.cache.RedisCacheManager
import org.springframework.data.redis.connection.RedisConnectionFactory
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.jedis.JedisClientConfiguration
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer
import org.springframework.data.redis.serializer.RedisSerializationContext
import redis.clients.jedis.JedisPoolConfig
import java.time.Duration

@Configuration
@EnableCaching
class RedisConfig : CachingConfigurerSupport() {

    @Value("\${spring.redis.host}")
    private val redisHost: String = ""

    @Value("\${spring.redis.port}")
    private val redisPort: Int = 0

    @Value("\${spring.redis.max_connections}")
    private val maxConnections: Int = 10

    @Value("\${spring.redis.min_idle}")
    private val minIdle: Int = 2

    @Value("\${spring.redis.max_idle}")
    private val maxIdle: Int = 4

    @Value("\${spring.redis.connection_time_out}")
    private val connectionTimeOut: Long = 10

    @Value("\${spring.redis.default_expire_time_seg}")
    private val defaultExpireTime: Long = 300

    @Value("\${spring.redis.cache_prefix}")
    private val redisCachePrefix: String = "test-ms:"

    @Value("\${spring.redis.max_wait_millis}")
    private val redisMaxWait: Long = 5000

    @Value("\${spring.redis.min_evitable_table_time_millis}")
    private val redisMinEvictableIdleTimeMillis: Long = 300000

    fun jedisPoolConfig(): JedisPoolConfig {

        val poolConfiguration = JedisPoolConfig()

        poolConfiguration.maxTotal = maxConnections
        poolConfiguration.minIdle = minIdle
        poolConfiguration.maxIdle = maxIdle
        poolConfiguration.maxWaitMillis = redisMaxWait
        poolConfiguration.minEvictableIdleTimeMillis = redisMinEvictableIdleTimeMillis
        poolConfiguration.blockWhenExhausted = true

        return poolConfiguration
    }

    fun redisConnectionFactory(): RedisConnectionFactory {

        val jedisClientConfiguration = JedisClientConfiguration.builder()
            .connectTimeout(Duration.ofSeconds(connectionTimeOut))
            .usePooling()
            .poolConfig(this.jedisPoolConfig())
            .build()

        return JedisConnectionFactory(RedisStandaloneConfiguration(redisHost, redisPort), jedisClientConfiguration)
    }

    fun redisCacheConfiguration(): RedisCacheConfiguration {

        val serializationPair = RedisSerializationContext.SerializationPair.fromSerializer(
            JdkSerializationRedisSerializer()
        )

        return RedisCacheConfiguration.defaultCacheConfig()
            .prefixCacheNameWith(redisCachePrefix)
            .entryTtl(Duration.ofSeconds(defaultExpireTime))
            .serializeValuesWith(serializationPair)
            .disableCachingNullValues()
    }

    @Bean
    @Primary
    override fun cacheManager(): CacheManager {
        return RedisCacheManager.builder(this.redisConnectionFactory())
            .cacheDefaults(this.redisCacheConfiguration())
            .build()
    }

    fun getCacheManagerByTtl(ttl: Long): RedisCacheManager {
        val configuration = this.redisCacheConfiguration().entryTtl(Duration.ofSeconds(ttl))

        return RedisCacheManager.builder(this.redisConnectionFactory())
            .cacheDefaults(configuration)
            .build()
    }

    @Bean("expire1day")
    fun cacheManagerOneDay(): CacheManager {
        return getCacheManagerByTtl(DAY)
    }

    @Bean("expire30min")
    fun cacheManager30Min() = getCacheManagerByTtl(30 * MINUTE)

    @Bean("expire1hour")
    fun cacheManager1Hour() = getCacheManagerByTtl(HOUR)

    @Bean("expire3hour")
    fun cacheManagerThreeHours(): CacheManager {
        return getCacheManagerByTtl(3 * HOUR)
    }

    @Bean("CacheKeyGenerator")
    override fun keyGenerator(): KeyGenerator {
        return CacheKeyGeneratorUtil()
    }

    @Bean
    override fun errorHandler(): CacheErrorHandler? {
        return CustomCacheErrorHandler()
    }
}
