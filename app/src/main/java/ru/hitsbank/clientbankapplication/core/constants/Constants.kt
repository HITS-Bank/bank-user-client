package ru.hitsbank.clientbankapplication.core.constants

object Constants {

    const val CLIENT_APP_CHANNEL = "CLIENT"

    const val DEEPLINK_APP_SCHEME = "hitsbankapp://"
    const val DEEPLINK_AUTH_HOST = "authorized"

    const val KEYCLOAK_BASE_URL = "http://10.0.2.2:8080/"
    const val AUTH_PAGE_PATH = "realms/bank/protocol/openid-connect/auth"
    const val AUTH_CLIENT_ID = "bank-rest-api"
    const val AUTH_REDIRECT_URI = "$DEEPLINK_APP_SCHEME$DEEPLINK_AUTH_HOST"

    const val GENERAL_ERROR_TEXT = "Что-то пошло не так…"

    const val DEFAULT_PAGE_SIZE = 10
}