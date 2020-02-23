package com.alfredposclient.javabean;

public class PayhalalCheckStatusDao {

    private int resultCode;
    private ResultData resultData;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public ResultData getResultData() {
        return resultData;
    }

    public void setResultData(ResultData resultData) {
        this.resultData = resultData;
    }

    public static class ResultData {
        private int last_update;
        private String currency;
        private String amount;
        private String status;

        public int getLast_update() {
            return last_update;
        }

        public void setLast_update(int last_update) {
            this.last_update = last_update;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
