package com.angellira.petvital1.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import java.util.UUID

@Serializable
@Entity
data class Petshop(
    @PrimaryKey
    val uid: String = UUID.randomUUID().toString(),
    var name: String,
    var imagem: String,
    var localizacao: String,
    var descricao: String,
    var servicos: String,
    val cnpj: String
    )