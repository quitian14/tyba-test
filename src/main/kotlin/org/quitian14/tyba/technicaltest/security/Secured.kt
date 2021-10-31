package org.quitian14.tyba.technicaltest.security

@Target(AnnotationTarget.CLASS, AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@MustBeDocumented
annotation class Secured(
    val permissions: Array<String> = arrayOf(),
)
