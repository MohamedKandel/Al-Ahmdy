package com.correct.alahmdy

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
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
    private lateinit var hiddenFragment:Array<Int>

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
            R.id.registerFragment
        )

        transparentStatusBar()
    }

    override fun onFragmentChangeListener(fragmentID: Int) {
        if (fragmentID in hiddenFragment) {
            binding.btmBar.hide()
        } else {
            binding.btmBar.show()
        }
    }
}