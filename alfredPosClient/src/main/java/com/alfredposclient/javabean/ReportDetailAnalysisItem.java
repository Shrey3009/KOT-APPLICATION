package com.alfredposclient.javabean;

import java.math.BigDecimal;

/**
 * Created by Alex on 2017/12/29.
 */

public class ReportDetailAnalysisItem {
    private String name;
    private boolean showOther;
    private int qty;
    private BigDecimal amount;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isShowOther() {
        return showOther;
    }

    public void setShowOther(boolean showOther) {
        this.showOther = showOther;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "ReportDetailAnalysisItem{" +
                "name='" + name + '\'' +
                ", showOther=" + showOther +
                ", qty=" + qty +
                ", amount=" + amount +
                '}';
    }
}
