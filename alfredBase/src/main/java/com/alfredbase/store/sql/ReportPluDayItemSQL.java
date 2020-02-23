package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;

import com.alfredbase.javabean.ReportPluDayItem;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.SQLiteStatementHelper;

import java.util.ArrayList;
import java.util.List;

public class ReportPluDayItemSQL {
	public static void addReportPluDayItem(ReportPluDayItem reportPluDayItem) {
		if (reportPluDayItem == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.ReportPluDayItem
					+ "(id, reportNo, restaurantId, restaurantName, revenueId, revenueName, businessDate, itemMainCategoryId, "
					+ "itemMainCategoryName, itemCategoryId, itemCategoryName, itemDetailId, itemName, itemPrice, itemCount, "
					+ "itemAmount, itemVoidQty, itemVoidPrice, itemHoldQty, itemHoldPrice, itemFocQty, itemFocPrice, billVoidQty, "
					+ "billVoidPrice, billFocQty, billFocPrice, isOpenItem, daySalesId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { reportPluDayItem.getId(),
							reportPluDayItem.getReportNo(),
							reportPluDayItem.getRestaurantId(),
							reportPluDayItem.getRestaurantName(),
							reportPluDayItem.getRevenueId(),
							reportPluDayItem.getRevenueName(),
							reportPluDayItem.getBusinessDate(),
							reportPluDayItem.getItemMainCategoryId(),
							reportPluDayItem.getItemMainCategoryName(),
							reportPluDayItem.getItemCategoryId(),
							reportPluDayItem.getItemCategoryName(),
							reportPluDayItem.getItemDetailId(),
							reportPluDayItem.getItemName(),
							reportPluDayItem.getItemPrice() == null ? "0.00" : reportPluDayItem.getItemPrice() ,
							reportPluDayItem.getItemCount() == null ? 0 : reportPluDayItem.getItemCount(),
							reportPluDayItem.getItemAmount() == null ? "0.00" : reportPluDayItem.getItemAmount(),
							reportPluDayItem.getItemVoidQty() == null ? 0 : reportPluDayItem.getItemVoidQty(),
							reportPluDayItem.getItemVoidPrice() == null ? "0.00" : reportPluDayItem.getItemVoidPrice(),
							reportPluDayItem.getItemHoldQty() == null ? 0 : reportPluDayItem.getItemHoldQty(),
							reportPluDayItem.getItemHoldPrice() == null ? "0.00" : reportPluDayItem.getItemHoldPrice(),
							reportPluDayItem.getItemFocQty() == null ? 0 :  reportPluDayItem.getItemFocQty(),
							reportPluDayItem.getItemFocPrice() == null ? "0.00" :  reportPluDayItem.getItemFocPrice(),
							reportPluDayItem.getBillVoidQty() == null ? 0 : reportPluDayItem.getBillVoidQty(),
							reportPluDayItem.getBillVoidPrice() == null ? "0.00" : reportPluDayItem.getBillVoidPrice(),
							reportPluDayItem.getBillFocQty() == null ? 0 : reportPluDayItem.getBillFocQty(),
							reportPluDayItem.getBillFocPrice() == null ? "0.00" : reportPluDayItem.getBillVoidPrice(),
							reportPluDayItem.getIsOpenItem(),
							reportPluDayItem.getDaySalesId()
									
							});
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	//ONLY USE to save PLU DAY ITEMs download from cloud
	public static void addReportPluDayItemsFromCloud(List<ReportPluDayItem> reportPluDayItems) {
		if (reportPluDayItems == null)
			return;
		SQLiteDatabase db = SQLExe.getDB();
		try {

			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.ReportPluDayItem
					+ "(reportNo, restaurantId, restaurantName, revenueId, revenueName, businessDate, "
					+ "itemMainCategoryId, itemMainCategoryName, itemCategoryId, itemCategoryName, " 
					+ "itemDetailId, itemName, itemPrice, itemCount, itemAmount, itemVoidQty, "
					+ "itemVoidPrice, itemHoldQty, itemHoldPrice, itemFocQty, itemFocPrice," 
					+ " billVoidQty, billVoidPrice, billFocQty, billFocPrice, isOpenItem, daySalesId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(sql);
			for (ReportPluDayItem reportPluDayItem : reportPluDayItems) {
				SQLiteStatementHelper.bindLong(sqLiteStatement, 1, reportPluDayItem.getReportNo());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 2, reportPluDayItem.getRestaurantId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 3, reportPluDayItem.getRestaurantName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 4, reportPluDayItem.getRevenueId());				
				SQLiteStatementHelper.bindString(sqLiteStatement, 5, reportPluDayItem.getRevenueName());
				
				SQLiteStatementHelper.bindLong(sqLiteStatement, 6, reportPluDayItem.getBusinessDate());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 7, reportPluDayItem.getItemMainCategoryId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 8, reportPluDayItem.getItemMainCategoryName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 9, reportPluDayItem.getItemCategoryId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 10, reportPluDayItem.getItemCategoryName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 11, reportPluDayItem.getItemDetailId());				
				SQLiteStatementHelper.bindString(sqLiteStatement, 12, reportPluDayItem.getItemName());
				

				SQLiteStatementHelper.bindString(sqLiteStatement, 13, reportPluDayItem.getItemPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 14, reportPluDayItem.getItemCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 15, reportPluDayItem.getItemAmount());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 16, reportPluDayItem.getItemVoidQty());				
				SQLiteStatementHelper.bindString(sqLiteStatement, 17, reportPluDayItem.getItemVoidPrice());
				
				SQLiteStatementHelper.bindLong(sqLiteStatement, 18, reportPluDayItem.getItemHoldQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 19, reportPluDayItem.getItemHoldPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 20, reportPluDayItem.getItemFocQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 21, reportPluDayItem.getItemFocPrice());				
				SQLiteStatementHelper.bindLong(sqLiteStatement, 22, reportPluDayItem.getBillVoidQty());				
				SQLiteStatementHelper.bindString(sqLiteStatement, 23, reportPluDayItem.getBillVoidPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 24, reportPluDayItem.getBillFocQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 25, reportPluDayItem.getBillFocPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 26, reportPluDayItem.getIsOpenItem());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 27, reportPluDayItem.getDaySalesId());
				sqLiteStatement.executeInsert();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}


	public static void addReportPluDayItems(List<ReportPluDayItem> reportPluDayItems) {
		if (reportPluDayItems == null)
			return;
		SQLiteDatabase db = SQLExe.getDB();
		try {

			db.beginTransaction();
			String sql = "replace into "
					+ TableNames.ReportPluDayItem
					+ "(reportNo, restaurantId, restaurantName, revenueId, revenueName, businessDate, "
					+ "itemMainCategoryId, itemMainCategoryName, itemCategoryId, itemCategoryName, "
					+ "itemDetailId, itemName, itemPrice, itemCount, itemAmount, itemVoidQty, "
					+ "itemVoidPrice, itemHoldQty, itemHoldPrice, itemFocQty, itemFocPrice,"
					+ " billVoidQty, billVoidPrice, billFocQty, billFocPrice, isOpenItem, daySalesId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(sql);
			for (ReportPluDayItem reportPluDayItem : reportPluDayItems) {
				SQLiteStatementHelper.bindLong(sqLiteStatement, 1, reportPluDayItem.getReportNo());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 2, reportPluDayItem.getRestaurantId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 3, reportPluDayItem.getRestaurantName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 4, reportPluDayItem.getRevenueId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 5, reportPluDayItem.getRevenueName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 6, reportPluDayItem.getBusinessDate());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 7, reportPluDayItem.getItemMainCategoryId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 8, reportPluDayItem.getItemMainCategoryName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 9, reportPluDayItem.getItemCategoryId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 10, reportPluDayItem.getItemCategoryName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 11, reportPluDayItem.getItemDetailId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 12, reportPluDayItem.getItemName());
				SQLiteStatementHelper.bindString(sqLiteStatement, 13, reportPluDayItem.getItemPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 14, reportPluDayItem.getItemCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 15, reportPluDayItem.getItemAmount());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 16, reportPluDayItem.getItemVoidQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 17, reportPluDayItem.getItemVoidPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 18, reportPluDayItem.getItemHoldQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 19, reportPluDayItem.getItemHoldPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 20, reportPluDayItem.getItemFocQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 21, reportPluDayItem.getItemFocPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 22, reportPluDayItem.getBillVoidQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 23, reportPluDayItem.getBillVoidPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 24, reportPluDayItem.getBillFocQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 25, reportPluDayItem.getBillFocPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 26, reportPluDayItem.getIsOpenItem());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 27, reportPluDayItem.getDaySalesId());
				sqLiteStatement.executeInsert();
			}
			db.setTransactionSuccessful();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			db.endTransaction();
		}
	}
	public static void addReportPluDayItems(int daySalesId, SQLiteDatabase db, List<ReportPluDayItem> reportPluDayItems) {
		if (reportPluDayItems == null)
			return;
		try {

			String sql = "replace into "
					+ TableNames.ReportPluDayItem
					+ "(reportNo, restaurantId, restaurantName, revenueId, revenueName, businessDate, "
					+ "itemMainCategoryId, itemMainCategoryName, itemCategoryId, itemCategoryName, "
					+ "itemDetailId, itemName, itemPrice, itemCount, itemAmount, itemVoidQty, "
					+ "itemVoidPrice, itemHoldQty, itemHoldPrice, itemFocQty, itemFocPrice,"
					+ " billVoidQty, billVoidPrice, billFocQty, billFocPrice, isOpenItem, daySalesId)"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLiteStatement sqLiteStatement = db.compileStatement(sql);
			for (ReportPluDayItem reportPluDayItem : reportPluDayItems) {
				SQLiteStatementHelper.bindLong(sqLiteStatement, 1, reportPluDayItem.getReportNo());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 2, reportPluDayItem.getRestaurantId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 3, reportPluDayItem.getRestaurantName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 4, reportPluDayItem.getRevenueId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 5, reportPluDayItem.getRevenueName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 6, reportPluDayItem.getBusinessDate());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 7, reportPluDayItem.getItemMainCategoryId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 8, reportPluDayItem.getItemMainCategoryName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 9, reportPluDayItem.getItemCategoryId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 10, reportPluDayItem.getItemCategoryName());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 11, reportPluDayItem.getItemDetailId());
				SQLiteStatementHelper.bindString(sqLiteStatement, 12, reportPluDayItem.getItemName());
				SQLiteStatementHelper.bindString(sqLiteStatement, 13, reportPluDayItem.getItemPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 14, reportPluDayItem.getItemCount());
				SQLiteStatementHelper.bindString(sqLiteStatement, 15, reportPluDayItem.getItemAmount());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 16, reportPluDayItem.getItemVoidQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 17, reportPluDayItem.getItemVoidPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 18, reportPluDayItem.getItemHoldQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 19, reportPluDayItem.getItemHoldPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 20, reportPluDayItem.getItemFocQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 21, reportPluDayItem.getItemFocPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 22, reportPluDayItem.getBillVoidQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 23, reportPluDayItem.getBillVoidPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 24, reportPluDayItem.getBillFocQty());
				SQLiteStatementHelper.bindString(sqLiteStatement, 25, reportPluDayItem.getBillFocPrice());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 26, reportPluDayItem.getIsOpenItem());
				SQLiteStatementHelper.bindLong(sqLiteStatement, 27, daySalesId);
				sqLiteStatement.executeInsert();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
	}

	public static ReportPluDayItem getReportPluDayItem(
			Integer reportPluDayItemID) {
		ReportPluDayItem reportPluDayItem = null;
		String sql = "select * from " + TableNames.ReportPluDayItem
				+ " where id = ?";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { reportPluDayItemID + "" });
			if (cursor.moveToFirst()) {
				reportPluDayItem = new ReportPluDayItem();
				reportPluDayItem.setId(cursor.getInt(0));
				reportPluDayItem.setReportNo(cursor.getInt(1));
				reportPluDayItem.setRestaurantId(cursor.getInt(2));
				reportPluDayItem.setRestaurantName(cursor.getString(3));
				reportPluDayItem.setRevenueId(cursor.getInt(4));
				reportPluDayItem.setRevenueName(cursor.getString(5));
				reportPluDayItem.setBusinessDate(cursor.getLong(6));
				reportPluDayItem.setItemMainCategoryId(cursor.getInt(7));
				reportPluDayItem.setItemMainCategoryName(cursor.getString(8));
				reportPluDayItem.setItemCategoryId(cursor.getInt(9));
				reportPluDayItem.setItemCategoryName(cursor.getString(10));
				reportPluDayItem.setItemDetailId(cursor.getInt(11));
				reportPluDayItem.setItemName(cursor.getString(12));
				reportPluDayItem.setItemPrice(cursor.getString(13));
				reportPluDayItem.setItemCount(cursor.getInt(14));
				reportPluDayItem.setItemAmount(cursor.getString(15));
				reportPluDayItem.setItemVoidQty(cursor.getInt(16));
				reportPluDayItem.setItemVoidPrice(cursor.getString(17));
				reportPluDayItem.setItemHoldQty(cursor.getInt(18));
				reportPluDayItem.setItemHoldPrice(cursor.getString(19));
				reportPluDayItem.setItemFocQty(cursor.getInt(20));
				reportPluDayItem.setItemFocPrice(cursor.getString(21));
				reportPluDayItem.setBillVoidQty(cursor.getInt(22));
				reportPluDayItem.setBillVoidPrice(cursor.getString(23));
				reportPluDayItem.setBillFocQty(cursor.getInt(24));
				reportPluDayItem.setBillFocPrice(cursor.getString(25));
				reportPluDayItem.setIsOpenItem(cursor.getInt(26));
				reportPluDayItem.setDaySalesId(cursor.getInt(27));
				return reportPluDayItem;
			}
		} catch (Exception e) {
			e.printStackTrace();

		} finally {
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}
		}
		return reportPluDayItem;
	}
	public static ArrayList<ReportPluDayItem> getReportPluDayItemForZReport(long businessDate) {
		ArrayList<ReportPluDayItem> result = new ArrayList<>();
		String sql = "select restaurantId, restaurantName, revenueId, revenueName, businessDate, itemMainCategoryId, itemMainCategoryName, itemCategoryId, itemCategoryName, itemDetailId, itemName,"
				+ " itemPrice, sum(itemCount) itemCount, sum(itemAmount) itemAmount, sum(itemVoidQty) itemVoidQty, sum(itemVoidPrice) itemVoidPrice,"
				+ " sum(itemHoldQty) itemHoldQty, sum(itemHoldPrice) itemHoldPrice, sum(itemFocQty) itemFocQty, sum(itemFocPrice) itemFocPrice,"
				+ " sum(billVoidQty) billVoidQty, sum(billVoidPrice) billVoidPrice, sum(billFocQty) billFocQty, sum(billFocPrice) billFocPrice from "
				+ TableNames.ReportPluDayItem
				+ " where businessDate = ? group by itemDetailId";
		Cursor cursor = null;
		try {
			cursor = SQLExe.getDB().rawQuery(sql,
					new String[] { businessDate + "" });
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ReportPluDayItem reportPluDayItem = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				reportPluDayItem = new ReportPluDayItem();
				reportPluDayItem.setRestaurantId(cursor.getInt(0));
				reportPluDayItem.setRestaurantName(cursor.getString(1));
				reportPluDayItem.setRevenueId(cursor.getInt(2));
				reportPluDayItem.setRevenueName(cursor.getString(3));
				reportPluDayItem.setBusinessDate(cursor.getLong(4));
				reportPluDayItem.setItemMainCategoryId(cursor.getInt(5));
				reportPluDayItem.setItemMainCategoryName(cursor.getString(6));
				reportPluDayItem.setItemCategoryId(cursor.getInt(7));
				reportPluDayItem.setItemCategoryName(cursor.getString(8));
				reportPluDayItem.setItemDetailId(cursor.getInt(9));
				reportPluDayItem.setItemName(cursor.getString(10));
				reportPluDayItem.setItemPrice(BH.getBD(cursor.getString(11)).toString());
				reportPluDayItem.setItemCount(cursor.getInt(12));
				reportPluDayItem.setItemAmount(BH.getBD(cursor.getString(13)).toString());
				reportPluDayItem.setItemVoidQty(cursor.getInt(14));
				reportPluDayItem.setItemVoidPrice(BH.getBD(cursor.getString(15)).toString());
				reportPluDayItem.setItemHoldQty(cursor.getInt(16));
				reportPluDayItem.setItemHoldPrice(BH.getBD(cursor.getString(17)).toString());
				reportPluDayItem.setItemFocQty(cursor.getInt(18));
				reportPluDayItem.setItemFocPrice(BH.getBD(cursor.getString(19)).toString());
				reportPluDayItem.setBillVoidQty(cursor.getInt(20));
				reportPluDayItem.setBillVoidPrice(BH.getBD(cursor.getString(21)).toString());
				reportPluDayItem.setBillFocQty(cursor.getInt(22));
				reportPluDayItem.setBillFocPrice(BH.getBD(cursor.getString(23)).toString());
				result.add(reportPluDayItem);
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

//	public static List<ReportVoidItem> getReportVoidItemForZReport(long businessDate) {
//		List<ReportVoidItem> result = new ArrayList<>();
//		String sql = "select itemName, itemPrice, sum(itemVoidQty + billVoidQty), sum(itemVoidPrice + billVoidPrice),"
//				+ " sum(itemHoldQty) itemHoldQty, sum(itemHoldPrice) itemHoldPrice, sum(itemFocQty) itemFocQty, sum(itemFocPrice) itemFocPrice,"
//				+ " sum(billVoidQty) billVoidQty, sum(billVoidPrice) billVoidPrice, sum(billFocQty) billFocQty, sum(billFocPrice) billFocPrice from "
//				+ TableNames.ReportPluDayItem
//				+ " where businessDate = ? and (itemVoidQty > 0  or billVoidQty > 0) group by itemDetailId";
//		Cursor cursor = null;
//		try {
//			cursor = SQLExe.getDB().rawQuery(sql,
//					new String[] { businessDate + "" });
//			int count = cursor.getCount();
//			if (count < 1) {
//				return result;
//			}
//			ReportPluDayItem reportPluDayItem = null;
//			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
//					.moveToNext()) {
//				reportPluDayItem = new ReportPluDayItem();
//				reportPluDayItem.setRestaurantId(cursor.getInt(0));
//				reportPluDayItem.setRestaurantName(cursor.getString(1));
//				reportPluDayItem.setRevenueId(cursor.getInt(2));
//				reportPluDayItem.setRevenueName(cursor.getString(3));
//				reportPluDayItem.setBusinessDate(cursor.getLong(4));
//				reportPluDayItem.setItemMainCategoryId(cursor.getInt(5));
//				reportPluDayItem.setItemMainCategoryName(cursor.getString(6));
//				reportPluDayItem.setItemCategoryId(cursor.getInt(7));
//				reportPluDayItem.setItemCategoryName(cursor.getString(8));
//				reportPluDayItem.setItemDetailId(cursor.getInt(9));
//				reportPluDayItem.setItemName(cursor.getString(10));
//				reportPluDayItem.setItemPrice(cursor.getString(11));
//				reportPluDayItem.setItemCount(cursor.getInt(12));
//				reportPluDayItem.setItemAmount(cursor.getString(13));
//				reportPluDayItem.setItemVoidQty(cursor.getInt(14));
//				reportPluDayItem.setItemVoidPrice(cursor.getString(15));
//				reportPluDayItem.setItemHoldQty(cursor.getInt(16));
//				reportPluDayItem.setItemHoldPrice(cursor.getString(17));
//				reportPluDayItem.setItemFocQty(cursor.getInt(18));
//				reportPluDayItem.setItemFocPrice(cursor.getString(19));
//				reportPluDayItem.setBillVoidQty(cursor.getInt(20));
//				reportPluDayItem.setBillVoidPrice(cursor.getString(21));
//				reportPluDayItem.setBillFocQty(cursor.getInt(22));
//				reportPluDayItem.setBillFocPrice(cursor.getString(23));
//				result.add(reportPluDayItem);
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//
//		} finally {
//			if (cursor != null && !cursor.isClosed()) {
//				cursor.close();
//			}
//		}
//		return result;
//	}

	public static ArrayList<ReportPluDayItem> getAllReportPluDayItem() {
		ArrayList<ReportPluDayItem> result = new ArrayList<ReportPluDayItem>();
		String sql = "select * from " + TableNames.ReportPluDayItem;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ReportPluDayItem reportPluDayItem = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				reportPluDayItem = new ReportPluDayItem();
				reportPluDayItem.setId(cursor.getInt(0));
				reportPluDayItem.setReportNo(cursor.getInt(1));
				reportPluDayItem.setRestaurantId(cursor.getInt(2));
				reportPluDayItem.setRestaurantName(cursor.getString(3));
				reportPluDayItem.setRevenueId(cursor.getInt(4));
				reportPluDayItem.setRevenueName(cursor.getString(5));
				reportPluDayItem.setBusinessDate(cursor.getLong(6));
				reportPluDayItem.setItemMainCategoryId(cursor.getInt(7));
				reportPluDayItem.setItemMainCategoryName(cursor.getString(8));
				reportPluDayItem.setItemCategoryId(cursor.getInt(9));
				reportPluDayItem.setItemCategoryName(cursor.getString(10));
				reportPluDayItem.setItemDetailId(cursor.getInt(11));
				reportPluDayItem.setItemName(cursor.getString(12));
				reportPluDayItem.setItemPrice(cursor.getString(13));
				reportPluDayItem.setItemCount(cursor.getInt(14));
				reportPluDayItem.setItemAmount(cursor.getString(15));
				reportPluDayItem.setItemVoidQty(cursor.getInt(16));
				reportPluDayItem.setItemVoidPrice(cursor.getString(17));
				reportPluDayItem.setItemHoldQty(cursor.getInt(18));
				reportPluDayItem.setItemHoldPrice(cursor.getString(19));
				reportPluDayItem.setItemFocQty(cursor.getInt(20));
				reportPluDayItem.setItemFocPrice(cursor.getString(21));
				reportPluDayItem.setBillVoidQty(cursor.getInt(22));
				reportPluDayItem.setBillVoidPrice(cursor.getString(23));
				reportPluDayItem.setBillFocQty(cursor.getInt(24));
				reportPluDayItem.setBillFocPrice(cursor.getString(25));
				reportPluDayItem.setIsOpenItem(cursor.getInt(26));
				reportPluDayItem.setDaySalesId(cursor.getInt(27));
				result.add(reportPluDayItem);
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
	public static ArrayList<ReportPluDayItem> getAllReportPluDayItemByDaySalesId(int daySalesId) {
		ArrayList<ReportPluDayItem> result = new ArrayList<>();
		String sql = "select * from " + TableNames.ReportPluDayItem + " where daySalesId = ?";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {daySalesId+""});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ReportPluDayItem reportPluDayItem = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				reportPluDayItem = new ReportPluDayItem();
				reportPluDayItem.setId(cursor.getInt(0));
				reportPluDayItem.setReportNo(cursor.getInt(1));
				reportPluDayItem.setRestaurantId(cursor.getInt(2));
				reportPluDayItem.setRestaurantName(cursor.getString(3));
				reportPluDayItem.setRevenueId(cursor.getInt(4));
				reportPluDayItem.setRevenueName(cursor.getString(5));
				reportPluDayItem.setBusinessDate(cursor.getLong(6));
				reportPluDayItem.setItemMainCategoryId(cursor.getInt(7));
				reportPluDayItem.setItemMainCategoryName(cursor.getString(8));
				reportPluDayItem.setItemCategoryId(cursor.getInt(9));
				reportPluDayItem.setItemCategoryName(cursor.getString(10));
				reportPluDayItem.setItemDetailId(cursor.getInt(11));
				reportPluDayItem.setItemName(cursor.getString(12));
				reportPluDayItem.setItemPrice(cursor.getString(13));
				reportPluDayItem.setItemCount(cursor.getInt(14));
				reportPluDayItem.setItemAmount(cursor.getString(15));
				reportPluDayItem.setItemVoidQty(cursor.getInt(16));
				reportPluDayItem.setItemVoidPrice(cursor.getString(17));
				reportPluDayItem.setItemHoldQty(cursor.getInt(18));
				reportPluDayItem.setItemHoldPrice(cursor.getString(19));
				reportPluDayItem.setItemFocQty(cursor.getInt(20));
				reportPluDayItem.setItemFocPrice(cursor.getString(21));
				reportPluDayItem.setBillVoidQty(cursor.getInt(22));
				reportPluDayItem.setBillVoidPrice(cursor.getString(23));
				reportPluDayItem.setBillFocQty(cursor.getInt(24));
				reportPluDayItem.setBillFocPrice(cursor.getString(25));
				reportPluDayItem.setIsOpenItem(cursor.getInt(26));
				reportPluDayItem.setDaySalesId(cursor.getInt(27));
				result.add(reportPluDayItem);
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

	public static ArrayList<ReportPluDayItem> getReportPluDayItemsByTime(long date) {
		ArrayList<ReportPluDayItem> result = new ArrayList<ReportPluDayItem>();
		String sql = "select * from "
				+ TableNames.ReportPluDayItem
				+ " where businessDate = ? ";
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {String.valueOf(date)});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			ReportPluDayItem reportPluDayItem = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				reportPluDayItem = new ReportPluDayItem();
				reportPluDayItem.setId(cursor.getInt(0));
				reportPluDayItem.setReportNo(cursor.getInt(1));
				reportPluDayItem.setRestaurantId(cursor.getInt(2));
				reportPluDayItem.setRestaurantName(cursor.getString(3));
				reportPluDayItem.setRevenueId(cursor.getInt(4));
				reportPluDayItem.setRevenueName(cursor.getString(5));
				reportPluDayItem.setBusinessDate(cursor.getLong(6));
				reportPluDayItem.setItemMainCategoryId(cursor.getInt(7));
				reportPluDayItem.setItemMainCategoryName(cursor.getString(8));
				reportPluDayItem.setItemCategoryId(cursor.getInt(9));
				reportPluDayItem.setItemCategoryName(cursor.getString(10));
				reportPluDayItem.setItemDetailId(cursor.getInt(11));
				reportPluDayItem.setItemName(cursor.getString(12));
				reportPluDayItem.setItemPrice(cursor.getString(13));
				reportPluDayItem.setItemCount(cursor.getInt(14));
				reportPluDayItem.setItemAmount(cursor.getString(15));
				reportPluDayItem.setItemVoidQty(cursor.getInt(16));
				reportPluDayItem.setItemVoidPrice(cursor.getString(17));
				reportPluDayItem.setItemHoldQty(cursor.getInt(18));
				reportPluDayItem.setItemHoldPrice(cursor.getString(19));
				reportPluDayItem.setItemFocQty(cursor.getInt(20));
				reportPluDayItem.setItemFocPrice(cursor.getString(21));
				reportPluDayItem.setBillVoidQty(cursor.getInt(22));
				reportPluDayItem.setBillVoidPrice(cursor.getString(23));
				reportPluDayItem.setBillFocQty(cursor.getInt(24));
				reportPluDayItem.setBillFocPrice(cursor.getString(25));
				reportPluDayItem.setIsOpenItem(cursor.getInt(26));
				reportPluDayItem.setDaySalesId(cursor.getInt(27));
				result.add(reportPluDayItem);
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

	public static void deleteReportPluDayItem(ReportPluDayItem reportPluDayItem) {
		String sql = "delete from " + TableNames.ReportPluDayItem
				+ " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { reportPluDayItem.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void deleteReportPluDayItemByBusinessDate(long businessDate) {
		String sql = "delete from " + TableNames.ReportPluDayItem
				+ " where businessDate = ?";
		try {
			SQLExe.getDB().execSQL(sql,
					new Object[] { businessDate + "" });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
