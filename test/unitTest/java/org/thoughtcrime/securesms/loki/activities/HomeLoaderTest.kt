package org.thoughtcrime.securesms.loki.activities

import android.content.Context
import android.database.Cursor
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.thoughtcrime.securesms.database.DatabaseFactory
import org.thoughtcrime.securesms.database.ThreadDatabase

@RunWith(PowerMockRunner::class)
@PrepareForTest(DatabaseFactory::class)
class HomeLoaderTest {

    private lateinit var homeLoader: HomeLoader

    private val context: Context = PowerMockito.mock(Context::class.java)
    private val threadDb: ThreadDatabase = PowerMockito.mock(ThreadDatabase::class.java)
    private val cursor: Cursor = PowerMockito.mock(Cursor::class.java)


    @Before
    fun setUp() {
        PowerMockito.`when`(context.applicationContext).thenReturn(context)

        homeLoader = HomeLoader(context)

        PowerMockito.mockStatic(DatabaseFactory::class.java)
        PowerMockito.`when`(DatabaseFactory.getThreadDatabase(context)).thenReturn(threadDb)
        PowerMockito.`when`(threadDb.conversationList).thenReturn(cursor)
    }

    @Test
    fun getCursor() {
        assertEquals(cursor, homeLoader.cursor)
    }
}