package io.rachaelnelson.single_activity_architecture;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class SimpleToolbarRootFragment extends BaseFrag {

    @Override
    public void setContentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.content_main, parent);
    }

    @Nullable
    @Override
    public BaseActivity.ScreenConfig getScreenConfigs() {
        return new BaseActivity.ScreenConfig()
                .setTitle("Simple Toolbar Frag")
                .setUseFAB(true)
                .setNavigationMode(BaseActivity.ScreenConfig.NAVIGATION_MODE.ROOT);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
//        activity.pinTabs(true);
    }

    @Override
    public void onStop() {
        super.onStop();
//        activity.pinTabs(false);
    }
}
