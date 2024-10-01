package com.correct.alahmdy.helper

import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.Color
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.view.animation.LinearInterpolator
import android.widget.ImageView
import android.widget.TextClock
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import java.lang.StringBuilder
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

fun Activity.transparentStatusBar() {
    @Suppress("DEPRECATION")
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    @Suppress("DEPRECATION")
    (setWindowFlag(
        WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
        false,
        this
    ))
    window.statusBarColor = Color.TRANSPARENT
}

private fun setWindowFlag(bits: Int, on: Boolean, activity: Activity) {
    val win = activity.window
    val winParams = win.attributes
    if (on) {
        winParams.flags = winParams.flags or bits
    } else {
        winParams.flags = winParams.flags and bits.inv()
    }
    win.attributes = winParams
}

fun Fragment.onBackPressed(function: () -> Unit) {
    (this.activity as AppCompatActivity).supportFragmentManager
    this.requireActivity().onBackPressedDispatcher.addCallback(
        this.requireActivity() /* lifecycle owner */,
        object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                function()
            }
        })
}

// Encrypt a string
fun String.generateKeyFromString(): SecretKey {
    val keyBytes = this.toByteArray(Charsets.UTF_8)
    // Ensure the key is 16 bytes for AES-128 (or 32 bytes for AES-256)
    val secretKeyBytes = keyBytes.copyOf(16) // If shorter, pad with zeros
    return SecretKeySpec(secretKeyBytes, "AES")
}

// Decrypt a string
fun String.decrypt(key: SecretKey, iv: ByteArray): String {
    val cipher = Cipher.getInstance("AES/CBC/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, key, IvParameterSpec(iv))
    val decodedBytes = Base64.decode(this, Base64.DEFAULT) // Android's Base64 decoding
    val decryptedBytes = cipher.doFinal(decodedBytes)
    return String(decryptedBytes, Charsets.UTF_8)
}

fun TextClock.get(type: TextClockConst = TextClockConst.AA, regex: String = ""): String {
    val arr = this.text.split(regex)
    return when (type) {
        TextClockConst.HOUR -> arr[0].trim()
        TextClockConst.MINUTE -> arr[1].trim()
        TextClockConst.AA -> return this.text.toString()
    }
}

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}

// call to get Time aa and set it to text clock
fun String.getAa(): String {
    val chars = this.toCharArray()
    val strBuilder = StringBuilder()
    for (c in chars) {
        if (!(c == '0' || c == '1' || c == '2' ||
                    c == '3' || c == '4' || c == '5' ||
                    c == '6' || c == '7' || c == '8' ||
                    c == '9')
        ) {
            strBuilder.append(c)
        }
    }
    return strBuilder.toString().replace(":","").trim()
}

// call to get Time and set it to text clock
fun String.getTime(): String {
    val chars = this.toCharArray()
    val strBuilder = StringBuilder()
    for (c in chars) {
        if (c == '0' || c == '1' || c == '2' ||
            c == '3' || c == '4' || c == '5' ||
            c == '6' || c == '7' || c == '8' ||
            c == '9' || c == ':'
        ) {
            strBuilder.append(c)
        }
    }
    return strBuilder.toString()
}

fun String.convertTimeToTimestamp(): Long? {
    val timeFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
    return try {
        // Parse the time string to a Date object
        val time = timeFormat.parse(this)
        // Return the time in milliseconds since epoch (as a timestamp)
        time?.time
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

fun Long.convertTimestampToTimeString(): String {
    // Define the time format (hh:mm:ss aa)
    val timeFormat = SimpleDateFormat("hh:mm aa", Locale.getDefault())
    // Create a Date object from the Long timestamp
    val date = Date(this)
    // Format the Date object into the time string
    return timeFormat.format(date)
}

fun String.reformat24HourTime(): String {
    // 18:40   = 6:40 PM
    val arr = this.split(":")       //[18,40]
    var aa = ""
    Log.v("Reformat 24 hour time", "${arr[0].toInt()}")
    Log.v("Reformat 24 hour time",arr[1])
    var hour = arr[0].toInt()
    if (arr[0].toInt() > 12) {
        hour = arr[0].toInt() - 12
        aa = "PM"
    } else {
        aa = "AM"
    }
    Log.d("Reformat 24 hour time", "$hour")

    return "${hour}:${arr[1]} $aa"
}