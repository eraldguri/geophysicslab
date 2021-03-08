package com.eraldguri.geophysicslab.adapter;

import android.os.Build;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.eraldguri.geophysicslab.fragments.tabs.EarthquakeListFragment;
import com.eraldguri.geophysicslab.fragments.tabs.MapViewFragment;
import com.eraldguri.geophysicslab.fragments.tabs.SignificantEarthquakesFragment;

@RequiresApi(api = Build.VERSION_CODES.M)
public class TabsAdapter extends FragmentStatePagerAdapter {

    private final Fragment[] childFragments;
    private final String[] fragmentTitles;

    public TabsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        childFragments = new Fragment[] {
                new EarthquakeListFragment(),
                new MapViewFragment(),
                new SignificantEarthquakesFragment()
        };
        fragmentTitles = new String[] {
                "Earthquake List",
                "Map View",
                "Significant Earthquakes"
        };

    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return childFragments[position];
    }

    @Override
    public int getCount() {
        return childFragments.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentTitles[position];
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {

    }

}
