package com.angellira.petvital1.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.angellira.petvital1.model.Petshop

@Dao
interface PetshopDao{
    @Query("SELECT * FROM Petshop")
    fun getAllpetshop(): List<Petshop>

    @Insert
    fun cadastrarPetshops(petshops: List<Petshop>)

    @Insert
    fun cadastrarPetshop(petshops: Petshop)

    @Query("delete from Petshop where uid = (:uid)")
    fun deletarPet(uid: String)

    @Query("SELECT * FROM petshop WHERE cnpj = :cnpj LIMIT 1")
    suspend fun pegarCnpj(cnpj: String): Petshop?
}