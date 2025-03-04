package ru.hitsbank.clientbankapplication.core.domain.repository

import ru.hitsbank.clientbankapplication.core.domain.common.Result
import ru.hitsbank.clientbankapplication.core.domain.model.ProfileEntity

interface IProfileRepository {

    suspend fun getSelfProfile(): Result<ProfileEntity>
}