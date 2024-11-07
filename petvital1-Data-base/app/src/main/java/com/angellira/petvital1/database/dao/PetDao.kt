package com.angellira.petvital1.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.angellira.petvital1.model.Pet
import com.angellira.petvital1.model.Usuario

@Dao
interface PetDao{
    @Query("SELECT * FROM Pet")
    fun getPet(): List<Pet>

    @Insert
    fun cadastrarPets(pet: List<Pet>)

    @Insert
    fun cadastrarPet(pet: Pet)

//    @Query("SELECT * FROM pet WHERE id = :id LIMIT 1")
//    suspend fun pegarIdPet(id: Long)

    @Query("delete from Pet where id = (:id)")
    fun deletarPet(id: String)

}

