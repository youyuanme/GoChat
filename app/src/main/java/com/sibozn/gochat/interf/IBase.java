package com.sibozn.gochat.interf;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.sibozn.gochat.presenter.BasePresenter;


/**
 * Created by sysadminl on 2015/12/10.
 */
public interface IBase {
    BasePresenter getPresenter();

    View createView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    void bindView(Bundle savedInstanceState);

    View getView();

    int getContentLayout();
}
