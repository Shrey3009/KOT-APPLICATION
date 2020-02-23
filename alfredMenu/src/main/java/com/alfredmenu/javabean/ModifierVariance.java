package com.alfredmenu.javabean;

/**
 * Created by Alex on 2017/4/8.
 */

public class ModifierVariance {
    private int modifierId1;
    private String modifierName1;
    private int modifierId2;
    private String modifierName2;
    private boolean isModifier;
    private int mustDefault;
    private int modQty = 0;

    public int getModQty() {
        return modQty;
    }

    public void setModQty(int modQty) {
        this.modQty = modQty;
    }

    public int getMustDefault() {
        return mustDefault;
    }

    public void setMustDefault(int mustDefault) {
        this.mustDefault = mustDefault;
    }

    public int getModifierId1() {
        return modifierId1;
    }

    public void setModifierId1(int modifierId1) {
        this.modifierId1 = modifierId1;
    }

    public String getModifierName1() {
        return modifierName1;
    }

    public void setModifierName1(String modifierName1) {
        this.modifierName1 = modifierName1;
    }

    public int getModifierId2() {
        return modifierId2;
    }

    public void setModifierId2(int modifierId2) {
        this.modifierId2 = modifierId2;
    }

    public String getModifierName2() {
        return modifierName2;
    }

    public void setModifierName2(String modifierName2) {
        this.modifierName2 = modifierName2;
    }

    public boolean isModifier() {
        return isModifier;
    }

    public void setModifier(boolean modifier) {
        isModifier = modifier;
    }

    @Override
    public String toString() {
        return "ModifierVariance{" +
                "modifierId1=" + modifierId1 +
                ", modifierName1='" + modifierName1 + '\'' +
                ", modifierId2=" + modifierId2 +
                ", modifierName2='" + modifierName2 + '\'' +
                ", isModifier=" + isModifier +
                ", mustDefault=" + mustDefault +
                ", modQty=" + modQty +
                '}';
    }
}
