package ru.hitsbank.clientbankapplication.core.data.repository

import kotlinx.coroutines.Dispatchers
import ru.hitsbank.clientbankapplication.core.data.api.ProfileApi
import ru.hitsbank.clientbankapplication.core.data.common.apiCall
import ru.hitsbank.clientbankapplication.core.data.common.toResult
import ru.hitsbank.clientbankapplication.core.data.datasource.SessionManager
import ru.hitsbank.clientbankapplication.core.data.mapper.ProfileMapper
import ru.hitsbank.clientbankapplication.core.domain.common.Result
import ru.hitsbank.clientbankapplication.core.domain.common.map
import ru.hitsbank.clientbankapplication.core.domain.model.ProfileEntity
import ru.hitsbank.clientbankapplication.core.domain.repository.IProfileRepository
import javax.inject.Inject

class ProfileRepository @Inject constructor(
    private val profileApi: ProfileApi,
    private val sessionManager: SessionManager,
    private val mapper: ProfileMapper,
) : IProfileRepository {

    override suspend fun getSelfProfile(): Result<ProfileEntity> {
        return apiCall(Dispatchers.IO) {
            profileApi.getSelfProfile()
                .toResult()
                .also { result ->
                    if (result is Result.Success) {
                        sessionManager.saveIsUserBlocked(result.data.isBanned)
                    }
                }
                .map(mapper::map)
        }
    }
}