package io.rachaelnelson.single_activity_architecture;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class CustomToolbarContentFrag extends BaseFrag {

    @Override
    public void setContentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.frag_custom_toolbar_content, parent);
        contentView.findViewById(R.id.fragment_launch_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.getNavigator().navigateTo(new NonRootFragment(), true);
            }
        });
    }

    @Nullable
    @Override
    public BaseActivity.ScreenConfig getScreenConfigs() {
        return new BaseActivity.ScreenConfig()
                .setNavigationMode(BaseActivity.ScreenConfig.NAVIGATION_MODE.ROOT)
                .setCustomCollaspingToolbarContent(R.layout.example_custom_collapsing_toolbar_content)
                .setTitle("Custom Toolbar Content Frag");
    }
}
