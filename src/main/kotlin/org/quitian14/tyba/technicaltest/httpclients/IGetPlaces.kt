package org.quitian14.tyba.technicaltest.httpclients

import org.quitian14.tyba.technicaltest.model.dtos.RestaurantDto

interface IGetPlaces {
    fun getPlaces(city: String?, latLon: String): List<RestaurantDto>
}