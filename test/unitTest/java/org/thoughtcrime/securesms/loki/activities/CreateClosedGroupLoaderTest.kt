package org.thoughtcrime.securesms.loki.activities

import android.content.Context
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.thoughtcrime.securesms.database.Address
import org.thoughtcrime.securesms.loki.utilities.Contact
import org.thoughtcrime.securesms.loki.utilities.ContactUtilities
import org.thoughtcrime.securesms.recipients.Recipient

@RunWith(PowerMockRunner::class)
@PrepareForTest(ContactUtilities::class)
class CreateClosedGroupLoaderTest {

    // the CreateClosedGroupAdapter we will use for tests
    private lateinit var createClosedGroupLoader: CreateClosedGroupLoader
    var context: Context = Mockito.mock(Context::class.java)

    @Before
    fun setUp() {
        //init the CreateClosedGroupLoader (as spy to be able to run its methods)
        createClosedGroupLoader = Mockito.spy(CreateClosedGroupLoader(context))
        // this is necessary to avoid a 'context must not be null' error
        Mockito.`when`(createClosedGroupLoader.context).thenReturn(context)
    }

    @Test
    fun loadInBackground() {
        val phoneString1 = "address1"
        val phoneString2 = "address2"
        val phoneString3 = "address3"
        val phoneString4 = "address4"
        val phoneString5 = "address5"
        // create contact 1
        val recipient1 = Mockito.mock(Recipient::class.java)
        Mockito.`when`(recipient1.isGroupRecipient).thenReturn(false)
        val address1 = Mockito.mock(Address::class.java)
        Mockito.`when`(address1.toPhoneString()).thenReturn(phoneString1)
        Mockito.`when`(recipient1.address).thenReturn(address1)
        val contact1 = Contact(recipient1,  false, false)

        // create contact 2 (isSlave = true)
        val recipient2 = Mockito.mock(Recipient::class.java)
        Mockito.`when`(recipient2.isGroupRecipient).thenReturn(false)
        val address2 = Mockito.mock(Address::class.java)
        Mockito.`when`(address2.toPhoneString()).thenReturn(phoneString2)
        Mockito.`when`(recipient2.address).thenReturn(address2)
        val contact2 = Contact(recipient2, true, false)

        // create contact 3 (isOurDevice = true)
        val recipient3 = Mockito.mock(Recipient::class.java)
        Mockito.`when`(recipient3.isGroupRecipient).thenReturn(false)
        val address3 = Mockito.mock(Address::class.java)
        Mockito.`when`(address3.toPhoneString()).thenReturn(phoneString3)
        Mockito.`when`(recipient3.address).thenReturn(address3)
        val contact3 = Contact(recipient3,  false, true)

        // create contact 4 (recipient.isGroupRecipient = true)
        val recipient4 = Mockito.mock(Recipient::class.java)
        Mockito.`when`(recipient4.isGroupRecipient).thenReturn(true)
        val address4 = Mockito.mock(Address::class.java)
        Mockito.`when`(address4.toPhoneString()).thenReturn(phoneString4)
        Mockito.`when`(recipient4.address).thenReturn(address4)
        val contact4 = Contact(recipient4,  false, false)

        // create contact 5
        val recipient5 = Mockito.mock(Recipient::class.java)
        Mockito.`when`(recipient5.isGroupRecipient).thenReturn(false)
        val address5 = Mockito.mock(Address::class.java)
        Mockito.`when`(address5.toPhoneString()).thenReturn(phoneString5)
        Mockito.`when`(recipient5.address).thenReturn(address5)
        val contact5 = Contact(recipient5,  false, false)

        val contacts = setOf<Contact>(contact1, contact2, contact3, contact4, contact5)

        PowerMockito.mockStatic(ContactUtilities::class.java)
        PowerMockito.`when`(ContactUtilities.getAllContacts(context)).thenReturn(contacts)

        val result = createClosedGroupLoader.loadInBackground()

        assertEquals(2, result.size)
        assertTrue(result.contains(phoneString1))
        assertFalse(result.contains(phoneString2))
        assertFalse(result.contains(phoneString3))
        assertFalse(result.contains(phoneString4))
        assertTrue(result.contains(phoneString5))
    }

    //TEMP code below is to use instead of Mockito.any()
    private fun <T> any(): T {
        Mockito.any<T>()
        return uninitialized()
    }
    private fun <T> uninitialized(): T = null as T
}