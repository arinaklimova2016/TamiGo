package com.tamigo.ui.home

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tamigo.base.BaseFragment
import com.tamigo.base.BindingInflation
import com.tamigo.databinding.FragmentHomeBinding
import com.tamigo.interfase.UpdateServiceListener
import com.tamigo.service.HealthService
import com.tamigo.viewModel.HomeViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : BaseFragment<FragmentHomeBinding>() {

    override val bindingInflation: BindingInflation<FragmentHomeBinding> =
        FragmentHomeBinding::inflate
    private val homeViewModel: HomeViewModel by viewModel()
    private var updateServiceListener: UpdateServiceListener? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        if (homeViewModel.isNeedStartService()) {
            val intent = Intent(requireContext(), HealthService::class.java)
            requireContext().startService(intent)
//        }
        homeViewModel.currentHealth.observe(viewLifecycleOwner) {
            binding.progressHealth.progress = it.toInt()
        }

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
            progressHealth.progress = homeViewModel.getHealth()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateServiceListener = context as? UpdateServiceListener
    }

    override fun onDetach() {
        super.onDetach()
        updateServiceListener = null
    }

    fun updateCoinsBalance() {
        binding.coinBalance.text = homeViewModel.getCoins()
    }

    fun updateHealthProgress(value: Int) {
        updateServiceListener?.updateCurrentHealthInService(value.toDouble())
    }

}
