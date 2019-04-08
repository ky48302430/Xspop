package com.lxj.xpopup.impl;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.lxj.xpopup.R;
import com.lxj.xpopup.XPopup;
import com.lxj.xpopup.core.CenterPopupView;
import com.lxj.xpopup.interfaces.OnCancelListener;
import com.lxj.xpopup.interfaces.OnConfirmListener;
import com.lxj.xpopup.util.OptAnimationLoader;
import com.lxj.xpopup.widget.SuccessTickView;

/**
 * Description: 简易对话框
 * Create by dance, at 2018/12/16
 */
public class TipPopupView extends CenterPopupView implements View.OnClickListener {
    public static final int NORMAL_TYPE = 0;
    public static final int ERROR_TYPE = 1;
    public static final int SUCCESS_TYPE = 2;
    public static final int WARNING_TYPE = 3;
    private int mAlertType;

    private FrameLayout mErrorFrame;
    private FrameLayout mWarningFrame;
    private FrameLayout mSuccessFrame;
    private SuccessTickView mSuccessTick;
    private ImageView mErrorX;
    private View mSuccessLeftMask;
    private View mSuccessRightMask;

    private Animation mErrorInAnim;
    private AnimationSet mErrorXInAnim;
    private AnimationSet mSuccessLayoutAnimSet;
    private Animation mSuccessBowAnim;

    public TipPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout._xpopup_center_impl_tip;
    }

    TextView tv_title, tv_content, tv_cancel, tv_confirm;

    @Override
    protected void initPopupContent() {
        super.initPopupContent();
        tv_title = findViewById(R.id.tv_title);
        tv_content = findViewById(R.id.tv_content);
        tv_cancel = findViewById(R.id.tv_cancel);
        tv_confirm = findViewById(R.id.tv_confirm);

        mErrorFrame = findViewById(R.id.error_frame);
        mErrorX = mErrorFrame.findViewById(R.id.error_x);
        mSuccessFrame = findViewById(R.id.success_frame);
        mSuccessTick = mSuccessFrame.findViewById(R.id.success_tick);
        mSuccessLeftMask = mSuccessFrame.findViewById(R.id.mask_left);
        mSuccessRightMask = mSuccessFrame.findViewById(R.id.mask_right);
        mWarningFrame = findViewById(R.id.warning_frame);

        applyPrimaryColor();

        tv_cancel.setOnClickListener(this);
        tv_confirm.setOnClickListener(this);

        if (!TextUtils.isEmpty(title)) {
            tv_title.setText(title);
        }
        if (!TextUtils.isEmpty(content)) {
            tv_content.setText(content);
        }

        mSuccessBowAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.success_bow_roate);
        mSuccessLayoutAnimSet = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.success_mask_layout);

        //布局控制
        HandlerAlertType();

        playAnimation();
    }

    protected void applyPrimaryColor() {
        tv_cancel.setTextColor(XPopup.getPrimaryColor());
        tv_confirm.setTextColor(XPopup.getPrimaryColor());
    }

    OnCancelListener cancelListener;
    OnConfirmListener confirmListener;

    public void setListener(OnConfirmListener confirmListener, OnCancelListener cancelListener) {
        this.cancelListener = cancelListener;
        this.confirmListener = confirmListener;
    }

    String title;
    String content;

    public void setTitleContent(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Override
    public void onClick(View v) {
        if (v == tv_cancel) {
            if (cancelListener != null) cancelListener.onCancel();
            dismiss();
        } else if (v == tv_confirm) {
            if (confirmListener != null) confirmListener.onConfirm();
            if (popupInfo.autoDismiss) dismiss();
        }
    }


    private void HandlerAlertType() {
        switch (mAlertType) {
            case ERROR_TYPE:
                mErrorFrame.setVisibility(View.VISIBLE);
                break;
            case SUCCESS_TYPE:
                mSuccessFrame.setVisibility(View.VISIBLE);
                // initial rotate layout of success mask
                mSuccessLeftMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(0));
                mSuccessRightMask.startAnimation(mSuccessLayoutAnimSet.getAnimations().get(1));
                break;
            case WARNING_TYPE:
                mWarningFrame.setVisibility(View.VISIBLE);
                break;
        }
    }

    private void playAnimation() {
        if (mAlertType == ERROR_TYPE) {
            mErrorInAnim = OptAnimationLoader.loadAnimation(getContext(), R.anim.error_frame_in);
            mErrorXInAnim = (AnimationSet) OptAnimationLoader.loadAnimation(getContext(), R.anim.error_x_in);

            mErrorFrame.startAnimation(mErrorInAnim);
            mErrorX.startAnimation(mErrorXInAnim);
        } else if (mAlertType == SUCCESS_TYPE) {
            mSuccessTick.startTickAnim();
            mSuccessRightMask.startAnimation(mSuccessBowAnim);
        }
    }

    public void setAlertType(int alertType) {
        this.mAlertType = alertType;
    }
}
