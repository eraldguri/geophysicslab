package com.eraldguri.geophysicslab;

import android.os.Build;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.eraldguri.geophysicslab.api.model.Features;
import com.eraldguri.geophysicslab.fragments.EarthquakeListFragment;
import com.eraldguri.geophysicslab.fragments.MapViewFragment;
import com.eraldguri.geophysicslab.fragments.NearYouFragment;

import java.util.List;

@RequiresApi(api = Build.VERSION_CODES.M)
public class TabsAdapter extends FragmentStatePagerAdapter {

    private final Fragment[] childFragments;
    private final String[] fragmentTitles;

    public TabsAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
        childFragments = new Fragment[] {
                new EarthquakeListFragment(),
                new MapViewFragment(),
                new NearYouFragment()
        };
        fragmentTitles = new String[] {
                "Earthquake List",
                "Map View",
                "Near You"
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
