package com.firstadie.csftcarroll.b00641329.firstaide.ui.TimelineActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.SparseArray;
import android.view.ViewGroup;

/**
 * Created by tigh on 17/11/17.
 */

public class AccessibleFragmentStatePagerAdapter extends FragmentStatePagerAdapter {

    private SparseArray<AccessibleFragment> mFragments;

    public AccessibleFragmentStatePagerAdapter(FragmentManager fm) {
        super(fm);
        mFragments = new SparseArray<>();
    }

    public AccessibleFragment getAccessibleFragment(int position) {
        return mFragments.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments != null ? mFragments.size() : 0;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        Object item = super.instantiateItem(container, position);
        if(item instanceof Fragment) {
            mFragments.put(position, (AccessibleFragment) item);
        }

        return item;
    }
}
