package com.alfredbase.store.sql.temporaryforapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.temporaryforapp.AppOrderDetail;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class AppOrderDetailSQL {
    /*
private Integer id; 	//'主键id',
private Integer orderId; 	//'订单id',
private Integer custId; 	//'顾客id',
private Integer itemId; 	//'菜的id',
private String itemName; 	//'菜名称',
private Integer itemNum; 	//'菜的数量',
private String itemPrice; 	//'菜单金额',
private String taxPrice; 	//'税收金额',
private String discountPrice; 	//'打折金额',
private String discountRate; 	//'打折比例',
private String realPrice; 	//'实收金额=(菜单金额-HappyHour金额+配料金额)*数量',
private Integer orderDetailStatus; 	//'订单详情状态(0未确认，1已确认，2已下单，3已支付)',
private Integer discountType; 	//'打折类型(0不打折、1根据比例打折、2直接减)',
private String modifierPrice; 	//'配料价格',
private String specialInstractions; 	//'手动存入的指令，如饭前上餐前酒、饭后上水果等',
private long createTime; 	//'创建时间',
private long updateTime; 	//'更新时间',
 */

    public static void addAppOrderDetai(AppOrderDetail appOrderDetail) {
        if (appOrderDetail == null) {
            return;
        }
        try {
            String sql = "insert into "
                    + TableNames.AppOrderDetail
                    + "(id, orderId, custId, itemId, itemName, itemNum, itemPrice, taxPrice, discountPrice, discountRate, "
                    + " realPrice, orderDetailStatus, discountType, modifierPrice, specialInstractions, createTime, updateTime, totalItemPrice)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLExe.getDB().execSQL(
                    sql,
                    new Object[]{appOrderDetail.getId(), appOrderDetail.getOrderId(),
                            appOrderDetail.getCustId(), appOrderDetail.getItemId(),
                            appOrderDetail.getItemName(), appOrderDetail.getItemNum(),
                            appOrderDetail.getItemPrice(), appOrderDetail.getTaxPrice(),
                            appOrderDetail.getDiscountPrice(), appOrderDetail.getDiscountRate(),
                            appOrderDetail.getRealPrice(), appOrderDetail.getOrderDetailStatus(),
                            appOrderDetail.getDiscountType(), appOrderDetail.getModifierPrice(),
                            appOrderDetail.getSpecialInstractions(), appOrderDetail.getCreateTime(),
                            appOrderDetail.getUpdateTime(), appOrderDetail.getTotalItemPrice()});
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void addAppOrderDetailList(List<AppOrderDetail> appOrderDetailList) {
        if (appOrderDetailList == null) {
            return;
        }
        SQLiteDatabase db = SQLExe.getDB();
        try {
            db.beginTransaction();
            String sql = "replace into "
                    + TableNames.AppOrderDetail
                    + "(id, orderId, custId, itemId, itemName, itemNum, itemPrice, taxPrice, discountPrice, discountRate, "
                    + " realPrice, orderDetailStatus, discountType, modifierPrice, specialInstractions, createTime, updateTime, totalItemPrice)"
                    + " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            SQLiteStatement sqLiteStatement = db.compileStatement(
                    sql);
            for (AppOrderDetail appOrderDetail : appOrderDetailList) {
                SQLiteStatementHelper.bindLong(sqLiteStatement, 1,
                        appOrderDetail.getId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 2,
                        appOrderDetail.getOrderId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 3,
                        appOrderDetail.getCustId());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 4,
                        appOrderDetail.getItemId());
                SQLiteStatementHelper.bindString(sqLiteStatement, 5,
                        appOrderDetail.getItemName());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 6,
                        appOrderDetail.getItemNum());
                SQLiteStatementHelper.bindString(sqLiteStatement, 7,
                        appOrderDetail.getItemPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 8,
                        appOrderDetail.getTaxPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 9,
                        appOrderDetail.getDiscountPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 10,
                        appOrderDetail.getDiscountRate());
                SQLiteStatementHelper.bindString(sqLiteStatement, 11,
                        appOrderDetail.getRealPrice());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 12,
                        appOrderDetail.getOrderDetailStatus());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 13,
                        appOrderDetail.getDiscountType());
                SQLiteStatementHelper.bindString(sqLiteStatement, 14,
                        appOrderDetail.getModifierPrice());
                SQLiteStatementHelper.bindString(sqLiteStatement, 15,
                        appOrderDetail.getSpecialInstractions());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 16,
                        appOrderDetail.getCreateTime());
                SQLiteStatementHelper.bindLong(sqLiteStatement, 17,
                        appOrderDetail.getUpdateTime());
                SQLiteStatementHelper.bindString(sqLiteStatement, 18,
                        appOrderDetail.getTotalItemPrice());

                sqLiteStatement.executeInsert();
            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.endTransaction();
        }
    }


    public static List<AppOrderDetail> getAppOrderDetailByAppOrderId(int appOrderId) {
        String sql = "select * from " + TableNames.AppOrderDetail + " where orderId = ?";
        Cursor cursor = null;
        List<AppOrderDetail> result = new ArrayList<AppOrderDetail>();
        SQLiteDatabase db = SQLExe.getDB();
        try {
            cursor = db.rawQuery(sql, new String[]{appOrderId + ""});
            for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
                    .moveToNext()) {
                AppOrderDetail appOrderDetail = new AppOrderDetail();
                appOrderDetail.setId(cursor.getInt(0));
                appOrderDetail.setOrderId(cursor.getInt(1));
                appOrderDetail.setCustId(cursor.getInt(2));
                appOrderDetail.setItemId(cursor.getInt(3));
                appOrderDetail.setItemName(cursor.getString(4));
                appOrderDetail.setItemNum(cursor.getInt(5));
                appOrderDetail.setItemPrice(cursor.getString(6));
                appOrderDetail.setTaxPrice(cursor.getString(7));
                appOrderDetail.setDiscountPrice(cursor.getString(8));
                appOrderDetail.setDiscountRate(cursor.getString(9));
                appOrderDetail.setRealPrice(cursor.getString(10));
                appOrderDetail.setOrderDetailStatus(cursor.getInt(11));
                appOrderDetail.setDiscountType(cursor.getInt(12));
                appOrderDetail.setModifierPrice(cursor.getString(13));
                appOrderDetail.setSpecialInstractions(cursor.getString(14));
                appOrderDetail.setCreateTime(cursor.getLong(15));
                appOrderDetail.setUpdateTime(cursor.getLong(16));
                appOrderDetail.setTotalItemPrice(cursor.getString(17));
                result.add(appOrderDetail);
            }
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return result;
    }
}