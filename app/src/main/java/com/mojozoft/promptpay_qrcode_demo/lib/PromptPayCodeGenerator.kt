package com.mojozoft.qrcodedemo.lib


/**
 * Created by Lojorider on 10/16/2017.
 * http://demonuts.com/2017/03/31/generate-qr-code-using-zxing-android-programmatically/
 * ref :  https://www.blognone.com/node/95133
 * ref : https://github.com/diewland/promptpay-qr-plus
 */

class PromptPayCodeGenerator(pp_id: String, money: String) {

    private var pp_01_version = "000201";
    private var pp_02_type = "010211";
    private var pp_03_merchant = "2937"
    private var pp_04_merchant_aid = "0016A000000677010111"
    private var pp_05_merchant_account_id = ""
    private var pp_06_country = "5802TH"
    private var pp_07_money = ""
    private var pp_08_currency = "5303764"
    private var pp_09_check_sum = "6304"

    init {
        this.pp_05_merchant_account_id = this.getPPMerchantAccountId(pp_id)
        this.pp_07_money = this.getPPMoney(money)
    }


    private fun getPPMoney(money: String): String {
        var pp_money = ""

        if (!money.isEmpty()) {
            pp_money = String.format("54%02d%s", money.length, money)
        }
        return pp_money
    }

    private fun getPPMerchantAccountId(pp_id: String): String {
        var ac_id = ""
        when {
            pp_id.length == 15 -> // truemoney e-wallet
                ac_id = "0315" + pp_id
            pp_id.length == 13 -> // id-card
                ac_id = "0213" + pp_id
            pp_id.length == 10 -> // mobile phone
                ac_id = "01130066" + pp_id.substring(1)
        }
        return ac_id
    }

    fun getCode(): String {
        val str_pp = this.pp_01_version +
                this.pp_02_type +
                this.pp_03_merchant +
                this.pp_04_merchant_aid +
                this.pp_05_merchant_account_id +
                this.pp_06_country +
                this.pp_07_money +
                this.pp_08_currency +
                this.pp_09_check_sum
        return str_pp + PromptPayCodeCheckSum().getChecksum(str_pp)
    }


}