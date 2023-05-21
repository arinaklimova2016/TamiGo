package com.tamigo.ui.registration

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.tamigo.R
import com.tamigo.base.BaseFragment
import com.tamigo.base.BindingInflation
import com.tamigo.databinding.FragmentRegistrationBinding
import com.tamigo.ui.adapter.TamiListAdapter
import com.tamigo.utils.constant.Constants.tamiList
import com.tamigo.utils.viewModel.RegistrationViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationFragment : BaseFragment<FragmentRegistrationBinding>() {

    override val bindingInflation: BindingInflation<FragmentRegistrationBinding> =
        FragmentRegistrationBinding::inflate

    private val registrationViewModel: RegistrationViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(binding) {
            var selectedSkin: Int = R.drawable.tami_blue
            recyclerTamiList.layoutManager = GridLayoutManager(requireContext(), 2)
            recyclerTamiList.adapter = TamiListAdapter(listTami = tamiList) {
                selectedSkin = it
            }
            button.setOnClickListener {
                registrationViewModel.saveInfoAboutTami(etTamiName.text.toString(), selectedSkin)
                registrationViewModel.navigateToHomeFragment()
            }
        }
    }
}
