package com.angellira.petvital1.model

import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
data class User(
    val uid: String = UUID.randomUUID().toString(),
    var cpf: String,
    var name: String,
    var email: String,
    var password: String,
    var imagem: String
)