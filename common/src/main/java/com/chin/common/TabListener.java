package com.chin.common;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ActionBar.Tab;

@SuppressLint("NewApi")
public class TabListener<T extends Fragment> implements ActionBar.TabListener {
    private Fragment mFragment;
    private final FragmentActivity mActivity;
    private final String mTag;
    private final Class<T> mClass;
    private final Bundle bundle;
    private final int viewGroupId;

    /** Constructor used each time a new tab is created.
      * @param activity  The host Activity, used to instantiate the fragment
      * @param tag  The identifier tag for the fragment
      * @param clz  The fragment's Class, used to instantiate the fragment
      */
    public TabListener(FragmentActivity activity, String tag, Class<T> clz, Bundle bundle, int viewGroupId) {
        mActivity = activity;
        mTag = tag;
        mClass = clz;
        this.bundle = bundle;
        this.viewGroupId = viewGroupId;
    }

    /* The following are each of the ActionBar.TabListener callbacks */

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // use our own v4 version of FragmentTransaction instead of the one passed in
        FragmentManager fm = mActivity.getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction fft = fm.beginTransaction();

        // previous fragment
        mFragment = fm.findFragmentByTag(mTag);

        // Check if the fragment is already initialized
        if (mFragment == null) {
            // If not, instantiate and add it to the activity
            mFragment = Fragment.instantiate(mActivity, mClass.getName(), bundle);
            fft.add(viewGroupId, mFragment, mTag);
            fft.commit();
        } else {
            // If it exists, simply attach it in order to show it
            fft.attach(mFragment);
            fft.commit();
        }
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        // use our own v4 version of FragmentTransaction instead of the one passed in
        FragmentManager fm = mActivity.getSupportFragmentManager();
        androidx.fragment.app.FragmentTransaction fft = fm.beginTransaction();

        Fragment currentlyShowing = fm.findFragmentByTag(mTag);

        if (currentlyShowing != null) {
            // Detach the fragment, another tab has been selected
            fft.detach(currentlyShowing);
            fft.commit();
        }
        else if (mFragment != null) {
            // Detach the fragment, because another one is being attached
            fft.detach(mFragment);
            fft.commit();
        }
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }
}