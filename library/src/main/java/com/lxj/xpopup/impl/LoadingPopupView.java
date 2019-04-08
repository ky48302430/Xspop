package com.lxj.xpopup.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.TextView;

import com.lxj.xpopup.R;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.util.ProgressHelper;
import com.lxj.xpopup.widget.LoadingView;
import com.lxj.xpopup.widget.ProgressWheel;

/**
 * Description: 加载对话框
 * Create by dance, at 2018/12/16
 */
public class LoadingPopupView extends CenterPopupView {
    private TextView tv_title;

    public static final int CIRCLE_NORMAL_TYPE = 0;
    public static final int CIRCLE_BAR_TYPE = 1;
    private int mType = 0;

    private ProgressWheel mProgressWheel;
    private LoadingView mlLoadingView;

    public LoadingPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout._xpopup_center_impl_loading;
    }

    @Override
    protected void initPopupContent() {
        super.initPopupContent();

        tv_title = findViewById(R.id.tv_title);
        if (title != null) {
            tv_title.setVisibility(VISIBLE);
            tv_title.setText(title);
        }
        mProgressWheel = findViewById(R.id.progressWheel);
        mlLoadingView = findViewById(R.id.loadview);
        handlerType();
    }

    private void handlerType() {
        switch (mType) {
            case CIRCLE_NORMAL_TYPE: {
                mProgressWheel.setVisibility(View.GONE);
                mlLoadingView.setVisibility(View.VISIBLE);
                break;
            }
            case CIRCLE_BAR_TYPE: {
                mProgressWheel.setVisibility(View.VISIBLE);
                mlLoadingView.setVisibility(View.GONE);

                ProgressHelper progressHelper = new ProgressHelper(getContext());
                progressHelper.setProgressWheel((ProgressWheel) findViewById(R.id.progressWheel));
                break;
            }
        }
    }

    private String title;

    public LoadingPopupView setTitle(String title) {
        this.title = title;
        return this;
    }

    public void setType(int mType) {
        this.mType = mType;
    }
}
