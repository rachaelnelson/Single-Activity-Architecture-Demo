package io.rachaelnelson.single_activity_architecture;

import android.annotation.SuppressLint;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class Navigator {
    private FragmentManager fragmentManager;
    private Fragment currentFragment;

    public Navigator(FragmentManager fragmentManager) {
        this.fragmentManager = fragmentManager;
    }

    public void setRoot(Fragment fragment) {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }

        navigateTo(fragment, false);
    }

    public void navigateTo(Fragment fragment) {
        navigateTo(fragment, false);
    }

    @SuppressLint("CommitTransaction")
    public void navigateTo(Fragment fragment, boolean addToBackStack) {
        String tag = fragment.getClass().getName();

        FragmentTransaction transaction = fragmentManager.beginTransaction()
                .replace(R.id.main_content, fragment, tag);

        if (addToBackStack) {
            transaction.addToBackStack(tag);
        }

        transaction.commit();
        fragmentManager.executePendingTransactions();
    }

    public void pressBack() {
        Fragment fragment = fragmentManager.findFragmentById(R.id.main_content);
        if (fragment instanceof BaseFrag) {
            BaseFrag baseFrag = (BaseFrag) fragment;
            if (!baseFrag.isOnBackpressOverridden()) {
                // back press not handled by BaseFrag
                fragmentManager.popBackStackImmediate();
            }
        } else {
            fragmentManager.popBackStackImmediate();
        }
    }

    public void goBackTo(Class<? extends Fragment> fragmentClass) {
        fragmentManager.popBackStackImmediate(fragmentClass.getName(), 0);
    }

    public void goToRoot() {
        while (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStackImmediate();
        }
    }

    public Fragment getCurrentFragment() {
        return currentFragment;
    }

    public void setCurrentFragment(Fragment fragment) {
        currentFragment = fragment;
    }
}
