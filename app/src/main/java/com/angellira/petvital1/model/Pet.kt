package com.angellira.petvital1.model

import kotlinx.serialization.Serializable

@Serializable
data class Pet(
    var id: String,
    var name: String,
    var descricao: String,
    var peso: String,
    var idade: String,
    var imagem: String
)