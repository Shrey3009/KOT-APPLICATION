package com.alfredposclient.activity;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import com.alfredbase.global.BugseeHelper;
import com.alfredposclient.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasDescendant;
import static android.support.test.espresso.matcher.ViewMatchers.isAssignableFrom;
import static android.support.test.espresso.matcher.ViewMatchers.isDescendantOfA;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Created by Arif S. on 6/24/19
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class BtnRecorderOpenRestaurantTest {
    @Rule
    public ActivityTestRule<OpenRestaruant> mOpenRestaurantActivityTestRule =
            new ActivityTestRule<>(OpenRestaruant.class);
    @Rule
    public ActivityTestRule<MainPage> mMainPageActivityTestRule =
            new ActivityTestRule<>(MainPage.class);

    @Before
    public void init() {

    }

    @Test
    public void btnRecorder_bugseeTest() {
        onView(withId(R.id.iv_setting)).perform(click());
//        BugseeHelper.reportBugsee();
    }
}
