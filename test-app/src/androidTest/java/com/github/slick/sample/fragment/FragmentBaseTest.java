package com.github.slick.sample.fragment;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.test.filters.LargeTest;
import android.support.test.runner.AndroidJUnit4;

import com.github.slick.sample.MainActivity;
import com.github.slick.sample.R;
import com.github.slick.sample.activity.ActivityBaseTest;
import com.github.slick.sample.activity.ViewTestable;

import org.junit.Before;
import org.junit.runner.RunWith;

/**
 * @author : Pedramrn@gmail.com
 *         Created on: 2018-03-10
 */
@LargeTest
@RunWith(AndroidJUnit4.class)
public abstract class FragmentBaseTest extends ActivityBaseTest {
    @Before
    public void setUp() throws Exception {
        MainActivity activity = testRule.getActivity();
        activity.startActivity(new Intent(activity, ActivityFragmentHost.class));
        ActivityFragmentHost currentActivity = (ActivityFragmentHost) getCurrentActivity();
        assert currentActivity != null;
        Fragment fragment = createFragment();
        currentActivity.getFragmentManager().beginTransaction()
                .replace(R.id.fragment, fragment)
                .addToBackStack(null)
                .commit();
        view = (ViewTestable) fragment;
    }

    @NonNull
    protected abstract Fragment createFragment();
}
