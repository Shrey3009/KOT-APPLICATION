package com.alfredposclient.javabean;

import java.math.BigDecimal;

public class PayHalalQrDao {

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
        private String billNumber;
        private int expire;
        private String currency;
        private String amount;
        private String transaction_id;
        private Qr_code qr_code;

        public String getBillNumber() {
            return billNumber;
        }

        public void setBillNumber(String billNumber) {
            this.billNumber = billNumber;
        }

        public int getExpire() {
            return expire;
        }

        public void setExpire(int expire) {
            this.expire = expire;
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

        public String getTransaction_id() {
            return transaction_id;
        }

        public void setTransaction_id(String transaction_id) {
            this.transaction_id = transaction_id;
        }

        public Qr_code getQr_code() {
            return qr_code;
        }

        public void setQr_code(Qr_code qr_code) {
            this.qr_code = qr_code;
        }
    }

    public static class Qr_code {
        private String content;
        private String image_url;

        public String getContent() {
            return content;
        }

        public void setContent(String content) {
            this.content = content;
        }

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }
    }


}
