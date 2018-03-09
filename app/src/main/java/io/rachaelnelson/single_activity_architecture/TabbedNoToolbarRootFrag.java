package io.rachaelnelson.single_activity_architecture;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

public class TabbedNoToolbarRootFrag extends TabbedFrag {

    @Override
    public void setTabbedContentView(LayoutInflater inflater, View parent, Bundle savedInstanceState) {
//        activity.getToolbar().setTitle("Tabbed No Toolbar Frag");
//        activity.getFAB().setVisibility(View.GONE);
//        activity.pinTabs(true);
//        activity.setToolbarMode(BaseActivity.TOOLBAR_MODE.TABBED);
//        activity.getBottomNav().setVisibility(View.VISIBLE);
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public Screen[] getScreens() {
        return new Screen[]{tabOneScreen, tabTwoScreen, tabThreeScreen};
    }

    private Screen tabOneScreen = new Screen() {
        @Override
        public String getTitle() {
            return "Tab 1";
        }

        @Override
        public Fragment getFragment() {
            return new TabOneFrag();
        }
    };

    private Screen tabTwoScreen = new Screen() {
        @Override
        public String getTitle() {
            return "Tab 2";
        }

        @Override
        public Fragment getFragment() {
            return new TabTwoFrag();
        }
    };

    private Screen tabThreeScreen = new Screen() {
        @Override
        public String getTitle() {
            return "Tab 3";
        }

        @Override
        public Fragment getFragment() {
            return new TabThreeFrag();
        }
    };
}
