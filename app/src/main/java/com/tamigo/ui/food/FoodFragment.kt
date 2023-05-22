package com.tamigo.ui.food

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.tamigo.R
import com.tamigo.base.BaseFragment
import com.tamigo.base.BindingInflation
import com.tamigo.databinding.FragmentShopBinding
import com.tamigo.ui.adapter.FoodAdapter
import com.tamigo.ui.adapter.FoodAdapter.Companion.INVENTORY_VIEW_HOLDER
import com.tamigo.ui.adapter.FoodAdapter.Companion.SHOP_VIEW_HOLDER
import com.tamigo.ui.dialog.PurchaseDialog
import com.tamigo.utils.constant.Constants.PLUS_HEALTH_INTENT
import com.tamigo.utils.constant.Constants.shopList
import com.tamigo.utils.data.FoodItem
import com.tamigo.utils.interfase.UpdateCoinsListener
import com.tamigo.utils.receivers.HealthReceiver
import com.tamigo.utils.receivers.HealthReceiver.Companion.PLUS_HEALTH
import com.tamigo.utils.viewModel.ShopViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FoodFragment : BaseFragment<FragmentShopBinding>() {
    override val bindingInflation: BindingInflation<FragmentShopBinding> =
        FragmentShopBinding::inflate

    private val viewModel: ShopViewModel by viewModel()
    private var updateCoinsListener: UpdateCoinsListener? = null

    private val inventory = MutableLiveData<List<FoodItem>?>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val inventoryAdapter = FoodAdapter(
            mutableMapOf(Pair(INVENTORY_VIEW_HOLDER, inventory.value)),
            onItemClick = {
                openConfirmationDialog(AGREE_USE, it)
            }
        )
        val shopAdapter = FoodAdapter(
            mutableMapOf(Pair(SHOP_VIEW_HOLDER, shopList)),
            onItemClick = {
                openConfirmationDialog(AGREE_BUY, it)
            }
        )
        inventory.observe(viewLifecycleOwner) {
            inventoryAdapter.updateAdapter(it)
        }
        with(binding) {
            shop.layoutManager = GridLayoutManager(requireContext(), 3)
            inventory.adapter = inventoryAdapter
            shop.adapter = shopAdapter
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        updateCoinsListener = context as? UpdateCoinsListener
    }

    override fun onResume() {
        super.onResume()
        inventory.value = viewModel.getProductsFromInventory()
    }

    override fun onDetach() {
        super.onDetach()
        updateCoinsListener = null
    }

    private fun openConfirmationDialog(title: String, foodItem: FoodItem) {
        PurchaseDialog(
            title
        ) {
            when (title) {
                AGREE_BUY -> {
                    if (viewModel.isBuyProduct(foodItem.cost)) {
                        updateCoinsListener?.onUpdateCoinsBalance()
                        viewModel.setProductToInventory(listOf(foodItem))
                        inventory.postValue(viewModel.getProductsFromInventory())
                    } else {
                        Toast.makeText(
                            requireContext(),
                            R.string.not_enough_money,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
                AGREE_USE -> {
                    lifecycleScope.launch(Dispatchers.IO) {
                        viewModel.removeProductFromInventory(foodItem)
                        inventory.postValue(viewModel.getProductsFromInventory())
                        val intent = Intent(requireContext(), HealthReceiver::class.java)
                        intent.action = PLUS_HEALTH_INTENT
                        intent.putExtra(PLUS_HEALTH, foodItem.healthRecovery)
                        requireContext().sendBroadcast(intent)
                    }
                }
            }
        }.show(parentFragmentManager, PurchaseDialog.TAG)
    }

    companion object {
        private const val AGREE_BUY = "Чи бажаєте ви купити цей продукт?"
        private const val AGREE_USE = "Чи бажаєте ви використати цей продукт?"
    }
}
