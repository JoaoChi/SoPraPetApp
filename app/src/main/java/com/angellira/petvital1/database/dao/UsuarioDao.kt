package com.angellira.petvital1.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.angellira.petvital1.model.Usuario
import retrofit2.http.Path

@Dao
interface UsuarioDao{
    @Query("SELECT * FROM Usuario")
    fun getAllUsuario(): List<Usuario>

    @Insert
    fun cadastrarUsuarios(usuario: List<Usuario>)

    @Insert
    fun cadastrarUsuario(usuario: Usuario)

    @Query("SELECT * FROM usuario WHERE email = :email LIMIT 1")
    suspend fun pegarEmailUsuario(email: String): Usuario?

    @Query("update usuario set password = (:newsenha) where email IN(:email)")
    fun updateSenha(newsenha: String, email: String)

}