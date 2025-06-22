package com.example.pharmaadminpro.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pharmaadminpro.repo.Repository
import com.example.pharmaadminpro.retrofit.dataTypes.AddProduct
import com.example.pharmaadminpro.retrofit.dataTypes.AllOrders
import com.example.pharmaadminpro.retrofit.dataTypes.AuthAdmin
import com.example.pharmaadminpro.retrofit.dataTypes.GetAllProduct
import com.example.pharmaadminpro.retrofit.dataTypes.GetAllUsers
import com.example.pharmaadminpro.retrofit.dataTypes.GetSpecificProduct
import com.example.pharmaadminpro.retrofit.dataTypes.OrderDetail
import com.example.pharmaadminpro.retrofit.dataTypes.OrderUpdate
import com.example.pharmaadminpro.retrofit.dataTypes.ToggleApprove
import com.example.pharmaadminpro.retrofit.dataTypes.ToggleBlock
import com.example.pharmaadminpro.retrofit.dataTypes.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AdminViewModel(

) : ViewModel() {

    private val repository = Repository()
    private val _adminState = MutableStateFlow<State<AuthAdmin>>(State.IDLE)
    val adminState = _adminState.asStateFlow()
    private val _usersListState = MutableStateFlow<State<GetAllUsers>>(State.IDLE)
    val userListState = _usersListState.asStateFlow()
    private val _userDetailsState = MutableStateFlow<State<User>>(State.Loading)
    val userDetailsState = _userDetailsState.asStateFlow()
    private val _toggleApproveState = MutableStateFlow<State<ToggleApprove>>(State.IDLE)
    val toggleApproveState = _toggleApproveState.asStateFlow()
    private val _toggleBlockState = MutableStateFlow<State<ToggleBlock>>(State.IDLE)
    val toggleBlockState = _toggleBlockState.asStateFlow()
    private val _allProductsListState = MutableStateFlow<State<GetAllProduct>>(State.IDLE)
    val allProductSListState = _allProductsListState.asStateFlow()
    private val _productDetailsState = MutableStateFlow<State<GetSpecificProduct>>(State.IDLE)
    val productDetailsState = _productDetailsState.asStateFlow()
    private val _addProductState = MutableStateFlow<State<AddProduct>>(State.IDLE)
    val addProductState = _addProductState.asStateFlow()
    private val _ordersListState = MutableStateFlow<State<AllOrders>>(State.IDLE)
    val ordersListState = _ordersListState.asStateFlow()
    private val _orderDetailsState = MutableStateFlow<State<OrderDetail>>(State.IDLE)
    val orderDetailsState = _orderDetailsState.asStateFlow()
    private val _orderUpdateState = MutableStateFlow<State<OrderUpdate>>(State.IDLE)
    val orderUpdateState = _orderUpdateState.asStateFlow()


    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            repository.getAllUsers().collect {
//                when (it) {
//                    is State.Error -> _usersListState.value = State.Error(it.message)
//                    State.IDLE -> _usersListState.value = State.IDLE
//                    State.Loading -> _usersListState.value = State.Loading
//                    is State.Success<*> -> _usersListState.value =
//                        State.Success<GetAllUsers>(it.data as GetAllUsers)
//                }
//            }
//        }
        getAllUsers()
        getAllCounterproductive()
        getAllOrders()

    }

    fun getAllUsers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllUsers().collect {
                when (it) {
                    is State.Error -> _usersListState.value = State.Error(it.message)
                    State.IDLE -> _usersListState.value = State.IDLE
                    State.Loading -> _usersListState.value = State.Loading
                    is State.Success<*> -> _usersListState.value =
                        State.Success<GetAllUsers>(it.data as GetAllUsers)
                }
            }
        }
    }

    fun loginAdmin(key: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.loginAdmin(key).collect {
                when (it) {
                    is State.Loading -> _adminState.value = State.Loading
                    is State.Success -> _adminState.value = State.Success(it.data.body()!!)
                    is State.Error -> _adminState.value = State.Error(it.message)
                    State.IDLE -> _adminState.value = State.IDLE
                }
            }
        }
    }

    fun goBackToLogin() {
        _adminState.value = State.IDLE
    }

    fun getUserById(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getUserbyId(userID).collect {
                when (it) {
                    is State.Error -> _userDetailsState.value = State.Error(it.message)
                    State.IDLE -> _userDetailsState.value = State.IDLE
                    State.Loading -> _userDetailsState.value = State.Loading
                    is State.Success<*> -> _userDetailsState.value =
                        State.Success<User>(it.data as User)
                }
            }
        }
    }


    fun approveUser(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.approveUser(userID).collect {
                when (it) {
                    is State.Error -> _toggleApproveState.value = State.Error(it.message)
                    State.IDLE -> _toggleApproveState.value = State.IDLE
                    State.Loading -> _toggleApproveState.value = State.Loading
                    is State.Success<*> -> _toggleApproveState.value =
                        State.Success(it.data as ToggleApprove)
                }
            }
        }
    }

    fun disApproveUser(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.disApproveUser(userID).collect {
                when (it) {
                    is State.Error -> _toggleApproveState.value = State.Error(it.message)
                    State.IDLE -> _toggleApproveState.value = State.IDLE
                    State.Loading -> _toggleApproveState.value = State.Loading
                    is State.Success<*> -> _toggleApproveState.value =
                        State.Success(it.data as ToggleApprove)
                }
            }
        }
    }


    fun blockUser(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.blockUser(userID).collect {
                when (it) {
                    is State.Error -> _toggleBlockState.value = State.Error(it.message)
                    State.IDLE -> _toggleBlockState.value = State.IDLE
                    State.Loading -> _toggleBlockState.value = State.Loading
                    is State.Success<*> -> _toggleBlockState.value =
                        State.Success(it.data as ToggleBlock)
                }
            }
        }
    }

    fun unBlockUser(userID: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.unBlockUser(userID).collect {
                when (it) {
                    is State.Error -> _toggleBlockState.value = State.Error(it.message)
                    State.IDLE -> _toggleBlockState.value = State.IDLE
                    State.Loading -> _toggleBlockState.value = State.Loading
                    is State.Success<*> -> _toggleBlockState.value =
                        State.Success(it.data as ToggleBlock)
                }
            }
        }
    }

    fun resetToggleStates() {
        _toggleApproveState.value = State.IDLE
        _toggleBlockState.value = State.IDLE
    }

    fun resetIdle() {
        _orderUpdateState.value = State.IDLE
    }

    fun getAllCounterproductive() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllProductsinrepo().collect {
                when (it) {
                    is State.Error -> _allProductsListState.value = State.Error(it.message)
                    State.IDLE -> _allProductsListState.value = State.IDLE
                    State.Loading -> _allProductsListState.value = State.Loading
                    is State.Success<*> -> _allProductsListState.value =
                        State.Success(it.data as GetAllProduct)
                }
            }
        }
    }

    fun getSpecificProduct(id: String) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSpecificProduct(id).collect {
                when (it) {
                    is State.Error -> _productDetailsState.value = State.Error(it.message)
                    State.IDLE -> _productDetailsState.value = State.IDLE
                    State.Loading -> _productDetailsState.value = State.Loading
                    is State.Success<*> -> _productDetailsState.value =
                        State.Success(it.data as GetSpecificProduct)
                }
            }
        }
    }

    fun createProduct(
        productName: String,
        productPrice: String,
        productQuantity: String,
        category: String
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.createProduct(
                productName = productName,
                productPrice = productPrice,
                productQuantity = productQuantity,
                category = category
            ).collect {
                when (it) {
                    is State.Error -> _addProductState.value = State.Error(it.message)
                    State.IDLE -> _addProductState.value = State.IDLE
                    State.Loading -> _addProductState.value = State.Loading
                    is State.Success<*> -> _addProductState.value =
                        State.Success(it.data as AddProduct)
                }
            }
        }
    }


    fun getAllOrders() {
        viewModelScope.launch {
            repository.getAllOrders().collect {
                when (it) {
                    is State.Error -> _ordersListState.value = State.Error(it.toString())
                    State.IDLE -> _ordersListState.value = State.IDLE
                    State.Loading -> _ordersListState.value = State.Loading
                    is State.Success<*> -> _ordersListState.value =
                        State.Success(it.data as AllOrders)
                }
            }
        }
    }

    fun getOrder(orderId: String) {
        viewModelScope.launch {
            repository.getOrder(orderId).collect {
                when (it) {
                    is State.Error -> _orderDetailsState.value = State.Error(it.toString())
                    State.IDLE -> _orderDetailsState.value = State.IDLE
                    State.Loading -> _orderDetailsState.value = State.Loading
                    is State.Success<*> -> _orderDetailsState.value =
                        State.Success(it.data as OrderDetail)
                }
            }
        }
    }

    fun updateOrder(orderId: String, status: String) {
        viewModelScope.launch {
            repository.updateOrder(orderId, status).collect {
                when (it) {
                    is State.Error -> _orderUpdateState.value = State.Error(it.toString())
                    State.IDLE -> _orderUpdateState.value = State.IDLE
                    State.Loading -> _orderUpdateState.value = State.Loading
                    is State.Success<*> -> _orderUpdateState.value =
                        State.Success(it.data as OrderUpdate)
                }
            }
        }
    }


}

sealed class State<out T> {
    data object Loading : State<Nothing>()
    data class Success<out T>(val data: T) : State<T>()
    data class Error(val message: String) : State<Nothing>()
    data object IDLE : State<Nothing>()

}