package com.tamigo.ui.info

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tamigo.base.BaseFragment
import com.tamigo.base.BindingInflation
import com.tamigo.databinding.FragmentInfoBinding

class InfoFragment : BaseFragment<FragmentInfoBinding>() {
    override val bindingInflation: BindingInflation<FragmentInfoBinding> =
        FragmentInfoBinding::inflate

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }
}