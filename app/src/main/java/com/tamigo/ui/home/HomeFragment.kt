package com.tamigo.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tamigo.base.BaseFragment
import com.tamigo.base.BindingInflation
import com.tamigo.databinding.FragmentHomeBinding
import com.tamigo.viewModel.HomeViewModel
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
        with(binding) {
            txtName.text = homeViewModel.getTamiName()
            skin.setImageResource(homeViewModel.getTamiSkin())
            homeViewModel.currentSteps.observe(viewLifecycleOwner) {
                txtCurrentSteps.text = it.toString()
            }
            btnTarget.setOnClickListener {
                homeViewModel.getSteps()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        homeViewModel.recordSteps()
    }

}
