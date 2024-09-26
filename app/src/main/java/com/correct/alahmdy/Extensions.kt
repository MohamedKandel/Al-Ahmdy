package com.correct.alahmdy

import android.app.Activity
import android.graphics.Color
import android.view.View
import android.view.WindowManager
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

fun Activity.transparentStatusBar() {
    @Suppress("DEPRECATION")
    window.decorView.systemUiVisibility =
        View.SYSTEM_UI_FLAG_LAYOUT_STABLE or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
    @Suppress("DEPRECATION")
    setWindowFlag(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false, this)
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