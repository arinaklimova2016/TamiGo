package com.tamigo.ui.targets

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import com.tamigo.base.BaseFragment
import com.tamigo.base.BindingInflation
import com.tamigo.databinding.FragmentTargetsBinding
import com.tamigo.ui.adapter.TargetsAdapter
import com.tamigo.ui.dialog.ConfirmationDialog
import com.tamigo.viewModel.TargetsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.tamigo.constant.Constants
import com.tamigo.data.Target
import com.tamigo.interfase.UpdateCoinsListener

class TargetsFragment : BaseFragment<FragmentTargetsBinding>() {
    override val bindingInflation: BindingInflation<FragmentTargetsBinding> =
        FragmentTargetsBinding::inflate
    private val viewModel: TargetsViewModel by viewModel()

    private var target = MutableLiveData<Target>()
    private var updateCoinsListener: UpdateCoinsListener? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        target.value = viewModel.getTarget()
        with(binding) {
            btnGetPrize.isEnabled = false
            viewModel.currentSteps.observe(viewLifecycleOwner) {
                txtCurrentSteps.text = it.toString()
                progressTarget.progress = getProgress(it, target.value?.steps).toInt()
                if (it.toString() >= target.value?.steps.toString()) {
                    btnGetPrize.isEnabled = true
                }
            }
            target.observe(viewLifecycleOwner) {
                txtTargetSteps.text = it.steps.toString()
            }

            targetsList.adapter = TargetsAdapter(
                listTargets = Constants.targetsList,
                onItemClick = {
                    openConfirmationDialog {
                        viewModel.setTarget(it)
                        target.value = it
                    }
                }
            )
            btnGetPrize.setOnClickListener {
                viewModel.setCoinsToBalance(target.value?.coins)
                updateCoinsListener?.onUpdateCoinsBalance()
                removeTarget()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateCoinsListener = context as? UpdateCoinsListener
    }

    override fun onResume() {
        super.onResume()
        viewModel.getSteps()
    }

    override fun onDetach() {
        super.onDetach()
        updateCoinsListener = null
    }

    private fun openConfirmationDialog(onClickAgree: () -> Unit) {
        ConfirmationDialog {
            onClickAgree()
        }.show(parentFragmentManager, ConfirmationDialog.TAG)
    }

    private fun getProgress(currentSteps: Long, targetSteps: Int?): Double {
        return when {
            currentSteps == 0L || targetSteps == null -> {
                0.0
            }
            else -> {
                (currentSteps.toDouble() / targetSteps.toDouble()) * 100
            }
        }
    }

    private fun removeTarget() {
        binding.btnGetPrize.isEnabled = false
        binding.progressTarget.progress = 0
        binding.txtTargetSteps.text = ""
        val emptyTarget = Target(0, 0)
        target.value = emptyTarget
        viewModel.setTarget(emptyTarget)
    }
}