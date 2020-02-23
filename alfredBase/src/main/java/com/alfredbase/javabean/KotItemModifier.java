package com.alfredbase.javabean;

import java.io.Serializable;

public class KotItemModifier implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = -1092667120979695798L;

    private Integer id;

    private Integer kotItemDetailId;

    private Integer modifierId;

    private String modifierName;

    private Integer modifierNum;

    private Integer status;

    private Integer printerId;

    private String uniqueId;

    private String kotItemDetailUniqueId;

    public KotItemModifier() {
    }

    public KotItemModifier(Integer id, Integer kotItemDetailId,
                           Integer modifierId, String modifierName, Integer modifierNum,
                           Integer status) {
        super();
        this.id = id;
        this.kotItemDetailId = kotItemDetailId;
        this.modifierId = modifierId;
        this.modifierName = modifierName;
        this.modifierNum = modifierNum;
        this.status = status;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getKotItemDetailId() {
        return kotItemDetailId;
    }

    public void setKotItemDetailId(Integer kotItemDetailId) {
        this.kotItemDetailId = kotItemDetailId;
    }

    public Integer getModifierId() {
        return modifierId;
    }

    public void setModifierId(Integer modifierId) {
        this.modifierId = modifierId;
    }

    public String getModifierName() {
        return modifierName;
    }

    public void setModifierName(String modifierName) {
        this.modifierName = modifierName == null ? null : modifierName.trim();
    }

    public Integer getModifierNum() {
        return modifierNum;
    }

    public void setModifierNum(Integer modifierNum) {
        this.modifierNum = modifierNum;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getPrinterId() {
        return printerId;
    }

    public void setPrinterId(Integer printerId) {
        this.printerId = printerId;
    }

    public String getUniqueId() {
        return uniqueId;
    }

    public void setUniqueId(String uniqueId) {
        this.uniqueId = uniqueId;
    }

    public String getKotItemDetailUniqueId() {
        return kotItemDetailUniqueId;
    }

    public void setKotItemDetailUniqueId(String kotItemDetailUniqueId) {
        this.kotItemDetailUniqueId = kotItemDetailUniqueId;
    }

    @Override
    public String toString() {
        return "KotItemModifier{" +
                "id=" + id +
                ", kotItemDetailId=" + kotItemDetailId +
                ", modifierId=" + modifierId +
                ", modifierName='" + modifierName + '\'' +
                ", modifierNum=" + modifierNum +
                ", status=" + status +
                ", printerId=" + printerId +
                ", uniqueId='" + uniqueId + '\'' +
                ", kotItemDetailUniqueId='" + kotItemDetailUniqueId + '\'' +
                '}';
    }

}