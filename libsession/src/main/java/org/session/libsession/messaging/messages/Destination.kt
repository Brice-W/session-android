package org.session.libsession.messaging.messages

sealed class Destination {

    class Contact(val publicKey: String)
    class ClosedGroup(val groupPublicKey: String)
    class OpenGroup(val channel: Long, val server: String)

}