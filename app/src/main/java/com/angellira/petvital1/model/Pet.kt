package com.angellira.petvital1.model

import kotlinx.serialization.Serializable

@Serializable
data class Pet(
    var id: String,
    var name: String,
    var tipo: String,
    var peso: Int,
    var idade: Int,
    var imagem: String
)