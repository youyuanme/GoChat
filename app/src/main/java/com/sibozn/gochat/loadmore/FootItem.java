package com.sibozn.gochat.loadmore;

import android.view.View;
import android.view.ViewGroup;

/**
 * Footer item
 *
 * @author wjk on 2016/8/23.
 */
public abstract class FootItem {

    public CharSequence loadingText;
    public CharSequence endText;
    public CharSequence pullToLoadText;

    public abstract View onCreateView(ViewGroup parent);

    public abstract void onBindData(View view, int state);

}
