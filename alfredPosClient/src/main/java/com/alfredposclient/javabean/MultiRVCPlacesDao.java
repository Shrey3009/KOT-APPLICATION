package com.alfredposclient.javabean;

import com.alfredbase.javabean.PlaceInfo;
import com.alfredbase.javabean.TableInfo;
import com.alfredbase.javabean.model.MainPosInfo;

import java.util.List;

public class MultiRVCPlacesDao {
    private int resultCode;
    private MultiRVCPlacesDao.Data data;

    public static class Data {
        List<MultiRVCPlacesDao.Places> places;

        public List<Places> getPlaces() {
            return places;
        }

        public void setPlaces(List<Places> places) {
            this.places = places;
        }
    }

    public static class Places {
        private Integer posId;
        private String ip;
        private String placeName;
        private String placeDescription;
        private Integer restaurantId;
        private Integer revenueId;
        private String unionId;
        private Integer isActive;
        private Integer isKiosk = 0;
        private MainPosInfo mainPosInfo;
        private List<TableInfo> tables;

        public Places() {
        }

        public Places(MainPosInfo mainPosInfo, PlaceInfo placeInfo, List<TableInfo> tables) {
            this.ip = mainPosInfo.getIP();
            this.posId = placeInfo.getId();
            this.mainPosInfo = mainPosInfo;
            this.placeName = placeInfo.getPlaceName() + " ( " + mainPosInfo.getName() + " )";
            this.placeDescription = placeInfo.getPlaceDescription();
            this.restaurantId = placeInfo.getRestaurantId();
            this.revenueId = placeInfo.getRevenueId();
            this.unionId = placeInfo.getUnionId();
            this.isActive = placeInfo.getIsActive();
            this.isKiosk = placeInfo.getIsKiosk();
            this.tables = tables;
        }

        public PlaceInfo getPlaceInfo() {
            PlaceInfo placeInfo = new PlaceInfo();
            placeInfo.setId(this.posId);
            placeInfo.setPlaceName(this.placeName);
            placeInfo.setPlaceDescription(this.placeDescription);
            placeInfo.setRestaurantId(this.restaurantId);
            placeInfo.setRevenueId(this.revenueId);
            placeInfo.setUnionId(this.unionId);
            placeInfo.setIsActive(this.isActive);
            placeInfo.setIsKiosk(this.isKiosk);
            return placeInfo;
        }

        public String getIp() {
            return ip;
        }

        public void setIp(String ip) {
            this.ip = ip;
        }

        public MainPosInfo getMainPosInfo() {
            return mainPosInfo;
        }

        public void setMainPosInfo(MainPosInfo mainPosInfo) {
            this.mainPosInfo = mainPosInfo;
        }

        public Integer getPosId() {
            return posId;
        }

        public void setPosId(Integer posId) {
            this.posId = posId;
        }

        public String getPlaceName() {
            return placeName;
        }

        public void setPlaceName(String placeName) {
            this.placeName = placeName;
        }

        public String getPlaceDescription() {
            return placeDescription;
        }

        public void setPlaceDescription(String placeDescription) {
            this.placeDescription = placeDescription;
        }

        public Integer getRestaurantId() {
            return restaurantId;
        }

        public void setRestaurantId(Integer restaurantId) {
            this.restaurantId = restaurantId;
        }

        public Integer getRevenueId() {
            return revenueId;
        }

        public void setRevenueId(Integer revenueId) {
            this.revenueId = revenueId;
        }

        public String getUnionId() {
            return unionId;
        }

        public void setUnionId(String unionId) {
            this.unionId = unionId;
        }

        public Integer getIsActive() {
            return isActive;
        }

        public void setIsActive(Integer isActive) {
            this.isActive = isActive;
        }

        public Integer getIsKiosk() {
            return isKiosk;
        }

        public void setIsKiosk(Integer isKiosk) {
            this.isKiosk = isKiosk;
        }

        public List<TableInfo> getTables() {
            return tables;
        }

        public void setTables(List<TableInfo> tables) {
            this.tables = tables;
        }
    }

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}
