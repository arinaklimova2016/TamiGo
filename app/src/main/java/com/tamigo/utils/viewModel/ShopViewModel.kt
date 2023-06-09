package com.tamigo.utils.viewModel

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.tamigo.utils.data.FoodItem
import com.tamigo.utils.preferences.Preferences

abstract class ShopViewModel : ViewModel() {
    abstract fun setProductToInventory(product: List<FoodItem>)
    abstract fun getProductsFromInventory(): List<FoodItem>?
    abstract fun removeProductFromInventory(product: FoodItem)
    abstract fun isBuyProduct(cost: Int): Boolean
}

class ShopViewModelImpl(
    private val preferences: Preferences
) : ShopViewModel() {

    private val gson = Gson()
    private val listType = object : TypeToken<List<FoodItem>>() {}.type
    override fun setProductToInventory(product: List<FoodItem>) {
        val currentInventory = preferences.getProducts()
        if (currentInventory.isNullOrEmpty())
            preferences.setProducts(gson.toJson(product))
        else {
            val inventoryData: MutableList<FoodItem> = gson.fromJson(currentInventory, listType)
            inventoryData.add(product.first())
            preferences.setProducts(gson.toJson(inventoryData))
        }
    }

    override fun getProductsFromInventory(): List<FoodItem>? {
        return gson.fromJson(preferences.getProducts(), listType)
    }

    override fun removeProductFromInventory(product: FoodItem) {
        val currentInventory = preferences.getProducts()
        if (!currentInventory.isNullOrEmpty()) {
            val inventoryData: MutableList<FoodItem> = gson.fromJson(currentInventory, listType)
            inventoryData.remove(product)
            preferences.setProducts(gson.toJson(inventoryData))
        }
    }

    override fun isBuyProduct(cost: Int): Boolean {
        return if (preferences.getCoinsBalance() >= cost) {
            preferences.removeCoinsFromBalance(cost)
            true
        } else
            false
    }
}