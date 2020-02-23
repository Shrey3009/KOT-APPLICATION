package com.alfred.callnum.utils;

public class CallNumQueueUtil {

	public String queid;	// 
	public String value;	//	订单号 例如 "02345,"
	public String quenamevoice;
	public int called_num; // 默认为1
	public int number;
	public int custcallwav; // >0
	public int index; // 默认叫两次 第一个为1 第二个为2
	
	public boolean callInEn;
	/**
	 * 参考连续叫两次
	 * QNum num = new QNum("123,", 1,0,1);
	 * QNum num1 = new QNum("123,", 1,0,2);
	 * @param value
	 * @param time
	 * @param param
	 * @param index
	 */
	public CallNumQueueUtil(String value, int time, int param, int index)
	{
		this.value = value;
		this.called_num=time;
		this.number = 1;
		callInEn=false;
		queid="";
		quenamevoice="";
		custcallwav=param;
		this.index = index;
	}
	public CallNumQueueUtil(String file)
	{
		this.value = file;
		this.called_num=1;
		this.number = 0;
	}
	public int getIndex() {
		return index;
	}
	public void setIndex(int index) {
		this.index = index;
	}
	
}
