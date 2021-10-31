package org.quitian14.tyba.technicaltest.model.request

import javax.validation.constraints.Email
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

data class CreateUserRequest(
    @Pattern(regexp = "[a-zA-Z0-9_-]*")
    @Size(min = 6)
    val userName: String,

    @Pattern(regexp = "(?=.*[a-z])(?=.*[A-Z])(?=.*[0-9])(?=.*[^A-Za-z0-9])")
    @Size(min = 8)
    val password: String,

    @Email
    val mail: String,

    @Pattern(regexp = "[a-zA-Z ]*")
    val name: String,
)
