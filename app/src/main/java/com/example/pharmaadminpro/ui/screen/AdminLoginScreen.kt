package com.example.pharmaadminpro.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavBackStack
import com.example.pharmaadminpro.navigation.AdminLoginScreen
import com.example.pharmaadminpro.navigation.AdminPanelScreen
import com.example.pharmaadminpro.preference.PreferenceDataStore
import com.example.pharmaadminpro.viewModel.AdminViewModel
import com.example.pharmaadminpro.viewModel.State
import kotlinx.coroutines.launch


@Composable
fun AdminLoginUI(
    viewModel: AdminViewModel,
    navBackStack: NavBackStack,
    preferenceDataStore: PreferenceDataStore
) {
    val scope = rememberCoroutineScope()
    var keySavedInPreference by remember { mutableStateOf("") }



        var key by remember { mutableStateOf("") }
        LaunchedEffect(Unit) {
            scope.launch {

                if(preferenceDataStore.getAdminKeyValue() != null)
                    keySavedInPreference =  preferenceDataStore.getAdminKeyValue().toString()
            }
        }


        val state = viewModel.adminState.collectAsState()
        Scaffold(modifier = Modifier.fillMaxSize()) { paddingValues ->
            Column(
                Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                when (state.value) {
                    is State.Error -> {
                        Text(text = (state.value as State.Error).message)
                    }

                    State.IDLE -> {
                        if(keySavedInPreference.isNotEmpty()) {

                            CircularProgressIndicator()
                            viewModel.loginAdmin(keySavedInPreference)

                        }
                        else if (keySavedInPreference.isEmpty()){
                            OutlinedTextField(
                                value = key,
                                onValueChange = { key = it }
                            )
                            Spacer(modifier = Modifier.padding(top = 10.dp))
                            OutlinedButton(onClick = {
                                viewModel.loginAdmin(key)

                            }) {
                                Text(text = "Login")
                            }

                            Text(text = "keySavedInPreference: $keySavedInPreference")
                        }


                    }

                    State.Loading -> {
                        CircularProgressIndicator()
                    }

                    is State.Success<*> -> {

                        var isFailed by remember { mutableStateOf(false) }
                        isFailed = (state.value as State.Success).data.status != "200"

                        if (isFailed) {
                            AlertDialog(
                                onDismissRequest = {

                                    if (isFailed)
                                        viewModel.goBackToLogin()


                                },
                                title = {
                                    if (!isFailed)
                                        Text("Success")
                                    else
                                        Text("Failed")
                                },
                                text = {
                                    Column {
                                        if (!isFailed)
                                            Text("Logged In Successfully")
                                        else
                                            Text("Login Failed")
                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text("Message: " + (state.value as State.Success).data.message)

                                        Spacer(modifier = Modifier.height(8.dp))

                                        Text("Status: " + (state.value as State.Success).data.status)

                                    }
                                },
                                confirmButton = {
                                    TextButton(onClick = {
                                        if (isFailed)
                                            viewModel.goBackToLogin()

                                    }) {
                                        Text("OK")
                                    }
                                }
                            )
                        } else {

                            LaunchedEffect(Unit) {
                                scope.launch {
                                    if(key.isNotEmpty()){
                                        preferenceDataStore.saveAdminKeyInPreference(key = key)

                                    }
                                }

                                navBackStack.add(AdminPanelScreen)
                                navBackStack.remove(AdminLoginScreen)
                            }




                    }
                }


            }
        }

    }


}