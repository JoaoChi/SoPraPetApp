package com.angellira.petvital1.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    var cpf: String,
    var name: String,
    var email: String,
    var password: String,
    var imagem: String
)