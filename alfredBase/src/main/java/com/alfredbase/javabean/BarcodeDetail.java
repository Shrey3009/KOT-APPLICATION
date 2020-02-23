package com.alfredbase.javabean;

import java.io.Serializable;

public class BarcodeDetail implements Serializable
{
    private Integer barCodeLength;
    private Integer barCodePriority;
    private Integer barCodePriceFront;

    private String barCodeName;

    public BarcodeDetail()
    {

    }

    public Integer getBarCodeLength()
    {
        return barCodeLength;
    }

    public Integer getBarCodePriority()
    {
        return barCodePriority;
    }

    public String getBarCodeName()
    {
        return barCodeName;
    }

    public Integer getBarCodePriceFront()
    {
        return barCodePriceFront;
    }

    public void setBarCodeLength(Integer barCodeLength)
    {
        this.barCodeLength = barCodeLength;
    }

    public void setBarCodePriority(Integer barCodePriority)
    {
        this.barCodePriority = barCodePriority;
    }

    public void setBarCodeName(String barCodeName)
    {
        this.barCodeName = barCodeName;
    }

    public void setBarCodePriceFront(Integer barCodePriceFront)
    {
        this.barCodePriceFront = barCodePriceFront;
    }
}