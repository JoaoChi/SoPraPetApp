package com.angellira.petvital1.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

private const val BASE_URL = ""

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()


interface UsuariosApiService{
    @GET("users.json")
    suspend fun getUsers() :Map<String, User>

    @GET("users/{id}.json")
    suspend fun getUser(@Path("id") id: String) : User

    @POST("users.json")
    suspend fun saveUser(@Body user: User)

    @PUT("users/{id}.json")
    suspend fun editUser(@Path("id") id: String, @Body user: User): Response<Unit>

    @DELETE("users/{id}.json")
    suspend fun deleteUser(@Path("id") id: String)
}

@Serializable
data class User(
    var id: String,
    var name: String,
    var email: String,
    var password: String,
    var imagem: String
)

@Serializable
data class Administrador(
    var id: String,
    var name: String,
    var email: String,
    var password: String,
    var imagem: String
)

@Serializable
data class Pet(
    var id: String,
    var name: String,
    var tipo: String,
    var peso: Int,
    var idade: Int,
    var imagem: String
)

@Serializable
data class Petshops(
    var id: String,
    var name: String,
    var localizacao: String,
    var descricao: String,
    var servicos: String
)

@Serializable
data class endereco(
    var cidade: String,
    var rua: String,
    var numero: Int,
    var referencia: String
)



object UsersApi{
    val retrofitService : UsuariosApiService by lazy{
        retrofit.create(UsuariosApiService::class.java)
    }
}
