package io.rachaelnelson.single_activity_architecture;

import android.os.Bundle;
import android.support.annotation.CallSuper;
import android.support.annotation.MenuRes;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public abstract class TabbedFrag extends BaseFrag {
    private Screen[] screens;
    private ViewPager viewPager;

    public abstract class Screen {
        public abstract String getTitle();

        public abstract Fragment getFragment();

        @MenuRes
        public int getMenu() {
            return -1;
        }

        public Toolbar.OnMenuItemClickListener getMenuItemClickListener() {
            return null;
        }

        public void selected() {
        }
    }

    public abstract Screen[] getScreens();

    public abstract void setTabbedContentView(LayoutInflater inflater, View parent, Bundle savedInstanceState);

    @Override
    public void setContentView(LayoutInflater inflater, ViewGroup content, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.frag_tabbed, content);
        viewPager = rootView.findViewById(R.id.content_viewpager);

        screens = getScreens();

        getViewPager().setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return screens[position].getFragment();
            }

            @Override
            public int getCount() {
                return screens.length;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return screens[position].getTitle();
            }
        });

        getViewPager().addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                //// TODO: 8/22/17 this is an issue. call the callback method for bottom nav scrolling
                onTabPageScrolled(position);
                ViewPropertyAnimatorCompat mOffsetValueAnimator = ViewCompat.animate(activity.getBottomNav());
                mOffsetValueAnimator.setDuration(100);
                mOffsetValueAnimator.setInterpolator(new LinearOutSlowInInterpolator());
                mOffsetValueAnimator.translationY(0).start();


            }

            @Override
            public void onPageSelected(int position) {
//                configureToolbar();
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        int currentItem = getInitialScreenIndex();
        if (savedInstanceState != null) {
            currentItem = savedInstanceState.getInt("current_item");
        }

        setTabbedContentView(inflater, rootView, savedInstanceState);
    }

    @Nullable
    @Override
    public BaseActivity.ScreenConfig getScreenConfigs() {
        return new BaseActivity.ScreenConfig()
                .setNavigationMode(BaseActivity.ScreenConfig.NAVIGATION_MODE.ROOT)
                .setUseFAB(true)
                .setPinTabs(true)
                .setPinToolbar(true)
                .setViewPagerForTabs(R.id.content_viewpager);
    }

    public int getInitialScreenIndex() {
        return 0;
    }

    @Override
    @CallSuper
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("current_item", getViewPager().getCurrentItem());
    }

    private void configureToolbar() {
        Screen screen = screens[getViewPager().getCurrentItem()];

    }

    public void onTabPageScrolled(int position) {
    }

    public ViewPager getViewPager() {
        return viewPager;
    }


}
