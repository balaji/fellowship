package com.thoughtworks.pumpkin.test;

import android.test.ActivityInstrumentationTestCase2;
import com.thoughtworks.pumpkin.SigninActivity;

public class IndexActivityTest extends ActivityInstrumentationTestCase2<SigninActivity> {

    public IndexActivityTest() {
        super(SigninActivity.class);
    }

    public void testActivity() {
        SigninActivity activity = getActivity();
        assertNotNull(activity);
    }
}

