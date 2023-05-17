package com.tamigo.constant

import com.tamigo.R
import com.tamigo.data.FoodItem
import com.tamigo.data.Target

object Constants {
    val tamiList = listOf(
        R.drawable.tami_blue,
        R.drawable.tami_dark_blue,
        R.drawable.tami_garnet,
        R.drawable.tami_green,
        R.drawable.tami_jade,
        R.drawable.tami_lime,
        R.drawable.tami_mint,
        R.drawable.tami_pink,
        R.drawable.tami_purple,
        R.drawable.tami_red,
        R.drawable.tami_violet,
        R.drawable.tami_yellow
    )

    val targetsList = listOf(
        Target(1000, 10),
        Target(2000, 20),
        Target(5000, 50),
        Target(8000, 80),
        Target(10000, 100),
        Target(12000, 120),
        Target(15000, 150),
        Target(18000, 180),
        Target(20000, 200),
        Target(25000, 250),
        Target(50000, 500),
        Target(100000, 1000)
    )

    val shopList = listOf(
        FoodItem(R.drawable.food_strawberry, 15, 5),
        FoodItem(R.drawable.food_banana, 15, 5),
        FoodItem(R.drawable.food_tomato, 15, 5),
        FoodItem(R.drawable.food_coke, 30, 10),
        FoodItem(R.drawable.food_orange_juice, 30, 10),
        FoodItem(R.drawable.food_chicken, 60, 25),
        FoodItem(R.drawable.food_soup, 60, 25),
        FoodItem(R.drawable.food_noodles, 60, 25),
        FoodItem(R.drawable.food_burger, 100, 50),
        FoodItem(R.drawable.food_pizza, 100, 50),
        FoodItem(R.drawable.food_cake, 200, 100),
        FoodItem(R.drawable.food_cookie, 200, 100)
    )
}
