package com.eraldguri.geophysicslab;

import android.os.Bundle;
import android.view.Menu;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initViews();
        startNavigationMenu();
        setupTabLayout();
    }

    /**
     * @brief
     *      Initialize View IDs for each elements of the Views contained in the layout.
     */
    private void initViews() {
        mDrawer = findViewById(R.id.drawer_layout);
        mNavigationView = findViewById(R.id.nav_view);
        mTabLayout = findViewById(R.id.tabLayout);
        mViewPager = findViewById(R.id.view_pager);
    }

    /**
     * @brief
     *      This method allows to navigate between fragments
     *      (EarthquakeListFragment, MapViewFragment, NearYouFragment) using tabs with
     *      swipeable effect.
     * */
    private void setupTabLayout() {
        int FRAGMENT_BEHAVIOUR = FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT;
        TabsAdapter mTabsAdapter = new TabsAdapter(getSupportFragmentManager(), FRAGMENT_BEHAVIOUR);
        mTabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        mViewPager.setAdapter(mTabsAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
    }

    /**
     * @brief
     *      This method allows to start the navigation drawer and to
     *      navigate between the fragments contained in the navigation drawer.
     */
    private void startNavigationMenu() {
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(mDrawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(mNavigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

}