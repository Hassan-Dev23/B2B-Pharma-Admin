package com.example.pharmaadminpro.ui.screen


import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import com.example.pharmaadminpro.navigation.OrderListScreen
import com.example.pharmaadminpro.navigation.ProductListScreen
import com.example.pharmaadminpro.navigation.UsersListScreen
import com.example.pharmaadminpro.retrofit.dataTypes.GetAllProduct
import com.example.pharmaadminpro.retrofit.dataTypes.GetAllUsers
import com.example.pharmaadminpro.viewModel.AdminViewModel
import com.example.pharmaadminpro.viewModel.State
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminPanelUI(
    viewModel: AdminViewModel, navBackStack: NavBackStack, innerPadding: PaddingValues
) {

    val userListUiState by viewModel.userListState.collectAsState()
    val productState by viewModel.allProductSListState.collectAsState()
    val orderListState by viewModel.ordersListState.collectAsState()
    var isRefreshing by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()




    PullToRefreshBox(
        isRefreshing = isRefreshing,
        onRefresh = {
            scope.launch {
                isRefreshing = true
                viewModel.getAllUsers()
                viewModel.getAllCounterproductive()
                viewModel.getAllOrders()
                delay(1000)
                isRefreshing = false

            }

        },
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
        state = rememberPullToRefreshState()
    ) {
        LazyColumn(
            modifier = Modifier

                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {



            val productCount = when (productState) {
                is State.Success<*> -> {
                    val data = (productState as State.Success<GetAllProduct>).data
                    data.data.size

                }

                else -> 0
            }
            if (productState is State.Success) {
                item {
                    ProductsCountCard(
                        productCount
                    ) {
                        navBackStack.add(ProductListScreen)
                    }
                }
            } else if (productState is State.Loading) {
                item { CircularProgressIndicator() }
            }
            when (orderListState) {
                is State.Success<*> -> {
                    val data = (orderListState as State.Success).data.data

                    item {
                        OrdersCountCard(data.size) {

                            navBackStack.add(OrderListScreen)
                        }
                    }


                }

                is State.Error -> item {
                    Text((orderListState as State.Error).message)
                }

                is State.Loading -> item {
                    CircularProgressIndicator()
                }

                State.IDLE -> {

                }
            }
            when (userListUiState) {
                is State.Loading -> {
                    item {
                        CircularProgressIndicator()

                    }

                }

                is State.Error -> item {
                    Text(
                        text = (userListUiState as State.Error).message,
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                is State.Success<*> -> {
                    val userListState = userListUiState as State.Success<GetAllUsers>
                    item {
                        UserCountCard(userListState.data.AllUsers.size) {
                            navBackStack.add(UsersListScreen)
                        }
                    }


                }

                State.IDLE -> {
                    item { Text(text = "Waiting for data...") }
                }
            }
        }
    }

}


@Composable
fun UserCountCard(
    userCount: Int, onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(150.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = "Total Users",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$userCount",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // You can place an icon or decorative element here if needed
        }
    }
}


@Composable
fun ProductsCountCard(
    userCount: Int, onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(150.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = "Total Products",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$userCount",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // You can place an icon or decorative element here if needed
        }
    }
}

@Composable
fun OrdersCountCard(
    userCount: Int, onClick: () -> Unit
) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(150.dp)
            .clickable(onClick = onClick),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = "Total Orders",
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = "$userCount",
                    style = MaterialTheme.typography.displaySmall,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // You can place an icon or decorative element here if needed
        }
    }
}
