package io.rachaelnelson.single_activity_architecture;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.MenuRes;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;

import java.lang.reflect.Field;

public class BaseActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private Navigator navigator;
    private ScreenConfig.NAVIGATION_MODE navigationMode = ScreenConfig.NAVIGATION_MODE.NONE;

    private AppBarLayout appBar;
    private Toolbar toolbar;
    private ImageView toolbarLogoContainer;
    private EditText searchView;
    private BottomNavigationView bottomNav;
    private FrameLayout customToolbarContentContainer;
    private FloatingActionButton fab;
    private TabLayout tabs;
    private boolean activityVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);

        navigator = new Navigator(getSupportFragmentManager());

        appBar = (AppBarLayout) findViewById(R.id.app_bar_layout);

        setupToolbar();

        customToolbarContentContainer = (FrameLayout) findViewById(R.id.collapsible_toolbar_custom_content);

        bottomNav = (BottomNavigationView) findViewById(R.id.navigation);
        bottomNav.setOnNavigationItemSelectedListener(this);
        removeShiftMode((BottomNavigationView) findViewById(R.id.navigation));

        searchView = (EditText) findViewById(R.id.search_text);

        tabs = (TabLayout) findViewById(R.id.tab_layout);

        fab = (FloatingActionButton) findViewById(R.id.fab);

        getNavigator().setRoot(new SimpleToolbarRootFragment());
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        findViewById(R.id.coordinator).requestFocus();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Navigator navigator = getNavigator();
        navigator.goToRoot();

        Fragment rootFragment = navigator.getCurrentFragment();

        switch (item.getItemId()) {
            case R.id.action_bottom_nav_one:
                navigator.setRoot(new SimpleToolbarRootFragment());
                break;
            case R.id.action_bottom_nav_two:
                navigator.setRoot(new SearchToolbarRootFragment());
                break;
            case R.id.action_bottom_nav_three:
                navigator.setRoot(new TabbedNoToolbarRootFrag());
                break;
            case R.id.action_bottom_nav_four:
                navigator.setRoot(new CustomToolbarContentFrag());
                break;
//            case R.id.action_bottom_nav_five:
//                navigator.setRoot(new ProfileFrag());
//                break;
        }
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("RestrictedApi")
    void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to read shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }

    @Override
    public void onBackPressed() {
        if (navigationMode == ScreenConfig.NAVIGATION_MODE.LOCK) {
            moveTaskToBack(true);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                navigator.pressBack();
            } else {
                super.onBackPressed();
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        activityVisible = false;
    }

    private void setupToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarLogoContainer = (ImageView) toolbar.findViewById(R.id.toolbar_image);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("My Toolbar Activity");

        CollapsingToolbarLayout collapsingToolbarLayout = ((CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout));
        if (collapsingToolbarLayout != null) {
            collapsingToolbarLayout.setTitleEnabled(false);
        }

    }

    public final Navigator getNavigator() {
        return navigator;
    }

    public BottomNavigationView getBottomNav() {
        return bottomNav;
    }

    public void setNavigationMode(ScreenConfig.NAVIGATION_MODE navigationMode) {
        this.navigationMode = navigationMode;

        if (navigationMode != ScreenConfig.NAVIGATION_MODE.ROOT) {
            getBottomNav().setVisibility(View.GONE);
        }
        getToolbar().setNavigationIcon(null);

        switch (this.navigationMode) {
            case ROOT:
                getToolbar().setVisibility(View.VISIBLE);
                getBottomNav().setVisibility(View.VISIBLE);
                break;
            case BACK:
                getToolbar().setNavigationIcon(R.drawable.back_icon_16dp);
                getToolbar().setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getNavigator().pressBack();
                    }
                });
                getToolbar().setVisibility(View.VISIBLE);
                break;
            case LOCK:
                getToolbar().setNavigationIcon(null);
                break;
            case NONE:
                getToolbar().setVisibility(View.GONE);
                break;
        }
    }

    public ScreenConfig.NAVIGATION_MODE getNavigationMode() {
        return navigationMode;
    }

    public TabLayout getTabLayout() {
        return tabs;
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    public AppBarLayout getAppBar() {
        return appBar;
    }

    public FloatingActionButton getFAB() {
        return fab;
    }

    public View.OnClickListener setFABClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RotateAnimation rotate = new RotateAnimation(0, -360,
                        Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                        0.5f);
                rotate.setInterpolator(new LinearInterpolator());
                rotate.setDuration(200);
                fab.startAnimation(rotate);

                Snackbar.make(fab, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        };
    }

    public void addCustomLayoutToCollapsingToolbar(@LayoutRes int collapsingToolbarContentLayoutID) {
        if (collapsingToolbarContentLayoutID > 0) {
            ViewGroup customContentContainer = (ViewGroup) LayoutInflater.from(this).inflate(collapsingToolbarContentLayoutID, customToolbarContentContainer);
            customToolbarContentContainer.setVisibility(View.VISIBLE);
            customContentContainer.requestLayout();
        } else {
            this.customToolbarContentContainer.setVisibility(View.GONE);
        }


    }

    public ImageView getToolbarLogoContainer() {
        return toolbarLogoContainer;
    }

    public final void setMenu(@MenuRes int resId) {
        toolbar.getMenu().clear();

        if (resId > 0) {
            toolbar.inflateMenu(resId);
        }
    }

    public void setMenuItemClickListener(Toolbar.OnMenuItemClickListener listener) {
        toolbar.setOnMenuItemClickListener(listener);
    }

    public void setupTabs(@IdRes int viewPagerId) {
        if (viewPagerId > 0) {
            ViewPager viewPager = (ViewPager) findViewById(viewPagerId);
            tabs.setupWithViewPager(viewPager);
            toolbar.setVisibility(View.GONE);
            findViewById(R.id.tab_layout).setVisibility(View.VISIBLE);

        } else {
            findViewById(R.id.tab_layout).setVisibility(View.GONE);
        }

    }

    public void showFloatingActionButton(boolean show) {
        getFAB().setVisibility(show ? View.VISIBLE : View.GONE);
    }

    public void setPinTabs(boolean pin) {
        ViewGroup.LayoutParams params = tabs.getLayoutParams();
        AppBarLayout.LayoutParams newParams;
        newParams = (AppBarLayout.LayoutParams) params;


        if (pin) {
            newParams.setScrollFlags(0);
        } else {
            newParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        }

        toolbar.setLayoutParams(newParams);
        tabs.setLayoutParams(newParams);
        toolbar.requestLayout();
    }

    public void setPinToolbar(boolean pin) {
        ViewGroup.LayoutParams params = toolbar.getLayoutParams();
        AppBarLayout.LayoutParams newParams;
        newParams = (AppBarLayout.LayoutParams) params;


        if (pin) {
            newParams.setScrollFlags(0);
        } else {
            newParams.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL | AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
        }

        toolbar.setLayoutParams(newParams);
        tabs.setLayoutParams(newParams);
        toolbar.requestLayout();
    }

    @SuppressLint("ClickableViewAccessibility")
    public void setSearchConfig(final SearchBarConfig config) {

        //in search layout:
        //// TODO: 8/22/17 if keydown turn voice icon to cancel icon
        //// TODO: 8/22/17 implement touch event for deleteAll icon (clear all the text but keep focus)
        //// TODO: 8/22/17 implement google voice service

        searchView.findViewById(R.id.search_text);

        if (config != null) {

            searchView.setHint(config.getHint());

            searchView.setOnKeyListener(new View.OnKeyListener() {
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        ////tODO: use RxJava to observe typed text after 3 seconds & display look ahead results
                        config.getTypeListener().onType(searchView);
                        switch (keyCode) {
                            case KeyEvent.KEYCODE_DPAD_CENTER:
                            case KeyEvent.KEYCODE_ENTER:
//                                config.getSearchListener().onSearch(searchView.getText().toString());
                                searchView.clearFocus();
                                //// TODO: 8/22/17 when focus is cleared, hide lookahead results and begin network query using input
                                //trigger callback method for searchListener
                                InputMethodManager imm = (InputMethodManager) searchView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
                                config.getSearchListener().onSearch(searchView.getText().toString());
                                return true;
                            default:
                                break;
                        }
                    }
                    return false;
                }
            });


            searchView.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View view, boolean hasFocus) {
                    if (hasFocus) {
                        toolbar.setVisibility(View.GONE);
                    } else {
                        toolbar.setVisibility(View.VISIBLE);
                    }
                }
            });

            searchView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    final int DRAWABLE_LEFT = 0;
                    final int DRAWABLE_RIGHT = 2;

                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        if (motionEvent.getRawX() >= (searchView.getLeft() - searchView.getCompoundDrawables()[DRAWABLE_LEFT].getBounds().width())) {
                        } else if (motionEvent.getRawX() >= (searchView.getRight() - searchView.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                        }
                    }
                    return false;
                }
            });
            searchView.setVisibility(View.VISIBLE);
        } else {
            searchView.setVisibility(View.GONE);
        }
    }

    public void setFABClickListener(View.OnClickListener listener) {
        fab.setOnClickListener(listener);
    }

    public static final class ScreenConfig {
        private SearchBarConfig searchBarConfig;
        private NAVIGATION_MODE navigationMode = NAVIGATION_MODE.NONE;
        private int menuResId;
        private Toolbar.OnMenuItemClickListener menuItemClickListener;
        private String title;
        private int titleLogoResID;
        private int customToolbarContentLayoutID;
        private int ViewPagerResID;
        private boolean useFAB;
        private View.OnClickListener fabClickListener;
        private boolean pinTabs = false;
        private boolean pinToolbar = false;

        enum NAVIGATION_MODE {
            ROOT,  //show bottom nav, and
            BACK,
            LOCK,
            NONE
        }


        public ScreenConfig setNavigationMode(NAVIGATION_MODE navigationMode) {
            this.navigationMode = navigationMode;
            return this;
        }

        public ScreenConfig setSearchBarConfig(SearchBarConfig searchBarConfig) {
            this.searchBarConfig = searchBarConfig;
            return this;
        }

        public ScreenConfig setTitle(String title) {
            this.title = title;
            return this;
        }

        public ScreenConfig setTitleLogo(int titleLogoResID) {
            this.titleLogoResID = titleLogoResID;
            return this;
        }

        public ScreenConfig setCustomCollaspingToolbarContent(@LayoutRes int customContentLayoutId) {
            this.customToolbarContentLayoutID = customContentLayoutId;
            return this;
        }

        public ScreenConfig setMenu(int menuResId) {
            this.menuResId = menuResId;
            return this;
        }

        public ScreenConfig setMenuItemClickListener(Toolbar.OnMenuItemClickListener listener) {
            this.menuItemClickListener = listener;
            return this;
        }

        public ScreenConfig setViewPagerForTabs(@IdRes int viewPagerResID) {
            this.ViewPagerResID = viewPagerResID;
            return this;
        }

        public ScreenConfig setPinTabs(boolean pinTabs) {
            this.pinTabs = pinTabs;
            return this;
        }

        public ScreenConfig setPinToolbar(boolean pinToolbar) {
            this.pinToolbar = pinToolbar;
            return this;
        }

        public ScreenConfig setUseFAB(boolean showFAB) {
            this.useFAB = showFAB;
            return this;
        }

        public ScreenConfig setFABClickListener(View.OnClickListener fabClickListener) {
            this.fabClickListener = fabClickListener;
            return this;
        }


        public NAVIGATION_MODE getNavigationMode() {
            return navigationMode;
        }

        public SearchBarConfig getSearchBarConfig() {
            return searchBarConfig;
        }

        public String getTitle() {
            return title;
        }

        public int getTitleLogo() {
            return titleLogoResID;
        }

        public int getCustomToolbarContentLayoutID() {
            return this.customToolbarContentLayoutID;
        }


        public int getMenuResId() {
            return menuResId;
        }

        public Toolbar.OnMenuItemClickListener getMenuItemClickListener() {
            return this.menuItemClickListener;
        }

        public int getViewPagerForTabs() {
            return ViewPagerResID;
        }

        public boolean getPinTabs() {
            return this.pinTabs;
        }

        public boolean getPinToolbar() {
            return this.pinToolbar;
        }

        public boolean getUsingFAB() {
            return this.useFAB;
        }

        public View.OnClickListener getFabClickListener() {
            return fabClickListener;
        }
    }

}
