 package com.alfredbase.javabean;

import android.text.TextUtils;
import android.util.Log;

import com.alfredbase.javabean.model.PrintOrderItem;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class PrinterTitle implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 8078053033115807909L;
	private String restaurantName;
	private String addressDetail;
	private String address;
	private String tel;
	private String email;
	private String webAddress;
	private String op;
	private String pos;
	private String date;
	private ArrayList<PrintOrderItem> list = new ArrayList<PrintOrderItem>();
	private String bill_NO;
	private String time;
	private String tableName;
	private String logo;
	private String bizDate;
	private String options;//header options
	private String footerOptions;// footer options
	private Integer isTakeAway;
	private String orderNo; //流水号
	private String groupNum;
	private Integer isKiosk;
	private Integer copy;

	private int spliteByPax = 0;

	private String revName;
	 private int trainType; // 0 正常模式  1培训模式
	public String getRevName() {
		return revName;
	}

	public void setRevName(String revName) {
		this.revName = revName;
	}

	public Integer getIsKiosk() {
		return isKiosk;
	}

	public void setIsKiosk(Integer isKiosk) {
		this.isKiosk = isKiosk;
	}

	public Integer getCopy() {
		return copy;
	}

	public void setCopy(Integer copy) {
		this.copy = copy;
	}

	public String getGroupNum() {
		return groupNum;
	}

	public void setGroupNum(String groupNum) {
		this.groupNum = groupNum;
	}

	public String getOptions() {
		return options;
	}
	public void setOptions(String options) {
		if (options!=null)
			options = options.trim();
		this.options = options;
	}
	
	public String getFooterOptions() {
		return footerOptions;
	}
	
	public void setFooterOptions(String options) {
		if (options!=null)
			footerOptions = options.trim();
		this.footerOptions = options;
	}

	
	public PrinterTitle(){
	}
	public String getRestaurantName() {
		return restaurantName;
	}
	public void setRestaurantName(String restaurantName) {
		this.restaurantName = restaurantName;
	}
	public String getAddressDetail() {
		if(!TextUtils.isEmpty(addressDetail)){
			// #32
			return addressDetail.replace("</br>", "\n");
		}
		return addressDetail;
	}
	public void setAddressDetail(String addressDetail) {
			this.addressDetail = addressDetail;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getWebAddress() {
		return webAddress;
	}
	public void setWebAddress(String webAddress) {
		this.webAddress = webAddress;
	}
	public String getOp() {
		return op;
	}
	public void setOp(String op) {
		this.op = op;
	}
	public String getPos() {
		return pos;
	}
	public void setPos(String pos) {
		this.pos = pos;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public List<PrintOrderItem> getList() {
		return list;
	}
	public void setList(ArrayList<PrintOrderItem> list) {
		this.list = list;
	}
	public String getBill_NO() {
		return bill_NO;
	}
	public void setBill_NO(String bill_NO) {
		this.bill_NO = bill_NO;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	
	public String getTableName() {
		return tableName;
	}
	public void setTableName(String tableName) {
		this.tableName = tableName;
	}
	public String getLogo() {
		return logo;
	}
	public void setLogo(String logo) {
		this.logo = logo;
	}
	public String getBizDate() {
		return bizDate;
	}
	public void setBizDate(String bizDate) {
		this.bizDate = bizDate;
	}
	public Integer getIsTakeAway() {
		return isTakeAway;
	}
	public void setIsTakeAway(Integer isTakeAway) {
		this.isTakeAway = isTakeAway;
	}
	public String getOrderNo() {
		return orderNo;
	}
	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public Integer getSpliteByPax() {
		return spliteByPax;
	}

	public void setSpliteByPax(Integer spliteByPax) {
		this.spliteByPax = spliteByPax;
	}

	public int getTrainType() {
		return trainType;
	}

	public void setTrainType(int trainType) {
		this.trainType = trainType;
	}

	@Override
	public String toString() {
		return "PrinterTitle{" +
				"restaurantName='" + restaurantName + '\'' +
				", addressDetail='" + addressDetail + '\'' +
				", address='" + address + '\'' +
				", tel='" + tel + '\'' +
				", email='" + email + '\'' +
				", webAddress='" + webAddress + '\'' +
				", op='" + op + '\'' +
				", pos='" + pos + '\'' +
				", date='" + date + '\'' +
				", list=" + list +
				", bill_NO='" + bill_NO + '\'' +
				", time='" + time + '\'' +
				", tableName='" + tableName + '\'' +
				", logo='" + logo + '\'' +
				", bizDate='" + bizDate + '\'' +
				", options='" + options + '\'' +
				", footerOptions='" + footerOptions + '\'' +
				", isTakeAway=" + isTakeAway +
				", orderNo='" + orderNo + '\'' +
				", groupNum='" + groupNum + '\'' +
				", isKiosk=" + isKiosk +
				", copy=" + copy +
				", spliteByPax=" + spliteByPax +
				", revName='" + revName + '\'' +
				'}';
	}
}
