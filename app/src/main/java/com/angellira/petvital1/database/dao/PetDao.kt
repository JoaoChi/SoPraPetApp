package com.angellira.petvital1.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.angellira.petvital1.model.Pet

@Dao
interface PetDao{
    @Query("SELECT * FROM Pet")
    fun getPet(): List<Pet>

    @Insert
    fun cadastrarPets(pet: List<Pet>)

    @Insert
    fun cadastrarPet(pet: Pet)

//    @Delete
//    fun deletarPet()

}

