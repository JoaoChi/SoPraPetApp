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
    fun cadastrarUsuarios(usuarios: List<Usuario>)

    @Insert
    fun cadastrarUsuario(usuarios: Usuario)

//    @Delete
//    fun deletarUsuario(@Path("id") id: Int)

}