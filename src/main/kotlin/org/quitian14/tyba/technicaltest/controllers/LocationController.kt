package org.quitian14.tyba.technicaltest.controllers

import org.quitian14.tyba.technicaltest.exceptions.BusinessException
import org.quitian14.tyba.technicaltest.model.dtos.ParamDto
import org.quitian14.tyba.technicaltest.model.dtos.RestaurantDto
import org.quitian14.tyba.technicaltest.security.Secured
import org.quitian14.tyba.technicaltest.security.UserSecurity
import org.quitian14.tyba.technicaltest.services.LocationService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RequestMapping("/locations")
@RestController
class LocationController {

    @Autowired
    lateinit var locationService: LocationService

    @Autowired
    lateinit var userSecurity: UserSecurity

    @Secured(permissions = ["get"])
    @PostMapping
    fun getLocations(@RequestBody params: ParamDto, request: HttpServletRequest): List<RestaurantDto> {
        if (params.city == null && (params.lat == null || params.lng == null)) {
            throw BusinessException("bad parameters", HttpStatus.BAD_REQUEST)
        }

        val user = userSecurity.getUser(request)

        return locationService.findPlaces(user.userName, params)
    }
}