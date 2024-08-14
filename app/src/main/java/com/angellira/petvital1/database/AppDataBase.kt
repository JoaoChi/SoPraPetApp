package com.angellira.petvital1.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.angellira.petvital1.database.dao.PetDao
import com.angellira.petvital1.database.dao.PetshopDao
import com.angellira.petvital1.database.dao.UsuarioDao
import com.angellira.petvital1.model.Pet
import com.angellira.petvital1.model.Petshop
import com.angellira.petvital1.model.Usuario

@Database(entities = [Pet::class, Usuario::class, Petshop::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun petshopDao(): PetshopDao
}