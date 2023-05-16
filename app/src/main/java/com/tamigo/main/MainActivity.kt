package com.tamigo.main

import android.os.Build
import android.os.Bundle
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import com.tamigo.R
import com.tamigo.base.BaseActivity
import com.tamigo.base.BindingInflation
import com.tamigo.databinding.ActivityMainBinding
import com.tamigo.interfase.UpdateCoinsListener
import com.tamigo.managers.HealthConnectManager
import com.tamigo.navigation.Router
import com.tamigo.ui.home.HomeFragment
import com.tamigo.ui.home.HomeScreen
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : BaseActivity<ActivityMainBinding>(), UpdateCoinsListener {
    override val bindingInflation: BindingInflation<ActivityMainBinding> =
        ActivityMainBinding::inflate
    private val router by inject<Router>()
    private val mainViewModel: MainViewModel by viewModel()
    private val healthConnectManager by inject<HealthConnectManager>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        router.attachTo(this)

        healthConnectManager.attach(this)
        mainViewModel.checkHealthConnectStatusAndPermissions()

        binding.navigation.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    //TODO:Logic of navigation
                }
                R.id.settings -> {
                    //TODO:Logic of navigation
                }
                R.id.info -> {
                    //TODO:Logic of navigation
                }
                R.id.restart -> {
                    //TODO:Logic of navigation
                }
            }
            binding.drawerLayout.closeDrawer(GravityCompat.START)
            return@setNavigationItemSelectedListener true
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            installSplashScreen().setKeepOnScreenCondition {
                false
            }

        mainViewModel.navigate()
    }

    override fun onUpdateCoinsBalance() {
        val homeFragment = supportFragmentManager
            .findFragmentByTag(HomeScreen().tag) as HomeFragment
        homeFragment.updateCoinsBalance()
    }
}
