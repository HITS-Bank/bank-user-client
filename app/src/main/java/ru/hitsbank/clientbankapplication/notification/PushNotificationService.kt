package ru.hitsbank.clientbankapplication.notification

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import ru.hitsbank.bank_common.domain.State
import ru.hitsbank.bank_common.domain.interactor.PushNotificationInteractor
import ru.hitsbank.bank_common.domain.model.RegisterFcmRequest
import javax.inject.Inject

@AndroidEntryPoint
class PushNotificationService : FirebaseMessagingService() {

    @Inject
    lateinit var notificationInteractor: PushNotificationInteractor

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        scope.launch {
            val request = RegisterFcmRequest(token)
            notificationInteractor.registerFcmToken(request).collectLatest { state ->
                when (state) {
                    State.Loading -> Unit
                    is State.Error -> Log.e(TAG, "error registering token: $token")
                    is State.Success -> Log.d(TAG, "successfully registered token: $token")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        scope.cancel()
    }

    private companion object {
        const val TAG = "PushNotificationService"
    }
}