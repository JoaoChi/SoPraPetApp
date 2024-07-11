package com.angellira.petvital1.Interfaces

interface autenticator {

    val isLocked: Boolean get() = false
    fun authenticate(email: String, password: String) : Boolean
}

