package com.example.pharmaadminpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pharmaadminpro.navigation.AppNavigation
import com.example.pharmaadminpro.preference.PreferenceDataStore
import com.example.pharmaadminpro.ui.theme.PharmaAdminProTheme
import com.example.pharmaadminpro.utils.AndroidConnectivityObserver
import com.example.pharmaadminpro.viewModel.AdminViewModel
import com.example.pharmaadminpro.viewModel.ConnectivityViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PharmaAdminProTheme {

                val connectivityViewModel = viewModel<ConnectivityViewModel> {
                    ConnectivityViewModel(
                        connectivityObserver = AndroidConnectivityObserver(
                            context = applicationContext
                        )
                    )
                }



                val context = LocalContext.current
                val preferenceDataStore = PreferenceDataStore(context) // Or get it via DI

                val viewModel by viewModels<AdminViewModel>()

                AppNavigation(viewModel,connectivityViewModel, preferenceDataStore)
            }
        }
    }
}

