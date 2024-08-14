package com.angellira.petvital1.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity
data class Petshop(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    var name: String,
    var imagem: String,
    var localizacao: String,
    var descricao: String,
    var servicos: String
)