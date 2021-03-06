package com.dou361.live.ui.activity;

import android.content.res.TypedArray;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.widget.EdgeEffectCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;

import com.dou361.baseutils.utils.UIUtils;
import com.dou361.customui.ui.IndicatorView;
import com.dou361.live.R;
import com.dou361.live.ui.adapter.GuideAdapter;
import com.dou361.live.ui.application.BaseApplication;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * ========================================
 * <p>
 * 版 权：dou361.com 版权所有 （C） 2015
 * <p>
 * 作 者：陈冠明
 * <p>
 * 个人网站：http://www.dou361.com
 * <p>
 * 版 本：1.0
 * <p>
 * 创建日期：2016/10/4 15:21
 * <p>
 * 描 述：开启应用的向导页面
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class GuideActivity extends BaseActivity implements
        OnPageChangeListener {

    /**
     * 装载向导图片的View
     */
    @BindView(R.id.view_pager)
    ViewPager view_pager;
    @BindView(R.id.ll_indicator)
    LinearLayout ll_indicator;
    /**
     * 进入主页面按钮
     */
    @BindView(R.id.btn_guide)
    Button btn_guide;
    /**
     * 图片列表
     */
    private List<View> imageLists;
    private EdgeEffectCompat leftEdge;
    private EdgeEffectCompat rightEdge;
    private IndicatorView mIndicator;

    @Override
    public boolean openStatus() {
        return false;
    }

    @Override
    public boolean openSliding() {
        return false;
    }

    @Override
    protected void initView() {
        setContentView(R.layout.activity_guide);
        view_pager.setOnPageChangeListener(this);
        mIndicator = new IndicatorView(UIUtils.getContext());
        // 设置点和点之间的间隙
        mIndicator.setInterval(UIUtils.dip2px(40));
        // 设置点的图片
        mIndicator.setIndicatorDrawable(UIUtils.getDrawable(R.drawable.live_indicator_selector));
        mIndicator.setSelection(0);
        ll_indicator.addView(mIndicator);
        TypedArray icons = UIUtils.getTypedArray(R.array.guide_picture);
        imageLists = new ArrayList<View>();
        for (int i = 0; i < icons.length(); i++) {
            ImageView iv = new ImageView(UIUtils.getContext());
            iv.setScaleType(ScaleType.FIT_XY);
            iv.setImageDrawable(icons.getDrawable(i));
            imageLists.add(iv);
        }
        mIndicator.setCount(imageLists.size());
        /** 如果导航页面只有一页，显示体验按钮 */
        if (imageLists.size() == 1) {
            btn_guide.setVisibility(View.VISIBLE);
        }
        GuideAdapter adapter = new GuideAdapter(imageLists);
        view_pager.setAdapter(adapter);

        /** viewpaer左滑最后一页时进入应用 */
        try {
            Field leftEdgeField = view_pager.getClass().getDeclaredField("mLeftEdge");
            Field rightEdgeField = view_pager.getClass().getDeclaredField("mRightEdge");
            if (leftEdgeField != null && rightEdgeField != null) {
                leftEdgeField.setAccessible(true);
                rightEdgeField.setAccessible(true);
                leftEdge = (EdgeEffectCompat) leftEdgeField.get(view_pager);
                rightEdge = (EdgeEffectCompat) rightEdgeField.get(view_pager);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @OnClick({R.id.btn_guide})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_guide:
                startActivity(LoginActivity.class);
                onBackPressed();
                break;
        }
    }

    /**
     * 进入应用
     */
    private void enterApp() {
        startActivity(LoginActivity.class);
        BaseApplication.getInstance().finishAll();
    }

    @Override
    public void onPageScrollStateChanged(int arg0) {
        /** 到了最后一张并且还继续拖动，出现蓝色限制边条了 */
        if (rightEdge != null && !rightEdge.isFinished()) {
            /** 最后一张往右滑进入应用 */
            enterApp();
        }
    }

    @Override
    public void onPageScrolled(int arg0, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        mIndicator.setSelection(position);
        /** 最后一张 */
        if (position == imageLists.size() - 1) {
            btn_guide.setVisibility(View.VISIBLE);
        } else {
            btn_guide.setVisibility(View.GONE);
        }
    }

}
