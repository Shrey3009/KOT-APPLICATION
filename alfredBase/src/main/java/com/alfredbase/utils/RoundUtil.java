package com.alfredbase.utils;

import com.alfredbase.ParamConst;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class RoundUtil {
	public static BigDecimal getPriceAfterRound(String roundType,
			BigDecimal priceBeforeRound) {
		if (roundType == null) {
			return priceBeforeRound;
		}
		if (roundType.equalsIgnoreCase(ParamConst.ROUND_10CENTS)) {
			DecimalFormat doubleFormat = new DecimalFormat("0", new DecimalFormatSymbols(Locale.US));
			BigDecimal bigDecimal = BH.div(priceBeforeRound, BH.getBDNoFormat("0.1"),
					false);
			return BH.mul(BH.getBDNoFormat(doubleFormat.format(bigDecimal)),
					BH.getBDNoFormat("0.1"), true);
		} else if (roundType.equalsIgnoreCase(
				ParamConst.ROUND_1DOLLAR)) {
			DecimalFormat doubleFormat = new DecimalFormat("0", new DecimalFormatSymbols(Locale.US));
			BigDecimal bigDecimal = BH.div(priceBeforeRound, BH.getBDNoFormat("1.0"),
					false);
			return BH.mul(BH.getBDNoFormat(doubleFormat.format(bigDecimal)),
					BH.getBDNoFormat("1.0"), true);
		}
		else if (roundType.equalsIgnoreCase(
				ParamConst.ROUND_50DOLLAR)) {
			DecimalFormat doubleFormat = new DecimalFormat("0", new DecimalFormatSymbols(Locale.US));
			BigDecimal bigDecimal = BH.div(priceBeforeRound, BH.getBDNoFormat("50"),
					false);
			return BH.mul(BH.getBDNoFormat(doubleFormat.format(bigDecimal)),
					BH.getBDNoFormat("50"), true);
		}
		else if (roundType.equalsIgnoreCase(
				ParamConst.ROUND_100DOLLAR)) {
			DecimalFormat doubleFormat = new DecimalFormat("0", new DecimalFormatSymbols(Locale.US));
			BigDecimal bigDecimal = BH.div(priceBeforeRound, BH.getBDNoFormat("100"),
					false);
			return BH.mul(BH.getBDNoFormat(doubleFormat.format(bigDecimal)),
					BH.getBDNoFormat("100"), true);
		}
		else if (roundType.equalsIgnoreCase(
				ParamConst.ROUND_500DOLLAR)) {
			DecimalFormat doubleFormat = new DecimalFormat("0", new DecimalFormatSymbols(Locale.US));
			BigDecimal bigDecimal = BH.div(priceBeforeRound, BH.getBDNoFormat("500"),
					false);
			return BH.mul(BH.getBDNoFormat(doubleFormat.format(bigDecimal)),
					BH.getBDNoFormat("500"), true);
		}

		else if (roundType.equalsIgnoreCase(
				ParamConst.ROUND_1000DOLLAR)) {
			DecimalFormat doubleFormat = new DecimalFormat("0", new DecimalFormatSymbols(Locale.US));
			BigDecimal bigDecimal = BH.div(priceBeforeRound, BH.getBDNoFormat("1000"),
					false);
			return BH.mul(BH.getBDNoFormat(doubleFormat.format(bigDecimal)),
					BH.getBDNoFormat("1000"), true);
		}
		else if (roundType.equalsIgnoreCase(
				ParamConst.ROUND_5CENTS)) {
			DecimalFormat doubleFormat = new DecimalFormat("0", new DecimalFormatSymbols(Locale.US));
			BigDecimal bigDecimal = BH.div(priceBeforeRound, BH.getBDNoFormat("0.05"),
					false);
			return BH.mul(BH.getBDNoFormat(doubleFormat.format(bigDecimal)),
					BH.getBDNoFormatz("0.05"), true);
		} else if(roundType.equalsIgnoreCase(ParamConst.ROUND_10CENTS_UP)){
			BigDecimal bigDecimal = BH.div(priceBeforeRound, BH.getBDNoFormat("0.1"),
					false);
			return BH.mul(BH.getBDNoFormat(bigDecimal.setScale(0, BigDecimal.ROUND_UP).toString()),
					BH.getBDNoFormat("0.1"), true);
		} else if(roundType.equalsIgnoreCase(ParamConst.ROUND_1DOLLAR_UP)){
			BigDecimal bigDecimal = BH.div(priceBeforeRound, BH.getBDNoFormat("1.0"),
					false);
			return BH.mul(BH.getBDNoFormat(bigDecimal.setScale(0, BigDecimal.ROUND_UP).toString()),
					BH.getBDNoFormat("1.0"), true);
		} else if(roundType.equalsIgnoreCase(ParamConst.ROUND_5CENTS_UP)){
			BigDecimal bigDecimal = BH.div(priceBeforeRound, BH.getBDNoFormat("0.05"),
					false);
			return BH.mul(BH.getBDNoFormat(bigDecimal.setScale(0, BigDecimal.ROUND_UP).toString()),
					BH.getBDNoFormat("0.05"), true);
		} else if (roundType.equalsIgnoreCase(
				ParamConst.ROUND_10CENTS_DOWN)) {
			BigDecimal bigDecimal = BH.div(priceBeforeRound, BH.getBDNoFormat("0.1"),
					false);
			return BH.mul(BH.getBDNoFormat(bigDecimal.setScale(0, BigDecimal.ROUND_DOWN).toString()),
					BH.getBDNoFormat("0.1"), true);
		} else if (roundType.equalsIgnoreCase(
				ParamConst.ROUND_5CENTS_DOWN)) {
			BigDecimal bigDecimal = BH.div(priceBeforeRound, BH.getBDNoFormat("0.05"),
					false);
			return BH.mul(BH.getBDNoFormat(bigDecimal.setScale(0, BigDecimal.ROUND_DOWN).toString()),
					BH.getBDNoFormat("0.05"), true);
		} else if (roundType.equalsIgnoreCase(
				ParamConst.ROUND_1DOLLAR_DOWN)) {
			BigDecimal bigDecimal = BH.div(priceBeforeRound, BH.getBDNoFormat("1.0"),
					false);
			return BH.mul(BH.getBDNoFormat(bigDecimal.setScale(0, BigDecimal.ROUND_DOWN).toString()),
					BH.getBDNoFormat("1.0"), true);
		} else {
			return priceBeforeRound;
		}
	}
	
	public static BigDecimal getRounding(String roundType,
			BigDecimal priceBeforeRound) {
		return BH.sub(priceBeforeRound, getPriceAfterRound(roundType, priceBeforeRound), true);
	}
}
