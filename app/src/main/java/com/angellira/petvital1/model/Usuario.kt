package com.angellira.petvital1.model

import kotlinx.serialization.Serializable

@Serializable
data class Usuario(
    var id: String,
    var name: String,
    var email: String,
    var password: String,
    var imagem: String
)