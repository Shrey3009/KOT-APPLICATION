package com.alfredbase.store.sql;

import android.database.Cursor;
import android.util.Log;

import com.alfredbase.javabean.BarcodeDetail;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class UserCustomSQL {
    public static ArrayList<BarcodeDetail> getAllBarCodeProperties()
    {
        ArrayList<BarcodeDetail> barCodeDetails = new ArrayList<>();
        String sql = "select * from " + TableNames.RestaurantConfig + " where paraType = '1007000'";
        Cursor cursor = null;
        try
        {
            cursor = SQLExe.getDB().rawQuery(sql, new String[]{});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor.moveToNext())
            {
                JSONObject barCode = new JSONObject(cursor.getString(5));
                JSONArray barCodeArray = barCode.getJSONArray("barcode_format");

                for (int i=0; i < barCodeArray.length(); i++)
                {
                    try {
                        BarcodeDetail barCodeDetail = new BarcodeDetail();
                        JSONObject barCodeObject = barCodeArray.getJSONObject(i);
                        // Pulling items from the array
                        barCodeDetail.setBarCodeName(barCodeObject.getString("variable_name"));
                        barCodeDetail.setBarCodeLength(Integer.valueOf(barCodeObject.getString("length")));
                        barCodeDetail.setBarCodePriority(Integer.valueOf(barCodeObject.getString("priority")));
                        barCodeDetail.setBarCodePriceFront(Integer.valueOf(barCodeObject.getString("barcode_front_price")));
                        barCodeDetails.add(barCodeDetail);
                    }
                    catch (JSONException e)
                    {
                        Log.e("Error Check", "Database issue: " + e);
                    }
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return barCodeDetails;
    }
}
