package com.mojozoft.promptpay_qrcode_demo

import com.mojozoft.qrcodedemo.lib.PromptPayCodeGenerator
import com.mojozoft.qrcodedemo.lib.PromptPayCodeCheckSum
import org.junit.Assert
import org.junit.Test

/**
 * Created by Lojorider on 10/17/2017.
 * ref : https://qr-generator.digio.co.th/
 */
class PromptPayCodeGeneratorTest {
    var mobile_phone = "0888888888"
    var id_card = "2370525724102"
    @Test
    fun check_sum_correct() {
        // by https://www.blognone.com/node/95133
        Assert.assertEquals("8956", PromptPayCodeCheckSum().getChecksum("00020101021129370016A000000677010111011300660000000005802TH53037646304"))
        // by sample https://qr-generator.digio.co.th/
        Assert.assertEquals("5154", PromptPayCodeCheckSum().getChecksum("00020101021129370016A000000677010111011300668888888885802TH540310053037646304"))
        Assert.assertEquals("70A8", PromptPayCodeCheckSum().getChecksum("00020101021129370016A000000677010111011300668888888885802TH5401053037646304"))
        Assert.assertEquals("792D", PromptPayCodeCheckSum().getChecksum("00020101021129370016A000000677010111021323705257241025802TH540310053037646304"))
        Assert.assertEquals("AEDE", PromptPayCodeCheckSum().getChecksum("00020101021129370016A000000677010111021323705257241025802TH5401053037646304"))
    }

    @Test
    fun check_mobile_100_bath() {
        var money = "100"
        var expect_output = "00020101021129370016A000000677010111011300668888888885802TH5403100530376463045154"
        var actual_output = PromptPayCodeGenerator(this.mobile_phone, "100").getCode();
        Assert.assertEquals(expect_output, actual_output)
    }

    @Test
    fun check_mobile_0_bath() {
        var expect_output = "00020101021129370016A000000677010111011300668888888885802TH540105303764630470A8"
        var actual_output = PromptPayCodeGenerator(this.mobile_phone, "0").getCode();
        Assert.assertEquals(expect_output, actual_output)
    }

    @Test
    fun check_idcard_100_bath() {
        var expect_output = "00020101021129370016A000000677010111021323705257241025802TH540310053037646304792D"
        var actual_output = PromptPayCodeGenerator(this.id_card, "100").getCode();
        Assert.assertEquals(expect_output, actual_output)
    }

    @Test
    fun check_idcard_0_bath() {
        var expect_output = "00020101021129370016A000000677010111021323705257241025802TH5401053037646304AEDE"
        var actual_output = PromptPayCodeGenerator(this.id_card, "0").getCode();
        Assert.assertEquals(expect_output, actual_output)
    }

}