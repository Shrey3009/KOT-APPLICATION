package com.alfredbase.javabean;


public class PrintQueueMsg {
	
    private String msgUUID; 
	private String msgType;
    private int charSize = 48;
	private String printerIp;
    private String data;
	private Integer status;
    private Long created;
    private Long bizDate;
	
	public PrintQueueMsg() {
		
	}
	public PrintQueueMsg(String msgUUID, String msgType, int charSize,
			String printerIp, String data, Integer status, Long created,
			Long bizDate) {
		super();
		this.msgUUID = msgUUID;
		this.msgType = msgType;
		this.charSize = charSize;
		this.printerIp = printerIp;
		this.data = data;
		this.status = status;
		this.created = created;
		this.bizDate = bizDate;
	}

	public String getMsgUUID() {
		return msgUUID;
	}

	public void setMsgUUID(String msgUUID) {
		this.msgUUID = msgUUID;
	}

	public int getCharSize() {
		return charSize;
	}

	public void setCharSize(int charSize) {
		this.charSize = charSize;
	}

	public String getPrinterIp() {
		return printerIp;
	}

	public void setPrinterIp(String printerIp) {
		this.printerIp = printerIp;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Long getCreated() {
		return created;
	}

	public void setCreated(Long created) {
		this.created = created;
	}

	public Long getBizDate() {
		return bizDate;
	}

	public void setBizDate(Long bizDate) {
		this.bizDate = bizDate;
	}


	public String getMsgType() {
		return msgType;
	}


	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}
	
	@Override
	public String toString() {
		return "PrintQueueMsg [msgUUID=" + msgUUID + ", msgType=" + msgType
				+ ", charSize=" + charSize + ", printerIp=" + printerIp
				+ ", data=" + data + ", status=" + status + ", created="
				+ created + ", bizDate=" + bizDate + "]";
	}
}
