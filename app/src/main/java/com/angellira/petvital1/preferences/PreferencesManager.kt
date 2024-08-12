package com.angellira.petvital1.preferences

import android.content.Context

const val preferenciaCadastro = "cadastro"

class PreferencesManager (context: Context){
    private val sharedPreferences =
        context.getSharedPreferences(preferenciaCadastro, Context.MODE_PRIVATE)

    companion object {
        private const val ID_USUARIO = "Id"
        private const val IS_LOGGED = "logou"
    }
    var userId: String?
        get() = sharedPreferences.getString(ID_USUARIO, null)
        set(value) = sharedPreferences.edit().putString(ID_USUARIO, value).apply()

    var estaLogado: Boolean
        get() = sharedPreferences.getBoolean(IS_LOGGED, false)
        set(value) = sharedPreferences.edit().putBoolean(IS_LOGGED, value).apply()

    fun logout() {

    }

}