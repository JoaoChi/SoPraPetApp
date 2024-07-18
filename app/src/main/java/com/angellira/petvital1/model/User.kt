package com.angellira.petvital1.model

import com.angellira.petvital1.preferences.Autenticator

data class User(
    var username: String = "",
    var email: String = "",
    var password: String = ""
) : Autenticator { // Closeable etc. Nomes de interfaces
    override fun authenticate(email: String, password: String): Boolean {
        return if (this.email == email && this.password == password
            && email.isNotEmpty() && password.isNotEmpty()) {
            true
        } else {
            return false
        }
    }
}
