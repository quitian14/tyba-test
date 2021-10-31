package org.quitian14.tyba.technicaltest.utils

import org.springframework.stereotype.Component
import java.security.MessageDigest
import javax.xml.bind.DatatypeConverter

@Component
class EncryptorUtil {

    fun encrypt(value: String): String {
        val md: MessageDigest = MessageDigest.getInstance("MD5")
        md.update(value.toByteArray())
        val digest: ByteArray = md.digest()
        return DatatypeConverter
            .printHexBinary(digest).toUpperCase()
    }

}