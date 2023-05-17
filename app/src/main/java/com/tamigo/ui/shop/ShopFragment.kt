package com.tamigo.ui.shop

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import com.tamigo.base.BaseFragment
import com.tamigo.base.BindingInflation
import com.tamigo.constant.Constants.shopList
import com.tamigo.data.FoodItem
import com.tamigo.databinding.FragmentShopBinding
import com.tamigo.interfase.UpdateCoinsListener
import com.tamigo.ui.adapter.FoodAdapter
import com.tamigo.ui.adapter.FoodAdapter.Companion.INVENTORY_VIEW_HOLDER
import com.tamigo.ui.adapter.FoodAdapter.Companion.SHOP_VIEW_HOLDER
import com.tamigo.ui.dialog.PurchaseDialog
import org.koin.androidx.viewmodel.ext.android.viewModel
import com.tamigo.viewModel.ShopViewModel

class ShopFragment : BaseFragment<FragmentShopBinding>() {
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
                    viewModel.setProductToInventory(listOf(foodItem))
                    viewModel.removeCoins(foodItem.cost)
                    updateCoinsListener?.onUpdateCoinsBalance()
                    inventory.value = viewModel.getProductsFromInventory()
                }
                AGREE_USE -> {
                    viewModel.removeProductFromInventory(foodItem)
                    inventory.value = viewModel.getProductsFromInventory()
                }
            }
        }.show(parentFragmentManager, PurchaseDialog.TAG)
    }

    companion object {
        private const val AGREE_BUY = "Are you willing to buy this product?"
        private const val AGREE_USE = "Are you willing to use this product?"
    }
}