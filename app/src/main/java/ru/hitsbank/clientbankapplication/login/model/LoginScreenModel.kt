package ru.hitsbank.clientbankapplication.login.model

data class LoginScreenModel(
    val email: String,
    val password: String,
    val isLoading: Boolean = false,
) {

    companion object {
        val EMPTY = LoginScreenModel(
            email = "",
            password = "",
        )
    }
}
