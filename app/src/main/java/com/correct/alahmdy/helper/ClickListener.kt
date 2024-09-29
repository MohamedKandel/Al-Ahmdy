package com.correct.alahmdy.helper

import android.os.Bundle

interface ClickListener {
    fun onItemClickListener(position: Int, extras: Bundle? = null)

    fun onItemLongClickListener(position: Int, extras: Bundle? = null)
}