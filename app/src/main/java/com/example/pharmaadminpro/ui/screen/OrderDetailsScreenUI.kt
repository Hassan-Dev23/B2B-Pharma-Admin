package com.example.pharmaadminpro.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.pharmaadminpro.viewModel.AdminViewModel
import com.example.pharmaadminpro.viewModel.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderDetailsScreenUI(
    viewModel: AdminViewModel,
    orderId: String? = null
) {

    val orderDetailsState by viewModel.orderDetailsState.collectAsState()
    val orderUpdateState by viewModel.orderUpdateState.collectAsState()


    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {

            scope.launch {
                isRefreshing = true
                if (orderId != null) {
                    viewModel.getOrder(orderId)

                }
                delay(1000)
                isRefreshing = false
            }

        },
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize(),
        state = rememberPullToRefreshState()
    ) {

        LazyColumn {
            item {
                when (orderDetailsState) {
                    is State.Error -> Box(
                        Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ) { Text((orderDetailsState as State.Error).message) }

                    State.IDLE -> {}
                    State.Loading -> Box(
                        Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }

                    is State.Success<*> -> {


                        val order = (orderDetailsState as State.Success).data.data
                        var currentStatus by remember { mutableStateOf(order.STATUS) }

                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp)
                        ) {

                            Text("Order Details", style = MaterialTheme.typography.headlineSmall)
                            Spacer(Modifier.height(16.dp))

                            Text("Product: ${order.PRODUCT_NAME}")
                            Text("Category: ${order.CATEGORY}")
                            Text("User: ${order.USER_NAME}")
                            Text("Message: ${order.MESSAGE}")
                            Text("Order Date: ${order.ORDER_DATE}")
                            Text("Price: Rs. ${order.PRICE}")
                            Text("Quantity: ${order.QUANTITY}")
                            Text("Total Price: Rs. ${order.TOTAL_PRICE}")
                            Spacer(Modifier.height(16.dp))

                            Text(
                                "Current Status: $currentStatus",
                                color = if (currentStatus == "DELIVERED") Color.Green else Color.Red
                            )
                            Spacer(Modifier.height(8.dp))

                            if (currentStatus != "DELIVERED") {
                                Button(
                                    onClick = {


                                        viewModel.updateOrder(order.ORDER_ID, "DELIVERED")

                                    }, modifier = Modifier.fillMaxWidth()
                                ) {
                                    when (orderUpdateState) {
                                        is State.Loading -> CircularProgressIndicator(
                                            modifier = Modifier.size(
                                                5.dp
                                            )
                                        )

                                        else -> Text("Mark As DELIVERED")
                                    }
                                }
                            }
                        }

                        if (orderUpdateState is State.Success) {
                            val text = (orderUpdateState as State.Success).data.message
                            currentStatus = "DELIVERED"
                            var showDialog by remember { mutableStateOf(true) }

                            if (showDialog) {
                                AlertDialog(

                                    title = {
                                        Text(text = "DELIVERED")
                                    }, text = {
                                        Text(text = text)
                                    }, onDismissRequest = {
                                        viewModel.resetIdle()

                                        showDialog = false
                                    }, confirmButton = {
                                        TextButton(
                                            onClick = {
                                                viewModel.resetIdle()
                                                showDialog = false
                                            }) {
                                            Text("OK")
                                        }
                                    }

                                )
                            }
                        }

                    }


                }
            }
        }
    }
}
