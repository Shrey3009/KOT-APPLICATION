package com.alfredmenu.activity;

import android.app.Application;
import android.app.Fragment;
import android.graphics.Color;

import android.os.Handler;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alfredbase.global.CoreData;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.OrderDetail;
import com.alfredmenu.popupwindow.SetItemCountWindow;
import com.alfredmenu.view.SlidePanelView;
import com.alfredmenu.R;
import com.alfredmenu.adapter.ItemDetailAdapter;
import com.alfredmenu.adapter.ItemHeaderDecoration;
import com.alfredmenu.adapter.ItemHeaderDetailDecoration;



import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItemDetailFragment extends BaseFragment<ItemDetailPresenter, String> implements CheckListener,CallBackMove {
    private static CheckListener checkListener;
    private RecyclerView mRv;

    private GridLayoutManager mManager;

    private ItemHeaderDetailDecoration mDecoration;
    private SetItemCountWindow setItemCountWindow;
    ItemDetailAdapter mAdapter;
    private boolean move = false;
    private int mIndex = 0;
    private List<ItemDetail> mDatas = new ArrayList<ItemDetail>();

  //  private CheckListener checkListener;
    protected int getLayoutId() {
        return R.layout.fragment_item_detail;
    }


    protected void initCustomView(View view) {
        mRv = (RecyclerView) view.findViewById(R.id.rv_item_detail);
        SlidePanelView.setCallBackMove(this);

    }


    protected void initListener() {
        mRv.addOnScrollListener(new RecyclerViewListener());
    }


//    protected ItemDetailPresenter initPresenter() {
//
//
//        mLinearLayoutManager = new LinearLayoutManager(mContext);
//
//        mRv.setLayoutManager(mLinearLayoutManager);
//        mAdapter = new ItemDetailAdapter(mContext, mDatas, new RvItemClickListener() {
//
//            @Override
//            public void onItemClick(View view, int position) {
//            }
//
////                Snackbar snackbar = Snackbar.make(mRv, "当前点击的是" + content + ":" + mDatas.get(position).getName(), Snackbar.LENGTH_SHORT);
////                View mView = snackbar.getView();
////                mView.setBackgroundColor(Color.BLUE);
////                TextView text = (TextView) mView.findViewById(android.support.design.R.id.snackbar_text);
////                text.setTextColor(Color.WHITE);
////                text.setTextSize(25);
////                snackbar.show();
////            }
//        });
//
//        mRv.setAdapter(mAdapter);
//      //  mDecoration = new ItemHeaderDetailDecoration(mContext, mDatas);
//      //  mRv.addItemDecoration(mDecoration);
//     //   mDecoration.setCheckListener(checkListener);
//        initData();
//        return new ItemDetailPresenter();
//    }

    @Override
    protected void getData() {
        initData();
    }


    private void initData() {


        ArrayList<ItemDetail> leftList =  getArguments().getParcelableArrayList("left");
          mDatas=leftList;
//        mLinearLayoutManager = new LinearLayoutManager(mContext);
////
////        mRv.setLayoutManager(mLinearLayoutManager);
        mManager = new GridLayoutManager(mContext, 1);
        //通过isTitle的标志来判断是否是title
        mManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return 1;
            }
        });
        mRv.setLayoutManager(mManager);
//        mAdapter = new ItemDetailAdapter(mContext, mDatas,setItemCountWindow, new RvItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//
//            }
//        }, new CountView.OnCountChange() {
//            @Override
//            public void onChange(ItemDetail selectedItemDetail, int count, boolean isAdd) {
//
//            }
//        });

//        mRv.setAdapter(mAdapter);
//         mDecoration = new ItemHeaderDetailDecoration(mContext, mDatas);
//          mRv.addItemDecoration(mDecoration);
//           mDecoration.setCheckListener(checkListener);
    //    initData();
//        ArrayList<ItemDetail> leftList =  getArguments().getParcelableArrayList("left");
//          mDatas=leftList;
    //    mAdapter.notifyDataSetChanged();
       // mDecoration.setData(mDatas);
    }
    public void onClick(View v) {

    }

//    @Override
//    public void refreshView(int code, String data) {
//
//    }

    public  void setData(int n) {
        mIndex = n;
        mRv.stopScroll();
        smoothMoveToPosition(n);
    }


    public static void setListener(CheckListener listener) {
        checkListener = listener;
    }

    private void smoothMoveToPosition(int n) {
        int firstItem = mManager.findFirstVisibleItemPosition();
        int lastItem = mManager.findLastVisibleItemPosition();
        Log.d("first--->", String.valueOf(firstItem));
        Log.d("last--->", String.valueOf(lastItem));
        if (n <= firstItem) {
            mRv.smoothScrollToPosition(n);
           // ((LinearLayoutManager)mRv.getLayoutManager()).scrollToPositionWithOffset(n,0);
        } else if (n <= lastItem) {
            Log.d("pos---->", String.valueOf(n) + "VS" + firstItem);
            int top = mRv.getChildAt(n - firstItem).getTop();
            Log.d("top---->", String.valueOf(top));
            mRv.scrollBy(0, top);
        } else {
            //((LinearLayoutManager)mRv.getLayoutManager()).scrollToPositionWithOffset(n,0);
            mRv.smoothScrollToPosition(n);
            move = true;
        }
    }

    @Override
    public void check(int position, boolean isScroll) {
        checkListener.check(position, isScroll);
    }

    @Override
    public void refreshView(int code, String data) {

    }

    @Override
    public void move(int n) {
        mIndex = n;
        mRv.stopScroll();
        smoothMoveToPosition(n);
    }


//
//    public void check(int position, boolean isScroll) {
//        checkListener.check(position, isScroll);
//
//    }


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {

                default:
                    break;
            }
        };
    };


    private class RecyclerViewListener extends RecyclerView.OnScrollListener {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
            super.onScrollStateChanged(recyclerView, newState);
            if (move && newState == RecyclerView.SCROLL_STATE_IDLE) {
                move = false;
                int n = mIndex - mManager.findFirstVisibleItemPosition();
                Log.d("n---->", String.valueOf(n));
                if (0 <= n && n < mRv.getChildCount()) {
                    int top = mRv.getChildAt(n).getTop();
                    Log.d("top--->", String.valueOf(top));
                    mRv.smoothScrollBy(0, top);
                }
            }
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);
//            if (move) {
//                move = false;
//                int n = mIndex - mManager.findFirstVisibleItemPosition();
//                if (0 <= n && n < mRv.getChildCount()) {
//                    int top = mRv.getChildAt(n).getTop();
//                    mRv.scrollBy(0, top);
//                }
//            }
        }
    }


}
