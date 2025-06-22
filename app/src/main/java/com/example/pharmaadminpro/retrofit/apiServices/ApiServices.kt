package com.example.pharmaadminpro.retrofit.apiServices


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
import retrofit2.Response
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST

interface ApiServices {


    @FormUrlEncoded
    @POST("auth_admin")
    suspend fun authAdmin(
        @Field("secret_key") adminKey : String
    ) : Response<AuthAdmin>


    @GET("getAllUsers")
    suspend fun getAllUsers(): Response<GetAllUsers>



    @FormUrlEncoded
    @POST("getUser")
    suspend fun getUserById(
        @Field("user_id") userId : String
    ): Response<User>

    @FormUrlEncoded
    @PATCH("approveUser")
    suspend fun toggleApprove(
        @Field("user_id") userId: String,
        @Field("isApproved") isApproved : Int
    ): Response<ToggleApprove>

    @FormUrlEncoded
    @PATCH("blockUser")
    suspend fun blockUser(
        @Field("user_id") userId: String,
        @Field("isBlocked") isBlocked : Int
    ): Response<ToggleBlock>


    @FormUrlEncoded
    @POST("createProduct")
    suspend fun createProduct(
        @Field("product_name") productName: String,
        @Field("product_price") productPrice : String,
        @Field("product_quantity") productQuantity : String,
        @Field("category") category : String
    ): Response<AddProduct>


    @GET("getAllProducts")
    suspend fun getAllProducts(): Response<GetAllProduct>

    @FormUrlEncoded
    @POST("getProduct")
    suspend fun getProduct(
        @Field("product_id") productID: String,
    ): Response<GetSpecificProduct>




    @GET("getAllOrders")
    suspend fun getAllOrders(): Response<AllOrders>

    @FormUrlEncoded
    @POST("getOrder")
    suspend fun getOrder(
        @Field("order_id") orderId: String,
    ): Response<OrderDetail>

    @FormUrlEncoded
    @PATCH("updateOrderStatus")
    suspend fun updateOrderStatus(
        @Field("order_id") orderId: String,
        @Field("status") status: String
    ): Response<OrderUpdate>







}