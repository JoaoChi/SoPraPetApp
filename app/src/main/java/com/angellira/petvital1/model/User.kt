package com.angellira.petvital1.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: String? = null,
    var cpf: String = "",
    var name: String,
    var email: String,
    var password: String,
    var imagem: String = ""
)