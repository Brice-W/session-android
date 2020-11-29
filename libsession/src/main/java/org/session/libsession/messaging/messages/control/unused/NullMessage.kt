package org.session.libsession.messaging.messages.control.unused

import org.session.libsession.messaging.messages.control.ControlMessage
import org.session.libsession.messaging.messages.control.ExpirationTimerUpdate
import org.session.libsignal.service.internal.push.SignalServiceProtos

class NullMessage() : ControlMessage() {


    companion object {
        fun fromProto(proto: SignalServiceProtos.Content): NullMessage? {
            TODO("Not yet implemented")
        }
    }

    override fun toProto(): SignalServiceProtos.Content? {
        TODO("Not yet implemented")
    }
}