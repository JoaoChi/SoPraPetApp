package com.angellira.petvital1.preferences

import android.content.Context

const val preferenciaCadastro = "cadastro"

class PreferencesManager (context: Context){
    private val sharedPreferences =
        context.getSharedPreferences(preferenciaCadastro, Context.MODE_PRIVATE)

    companion object {
        private const val KEY_GMAIL = "gmail"
        private const val KEY_PASSWORD = "senha"
        private const val KEY_USERNAME = "nome"
        private const val KEY_AUTHENTICATED = "autenticado"
    }

    var email: String?
        get() = sharedPreferences.getString(KEY_GMAIL, null)
        set(value) = sharedPreferences.edit().putString(KEY_GMAIL, value).apply()

    var password: String?
        get() = sharedPreferences.getString(KEY_PASSWORD, null)
        set(value) = sharedPreferences.edit().putString(KEY_PASSWORD, value).apply()

    var username: String?
        get() = sharedPreferences.getString(KEY_USERNAME, null)
        set(value) = sharedPreferences.edit().putString(KEY_USERNAME, value).apply()

    var estaLogado: Boolean
        get() = sharedPreferences.getBoolean("logou", false)
        set(value) {
            sharedPreferences.edit().putBoolean("logou", value).apply()
        }

    var isAuthenticated: Boolean
        get() = sharedPreferences.getBoolean(KEY_AUTHENTICATED, false)
        set(value) = sharedPreferences.edit().putBoolean(KEY_AUTHENTICATED, value).apply()

    fun logout() {
        sharedPreferences.edit().putBoolean(KEY_AUTHENTICATED, false).clear().apply()
        sharedPreferences.edit().putBoolean(KEY_GMAIL, false).clear().apply()
        sharedPreferences.edit().putBoolean(KEY_PASSWORD, false).clear().apply()
        sharedPreferences.edit().putBoolean(KEY_USERNAME, false).clear().apply()
    }

}