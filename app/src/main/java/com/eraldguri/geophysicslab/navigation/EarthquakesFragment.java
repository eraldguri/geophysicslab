package com.eraldguri.geophysicslab.navigation;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.eraldguri.geophysicslab.R;
import com.eraldguri.geophysicslab.adapter.TabsAdapter;
import com.google.android.material.tabs.TabLayout;

@RequiresApi(api = Build.VERSION_CODES.M)
public class EarthquakesFragment extends Fragment{

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_earthquakes, container, false);


        initViews(root);

        setupTabLayout();

        return root;
    }

    private void initViews(View root) {
        mTabLayout = root.findViewById(R.id.tab_layout);
        mViewPager = root.findViewById(R.id.view_pager);
    }

    /**
     * @brief
     *      This method allows to navigate between fragments
     *      (EarthquakeListFragment, MapViewFragment, NearYouFragment) using tabs with
     *      swipeable effect.
     * */
    private void setupTabLayout() {
        int FRAGMENT_BEHAVIOUR = FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
        TabsAdapter mTabsAdapter = new TabsAdapter(getChildFragmentManager(), FRAGMENT_BEHAVIOUR);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }


}