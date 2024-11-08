package com.angellira.petvital1.network

import com.angellira.petvital1.model.Pet
import com.angellira.petvital1.model.Petshop
import com.angellira.petvital1.model.User
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

private const val BASE_URL = "https://soprapetapi.onrender.com"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
    .baseUrl(BASE_URL)
    .build()

interface UsuariosApiService{

    @GET("/users/{email}")
    suspend fun getUsers(@retrofit2.http.Query("email") email: String): User

    @GET("/users/{id}")
    suspend fun getUser(@Path("id") id: String) : User

    @POST("/users")
    suspend fun saveUser(@Body user: User)

    @PATCH("/users/{email}/password")
    suspend fun putUser(@retrofit2.http.Query("email") email: String, @retrofit2.http.Query("newPassword") newPassword: String)

    @PATCH("/users/{email}/details")
    suspend fun editarPerfilUsuario(
        @Path("email") meuEmail: String,
        @retrofit2.http.Query("email") email: String,
        @retrofit2.http.Query("newName") newName: String,
        @retrofit2.http.Query("newCpf") newCpf: String,
        @retrofit2.http.Query("newImage") newImage: String
    )

    @DELETE("users/{email}")
    suspend fun deleteUser(@Path("email") email: String)

    @GET("/pets")
    suspend fun getPets() : List<Pet>

        @POST("/pets")
        suspend fun savePets(@Body pet: Pet)

    @POST("pets/{id}")
    suspend fun savePetId(@Body pet: Pet, @Path("id")id: String)

    @DELETE("pets/{id}")
    suspend fun deletePet(@Path("id")id: String)

    @DELETE("/petshops/{uid}")
    suspend fun deletarPetshop(@Path("uid") uid: String)

    @POST("/petshops")
    suspend fun savePetshop(@Body petshop: Petshop): Response<Void>

    @GET("/Petshops")
    suspend fun getPetshop(): List<Petshop>
}


object UsersApi{
    val retrofitService : UsuariosApiService by lazy{
        retrofit.create(UsuariosApiService::class.java)
    }
}
