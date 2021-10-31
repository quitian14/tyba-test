package org.quitian14.tyba.technicaltest.httpclients

import com.fasterxml.jackson.databind.ObjectMapper
import org.quitian14.tyba.technicaltest.constants.RedisKeys
import org.quitian14.tyba.technicaltest.model.dtos.RestaurantDto
import org.quitian14.tyba.technicaltest.utils.Requester
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.lang.RuntimeException

@Component("HereAPIClient")
@Qualifier("hereAPIClient")
class HereAPIClient: IGetPlaces {

    @Autowired
    lateinit var requester: Requester

    @Value("\${api.here.url}")
    lateinit var hereApiURL: String

    @Value("\${api.here.apikey}")
    lateinit var key: String

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Cacheable(
        value = [RedisKeys.TEST_MS_VALUE],
        key = RedisKeys.LOCATION_HERE_KEY,
        cacheManager = "expire30min",
        unless = "#result == null"
    )
    override fun getPlaces(city: String?, latLon: String): List<RestaurantDto> {
        val url = UriComponentsBuilder.fromHttpUrl(hereApiURL)
            .queryParam("at", latLon)
            .queryParam("q", "restaurant")
            .queryParam("apiKey", key)
            .toUriString()

        val response = requester.execute(
            url,
            method = HttpMethod.GET,
            responseEntity = String::class.java
        )

        val mapResponse =  if (!response.statusCode.is4xxClientError && !response.statusCode.is5xxServerError) {
            objectMapper.readValue(response.body, Map::class.java)
        } else {
            throw RuntimeException("Error calling google masp api")
        }

        val results = (mapResponse["items"] as List<Any?>).map {
            val map = it as Map<String, Any?>
            val address = map["address"] as Map<String, Any?>
            val position = map["position"] as Map<String, Any?>

            RestaurantDto(map["title"].toString(),address["label"].toString(),
                position["lat"].toString().toFloat(),
                position["lng"].toString().toFloat())
        }

        return results
    }
}