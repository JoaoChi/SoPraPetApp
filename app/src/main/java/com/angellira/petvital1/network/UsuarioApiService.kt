package com.angellira.petvital1.network

import com.angellira.petvital1.model.Pet
import com.angellira.petvital1.model.Usuario
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
    suspend fun getUsers() :Map<String, Usuario>

    @GET("users/{id}.json")
    suspend fun getUser(@Path("id") id: String) : Usuario

    @POST("users.json")
    suspend fun saveUser(@Body user: Usuario)

    @PUT("users/{id}.json")
    suspend fun editUser(@Path("id") id: String, @Body user: Usuario): Response<Unit>

    @DELETE("users/{id}.json")
    suspend fun deleteUser(@Path("id") id: String)

    @GET("sports.json")
    suspend fun getPets() : Map<String, Pet>
}


object UsersApi{
    val retrofitService : UsuariosApiService by lazy{
        retrofit.create(UsuariosApiService::class.java)
    }
}
