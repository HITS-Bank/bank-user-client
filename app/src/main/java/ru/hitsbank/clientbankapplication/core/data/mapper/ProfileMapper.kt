package ru.hitsbank.clientbankapplication.core.data.mapper

import ru.hitsbank.clientbankapplication.core.data.model.ProfileResponse
import ru.hitsbank.clientbankapplication.core.domain.model.ProfileEntity
import javax.inject.Inject

class ProfileMapper @Inject constructor() {

    fun map(response: ProfileResponse): ProfileEntity {
        with (response) {
            return ProfileEntity(
                id = id,
                firstName = firstName,
                lastName = lastName,
                isBanned = isBanned,
                email = email,
                role = role,
            )
        }
    }
}