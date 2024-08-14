package com.angellira.petvital1.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.angellira.petvital1.model.Usuario

@Dao
interface UsuarioDao{
    @Query("SELECT * FROM Usuario")
    fun getAll(): List<Usuario>

    @Insert
    fun cadastrarPets(usuarios: List<Usuario>)

    @Insert
    fun cadastrarPet(usuarios: Usuario)

    @Delete
    fun deletarPet()

}