package org.thoughtcrime.securesms.loki.activities

import android.app.Activity
import android.app.Application
import android.content.Context
import android.support.v4.app.FragmentActivity
import android.support.v7.widget.RecyclerView
import android.text.Editable
import android.widget.EditText
import android.widget.Toast
import network.loki.messenger.R
import org.junit.*
import org.junit.Assert.*
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.powermock.api.mockito.PowerMockito
import org.powermock.core.classloader.annotations.PowerMockIgnore
import org.powermock.core.classloader.annotations.PrepareForTest
import org.powermock.modules.junit4.rule.PowerMockRule
import org.robolectric.Robolectric
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.Implementation
import org.robolectric.annotation.Implements
import org.robolectric.annotation.RealObject
import org.robolectric.shadows.ShadowAdapterView
import org.robolectric.shadows.ShadowAppWidgetHostView
import org.robolectric.shadows.ShadowToast
import org.robolectric.shadows.ShadowView
import org.thoughtcrime.securesms.mms.GlideApp
import org.thoughtcrime.securesms.mms.GlideRequests
import java.lang.reflect.Method

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE, application = Application::class)
@PowerMockIgnore("org.mockito.*", "org.robolectric.*", "android.*", "androidx.*" ) //required to use PowerMock with Robolectric
class CreateClosedGroupActivityTest {

    // the Activity
    private lateinit var createClosedGroupActivity: Activity

    // required by PowerMock
    @get:Rule
    var rule: PowerMockRule = PowerMockRule()

    @Before
    fun setUp() {
        //init the Activity
        createClosedGroupActivity = Robolectric.buildActivity(CreateClosedGroupActivity::class.java).create().get()
        assertNotNull(createClosedGroupActivity)

        // need to call setContentView as onCreate of CreateClosedGroupActivity isn't called
        createClosedGroupActivity.setContentView(R.layout.activity_create_closed_group)
    }

    @After
    fun tearDown() {
    }

    @Test
    fun onOptionsItemSelected() {
    }

    @Test
    fun createClosedGroup_emptyName() {
        val expected = "Please enter a group name"

        val createClosedGroupMethod: Method = createClosedGroupActivity.javaClass.getDeclaredMethod("createClosedGroup")
        createClosedGroupMethod.isAccessible = true

        // name is empty
        createClosedGroupActivity.findViewById<EditText>(R.id.nameEditText).text = "".toEditable()

        createClosedGroupMethod.invoke(createClosedGroupActivity)

        assertEquals(expected, ShadowToast.getTextOfLatestToast())
    }

    @Test
    fun createClosedGroup_nameTooLong() {
        val expected = "Please enter a shorter group name"

        val createClosedGroupMethod: Method = createClosedGroupActivity.javaClass.getDeclaredMethod("createClosedGroup")
        createClosedGroupMethod.isAccessible = true

        // name.length > 64
        createClosedGroupActivity.findViewById<EditText>(R.id.nameEditText).text = "12345678901234567890123456789012345678901234567890123456789012345".toEditable()

        createClosedGroupMethod.invoke(createClosedGroupActivity)

        assertEquals(expected, ShadowToast.getTextOfLatestToast())
    }

    @Test
    @PrepareForTest(GlideApp::class)
    fun createClosedGroup_LessThanTwoMembersSelected() {
        val expected = "Please pick at least 2 group members"

        val createClosedGroupMethod: Method = createClosedGroupActivity.javaClass.getDeclaredMethod("createClosedGroup")
        createClosedGroupMethod.isAccessible = true

        // set a right group name
        createClosedGroupActivity.findViewById<EditText>(R.id.nameEditText).text = "test".toEditable()

        // TODO can be removed, will be useful for next test if possible to use whenNew
        // setting up some selected members
        val createClosedGroupAdapter = PowerMockito.spy(CreateClosedGroupAdapter(createClosedGroupActivity))
        createClosedGroupAdapter.selectedMembers.add("test")
        PowerMockito.whenNew(CreateClosedGroupAdapter::class.java).withArguments(createClosedGroupActivity).thenReturn(createClosedGroupAdapter)

        //ShadowCreateClosedGroupAdapter.test()

        //val toast = Toast(createClosedGroupActivity)


        // call to GlideApp.with(...) has to be mocked
        PowerMockito.mockStatic(GlideApp::class.java)
        PowerMockito.`when`(GlideApp.with(Mockito.any<FragmentActivity>())).thenReturn(PowerMockito.mock(GlideRequests::class.java))

        createClosedGroupMethod.invoke(createClosedGroupActivity)

        assertEquals(expected, ShadowToast.getTextOfLatestToast())
    }



    // util function to create Editable objects from String
    fun String.toEditable(): Editable =  Editable.Factory.getInstance().newEditable(this)
}

/*@Implements(CreateClosedGroupAdapter::class)
class ShadowCreateClosedGroupAdapter(context: Context) {

    //@RealObject private lateinit var adapter: CreateClosedGroupAdapter

    private val selectedMembers = mutableSetOf<String>()

    @Implementation
    fun __constructor__(context: Context) {
        onMemberClick("test")
    }

    @Implementation
    fun onMemberClick(member: String) {
        if (selectedMembers.contains(member)) {
            selectedMembers.remove(member)
        } else {
            selectedMembers.add(member)
        }
    }
}*/
