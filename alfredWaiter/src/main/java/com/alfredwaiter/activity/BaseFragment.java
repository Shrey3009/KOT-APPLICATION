package com.alfredwaiter.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * author pangchao
 * created on 2017/5/20
 * email fat_chao@163.com.
 */

public abstract class BaseFragment<T extends BasePresenter, V> extends Fragment implements View.OnClickListener, ViewCallBack<V> {
    public T presenter;
    protected boolean isVisible;
    protected Context mContext;
    protected boolean isPrepared;

    protected TextView tvTitle;
    private LinearLayout mLinearLayout;
    protected TextView tvRight;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mContext = getActivity();

        ViewGroup view = (ViewGroup) inflater.inflate(getLayoutId(), container, false);
        // View view = initView(inflater, container);
        initCustomView(view);
        getData();
        initListener();

//        mFlexibleLayout.loadData();
        isPrepared = true;
        return view;

    }





    @Override
    public void onResume() {
        super.onResume();
  //      presenter.add((ViewCallBack) this);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
      //  presenter.remove();

    }

    protected abstract int getLayoutId();

    protected abstract void initCustomView(View view);//初始化界面

    protected abstract void initListener();//初始化监听事件

//    protected abstract T initPresenter();//初始化数据以及请求参数

    protected abstract void getData();



    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mContext != null)
            mContext = null;
    }

}