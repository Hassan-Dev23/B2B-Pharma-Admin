package com.example.pharmaadminpro.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.pharmaadminpro.retrofit.dataTypes.User
import com.example.pharmaadminpro.viewModel.AdminViewModel
import com.example.pharmaadminpro.viewModel.State



@Composable
fun UserDetailsScreenUI(
    viewModel: AdminViewModel,

    ) {
    val approveButtonState by viewModel.toggleApproveState.collectAsState()
    val blockButtonState by viewModel.toggleBlockState.collectAsState()
    val state by viewModel.userDetailsState.collectAsState()
    when (state) {
        is State.Error -> Box(
            Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) { Text((state as State.Error).message) }

        State.IDLE -> {}
        State.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }

        is State.Success<*> -> {
            val successState = state as? State.Success<*>
            val user = (successState?.data as? User)?.user

            if (user != null) {
                var isApproved = user.isApproved == 1
                var isBlocked = user.Blocked == 1

                LaunchedEffect(approveButtonState, blockButtonState) {
                    when {
                        approveButtonState is State.Success<*> -> {
                            viewModel.getUserById(user.USER_ID)
                            viewModel.resetToggleStates() // Reset after handling
                        }
                        blockButtonState is State.Success<*> -> {
                            viewModel.getUserById(user.USER_ID)
                            viewModel.resetToggleStates() // Reset after handling
                        }
                    }
                }
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "User Details",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        // Glowing status
                        GlowingDotWithRadialEffect(isApproved && !isBlocked)
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    UserDetailItem("Name", user.USER_NAME)
                    UserDetailItem("Email", user.EMAIL)
                    UserDetailItem("Phone", user.PHONE_NUMBER)
                    UserDetailItem("Address", user.ADDRESS)
                    UserDetailItem("Pin Code", user.Pin_CODE)
                    UserDetailItem("User ID", user.USER_ID)
                    UserDetailItem("Created On", user.Date_of_Creation)

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Button(
                            onClick = {
                                if (user.isApproved == 1) {
                                    viewModel.disApproveUser(user.USER_ID)
                                } else {
                                    viewModel.approveUser(user.USER_ID)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isApproved) Color.Gray else Color.Green
                            ),
                            enabled = approveButtonState !is State.Loading
                        ) {
                            when (approveButtonState) {
                                is State.Error -> Text((approveButtonState as State.Error).message)
                                State.IDLE -> Text(if (isApproved) "Disapprove" else "Approve")
                                State.Loading -> CircularProgressIndicator()
                                is State.Success<*> -> {
                                    Text(if (isApproved) "Disapprove" else "Approve")
                                }
                            }

                        }

                        Button(
                            onClick = {
                                if (user.Blocked == 1) {
                                    viewModel.unBlockUser(user.USER_ID)
                                } else {
                                    viewModel.blockUser(user.USER_ID)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isBlocked) Color.Gray else Color.Red
                            ),
                            enabled = blockButtonState !is State.Loading
                        ) {
                            when (blockButtonState) {
                                is State.Error -> {
                                    Text((blockButtonState as State.Error).message)
                                }

                                State.IDLE -> Text(if (isBlocked) "Unblock" else "Block")
                                State.Loading -> CircularProgressIndicator()
                                is State.Success<*> -> {

                                    Text(if (isBlocked) "Unblock" else "Block")
                                }
                            }

                        }
                    }
                }

            }


        }
    }


}



@Composable
fun UserDetailItem(s: String, userName: String) {
    Column(modifier = Modifier.padding(vertical = 6.dp)) {
        Text(
            text = s,
            style = MaterialTheme.typography.labelSmall,
            color = Color.Gray
        )
        Text(
            text = userName,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }

}
