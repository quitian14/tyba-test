package org.quitian14.tyba.technicaltest.services

import org.quitian14.tyba.technicaltest.httpclients.IGetPlaces
import org.quitian14.tyba.technicaltest.model.dtos.ParamDto
import org.quitian14.tyba.technicaltest.model.dtos.RestaurantDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class LocationService {

    @Autowired
    @Qualifier("googleMapAPIClient")
    lateinit var googleMapAPIClient: IGetPlaces

    @Autowired
    @Qualifier("hereAPIClient")
    lateinit var hereAPIClient: IGetPlaces

    @Autowired
    lateinit var userTransactionService: UserTransactionService

    fun getPlaces(paramDto: ParamDto): List<RestaurantDto> {
        return try {
            googleMapAPIClient.getPlaces(paramDto.city, "${paramDto.lat},${paramDto.lng}")
        } catch (e: Exception) {
            e.printStackTrace()
            hereAPIClient.getPlaces(paramDto.city, "${paramDto.lat},${paramDto.lng}")
        }
    }


    fun findPlaces(user: String, paramDto: ParamDto): List<RestaurantDto> {
        val places = getPlaces(paramDto)
        userTransactionService.create(user, paramDto, places)

        return places
    }
}