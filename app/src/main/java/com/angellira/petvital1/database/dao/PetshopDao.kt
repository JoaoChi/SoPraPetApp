package com.angellira.petvital1.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.angellira.petvital1.model.Petshop
import com.angellira.petvital1.model.Usuario

@Dao
interface PetshopDao{
    @Query("SELECT * FROM Petshop")
    fun getAllpetshop(): List<Petshop>

    @Insert
    fun cadastrarPetshops(petshops: List<Petshop>)

    @Insert
    fun cadastrarPethops(petshops: Petshop)

//    @Delete
//    fun deletarPetshops()

}