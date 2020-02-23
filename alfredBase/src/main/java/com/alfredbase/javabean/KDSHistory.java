package com.alfredbase.javabean;

import com.alfredbase.javabean.model.KDSDevice;
import com.alfredbase.javabean.model.MainPosInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arif S. on 8/15/19
 */
public class KDSHistory {
    public KDSDevice kdsDevice;
    public KDSDevice kdsDeviceDest;
    public MainPosInfo mainPosInfo;
    public List<KotItemDetail> kotItemDetails = new ArrayList<>();
}
