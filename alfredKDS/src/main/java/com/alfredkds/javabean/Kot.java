package com.alfredkds.javabean;

import java.util.List;

import com.alfredbase.javabean.KotItemDetail;
import com.alfredbase.javabean.KotItemModifier;
import com.alfredbase.javabean.KotSummary;

public class Kot {
    private List<KotItemDetail> kotItemDetails;
    private List<KotItemModifier> kotItemModifiers;
    private KotSummary kotSummary;
    private boolean isPlaceOrder;

    public Kot() {

    }

    public Kot(List<KotItemDetail> kotItemDetails,
               List<KotItemModifier> kotItemModifiers, KotSummary kotSummary) {
        super();
        this.kotItemDetails = kotItemDetails;
        this.kotItemModifiers = kotItemModifiers;
        this.kotSummary = kotSummary;
    }

    /**
     * @return the kotItemDetails
     */
    public List<KotItemDetail> getKotItemDetails() {
        return kotItemDetails;
    }

    /**
     * @param kotItemDetails the kotItemDetails to set
     */
    public void setKotItemDetails(List<KotItemDetail> kotItemDetails) {
        this.kotItemDetails = kotItemDetails;
    }

    /**
     * @return the kotItemModifiers
     */
    public List<KotItemModifier> getKotItemModifiers() {
        return kotItemModifiers;
    }

    /**
     * @param kotItemModifiers the kotItemModifiers to set
     */
    public void setKotItemModifiers(List<KotItemModifier> kotItemModifiers) {
        this.kotItemModifiers = kotItemModifiers;
    }

    /**
     * @return the kotSummary
     */
    public KotSummary getKotSummary() {
        return kotSummary;
    }

    /**
     * @param kotSummary the kotSummary to set
     */
    public void setKotSummary(KotSummary kotSummary) {
        this.kotSummary = kotSummary;
    }

    public boolean isPlaceOrder() {
        return isPlaceOrder;
    }

    public void setPlaceOrder(boolean placeOrder) {
        isPlaceOrder = placeOrder;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "Kot [kotItemDetails=" + kotItemDetails + ", kotItemModifiers="
                + kotItemModifiers + ", kotSummary=" + kotSummary + "]";
    }

}
