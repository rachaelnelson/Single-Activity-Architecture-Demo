package io.rachaelnelson.single_activity_architecture;

import android.widget.EditText;

public class SearchBarConfig {
    private final OnTypeListener typeListener;
    private String hint;
    private OnSearchListener searchListener;

    public interface OnSearchListener {
        void onSearch(String query);
    }

    public interface OnTypeListener {
        void onType(EditText searchView);
    }

    public SearchBarConfig(String hint, OnSearchListener searchListener, OnTypeListener typeListener) {
        this.hint = hint;
        this.searchListener = searchListener;
        this.typeListener = typeListener;
    }

    public String getHint() {
        return hint;
    }

    public OnSearchListener getSearchListener() {
        return searchListener;
    }

    public OnTypeListener getTypeListener() {
        return typeListener;
    }



}
