package org.quitian14.tyba.technicaltest.model.entities

import com.fasterxml.jackson.annotation.JsonFormat
import org.quitian14.tyba.technicaltest.model.dtos.ParamDto
import org.quitian14.tyba.technicaltest.model.dtos.RestaurantDto
import java.io.Serializable
import java.util.Date

data class UserTransaction  (
    val id: Long? = null,

    val userName: String,

    var params: ParamDto? = null,

    var restaurants: List<RestaurantDto>? = listOf(),

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    var createdAt: Date? = null
): Serializable
