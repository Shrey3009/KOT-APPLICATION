package com.alfredposclient.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.CheckBox;

import com.alfredbase.javabean.temporaryforapp.AppOrder;
import com.alfredposclient.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DeliveryApdater extends BaseAdapter {


    private ArrayList<AppOrder> receiveDatas;// 接收的数据
    private OnSelectedItemChanged callBack;// 内部接口:监听选中项的个数（随着item被点击而改变）
    private LayoutInflater inflater;

    private Map<Integer, Boolean> map;// 保存被选中与否的状态的集合
    private ArrayList<AppOrder> selectedItems;// 被选中项的集合

    /**
     * 构造方法
     *
     * @param context
     * @param receiveDatas
     */
    public DeliveryApdater(Context context, ArrayList<AppOrder> receiveDatas,
                           OnSelectedItemChanged callBack) {
        inflater = LayoutInflater.from(context);
        this.receiveDatas = receiveDatas;
        this.callBack = callBack;

        // 初始化被选中项
        map = new HashMap<Integer, Boolean>();
        for (int i = 0; i < receiveDatas.size(); i++) {
            map.put(i, false);
        }
        // 初始化被选中项的集合
        selectedItems = new ArrayList<AppOrder>();
    }

    /**
     * 数据的设置
     *
     * @param receiveDatas
     */
    public void setDatas(ArrayList<AppOrder> receiveDatas) {
        this.receiveDatas = receiveDatas;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return receiveDatas.size();
    }

    @Override
    public AppOrder getItem(int position) {
        // TODO Auto-generated method stub
        return receiveDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return receiveDatas.get(position).getId();
    }

//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//        return null;
//    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 1.1、初始化ViewHolder控件
        ViewHolder holder = null;
        // 1.2、初始化控件属性
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_select, null);
            holder = new ViewHolder();
            holder.cb = (CheckBox) convertView.findViewById(R.id.select);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        // 2.1获取对象
        final AppOrder student = receiveDatas.get(position);
        // 2.2 获取对象属性
        holder.id = student.getId();
        final CheckBox cb = holder.cb;
        cb.setText( student.getId() + "");// CheckBox显示内容
        // 2.3 对象属性监听
        cb.setOnClickListener(new View.OnClickListener() {


            public void onClick(View v) {// 被点击
                if (cb.isChecked()) {// 被选中
                    map.put(position, true);
                    callBack.getSelectedCount(getSelectedCount());//个数监听
                    callBack.getSelectedItem(student);//被选项监听
                } else {// 未被选中
                    if (map.containsKey(position)) {// 选中项与否集合中包含此项
                        if (map.get(position) == true)
                            map.put(position, false);
                        callBack.getSelectedCount(getSelectedCount());//个数监听
                    }
                }

            }
        });
        // 2.4 设置对象属性
        cb.setChecked(map.get(position));
        return convertView;
    }

    /**
     * 被选中项的个数
     *
     * @return
     */
    private int getSelectedCount() {
        int i = 0;
        for (Map.Entry<Integer, Boolean> entry : map.entrySet()) {
            if (entry.getValue())
                i++;
        }
        return i;
    }

    public class ViewHolder {
        public int id;
        public CheckBox cb;
    }

    /**
     * 内部监听接口：向Activity暴露选择了多少项
     */
    public interface OnSelectedItemChanged {
        /**
         * 回调：处理被选中项个数
         *
         * @param count
         */
        public void getSelectedCount(int count);

        public void getSelectedItem(AppOrder appOrder);

    }

    /**
     * 全选:改变状态+更新item界面+个数监听
     */
    public void selectAll() {
        for (int i = 0; i < map.size(); i++) {
            map.put(i, true);
        }
        notifyDataSetChanged();
        callBack.getSelectedCount(getSelectedCount());
    }

    /**
     * 全不选：改变状态+更新item界面+个数监听
     */
    public void disSelectAll() {
        for (int i = 0; i < map.size(); i++) {
            map.put(i, false);
        }
        notifyDataSetChanged();
        callBack.getSelectedCount(getSelectedCount());
    }

    /**
     * 反选:改变状态+更新item界面+个数监听
     */
    public void switchSelect() {
        for (int i = 0; i < map.size(); i++) {
            boolean select = map.get(i);
            map.put(i, !select);
        }
        notifyDataSetChanged();
        callBack.getSelectedCount(getSelectedCount());
    }

    /**
     * 当前被选中项
     *
     * @return
     */
    public ArrayList<AppOrder> currentSelect() {
        selectedItems.clear();
        for (int i = 0; i < map.size(); i++) {
            if (map.get(i))
                this.selectedItems.add(receiveDatas.get(i));
        }
        return this.selectedItems;
    }
}
