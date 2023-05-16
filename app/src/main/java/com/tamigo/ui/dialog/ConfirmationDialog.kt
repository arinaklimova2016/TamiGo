package com.tamigo.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tamigo.databinding.DialogConfirmationBinding

class ConfirmationDialog(
    private val onCLickAgree: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogConfirmationBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogConfirmationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.btnAgree.setOnClickListener {
            dialog?.dismiss()
            onCLickAgree()
        }
        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    companion object {
        val TAG: String = ConfirmationDialog::class.java.simpleName
    }
}