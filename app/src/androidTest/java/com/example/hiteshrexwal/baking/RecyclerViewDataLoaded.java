package com.example.hiteshrexwal.baking;

import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.contrib.RecyclerViewActions;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.example.hiteshrexwal.baking.Activity.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class RecyclerViewDataLoaded {
    private IdlingResource idlingResource;

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(
            MainActivity.class);

    @Before
    public void registerIdlingResource() {
        idlingResource = mActivityRule.getActivity().getIdlingResource();
        // To prove that the test fails, omit this call:
        IdlingRegistry.getInstance().register(idlingResource);

    }
    @Test
    public void checkTestInMainActivity() throws InterruptedException {
        onView(ViewMatchers.withId(R.id.recipe_recyclerview)).perform(RecyclerViewActions.scrollToPosition(3));
        onView(withText("Cheesecake")).check(matches(isDisplayed()));
    }
    @Test
    public void checkRecipeDetailActivityExist() {
        onView(ViewMatchers.withId(R.id.recipe_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(ViewMatchers.withId(R.id.steps_recyclerview)).check(matches(isDisplayed()));
    }

    @Test
    public void checkVideoPlayerExist() {
        onView(ViewMatchers.withId(R.id.recipe_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(ViewMatchers.withId(R.id.steps_recyclerview)).perform(RecyclerViewActions.actionOnItemAtPosition(0,click()));
        onView(ViewMatchers.withId(R.id.video_player)).check(matches(isDisplayed()));
    }
    @After
    public void unregisterIdlingResource() {
        if (idlingResource != null) {
             IdlingRegistry.getInstance().unregister(idlingResource);
        }
    }
}
