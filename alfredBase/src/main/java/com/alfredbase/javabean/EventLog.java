package com.alfredbase.javabean;

/**
 * Created by Arif S. on 6/18/19
 */
public class EventLog {
    private int id;
    private int custId;
    private long createdDate;
    private String event;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCustId() {
        return custId;
    }

    public void setCustId(int custId) {
        this.custId = custId;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    @Override
    public String toString() {
        return "EventLog{" +
                "id=" + id +
                ", custId=" + custId +
                ", createdDate=" + createdDate +
                ", event='" + event + '\'' +
                '}';
    }
}
