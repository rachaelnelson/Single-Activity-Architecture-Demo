package io.rachaelnelson.single_activity_architecture;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

public class SearchToolbarRootFragment extends BaseFrag {

    @Override
    public void setContentView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.content_main, parent);
    }

    @Nullable
    @Override
    public BaseActivity.ScreenConfig getScreenConfigs() {
        return new BaseActivity.ScreenConfig()
                .setNavigationMode(BaseActivity.ScreenConfig.NAVIGATION_MODE.ROOT)
                .setUseFAB(true)
                .setTitle("Search Toolbar Frag")
                .setSearchBarConfig(new SearchBarConfig("Custom Search Hint...",
                        new SearchBarConfig.OnSearchListener() {
                    @Override
                    public void onSearch(String query) {
                        Toast.makeText(activity, "Activate Search!", Toast.LENGTH_SHORT).show();
                    }
                }, new SearchBarConfig.OnTypeListener() {
                    @Override
                    public void onType(EditText searchView) {
                        Toast.makeText(activity, "Typing!", Toast.LENGTH_SHORT).show();
                    }
                }));
    }

}
