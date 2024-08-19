package com.angellira.petvital1.model

import kotlinx.serialization.Serializable

@Serializable
data class Petshops(
    var id: String,
    var name: String,
    var imagem: String,
    var localizacao: String,
    var descricao: String,
    var servicos: String
)