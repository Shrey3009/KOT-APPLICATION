package com.alfred.callnum.utils;

import android.content.Context;

import com.alfred.callnum.R;
import com.alfred.callnum.adapter.CallBean;

import java.util.LinkedList;


public class MyQueue {
    private String ob = "";
    private LinkedList list = new LinkedList();

    public void clear()//销毁队列
    {
        list.clear();
        ob = "";
    }

    public boolean QueueEmpty()//判断队列是否为空
    {
        return list.isEmpty();
    }

    public void enQueue(Object o)//进队
    {

        CallBean callBean = (CallBean) o;
        list.addLast(o);
        ob = callBean.getCallNumber().toString();


    }

    public Object deQueue(Context context)//出队
    {
        if (!list.isEmpty()) {
            return list.removeFirst();
        }
        return context.getString(R.string.empty_queue);
    }

    public int QueueLength()//获取队列长度
    {
        return list.size();
    }

    public Object QueuePeek()//查看队首元素
    {
        return list.getFirst();
    }


}
