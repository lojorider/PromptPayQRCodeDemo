package com.mojozoft.promptpay_qrcode_demo

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.content.FileProvider
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import com.mojozoft.qrcodedemo.lib.PromptPayCodeGenerator
import com.mojozoft.qrcodedemo.lib.QRCodeGenerator
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private var sharedPref: SharedPreferences? = null
    var AUTHORITY_FILEPROVIDER = "com.mojozoft.promptpay_qrcode_demo.fileprovider"
    val QRcodeSize = 1000
    private val STATE_PP_ID = "STATE_PP_ID"
    private val STATE_MONEY = "STATE_MONEY"
    private val STATE_REMARK = "STATE_REMARK"
    private val IMAGE_DIRECTORY = "/MojoQRcodeDemo"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setListener()
    }

    private fun setListener() {
        pp_id.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                renderQR()
            }

        })
        money.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {
                renderQR()
            }

        })
        //https//stackoverflow.com/questions/9049143/android-share-intent-for-a-bitmap-is-it-possible-not-to-save-it-prior-sharing/30172792#30172792
        btn_share?.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View) {
                val prompt_pay_code = PromptPayCodeGenerator(pp_id.text.toString(), money.text.toString()).getCode()
                val cachePath = File(cacheDir, "images")
                val random_name = "pp-" + UUID.randomUUID() + ".png"
                var compress = false

                try {
                    cachePath.mkdirs() // don't forget to make the directory
                    val stream = FileOutputStream(cachePath.toString() + "/" + random_name) // overwrites this image every time
                    var qrCodeGenerator: QRCodeGenerator? = null
                    qrCodeGenerator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                        QRCodeGenerator(prompt_pay_code, QRcodeSize, getColor(R.color.QRcodeBackground), getColor(R.color.QRcodeforeground))
                    } else {
                        QRCodeGenerator(prompt_pay_code, QRcodeSize, resources.getColor(R.color.QRcodeBackground), resources.getColor(R.color.QRcodeforeground))
                    }

                    compress = qrCodeGenerator.getBitmap()?.compress(Bitmap.CompressFormat.PNG, 100, stream)!!
                    stream.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }

                if (compress) {
                    val newFile = File(cachePath, random_name)
                    val contentUri = FileProvider.getUriForFile(applicationContext, AUTHORITY_FILEPROVIDER, newFile)
                    if (contentUri != null) {
                        val shareIntent = Intent()
                        shareIntent.action = Intent.ACTION_SEND
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
                        shareIntent.setDataAndType(contentUri, contentResolver.getType(contentUri))
                        shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
                        startActivity(Intent.createChooser(shareIntent, "Choose an app"))
                    }
                }


            }
        })
    }

    private fun renderQR() {
        val prompt_pay_code = PromptPayCodeGenerator(pp_id.text.toString(), money.text.toString()).getCode()
        val qrCodeGenerator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            QRCodeGenerator(prompt_pay_code, QRcodeSize, getColor(R.color.QRcodeBackground), getColor(R.color.QRcodeforeground))
        } else {
            QRCodeGenerator(prompt_pay_code, QRcodeSize, resources.getColor(R.color.QRcodeBackground), resources.getColor(R.color.QRcodeforeground))
        }
        qr_code_image_view?.setImageBitmap(qrCodeGenerator.getBitmap())
    }

    private fun restorePref() {
        sharedPref = getSharedPreferences("SAVE_STATE", Context.MODE_PRIVATE)
        val prev_pp_id = sharedPref?.getString(STATE_PP_ID, "")
        val prev_money = sharedPref?.getString(STATE_MONEY, "")

        if (!prev_pp_id!!.isEmpty()) pp_id.setText(prev_pp_id)
        if (!prev_money!!.isEmpty()) money.setText(prev_money)
    }

    override fun onResume() {
        super.onResume()
        restorePref()
    }

    override fun onPause() {
        super.onPause()
        val editor = sharedPref?.edit()
        editor?.putString(STATE_PP_ID, pp_id.text.toString())
        editor?.putString(STATE_MONEY, money.text.toString())
        editor?.apply()
    }
}
