package com.alfredselfhelp.javabean;

public class NurTagDto {
    public NurTagDto(String barCode, int num) {
        this.barCode = barCode;
        this.num = num;
    }

    private String barCode;
    private int num = 1;

    public String getBarCode() {
        return barCode;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }
}
