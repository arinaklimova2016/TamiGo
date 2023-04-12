package com.tamigo.navigation

interface Router {
    fun attachTo(host: Any)
    fun navigate(screen: Screen)
    fun back()
    fun detach()
}
