package com.tamigo.ui.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.tamigo.databinding.DialogPurchaseBinding

class PurchaseDialog(
    private val title: String,
    private val onCLickAgree: () -> Unit
) : DialogFragment() {

    private lateinit var binding: DialogPurchaseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DialogPurchaseBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.title.text = title
        binding.btnAgree.setOnClickListener {
            dialog?.dismiss()
            onCLickAgree()
        }
        binding.btnCancel.setOnClickListener {
            dialog?.dismiss()
        }
    }

    companion object {
        val TAG: String = PurchaseDialog::class.java.simpleName
    }
}