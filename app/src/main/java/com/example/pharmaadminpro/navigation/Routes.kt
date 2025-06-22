package com.example.pharmaadminpro.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.serialization.Serializable

sealed class Routes(val route: String) {

    data object AdminLoginScreen : Routes("admin_login")

    data object AdminPanelScreen : Routes("admin_panel")

    data object UsersListScreen : Routes("users_list")

    data object UserDetailsScreen : Routes("user_details")

    data object ProductListScreen : Routes("product_list")

    data object ProductDetailsScreen : Routes("Product_details")

    data object AddProductScreen : Routes("add_product")

    data object OrderListScreen : Routes("orders_list")

    data object OrderDetailsScreen : Routes("order_details")
}




@Serializable
data object AdminLoginScreen : NavKey

@Serializable
data object AdminPanelScreen : NavKey

@Serializable
data object UsersListScreen : NavKey

@Serializable
data object UserDetailsScreen : NavKey

@Serializable
data object ProductListScreen : NavKey

@Serializable
data object ProductDetailsScreen : NavKey

@Serializable
data object AddProductScreen : NavKey

@Serializable
data object OrderListScreen : NavKey

@Serializable
data class OrderDetailsScreen(val orderId: String ?= null) : NavKey




