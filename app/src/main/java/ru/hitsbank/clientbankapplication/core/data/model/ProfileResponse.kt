package ru.hitsbank.clientbankapplication.core.data.model

import ru.hitsbank.clientbankapplication.core.domain.model.RoleType

data class ProfileResponse(
    val id: String,
    val firstName: String,
    val lastName: String,
    val isBanned: Boolean,
    val email: String,
    val role: RoleType,
)
