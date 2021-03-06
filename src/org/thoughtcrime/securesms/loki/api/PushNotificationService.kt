package org.thoughtcrime.securesms.loki.api

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import org.thoughtcrime.securesms.jobs.PushContentReceiveJob
import org.thoughtcrime.securesms.util.TextSecurePreferences
import org.whispersystems.libsignal.logging.Log
import org.whispersystems.signalservice.api.messages.SignalServiceEnvelope
import org.whispersystems.signalservice.internal.util.Base64
import org.whispersystems.signalservice.loki.api.MessageWrapper

class PushNotificationService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d("Loki", "New FCM token: $token.")
        val userHexEncodedPublicKey = TextSecurePreferences.getLocalNumber(this) ?: return
        LokiPushNotificationManager.register(token, userHexEncodedPublicKey, this, false)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        val base64EncodedData = message.data["ENCRYPTED_DATA"]
        val data = base64EncodedData?.let { Base64.decode(it) }
        if (data != null) {
            try {
                val envelope = MessageWrapper.unwrap(data)
                PushContentReceiveJob(this).processEnvelope(SignalServiceEnvelope(envelope), true)
            } catch (e: Exception) {
                Log.d("Loki", "Failed to unwrap data for message.")
            }
        } else {
            Log.d("Loki", "Failed to decode data for message.")
        }
    }
}