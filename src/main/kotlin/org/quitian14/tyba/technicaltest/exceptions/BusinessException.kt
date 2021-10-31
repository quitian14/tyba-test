package org.quitian14.tyba.technicaltest.exceptions

import org.springframework.http.HttpStatus

class BusinessException(message: String?, val status: HttpStatus = HttpStatus.PRECONDITION_FAILED) :
    RuntimeException(message)
