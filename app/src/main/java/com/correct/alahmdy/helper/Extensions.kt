package com.correct.alahmdy.helper

import android.app.Activity
import android.graphics.Color
import android.util.Base64
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
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

fun View.hide() {
    this.visibility = View.GONE
}

fun View.show() {
    this.visibility = View.VISIBLE
}