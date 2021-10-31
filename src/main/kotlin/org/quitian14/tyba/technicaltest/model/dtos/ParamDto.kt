package org.quitian14.tyba.technicaltest.model.dtos

import java.io.Serializable

data class ParamDto  (
    val city: String? = null,

    val lat: Float? = null,

    val lng: Float? = null
): Serializable
