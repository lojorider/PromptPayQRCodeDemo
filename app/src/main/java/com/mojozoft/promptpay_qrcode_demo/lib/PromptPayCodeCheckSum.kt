package com.mojozoft.qrcodedemo.lib

/**
 * Created by Lojorider on 10/15/2017.
 * ref :  http://introcs.cs.princeton.edu/java/61data/CRC16CCITT.java
 * ref : http://crccalc.com/
 */
public class PromptPayCodeCheckSum {
    public fun getChecksum(input: String): String {
        var crc = 0xFFFF          // initial value
        val polynomial = 0x1021   // 0001 0000 0010 0001  (0, 5, 12)
        val bytes = input.toByteArray()
        for (b in bytes) {
            for (i in 0..7) {
                val bit = b shr 7 - i and 1 == 1
                val c15 = crc shr 15 and 1 == 1
                crc = crc shl 1
                if (c15 xor bit) crc = crc xor polynomial
            }
        }
        crc = crc and 0xffff
        return String.format("%04x", crc).toUpperCase()
    }

    infix inline fun Byte.shr(shift: Int): Int = this.toInt() shr shift
}

