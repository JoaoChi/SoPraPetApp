package com.angellira.petvital1.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity
data class Pet(
    @PrimaryKey
    val id: String = UUID.randomUUID().toString(),
    var name: String,
    var descricao: String,
    var peso: String,
    var idade: String,
    var imagem: String
)