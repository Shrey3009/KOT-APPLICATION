package com.alfredbase.store.sql;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.alfredbase.javabean.Company;
import com.alfredbase.store.SQLExe;
import com.alfredbase.store.TableNames;

import java.util.ArrayList;

public class CompanySQL {
	public static void addCompany(Company company) {
		if (company == null)
			return;
		try {
			String sql = "replace into "
					+ TableNames.Company
					+ "(id,userId ,companyName ,email , level ,address1 ,address2 ,telNo ,country ,state ,city ,postalCode ,createTime ,updateTime )"
					+ " values (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
			SQLExe.getDB().execSQL(
					sql,
					new Object[] { company.getId(), company.getUserId(),
							company.getCompanyName(), company.getEmail(),
							company.getLevel(), company.getAddress1(),
							company.getAddress2(), company.getTelNo(),
							company.getCountry(), company.getState(),
							company.getCity(), company.getPostalCode(),
							company.getCreateTime(), company.getUpdateTime() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static ArrayList<Company> getAllCompany() {
		ArrayList<Company> result = new ArrayList<Company>();
		String sql = "select * from " + TableNames.Company;
		Cursor cursor = null;
		SQLiteDatabase db = SQLExe.getDB();
		try {
			cursor = db.rawQuery(sql, new String[] {});
			int count = cursor.getCount();
			if (count < 1) {
				return result;
			}
			Company company = null;
			for (cursor.moveToFirst(); !cursor.isAfterLast(); cursor
					.moveToNext()) {
				company = new Company();
				company.setId(cursor.getInt(0));
				company.setUserId(cursor.getInt(1));
				company.setCompanyName(cursor.getString(2));
				company.setEmail(cursor.getString(3));
				company.setLevel(cursor.getInt(4));
				company.setAddress1(cursor.getString(5));
				company.setAddress2(cursor.getString(6));
				company.setTelNo(cursor.getString(7));
				company.setCountry(cursor.getString(8));
				company.setState(cursor.getString(9));
				company.setCity(cursor.getString(10));
				company.setPostalCode(cursor.getString(11));
				company.setCreateTime(cursor.getLong(12));
				company.setUpdateTime(cursor.getLong(13));
				result.add(company);
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

	public static void deleteCompany(Company company) {
		String sql = "delete from " + TableNames.Company + " where id = ?";
		try {
			SQLExe.getDB().execSQL(sql, new Object[] { company.getId() });
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
