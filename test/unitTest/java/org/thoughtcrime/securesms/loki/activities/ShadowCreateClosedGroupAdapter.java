package org.thoughtcrime.securesms.loki.activities;

import android.content.Context;
import android.widget.Toast;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings({"UnusedDeclaration"})
@Implements(CreateClosedGroupAdapter.class)
public class ShadowCreateClosedGroupAdapter {

    private Set<String> selectedMembers = new HashSet<String>();

    @RealObject
    ShadowCreateClosedGroupAdapter shadowCreateClosedGroupAdapter;

    @Implementation
    public void __constructor__(Context context) {}

    @Implementation
    public void onMemberClick(String member) {
        if (selectedMembers.contains(member)) {
            selectedMembers.remove(member);
        } else {
            selectedMembers.add(member);
        }
    }

    public static void test() {
        System.out.println("test2");
    }
}
