package org.thoughtcrime.securesms.loki.activities

import android.app.Activity
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.view.ViewGroup
import network.loki.messenger.R
import org.junit.Assert
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.PowerMockRunner
import org.powermock.modules.junit4.rule.PowerMockRule
import org.robolectric.Robolectric
import org.thoughtcrime.securesms.database.Address
import org.thoughtcrime.securesms.loki.views.UserView
import org.thoughtcrime.securesms.mms.GlideApp
import org.thoughtcrime.securesms.mms.GlideRequests
import org.thoughtcrime.securesms.recipients.Recipient

@RunWith(PowerMockRunner::class)
@PrepareForTest(CreateClosedGroupAdapter::class, Recipient::class)
class CreateClosedGroupAdapterTest {

    // the CreateClosedGroupAdapter we will use for tests
    private lateinit var createClosedGroupAdapter: CreateClosedGroupAdapter
    var context: Context = Mockito.mock(Context::class.java)

    @Before
    fun setUp() {
        //init the Adapter (as spy to be able to run its methods)
        createClosedGroupAdapter = Mockito.spy(CreateClosedGroupAdapter(context))
    }

    @Test
    fun getItemCount() {
        // no items
        Assert.assertEquals(0, createClosedGroupAdapter.itemCount)

        // we don't want to call notifyDataSetChanged when setting createClosedGroupAdapter.members
        Mockito.doNothing().`when`(createClosedGroupAdapter).notifyDataSetChanged()

        // 1 item
        val member = "test"
        createClosedGroupAdapter.members = listOf(member)
        Assert.assertEquals(1, createClosedGroupAdapter.itemCount)

        val member2 = "test2"
        createClosedGroupAdapter.members = listOf(member, member2)
        Assert.assertEquals(2, createClosedGroupAdapter.itemCount)
    }

    @Test
    fun onCreateViewHolder() {
        val userView = Mockito.mock(UserView::class.java)
        PowerMockito.whenNew(UserView::class.java).withArguments(context).thenReturn(userView)

        val result = createClosedGroupAdapter.onCreateViewHolder(Mockito.mock(ViewGroup::class.java), 0)

        PowerMockito.verifyNew(UserView::class.java).withArguments(context)

        Assert.assertTrue(result is CreateClosedGroupAdapter.ViewHolder)
        Assert.assertEquals(userView, result.view)
    }

    @Test
    fun onBindViewHolder() {
        val glideRequests = Mockito.mock(GlideRequests::class.java)
        createClosedGroupAdapter.glide = glideRequests

        // we don't want to call notifyDataSetChanged when setting createClosedGroupAdapter.members
        Mockito.doNothing().`when`(createClosedGroupAdapter).notifyDataSetChanged()

        val view = Mockito.mock(UserView::class.java)
        var viewHolder = CreateClosedGroupAdapter.ViewHolder(view)
        val member = "test"
        createClosedGroupAdapter.selectedMembers.add(member)
        createClosedGroupAdapter.members = listOf(member)

        val recipient = Mockito.mock(Recipient::class.java)
        PowerMockito.mockStatic(Recipient::class.java)
        PowerMockito.`when`(Recipient.from(context, Address.fromSerialized(member), false)).thenReturn(recipient)

        Mockito.doNothing().`when`(view).bind(recipient, true, glideRequests)

        createClosedGroupAdapter.onBindViewHolder(viewHolder, 0)

        Mockito.verify(view).bind(recipient, true, glideRequests)
    }

    @Test
    fun onMemberClick() {
        // we don't want to call notifyDataSetChanged
        Mockito.doNothing().`when`(createClosedGroupAdapter).notifyItemChanged(Mockito.anyInt())

        val member1 = "test"
        val member2 = "Test2"

        // add a member
        createClosedGroupAdapter.onMemberClick(member1)
        Assert.assertTrue(createClosedGroupAdapter.selectedMembers.contains(member1))
        Assert.assertEquals(1, createClosedGroupAdapter.selectedMembers.size)

        // add a second member
        createClosedGroupAdapter.onMemberClick(member2)
        Assert.assertTrue(createClosedGroupAdapter.selectedMembers.contains(member2))
        Assert.assertEquals(2, createClosedGroupAdapter.selectedMembers.size)

        // remove a member
        createClosedGroupAdapter.onMemberClick(member1)
        Assert.assertFalse(createClosedGroupAdapter.selectedMembers.contains(member1))
        Assert.assertEquals(1, createClosedGroupAdapter.selectedMembers.size)

        // check that notifyItemChanged is called 3 times
        Mockito.verify(createClosedGroupAdapter, Mockito.times(3)).notifyItemChanged(-1)
    }
}