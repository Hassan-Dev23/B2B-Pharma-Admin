package com.example.pharmaadminpro.ui.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.pharmaadminpro.viewModel.AdminViewModel
import com.example.pharmaadminpro.viewModel.State



@Composable
fun ProductDetailsScreenUI(viewModel: AdminViewModel) {
    val state by viewModel.productDetailsState.collectAsState()
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
            val product = (state as State.Success).data.data
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Product Name
                Text(
                    text = product.PRODUCT_NAME,
                    style = TextStyle(
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Category
                Text(
                    text = "Category: ${product.CATEGORY}",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Price
                Text(
                    text = "Price: \$${product.PRODUCT_PRICE}",
                    style = TextStyle(
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Quantity
                Text(
                    text = "Quantity: ${product.PRODUCT_QUANTITY}",
                    style = TextStyle(
                        fontSize = 16.sp
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Product ID
                Text(
                    text = "Product ID: ${product.PRODUCT_ID}",
                    style = TextStyle(
                        color = Color.Gray,
                        fontSize = 14.sp
                    )
                )

                // Add any additional actions or information here (e.g., "Add to Cart" button)
                Spacer(modifier = Modifier.height(16.dp))


            }

        }
    }

}




