package com.example.pharmaadminpro.ui.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import com.example.pharmaadminpro.navigation.OrderDetailsScreen
import com.example.pharmaadminpro.navigation.Routes
import com.example.pharmaadminpro.retrofit.dataTypes.DataXX
import com.example.pharmaadminpro.viewModel.AdminViewModel
import com.example.pharmaadminpro.viewModel.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrdersListScreenUI(
    viewModel: AdminViewModel,
    navBackStack: NavBackStack
) {
    val state by viewModel.ordersListState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {

            scope.launch {
                isRefreshing = true
                viewModel.getAllOrders()
                delay(1000)
                isRefreshing = false
            }

        },
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
        state = rememberPullToRefreshState()
    ) {
        when (state) {
            is State.Loading -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            is State.Success -> {
                val ordersList = (state as State.Success).data.data

                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(ordersList) { order ->
                        OrderItemCard(order = order, viewModel, navBackStack)
                    }
                }
            }

            is State.Error -> {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("Failed to load orders.")
                }
            }

            else -> {}
        }
    }
}

@Composable
fun OrderItemCard(order: DataXX, viewModel: AdminViewModel, navBackStack: NavBackStack) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                viewModel.getOrder(order.ORDER_ID)
                navBackStack.add(OrderDetailsScreen(orderId = order.ORDER_ID))
            },
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("Order ID: ${order.ORDER_ID}")
            Text("Product: ${order.PRODUCT_NAME}")
            Text("User: ${order.USER_NAME}")
            Text("Quantity: ${order.QUANTITY}")
            Text("Total: Rs. ${order.TOTAL_PRICE}")
            Text("Status: ${order.STATUS}", color = if (order.STATUS == "DELIVERED") Color.Green else Color.Red)
        }
    }
}
