package com.alfredposclient.javabean;

public class Ipay88CheckStatusDao {
    private int resultCode;
    private Ipay88CheckStatusDao.Data paymentStatus;

    public int getResultCode() {
        return resultCode;
    }

    public Data getPaymentStatus() {
        return paymentStatus;
    }

    public class Data {
        private String amount;
        private String currency;
        private String merchantCode;
        private int paymentId;
        private String status;
        private String transactionId;
        private String errorMessage;

        public String getAmount() {
            return amount;
        }

        public String getCurrency() {
            return currency;
        }

        public String getMerchantCode() {
            return merchantCode;
        }

        public int getPaymentId() {
            return paymentId;
        }

        public String getStatus() {
            return status;
        }

        public String getTransactionId() {
            return transactionId;
        }

        public String getErrorMessage() {
            return errorMessage;
        }
    }

}
