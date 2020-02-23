package com.alfredbase.javabean.model;

import java.io.Serializable;

/**
 * 餐厅开店时间，是哪一餐
 * 
 * @author
 * 
 */
public class SessionStatus implements Serializable {
	private static final long serialVersionUID = -2976701878339858607L;
	private int session_status;
	private long time;

	public SessionStatus() {
	}

	public SessionStatus(int session_status, long time) {
		super();
		this.session_status = session_status;
		this.time = time;
	}

	public int getSession_status() {
		return session_status;
	}

	public void setSession_status(int session_status) {
		this.session_status = session_status;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	@Override
	public String toString() {
		return "SessionStatus [session_status=" + session_status + ", time="
				+ time + "]";
	}

}
