package com.example.pharmaadminpro.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.sharp.Face
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.entry
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSavedStateNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.navigation3.ui.rememberSceneSetupNavEntryDecorator
import com.example.jcmodule.DataClassBottomAppBar.BounceBottomBar
import com.example.jcmodule.DataClassBottomAppBar.BounceBottomBarItem
import com.example.pharmaadminpro.R
import com.example.pharmaadminpro.preference.PreferenceDataStore
import com.example.pharmaadminpro.ui.screen.AdminLoginUI
import com.example.pharmaadminpro.ui.screen.AdminPanelUI
import com.example.pharmaadminpro.ui.screen.CreateProductScreenUI
import com.example.pharmaadminpro.ui.screen.OrderDetailsScreenUI
import com.example.pharmaadminpro.ui.screen.OrdersListScreenUI
import com.example.pharmaadminpro.ui.screen.ProductDetailsScreenUI
import com.example.pharmaadminpro.ui.screen.ProductListScreenUI
import com.example.pharmaadminpro.ui.screen.UserDetailsScreenUI
import com.example.pharmaadminpro.ui.screen.UsersListUI
import com.example.pharmaadminpro.viewModel.AdminViewModel
import com.example.pharmaadminpro.viewModel.ConnectivityViewModel


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun AppNavigation(
    viewModel: AdminViewModel,
    connectivityViewModel: ConnectivityViewModel,
    preferenceDataStore: PreferenceDataStore
) {

    val isConnected by connectivityViewModel.isConnected.collectAsStateWithLifecycle()
    val isDarkTheme = isSystemInDarkTheme()
    val snackBarHostState = remember { SnackbarHostState() }
    //nav3

    val backStack = rememberNavBackStack(AdminLoginScreen)
    val currentScreen = backStack.lastOrNull()
    val selectedIndex = when (currentScreen) {
        AdminPanelScreen -> 0
        UsersListScreen -> 1
        ProductListScreen -> 2
        AddProductScreen -> 3
        else -> -1
    }


    //OldNav
//    val navController = rememberNavController()
//    val navBackStackEntry by navController.currentBackStackEntryAsState()
//    val currentRoute = navBackStackEntry?.destination?.route
//    val selectedIndex = when (currentRoute) {
//        Routes.AdminPanelScreen.route -> 0
//        Routes.UsersListScreen.route -> 1
//        Routes.ProductListScreen.route -> 2
//        Routes.AddProductScreen.route -> 3
//        else -> -1 // or default
//    }


    Scaffold(
        snackbarHost = {
            SnackbarHost(hostState = snackBarHostState)
        },
        bottomBar = {
            if (currentScreen != AdminLoginScreen) {
                BounceBottomBar(
                    selectedIndex = selectedIndex,
                    onItemSelected = {
                        when (it) {
                            0 -> backStack.add(AdminPanelScreen)
                            1 -> backStack.add(UsersListScreen)
                            2 -> backStack.add(ProductListScreen)
                            3 -> backStack.add(AddProductScreen)
                        }
                    },
                    items = listOf(
                        BounceBottomBarItem(Icons.Filled.Home, "Home"),
                        BounceBottomBarItem(Icons.Sharp.Face, "Users"),
                        BounceBottomBarItem(Icons.Filled.Menu, "Products"),
                        BounceBottomBarItem(Icons.Filled.Add, "Add Product"),
                    ),
                    bounceScale = 2.0f,
                    selectedColor = if (isDarkTheme) Color.White else Color.Black,
                    containerColor = if (isDarkTheme) Color(0xFF121212) else Color.White,
                    showLabels = false,
                    barHeight = 70.dp,
                    unselectedColor = if (isDarkTheme) Color(0xFFB0B0B0) else Color(0xFF7F7F7F)


                )
            }
        }
    ) { innerPadding ->


        LaunchedEffect(isConnected) {
            if (!isConnected) {
                snackBarHostState.showSnackbar("No internet connection")
            }
        }

        if (isConnected) NavDisplay(
            backStack = backStack,
            entryDecorators = listOf(
                rememberSavedStateNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator(),
                rememberSceneSetupNavEntryDecorator()
            ),
            modifier = Modifier.padding(innerPadding),
            onBack = { backStack.removeLastOrNull() },
            entryProvider = entryProvider {
                entry<AdminLoginScreen> {
                    AdminLoginUI(viewModel, backStack, preferenceDataStore)
                }
                entry<AdminPanelScreen> {
                    AdminPanelUI(viewModel, backStack, innerPadding)
                }
                entry<UsersListScreen> {
                    UsersListUI(viewModel, backStack)
                }
                entry<UserDetailsScreen> {
                    UserDetailsScreenUI(viewModel)
                }
                entry<ProductListScreen> {
                    ProductListScreenUI(viewModel, backStack)
                }
                entry<ProductDetailsScreen> {
                    ProductDetailsScreenUI(viewModel)
                }
                entry<AddProductScreen> {
                    CreateProductScreenUI(viewModel)
                }
                entry<OrderListScreen> {
                    OrdersListScreenUI(viewModel, backStack)
                }
                entry<OrderDetailsScreen> { key ->
                    OrderDetailsScreenUI(viewModel, key.orderId)
                }

            }


        )else{

            Dialog(
                onDismissRequest = { /* no-op to block dismiss */ },
                properties = DialogProperties(
                    usePlatformDefaultWidth = false // full width
                )
            ) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.img),
                            contentDescription = "No internet",
                            modifier = Modifier
                                .height(180.dp)
                                .fillMaxWidth()
                        )
                        Spacer(Modifier.height(24.dp))
                        Text(
                            text = "Uh-oh!",
                            style = MaterialTheme.typography.titleLarge.copy(
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold
                            ),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "It seems you’re offline.\nCheck your connection and try again.",
                            style = MaterialTheme.typography.bodyLarge,
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(24.dp))
                        Button(onClick = {}) {
                            Text(text = "Retry")
                        }
                    }
                }
            }
        }


//        NavHost(
//            navController = navController,
//            startDestination = Routes.AdminLoginScreen.route,
//            modifier = Modifier.padding(innerPadding)
//        ) {
//            composable(Routes.AdminLoginScreen.route) {
//                AdminLoginUI(viewModel, navController, preferenceDataStore)
//            }
//            composable(Routes.AdminPanelScreen.route) {
//                AdminPanelUI(viewModel, navController, innerPadding)
//            }
//            composable(Routes.UsersListScreen.route) {
//                UsersListUI(viewModel, navController)
//            }
//            composable(Routes.UserDetailsScreen.route) {
//                UserDetailsScreenUI(viewModel)
//            }
//            composable(Routes.ProductListScreen.route) {
//                ProductListScreenUI(viewModel, navController)
//            }
//            composable(Routes.ProductDetailsScreen.route) {
//                ProductDetailsScreenUI(viewModel)
//            }
//            composable(Routes.AddProductScreen.route) {
//                CreateProductScreenUI(viewModel)
//            }
//            composable(Routes.OrderListScreen.route) {
//
//                OrdersListScreenUI(viewModel, navController)
//            }
//            composable(Routes.OrderDetailsScreen.route) {
//
//                OrderDetailsScreenUI(
//                    viewModel = viewModel
//                )
//            }
//
//
//        }
    }
}


