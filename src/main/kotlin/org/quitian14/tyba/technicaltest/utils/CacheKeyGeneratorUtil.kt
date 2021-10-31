package org.quitian14.tyba.technicaltest.utils

import org.springframework.cache.interceptor.KeyGenerator
import org.springframework.util.StringUtils
import java.lang.reflect.Method

class CacheKeyGeneratorUtil : KeyGenerator {
    override fun generate(target: Any, method: Method, vararg params: Any?): Any {
        return target.javaClass.simpleName + ":" + method.name + "(" + StringUtils.arrayToDelimitedString(params, ",") + ")"
    }
}
