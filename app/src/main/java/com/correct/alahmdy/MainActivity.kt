package com.correct.alahmdy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.correct.alahmdy.databinding.ActivityMainBinding
import com.correct.alahmdy.helper.FragmentChangeListener
import com.correct.alahmdy.helper.hide
import com.correct.alahmdy.helper.show
import com.correct.alahmdy.helper.transparentStatusBar

class MainActivity : AppCompatActivity(), FragmentChangeListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var hiddenFragment: Array<Int>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)

        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host) as NavHostFragment
        navController = navHost.navController

        setContentView(binding.root)

        hiddenFragment = arrayOf(
            R.id.splashFragment,
            R.id.detectLocationFragment,
            R.id.registerFragment,
            R.id.surahFragment
        )

        binding.homeBtn.setOnClickListener {
            // navigate to home
            navController.navigate(R.id.homeFragment)
        }

        binding.qiblaBtn.setOnClickListener {
            // navigate to qibla
            navController.navigate(R.id.qiblaFragment)
        }

        binding.quranBtn.setOnClickListener {
            // navigate to quraan
            navController.navigate(R.id.quranFragment)
        }

        transparentStatusBar()
    }

    override fun onFragmentChangeListener(fragmentID: Int) {
        if (fragmentID in hiddenFragment) {
            binding.btmBar.hide()
        } else {
            when (fragmentID) {
                R.id.homeFragment -> {
                    binding.apply {
                        txtQuran.setBackgroundResource(0)
                        txtMore.setBackgroundResource(0)
                        txtQibla.setBackgroundResource(0)
                        txtHome.setBackgroundResource(R.drawable.pointer)

                        txtHome.setTextColor(resources.getColor(R.color.alpha_white,this@MainActivity.theme))
                        homeIcon.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.alpha_white), android.graphics.PorterDuff.Mode.MULTIPLY)

                        txtQuran.setTextColor(resources.getColor(R.color.white,this@MainActivity.theme))
                        quranIcon.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)

                        txtMore.setTextColor(resources.getColor(R.color.white,this@MainActivity.theme))
                        moreIcon.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)

                        txtQibla.setTextColor(resources.getColor(R.color.white,this@MainActivity.theme))
                        qiblaIcon.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
                    }
                }
                R.id.qiblaFragment -> {
                    binding.apply {
                        txtQuran.setBackgroundResource(0)
                        txtMore.setBackgroundResource(0)
                        txtHome.setBackgroundResource(0)
                        txtQibla.setBackgroundResource(R.drawable.pointer)

                        txtQibla.setTextColor(resources.getColor(R.color.alpha_white,this@MainActivity.theme))
                        qiblaIcon.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.alpha_white), android.graphics.PorterDuff.Mode.MULTIPLY)

                        txtQuran.setTextColor(resources.getColor(R.color.white,this@MainActivity.theme))
                        quranIcon.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)

                        txtMore.setTextColor(resources.getColor(R.color.white,this@MainActivity.theme))
                        moreIcon.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)

                        txtHome.setTextColor(resources.getColor(R.color.white,this@MainActivity.theme))
                        homeIcon.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
                    }
                }
                R.id.quranFragment -> {
                    binding.apply {
                        txtQibla.setBackgroundResource(0)
                        txtMore.setBackgroundResource(0)
                        txtHome.setBackgroundResource(0)
                        txtQuran.setBackgroundResource(R.drawable.pointer)

                        txtQuran.setTextColor(resources.getColor(R.color.alpha_white,this@MainActivity.theme))
                        quranIcon.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.alpha_white), android.graphics.PorterDuff.Mode.MULTIPLY)

                        txtQibla.setTextColor(resources.getColor(R.color.white,this@MainActivity.theme))
                        qiblaIcon.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)

                        txtMore.setTextColor(resources.getColor(R.color.white,this@MainActivity.theme))
                        moreIcon.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)

                        txtHome.setTextColor(resources.getColor(R.color.white,this@MainActivity.theme))
                        homeIcon.setColorFilter(ContextCompat.getColor(this@MainActivity, R.color.white), android.graphics.PorterDuff.Mode.MULTIPLY)
                    }
                }
            }
            binding.btmBar.show()
        }
    }
}