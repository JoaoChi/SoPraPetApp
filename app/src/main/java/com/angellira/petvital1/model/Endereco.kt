package com.angellira.petvital1.model

import kotlinx.serialization.Serializable

@Serializable
data class Endereco(
    var cidade: String,
    var rua: String,
    var numero: Int,
    var referencia: String
)