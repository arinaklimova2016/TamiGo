package com.tamigo.navigation

sealed class Command {
    object Back : Command()
    data class Navigate(val screen: Screen) : Command()
}
