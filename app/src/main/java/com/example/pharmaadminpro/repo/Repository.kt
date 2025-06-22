package com.example.pharmaadminpro.repo

import android.util.Log
import com.example.pharmaadminpro.retrofit.apiServices.ApiServiceProvider
import com.example.pharmaadminpro.retrofit.dataTypes.AddProduct
import com.example.pharmaadminpro.viewModel.State
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class Repository {

    fun loginAdmin(key: String) = flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.provideApiService().authAdmin(key)
            emit(State.Success(response))
        } catch (e: Exception) {
            emit(State.Error(e.message ?: "Unknown error"))
        }
    }

    fun getAllUsers() = flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.provideApiService().getAllUsers()

            emit(State.Success(response.body()))
        } catch (e: Exception) {
            emit(State.Error(e.toString()))
        }
    }


    fun getUserbyId(userID: String) = flow {
        emit(State.Loading)
        val response = ApiServiceProvider.provideApiService().getUserById(userId = userID)
        if (response.isSuccessful && response.body() != null) {
            emit(State.Success(response.body()!!))
        } else {
            emit(State.Error(response.message()))
        }
    }.catch { e ->
        emit(State.Error(e.toString()))
    }


    fun approveUser(userID: String) = flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.provideApiService().toggleApprove(
                userId = userID,
                isApproved = 1
            )
            emit(State.Success(response.body()))
        } catch (e: Exception) {
            emit(State.Error(e.toString()))
        }


    }

    fun disApproveUser(userID: String) = flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.provideApiService().toggleApprove(
                userId = userID,
                isApproved = 0
            )
            emit(State.Success(response.body()))
        } catch (e: Exception) {
            emit(State.Error(e.toString()))
        }

    }

    fun blockUser(userID: String) = flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.provideApiService().blockUser(
                userId = userID,
                isBlocked = 1
            )
            emit(State.Success(response.body()))
        } catch (e: Exception) {
            emit(State.Error(e.toString()))
        }

    }

    fun unBlockUser(userID: String) = flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.provideApiService().blockUser(
                userId = userID,
                isBlocked = 0
            )
            emit(State.Success(response.body()))
        } catch (e: Exception) {
            emit(State.Error(e.toString()))
        }

    }


    fun getAllProductsinrepo() = flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.provideApiService().getAllProducts()

            if (response.isSuccessful && response.body() != null)
                emit(State.Success(response.body()))
            else emit(State.Error(response.message()))
        } catch (e: Exception) {
            emit(State.Error(e.toString()))
        }
    }


    fun getSpecificProduct(productId: String) = flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.provideApiService().getProduct(productId)
            emit(State.Success(response.body()!!))
        } catch (e: Exception) {
            emit(State.Error(e.toString()))
        }
    }

    fun createProduct(
        productName: String,
        productPrice: String,
        productQuantity: String,
        category: String
    ) = flow {
        emit(State.Loading)
        try {
            val response = ApiServiceProvider.provideApiService().createProduct(
                productName =productName,
                productPrice = productPrice,
                productQuantity = productQuantity,
                category = category
            )
            emit(State.Success(response.body()!!))
        } catch (e: Exception) {
            emit(State.Error(e.toString()))
        }
    }

    fun getAllOrders() = flow {
        emit(State.Loading)
        try {

            val response = ApiServiceProvider.provideApiService().getAllOrders()
            if (response.isSuccessful){
                emit(State.Success(response.body()!!))
            }
        }
        catch (e: Exception){
            emit(State.Error(e.toString()))
        }
    }


    fun getOrder(orderId: String) = flow {
        emit(State.Loading)
        try {

            val response = ApiServiceProvider.provideApiService().getOrder(orderId)
            if (response.isSuccessful){
                emit(State.Success(response.body()!!))
            }
        }
        catch (e: Exception){
            emit(State.Error(e.toString()))
        }
    }

    fun updateOrder(orderId: String,status: String) = flow {
        emit(State.Loading)
        try {

            val response = ApiServiceProvider.provideApiService().updateOrderStatus(orderId,status)
            if (response.isSuccessful){
                emit(State.Success(response.body()!!))
            }
        }
        catch (e: Exception){
            emit(State.Error(e.toString()))
        }
    }


}