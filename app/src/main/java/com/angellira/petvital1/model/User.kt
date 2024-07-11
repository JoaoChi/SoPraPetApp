package com.angellira.petvital1.model

import com.angellira.petvital1.Interfaces.autenticator

@Suppress("UNREACHABLE_CODE")
data class User(
    var username: String = "",
    var email: String = "",
    var password: String = ""
) : autenticator { // Closeable etc. Nomes de interfaces
    override fun authenticate(email: String, password: String): Boolean {
        return if (this.email == email && this.password == password) {
            true
        } else {
            return false
        }
    }
}
