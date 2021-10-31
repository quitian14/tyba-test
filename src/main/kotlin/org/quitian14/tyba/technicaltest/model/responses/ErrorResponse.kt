package org.quitian14.tyba.technicaltest.model.responses

data class ErrorResponse(
    val status: Int? = 500,
    val message: String? = "Exception"
)
