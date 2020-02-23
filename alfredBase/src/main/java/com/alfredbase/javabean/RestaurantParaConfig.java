package com.alfredbase.javabean;

public class RestaurantParaConfig {
    private Integer id;

    private Integer restaurantId;

    private Integer paraId;

    private Integer paraType;

    private String paraName;
    
    private String paraValue1;

    private String paraValue2;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(Integer restaurantId) {
        this.restaurantId = restaurantId;
    }

    public Integer getParaId() {
        return paraId;
    }

    public void setParaId(Integer paraId) {
        this.paraId = paraId;
    }

    public Integer getParaType() {
        return paraType;
    }

    public void setParaType(Integer paraType) {
        this.paraType = paraType;
    }

    public String getParaName() {
        return paraName;
    }

    public void setParaName(String paraName) {
        this.paraName = paraName == null ? null : paraName.trim();
    }

    public String getParaValue1() {
        return paraValue1;
    }

    public void setParaValue1(String paraValue1) {
        this.paraValue1 = paraValue1 == null ? null : paraValue1.trim();
    }

    public String getParaValue2() {
        return paraValue2;
    }

    public void setParaValue2(String paraValue2) {
        this.paraValue2 = paraValue2 == null ? null : paraValue2.trim();
    }
}