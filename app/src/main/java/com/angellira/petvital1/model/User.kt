package com.angellira.petvital1.model

import com.angellira.petvital1.Interfaces.autenticator

@Suppress("UNREACHABLE_CODE")
data class User(
    private val email: String = "",
    private val password: String = ""
) : autenticator { // Closeable etc. Nomes de interfaces
    override fun emailinserido(email: String): Boolean {
        TODO("Not yet implemented")
        // colocar comportamento
        if (this.email == email) {
            println("Email correto!")
        } else {
            println("Email incorreto!")
        }
    }

    override fun resetPassword(newPassword: String) {
        TODO("Not yet implemented")
    }

    override fun authenticate(password: String): Boolean {
        TODO("Not yet implemented")
    }
}