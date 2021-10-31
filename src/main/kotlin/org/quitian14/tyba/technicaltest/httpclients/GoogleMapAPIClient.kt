package org.quitian14.tyba.technicaltest.httpclients

import com.fasterxml.jackson.databind.ObjectMapper
import org.quitian14.tyba.technicaltest.constants.RedisKeys
import org.quitian14.tyba.technicaltest.model.dtos.RestaurantDto
import org.quitian14.tyba.technicaltest.utils.Requester
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.cache.annotation.Cacheable
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Component
import org.springframework.web.util.UriComponentsBuilder
import java.lang.RuntimeException

@Component("GoogleMapAPIClient")
@Primary
@Qualifier("googleMapAPIClient")
class GoogleMapAPIClient: IGetPlaces {

    @Autowired
    lateinit var requester: Requester

    @Value("\${api.googlemaps.url}")
    lateinit var googleMapsApiURL: String

    @Value("\${api.googlemaps.radius:1500}")
    lateinit var radius: String

    @Value("\${api.googlemaps.apikey}")
    lateinit var key: String

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @Cacheable(
        value = [RedisKeys.TEST_MS_VALUE],
        key = RedisKeys.LOCATION_GOOGLE_KEY,
        cacheManager = "expire30min",
        unless = "#result == null"
    )
    override fun getPlaces(city: String?, latLon: String): List<RestaurantDto> {
        val url = UriComponentsBuilder.fromHttpUrl(googleMapsApiURL)
            .queryParam("location", latLon)
            .queryParam("type", "restauran")
            .queryParam("radius", radius)
            .queryParam("key", key)
            .toUriString()

        val response = requester.execute(
            url,
            method = HttpMethod.GET,
            responseEntity = String::class.java
        )

        val responseString = response.body

        val mapResponse =  if (!response.statusCode.is4xxClientError && !response.statusCode.is5xxServerError && !responseString!!.contains("<200")) {
            objectMapper.readValue(response.body, Map::class.java)
        } else {
            throw RuntimeException("Error calling google masp api")
        }

        if (mapResponse["error_message"] !=null) {
            throw RuntimeException(mapResponse["error_message"].toString())
        }

        val results = (mapResponse["results"] as List<Any?>).map {
            val map = it as Map<String, Any?>
            val geometry = it["geometry"] as Map<String, Any?>
            val location = geometry["location"] as Map<String, Any?>

            RestaurantDto(it["name"].toString(),it["vicinity"].toString(),
                location["lat"].toString().toFloat(),
                location["lng"].toString().toFloat())
        }

        return results
    }
}