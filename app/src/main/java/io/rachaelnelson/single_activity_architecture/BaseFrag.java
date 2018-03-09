package io.rachaelnelson.single_activity_architecture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.rachaelnelson.single_activity_architecture.BaseActivity.ScreenConfig;


public abstract class BaseFrag extends Fragment {

    public BaseActivity activity;
    private ViewGroup content;

    //modify UI elements here
    public abstract void setContentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState);

    @Nullable
    public abstract ScreenConfig getScreenConfigs();

    // For non-graphical intializations.
    // Can be called when activity's OnCreate is not finished. Accessing view hierarchy here may cause a crash if inflated views are not attached
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.frag_base, container, false);

        activity = (BaseActivity) getActivity();
        content = rootView.findViewById(R.id.content);

        setContentView(inflater, content, savedInstanceState);

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateScreenConfigs();
    }

    public void updateScreenConfigs() {
        ScreenConfig config = getScreenConfigs();

        if (config != null) {

            activity.setNavigationMode(config.getNavigationMode());

            if (config.getTitleLogo() > 0) {
                activity.getToolbar().setTitle(null);
                activity.getToolbarLogoContainer().setImageDrawable(ContextCompat.getDrawable(getContext(), config.getTitleLogo()));
                activity.getToolbarLogoContainer().setVisibility(View.VISIBLE);
            } else if (config.getTitleLogo() <= 0) {
                activity.getToolbar().setTitle(config.getTitle());
                activity.getToolbarLogoContainer().setVisibility(View.GONE);
            }

            activity.setMenu(config.getMenuResId());
            activity.setMenuItemClickListener(config.getMenuItemClickListener());
            activity.setupTabs(config.getViewPagerForTabs());
            activity.setPinTabs(config.getPinTabs());
            activity.setPinToolbar(config.getPinToolbar());
            activity.showFloatingActionButton(config.getUsingFAB());
            activity.setSearchConfig(config.getSearchBarConfig());
            activity.setFABClickListener(config.getFabClickListener());
            activity.addCustomLayoutToCollapsingToolbar(config.getCustomToolbarContentLayoutID());

        } else {
            activity.setNavigationMode(ScreenConfig.NAVIGATION_MODE.NONE);
        }
        activity.getAppBar().setElevation(getResources().getDimension(R.dimen.toolbar_elevation));

    }

    public boolean isOnBackpressOverridden() {
        return activity.getNavigationMode().equals(ScreenConfig.NAVIGATION_MODE.LOCK);
    }
}
