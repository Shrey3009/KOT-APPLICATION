package com.alfred.callnum.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.SweepGradient;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.alfred.callnum.R;
import com.alfred.callnum.activity.MainActivity;
import com.alfred.callnum.adapter.CallBean;
import com.alfred.callnum.adapter.MycallAdapter;
import com.alfred.callnum.adapter.RvListener;
import com.alfred.callnum.global.App;
import com.alfredbase.utils.LogUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OneFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OneFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private RecyclerView re_one, re_two, re_three, re_four;
    private List<CallBean> mDatas = new ArrayList<>();
    private List<CallBean> mDatas1 = new ArrayList<>();
    private List<CallBean> mDatas2 = new ArrayList<>();
    private List<CallBean> mDatas3 = new ArrayList<>();
    private List<CallBean> mDatas4 = new ArrayList<>();
    MycallAdapter mAdapter1;
    MycallAdapter mAdapter2;
    MycallAdapter mAdapter3;
    MycallAdapter mAdapter4;
    private int vid;
    Handler handler;

    Map<String, Object> callMap = new HashMap<String, Object>();

    private Boolean type = true;

    private TextView line1, line2, line3;
    private TextView tvTitle1;
    private TextView tvTitle2;
    private TextView tvTitle3;
    private TextView tvTitle4;


    @SuppressLint("ValidFragment")
    public OneFragment() {

    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OneFragment newInstance(String param1, String param2) {
        OneFragment fragment = new OneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_one, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
    }

    public void setViewId(int vid, Handler mhandler) {
        this.vid = vid;
        this.handler = mhandler;
    }


    private void initView() {
        re_one = (RecyclerView) getActivity().findViewById(R.id.review_one);
        re_two = (RecyclerView) getActivity().findViewById(R.id.review_two);
        re_three = (RecyclerView) getActivity().findViewById(R.id.review_three);
        re_four = (RecyclerView) getActivity().findViewById(R.id.review_four);
        line1 = (TextView) getActivity().findViewById(R.id.tv_line1);
        line2 = (TextView) getActivity().findViewById(R.id.tv_line2);
        line3 = (TextView) getActivity().findViewById(R.id.tv_line3);
        //   bg = (ImageView) getActivity().findViewById(R.id.img_call_bgs);


//            re_three.setVisibility(View.VISIBLE);
//            re_four.setVisibility(View.VISIBLE);

        LinearLayoutManager layoutManager1 = new LinearLayoutManager(getActivity()); //设置布局管理器
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity()); //设置布局管理器
        LinearLayoutManager layoutManager3 = new LinearLayoutManager(getActivity()); //设置布局管理器
        LinearLayoutManager layoutManager4 = new LinearLayoutManager(getActivity()); //设置布局管理器
        re_one.setLayoutManager(layoutManager1); //设置为垂直布局，这也是默认的
        re_two.setLayoutManager(layoutManager2);
        re_three.setLayoutManager(layoutManager3);
        re_four.setLayoutManager(layoutManager4);

        tvTitle1 = (TextView) getActivity().findViewById(R.id.tvTitle1);
        tvTitle2 = (TextView) getActivity().findViewById(R.id.tvTitle2);
        tvTitle3 = (TextView) getActivity().findViewById(R.id.tvTitle3);
        tvTitle4 = (TextView) getActivity().findViewById(R.id.tvTitle4);


        mAdapter1 = new MycallAdapter(getActivity(), mDatas1, new RvListener() {
            @Override
            public void onItemClick(int id, int position) {

            }
        });


        mAdapter2 = new MycallAdapter(getActivity(), mDatas2, new RvListener() {
            @Override
            public void onItemClick(int id, int position) {

            }
        });


        mAdapter3 = new MycallAdapter(getActivity(), mDatas3, new RvListener() {
            @Override
            public void onItemClick(int id, int position) {

            }
        });


        mAdapter4 = new MycallAdapter(getActivity(), mDatas4, new RvListener() {
            @Override
            public void onItemClick(int id, int position) {

            }
        });


        re_one.setAdapter(mAdapter1);
        re_two.setAdapter(mAdapter2);
        re_three.setAdapter(mAdapter3);
        re_four.setAdapter(mAdapter4);
        //  initData();
//        //

    }


    public void dataClear() {
        mDatas1.clear();
        mDatas2.clear();
        mDatas3.clear();
        mDatas4.clear();
        mAdapter1.notifyDataSetChanged();
        mAdapter2.notifyDataSetChanged();
        mAdapter3.notifyDataSetChanged();
        mAdapter4.notifyDataSetChanged();
    }

    public void addData(int position, CallBean call) {
        line1.setVisibility(View.VISIBLE);
        line2.setVisibility(View.VISIBLE);
        line3.setVisibility(View.VISIBLE);
        // bg.setVisibility(View.GONE);
//        if(callMap!=null) {
//
//            Set<Map.Entry<String, Object>> set = callMap.entrySet();
//            // 遍历键值对对象的集合，得到每一个键值对对象
//            for (Map.Entry<String, Object> me : set) {
//                // 根据键值对对象获取键和值
//                String key = me.getKey();
//                LogUtil.e("--1111-",key+"-----"+call.getName());
//                if (key.equals(call.getName())) {
////
//                   type = true;
////
//              }
////
//            }
////
//        }
//
//        callMap.put(call.getName(),call);

        if (call.getCallType() == 5) {//setting value from pos
            if (mAdapter1.getPrinterGroupId() <= 0 || mAdapter1.getPrinterGroupId() == call.getPrinterGroupId()) {
                tvTitle1.setText(call.getPrinterGroupName());
                addData(re_one, mAdapter1, mDatas1, call, position);
            } else if (mAdapter2.getPrinterGroupId() <= 0 || mAdapter2.getPrinterGroupId() == call.getPrinterGroupId()) {
                tvTitle2.setText(call.getPrinterGroupName());
                addData(re_two, mAdapter2, mDatas2, call, position);
            } else if (mAdapter3.getPrinterGroupId() <= 0 || mAdapter3.getPrinterGroupId() == call.getPrinterGroupId()) {
                tvTitle3.setText(call.getPrinterGroupName());
                addData(re_three, mAdapter3, mDatas3, call, position);
            } else if (mAdapter4.getPrinterGroupId() <= 0 || mAdapter4.getPrinterGroupId() == call.getPrinterGroupId()) {
                tvTitle4.setText(call.getPrinterGroupName());
                addData(re_four, mAdapter4, mDatas4, call, position);
            }
        } else {

            //region old method
            Iterator<CallBean> it;
            int v = call.getCallTag() % 4;

            switch (v) {
                case 0:
                    it = mDatas1.iterator();
                    while (it.hasNext()) {
                        CallBean calls = it.next();
                        if (calls.getCallNumber().equals(call.getCallNumber())) {
                            it.remove();

                            mAdapter1.notifyDataSetChanged();
                        } else {
                            App.instance.setCall(call);
                        }
                    }
                    mDatas1.add(position, call);
                    mAdapter1.notifyItemInserted(position);
                    re_one.scrollToPosition(position);
                    break;
                case 1:
                    it = mDatas2.iterator();
                    while (it.hasNext()) {
                        CallBean calls = it.next();
                        if (calls.getCallNumber().equals(call.getCallNumber())) {
                            it.remove();
                            mAdapter2.notifyDataSetChanged();
                        } else {
                            App.instance.setCall(call);
                        }
                    }
                    mDatas2.add(position, call);
                    mAdapter2.notifyItemInserted(position);
                    re_two.scrollToPosition(position);

                    break;

                case 2:
                    it = mDatas3.iterator();
                    while (it.hasNext()) {
                        CallBean calls = it.next();
                        if (calls.getCallNumber().equals(call.getCallNumber())) {
                            it.remove();
                            mAdapter3.notifyDataSetChanged();
                        } else {
                            App.instance.setCall(call);
                        }
                    }
                    mDatas3.add(position, call);
                    mAdapter3.notifyItemInserted(position);
                    re_three.scrollToPosition(position);
                    break;
                case 3:
                    it = mDatas4.iterator();
                    while (it.hasNext()) {
                        CallBean calls = it.next();
                        if (calls.getCallNumber().equals(call.getCallNumber())) {
                            it.remove();
                            mAdapter4.notifyDataSetChanged();
                        } else {
                            App.instance.setCall(call);
                        }
                    }
                    mDatas4.add(position, call);
                    mAdapter4.notifyItemInserted(position);
                    re_four.scrollToPosition(position);
                    break;

            }
            //endregion
        }


        //  mAdapter.notifyItemRangeChanged(position,mDatas.size()-position);


//        }else {
//
//            Iterator<CallBean> it = mDatas1.iterator();
//            while (it.hasNext())
//            {
//                CallBean calls = it.next();
//                if (calls.getName().equals(call.getName()) )
//                {
//                    it.remove();
//                }
//            }
//            mAdapter1.notifyDataSetChanged();
//            mDatas1.add(position, call);
//            mAdapter1.notifyItemInserted(position);
//
//            //  mAdapter.notifyItemRangeChanged(position,mDatas.size()-position);
//
//            re_one.scrollToPosition(position);
//            re_two.scrollToPosition(position);
//            re_three.scrollToPosition(position);
//            re_four.scrollToPosition(position);
//
//        }


    }

    private void addData(RecyclerView rcv, MycallAdapter adapter, List<CallBean> data,
                         CallBean call, int position) {

        Iterator<CallBean> it = data.iterator();
        while (it.hasNext()) {
            CallBean calls = it.next();
            if (calls.getCallNumber().equals(call.getCallNumber())) {
                it.remove();

                adapter.notifyDataSetChanged();
            } else {
                App.instance.setCall(call);
            }
        }

        if (adapter.getPrinterGroupId() <= 0) {
            adapter.setPrinterGroupId(call.getPrinterGroupId());
        }

        data.add(position, call);
        adapter.notifyItemInserted(position);
        rcv.scrollToPosition(position);
    }


    @Override
    public void onResume() {

        //   initData();
        super.onResume();
    }


    private void initData() {
        List<CallBean> callList = App.instance.getCallList();


        for (int i = 0; i < callList.size(); i++) {


            CallBean call = callList.get(i);
            int v = call.getCallTag() % 4;
            switch (v) {
                case 1:

                    mDatas1.add(call);

                    break;
                case 2:
                    mDatas2.add(call);
                    break;

                case 3:
                    mDatas3.add(call);
                    break;
                case 0:
                    mDatas4.add(call);

                    break;

            }


//
//        for (int i = 0; i < 20; i++) {
//
//            CallBean call = new CallBean();
//            call.setId(i);
//            call.setName("name " + i);
//            mDatas.add(call);
//        }

//        Collections.reverse(mDatas);
//        mAdapter.notifyDataSetChanged();
        }
        mAdapter1.notifyDataSetChanged();
        mAdapter2.notifyDataSetChanged();
        mAdapter3.notifyDataSetChanged();
        mAdapter4.notifyDataSetChanged();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        //    App.instance.setSave();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
