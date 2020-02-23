package com.alfredbase.javabean.model;

/**
 * Created by Alex on 16/8/30.
 */

public class SendFoodCardNum {
    private int cardNum;
    private String cardName;

    public SendFoodCardNum(int cardNum, String cardName) {
        this.cardNum = cardNum;
        this.cardName = cardName;
    }

    public int getCardNum() {
        return cardNum;
    }

    public void setCardNum(int cardNum) {
        this.cardNum = cardNum;
    }

    public String getCardName() {
        return cardName;
    }

    public void setCardName(String cardName) {
        this.cardName = cardName;
    }

    @Override
    public String toString() {
        return "SendFoodCardNum{" +
                "cardNum=" + cardNum +
                ", cardName='" + cardName + '\'' +
                '}';
    }
}
