package com.angellira.petvital1.preferences

import android.content.Context

const val preferenciaCadastro = "cadastro"

class PreferencesManager (context: Context){
    private val sharedPreferences =
        context.getSharedPreferences(preferenciaCadastro, Context.MODE_PRIVATE)

    companion object {
        private const val ID_USUARIO = "Id"
        private const val IS_LOGGED = "logou"
        private const val ID_IMAGE = "ImageUser"
        private const val ID_IMAGEPET = "ImagePet"
        private const val ID_IMAGEPETSHOP = "ImagePetshop"

    }
    var petshopImage: String?
        get() = sharedPreferences.getString(ID_IMAGEPETSHOP, null)
        set(value) = sharedPreferences.edit().putString(ID_IMAGEPETSHOP, value).apply()

    var petImage: String?
        get() = sharedPreferences.getString(ID_IMAGEPET, null)
        set(value) = sharedPreferences.edit().putString(ID_IMAGEPET, value).apply()

    var userImage: String?
        get() = sharedPreferences.getString(ID_IMAGE, null)
        set(value) = sharedPreferences.edit().putString(ID_IMAGE, value).apply()

    var userId: String?
        get() = sharedPreferences.getString(ID_USUARIO, null)
        set(value) = sharedPreferences.edit().putString(ID_USUARIO, value).apply()

    var estaLogado: Boolean
        get() = sharedPreferences.getBoolean(IS_LOGGED, false)
        set(value) = sharedPreferences.edit().putBoolean(IS_LOGGED, value).apply()

    fun logout() {
        sharedPreferences.edit().putBoolean(preferenciaCadastro, false).clear().apply()
    }

}