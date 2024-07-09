package com.angellira.petvital1.Interfaces

interface autenticator {

    val isLocked: Boolean get() = false
    fun emailinserido(email: String) : Boolean
    fun authenticate(password: String) : Boolean
    fun resetPassword(newPassword: String)
}
