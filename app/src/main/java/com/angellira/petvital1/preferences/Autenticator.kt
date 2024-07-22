package com.angellira.petvital1.preferences

interface Autenticator {

    fun authenticate(email: String, password: String) : Boolean
}

