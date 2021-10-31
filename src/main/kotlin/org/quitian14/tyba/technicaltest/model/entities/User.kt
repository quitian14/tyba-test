package org.quitian14.tyba.technicaltest.model.entities

import com.fasterxml.jackson.annotation.JsonIgnore
import java.io.Serializable

data class User  (
    val userName: String,
    val mail: String,
    val name: String,

    @JsonIgnore
    var password: String? = null,

    var permissions: List<String>? = mutableListOf(),
): Serializable
