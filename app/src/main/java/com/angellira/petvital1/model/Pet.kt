package com.angellira.petvital1.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Pet(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    var name: String,
    var descricao: String,
    var peso: String,
    var idade: String,
    var imagem: String
)