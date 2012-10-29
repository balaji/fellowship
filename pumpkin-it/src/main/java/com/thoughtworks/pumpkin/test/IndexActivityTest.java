package com.thoughtworks.pumpkin.test;

import android.test.ActivityInstrumentationTestCase2;
import com.thoughtworks.pumpkin.IndexActivity;

public class IndexActivityTest extends ActivityInstrumentationTestCase2<IndexActivity> {

    public IndexActivityTest() {
        super(IndexActivity.class);
    }

    public void testActivity() {
        IndexActivity activity = getActivity();
        assertNotNull(activity);
    }
}

