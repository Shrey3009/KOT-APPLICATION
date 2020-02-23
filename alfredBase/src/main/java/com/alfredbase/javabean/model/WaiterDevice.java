package com.alfredbase.javabean.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class WaiterDevice implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2131838222405162255L;
	int waiterId;	//	userId
	String mac;
	String ip;
	
	public String getMac() {
		return mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getIP() {
		return ip;
	}
	public void setIP(String ip) {
		this.ip = ip;
	}
	public int getWaiterId() {
		return this.waiterId;
	}
	public void setWaiterId(int waiterId) {
		this.waiterId = waiterId;
	}

    public  Map<String, Object> toMap() {  
        Map<String, Object> map = new HashMap<String, Object>();  
        map.put("mac", this.mac);
        map.put("ip", this.ip);
        map.put("waiter", this.waiterId);
        return map;   
    }
    
    public WaiterDevice clone(){
    	WaiterDevice newone = new WaiterDevice();
    	newone.setIP(this.ip);
    	newone.setMac(this.mac);
    	newone.setWaiterId(this.waiterId);
    	return newone;
    }
	@Override
	public String toString() {
		return "WaiterDevice [waiterId=" + waiterId + ", mac=" + mac + ", IP="
				+ ip + "]";
	}  
}
