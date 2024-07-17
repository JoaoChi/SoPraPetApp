package com.angellira.petvital1.interfaces

interface Autenticator {

    fun authenticate(email: String, password: String) : Boolean
}

