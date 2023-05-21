package com.tamigo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tamigo.base.BaseFragment
import com.tamigo.base.BindingInflation
import com.tamigo.databinding.FragmentHomeBinding
import com.tamigo.utils.receivers.HealthReceiver
import com.tamigo.utils.viewModel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val bindingInflation: BindingInflation<FragmentHomeBinding> =
        FragmentHomeBinding::inflate
    private val homeViewModel: HomeViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (homeViewModel.isNeedStartAlarm()) {
            HealthReceiver.setAlarm(requireContext())
        }
        homeViewModel.registerPrefsListener()
        homeViewModel.openTargetsFragment()
        with(binding) {
            txtName.text = homeViewModel.getTamiName()
            skin.setImageResource(homeViewModel.getTamiSkin())
            btnTarget.setOnClickListener {
                homeViewModel.openTargetsFragment()
            }
            btnShop.setOnClickListener {
                homeViewModel.openShopFragment()
            }
            coinBalance.text = homeViewModel.getCoins()
            progressHealth.progress = homeViewModel.getHealthFromPref().toInt()
            homeViewModel.health.observe(viewLifecycleOwner) {
                progressHealth.progress = it.toInt()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        homeViewModel.unregisterPrefsListener()
    }

    fun updateCoinsBalance() {
        binding.coinBalance.text = homeViewModel.getCoins()
    }
}
