package io.rachaelnelson.single_activity_architecture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

public class NonRootFragment extends BaseFrag {
    @Override
    public void setContentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        inflater.inflate(R.layout.content_main, parent);
    }

    @Nullable
    @Override
    public BaseActivity.ScreenConfig getScreenConfigs() {
        return new BaseActivity.ScreenConfig()
                .setTitle("Non-Root Frag")
                .setNavigationMode(BaseActivity.ScreenConfig.NAVIGATION_MODE.BACK)
                .setPinToolbar(true);
    }
}
