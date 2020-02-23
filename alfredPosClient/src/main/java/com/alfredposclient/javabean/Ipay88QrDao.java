package com.alfredposclient.javabean;

public class Ipay88QrDao {
    private int resultCode;
    private Ipay88QrDao.Data ipay88QrCode;

    public class Data {
        private String amount;
        private String currency;
        private String merchantCode;
        private int paymentId;
        private String qrCode;
        private String qrValue;
        private String refNo;
        private String transactionId;
        private String errorMessage;

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getMerchantCode() {
            return merchantCode;
        }

        public void setMerchantCode(String merchantCode) {
            this.merchantCode = merchantCode;
        }

        public int getPaymentId() {
            return paymentId;
        }

        public void setPaymentId(int paymentId) {
            this.paymentId = paymentId;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        public String getQrValue() {
            return qrValue;
        }

        public void setQrValue(String qrValue) {
            this.qrValue = qrValue;
        }

        public String getRefNo() {
            return refNo;
        }

        public void setRefNo(String refNo) {
            this.refNo = refNo;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public String getErrorMessage() {
            return errorMessage;
        }

        public void setErrorMessage(String errorMessage) {
            this.errorMessage = errorMessage;
        }
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Data getIpay88QrCode() {
        return ipay88QrCode;
    }

    public void setIpay88QrCode(Data ipay88QrCode) {
        this.ipay88QrCode = ipay88QrCode;
    }
}
