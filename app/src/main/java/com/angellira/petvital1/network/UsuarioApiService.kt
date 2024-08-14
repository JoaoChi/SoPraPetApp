package com.angellira.petvital1.network

import com.angellira.petvital1.model.Pet
import com.angellira.petvital1.model.Petshop
import com.angellira.petvital1.model.Usuario
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
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

private const val BASE_URL = "https://pets-f26d1-default-rtdb.firebaseio.com/"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

val json = Json {
    ignoreUnknownKeys = true
}

interface UsuariosApiService{
    @GET("Usuario.json")
    suspend fun getUsers() :Map<String, Usuario>

    @GET("Usuario/{id}.json")
    suspend fun getUser(@Path("id") id: String) : Usuario

    @POST("Usuario.json")
    suspend fun saveUser(@Body user: Usuario)

    @PUT("Usuario/{id}.json")
    suspend fun editUser(@Path("id") id: String, @Body user: Usuario): Response<Unit>

    @DELETE("Usuario/{id}.json")
    suspend fun deleteUser(@Path("id") id: String)

    @GET("pets.json")
    suspend fun getPets() : Map<String, Pet>

    @POST("pets.json")
    suspend fun savePets(@Body pet: Pet)

    @POST("pets/{id}.json")
    suspend fun savePetId(@Body pet: Pet, @Path("id")id: String)

    @DELETE("pets/{id}.json")
    suspend fun deletePet(@Path("id")id: String)

    @POST("Petshops.json")
    suspend fun savePetshop(@Body petshop: Petshop)

    @GET("Petshops.json")
    suspend fun getPetshop() : Map<String, Petshop>
}


object UsersApi{
    val retrofitService : UsuariosApiService by lazy{
        retrofit.create(UsuariosApiService::class.java)
    }
}
