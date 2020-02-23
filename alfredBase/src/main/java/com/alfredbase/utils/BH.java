package com.alfredbase.utils;

import android.text.TextUtils;
import android.util.Log;

import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;
import com.alfredbase.store.Store;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

/**
 * BigDecimal 辅助类
 *
 * @author
 */
public class BH {

    public static final int FORMAT_AFTER = 1;//小数点后两位四舍五入
    public static final int FORMAT_FRONT = 2;//小数点前两位四舍五入


    private static final DecimalFormat doubleFormat = new DecimalFormat("0.00", new DecimalFormatSymbols(Locale.US));
    private static final DecimalFormat threeFormat = new DecimalFormat("0.000", new DecimalFormatSymbols(Locale.US));
    private static final DecimalFormat fourFormat = new DecimalFormat("0.0000", new DecimalFormatSymbols(Locale.US));// 运算工程中使用。
    public static final DecimalFormat intFormat = new DecimalFormat("0", new DecimalFormatSymbols(Locale.US));
    //    private static DecimalFormat format = doubleFormat;
    static DecimalFormat format;
    static DecimalFormat format1;
    private static boolean isDouble = true;
    private static String defaultType = "0.01";
    private static String type = defaultType;
    private static BigDecimal formatM;

    //	private static int operatingType=100;
    private static DecimalFormat decimalFormat = doubleFormat;
    private static String currencySymbol;

    public static void initFormart(String typeFormat, String currency) {
        if (!TextUtils.isEmpty(currency)) {
            currencySymbol = currency;
        }
        if (!TextUtils.isEmpty(typeFormat)) {
            type = typeFormat;

            if (Double.parseDouble(type) >= 1) {
                decimalFormat = threeFormat;
                //decimalFormat.setRoundingMode(RoundingMode.HALF_UP);
            } else {
                decimalFormat = doubleFormat;
                //decimalFormat.setRoundingMode(RoundingMode.HALF_UP);

            }
            if (!TextUtils.isEmpty(currencySymbol)) {
                if (currencySymbol.equals("Rp")) {
                    decimalFormat = intFormat;
                }
            }
        }

        if (type.equals(defaultType)) {
            format = new DecimalFormat(formatNarrow().toString(), new DecimalFormatSymbols(Locale.US));
            if (!TextUtils.isEmpty(currencySymbol)) {
                if (currencySymbol.equals("Rp")) {
                    format = intFormat;
                }
            }
        } else {
            if (("" + formatNarrow()).equals("1")) {
                format = doubleFormat;
            } else {
                format = new DecimalFormat(formatNarrow().toString() , new DecimalFormatSymbols(Locale.US));
            }
        }
        format.setRoundingMode(RoundingMode.HALF_UP);
    }

    public static BigDecimal formatMoneyBigDecimal(String string) {
        Store.putInt(BaseApplication.instance, Store.FORMAT_MONEY_TYPE, 1);
        int type = Store.getInt(BaseApplication.instance, Store.FORMAT_MONEY_TYPE, 0);
        if (CommonUtil.isNull(string))
            return new BigDecimal("0.00");
        BigDecimal value2 = null;
        int money;
        if (type == FORMAT_FRONT) {
            if (string.toString().contains(".")) {
                money = Integer.valueOf(string.toString().substring(0, string.toString().indexOf(".")));
            } else {
                return value2;
            }
            int r;
            r = money % 100;
            money -= r;
            if (r >= 50) {
                money += 100;
            }

            return new BigDecimal(money);
        } else {
            value2 = new BigDecimal(string);
            //return new BigDecimal(doubleFormat.format(value2));
            return value2.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }


    public static BigDecimal formatMoneyBigDecimal(Integer integer) {
        Store.putInt(BaseApplication.instance, Store.FORMAT_MONEY_TYPE, 1);
        int type = Store.getInt(BaseApplication.instance, Store.FORMAT_MONEY_TYPE, 0);
        if (CommonUtil.isNull(integer))
            return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
        BigDecimal value2 = null;
        int money;
        if (type == FORMAT_FRONT) {
            if (integer.toString().contains(".")) {
                money = Integer.valueOf(integer.toString().substring(0, integer.toString().indexOf(".")));
            } else {
                return value2;
            }
            int r;
            r = money % 100;
            money -= r;
            if (r >= 50) {
                money += 100;
            }

            return new BigDecimal(money);
        } else {
            value2 = new BigDecimal(integer);
            //return new BigDecimal(doubleFormat.format(value2));
            return value2.setScale(2, BigDecimal.ROUND_HALF_UP);
        }
    }


    /**
     * 格式化金额(每三位以 ，分割)
     *
     * @param string
     * @param
     * @param
     * @return
     */
    public static String formatThree(String string
    ) {

        if (CommonUtil.isNull(string)) {

            return decimalFormat.format(new BigDecimal(string));
        } else {

            return decimalFormat.format(new BigDecimal(string));
        }


    }

    /**
     * 格式化金额
     *
     * @param string
     * @param
     * @param
     * @return
     */
    public static BigDecimal formatType(String string
    ) {
        //Store.putInt(BaseApplication.instance, Store.FORMAT_MONEY_TYPE, 1);
        //	int type = Store.getInt(BaseApplication.instance, Store.FORMAT_MONEY_TYPE, 0);


        format1 = new DecimalFormat(type , new DecimalFormatSymbols(Locale.US));
        format1.setRoundingMode(RoundingMode.HALF_UP);
        formatM = BigDecimal.ZERO;
        if (CommonUtil.isNull(string)) {
            formatM = new BigDecimal(format1.format(new BigDecimal("0")));
            return formatM;
        } else {
            formatM = new BigDecimal(format1.format(new BigDecimal(string)));
            return formatM;
        }


    }

    /**
     * 格式化金额
     *
     * @param string
     * @param
     * @param
     * @return
     */
    public static String formatMoney(String string) {
        //Store.putInt(BaseApplication.instance, Store.FORMAT_MONEY_TYPE, 1);
        //	int type = Store.getInt(BaseApplication.instance, Store.FORMAT_MONEY_TYPE, 0);


        format1 = doubleFormat;
        format1.setRoundingMode(RoundingMode.HALF_UP);
        formatM = BigDecimal.ZERO;
        if (CommonUtil.isNull(string)) {
            formatM = new BigDecimal(format1.format(new BigDecimal("0")));
            return decimalFormat.format(formatM);
        } else {
            formatM = new BigDecimal(format1.format(new BigDecimal(string)));
            return decimalFormat.format(formatM);
        }
//		if (CommonUtil.isNull(string)) {
//			return new BigDecimal(format1.format(new BigDecimal("0")));
//		}else {
//
//			return new BigDecimal(format1.format(new BigDecimal(string)));
//		}
//		if (CommonUtil.isNull(string))
//			return new BigDecimal("0.00");
//		BigDecimal value2 = null;
//		int money;
//		if (Double.valueOf(type) >=10) {
//            if (string.contains(".")) {
//                money = Integer.valueOf(string.substring(0, string.indexOf(".")));
//            } else {
//                money=Integer.valueOf(string);
//            }
//			int r;
//			r = (int) (money % Double.valueOf(type));
//			money -= r;
//			if (r >= Double.valueOf(type)/2) {
//				money += Double.valueOf(type);
//			}
//
//			return new BigDecimal(money);
//		} else {
//            format1=   new DecimalFormat(type);
//            //value2 = new BigDecimal(string);
//            //return new BigDecimal(doubleFormat.format(value2));
//            return  new BigDecimal(format1.format(new BigDecimal(string)));
//		}
    }

    // 显示金额格式
    public static String formatMoney(Integer integer
    ) {


        format1 = new DecimalFormat(type , new DecimalFormatSymbols(Locale.US));
        format1.setRoundingMode(RoundingMode.HALF_UP);
        formatM = BigDecimal.ZERO;
        if (CommonUtil.isNull(integer)) {
            formatM = new BigDecimal(format1.format(new BigDecimal("0")));
            return decimalFormat.format(formatM);
        } else {
            formatM = new BigDecimal(format1.format(new BigDecimal(integer)));
            return decimalFormat.format(formatM);
        }
////		Store.putInt(BaseApplication.instance, Store.FORMAT_MONEY_TYPE, 1);
////		int type = Store.getInt(BaseApplication.instance, Store.FORMAT_MONEY_TYPE, 0);
//		if (CommonUtil.isNull(integer))
//			return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
//		BigDecimal value2 = null;
//		int money;
//		if (Double.valueOf(type) >=10) {
//			if (integer.toString().contains(".")) {
//				money = Integer.valueOf(integer.toString().substring(0, integer.toString().indexOf(".")));
//			} else {
//				money= integer.intValue();
//			}
//			int r;
//			r = (int) (money % Double.valueOf(type));
//			money -= r;
//			if (r >= Double.valueOf(type)/2) {
//				money += Double.valueOf(type);
//			}
//
//			return new BigDecimal(money);
//		} else {
////			value2 = new BigDecimal(integer);
////			//return new BigDecimal(doubleFormat.format(value2));
////			return value2.setScale(type, BigDecimal.ROUND_HALF_UP);
//            format1=   new DecimalFormat(type);
//            return  new BigDecimal(format1.format(new BigDecimal(integer)));
//		}
    }

    // 运算时后移两位
    public static BigDecimal formatNarrow(
    ) {
        BigDecimal newformat = BigDecimal.ZERO;
        BigDecimal big1 = new BigDecimal(type);
        BigDecimal big2 = new BigDecimal("100");

        newformat = big1.divide(big2);
        return newformat;
//        if (Double.valueOf(type)<10){
//            String typeString=String.valueOf(type);
//            if(typeString.contains(".")){
//                newZero=new BigDecimal(typeString+"00");
//            }else {
//                newZero=new BigDecimal(typeString+".00");
//            }
////			//	newZero = newZero.add(zero.setScale(type+2, RoundingMode.HALF_UP));
//        }else if(Double.valueOf(type)==10){
//            newZero= new BigDecimal("0.0") ;
//        }else {
//            newZero= new BigDecimal("0") ;
//        }
//        return newZero;
    }


    /**
     * 获取 格式后的等差
     *
     * @param value
     * @param
     * @param
     * @return
     */

    public static BigDecimal formatRound(BigDecimal value
    ) {
        value = BH.sub(new BigDecimal(BH.formatMoney(value.toString())), value, true);
        return value;
    }


//    public static BigDecimal formatOperation(BigDecimal value
//    ) {
//        if (Double.valueOf(type) < 10) {
//            value = new BigDecimal(format.format(value));
//        } else if (Double.valueOf(type) == 10) {
//            format = new DecimalFormat("0.0");
//            value = new BigDecimal(format.format(value));
//        } else {
//            format = new DecimalFormat("0");
//            value = new BigDecimal(format.format(value));
//        }
//        return value;
//    }

    /**
     * 加法
     *
     * @param value1
     * @param value2
     * @param needFormat
     * @return
     */

    public static BigDecimal add(BigDecimal value1, BigDecimal value2,
                                 boolean needFormat) {
//		if (needFormat) {
        return new BigDecimal(format.format(value1.add(value2)));
//		} else {
//			return value1.add(value2);
//		}
    }

    /**
     * 减法
     *
     * @param value1
     * @param value2
     * @param needFormat
     * @return
     */
    public static BigDecimal sub(BigDecimal value1, BigDecimal value2,
                                 boolean needFormat) {
        //if (needFormat) {
        return new BigDecimal(format.format(value1.subtract(value2)));
//		} else {
//			return value1.subtract(value2);
//		}
    }

    /**
     * 乘法
     *
     * @param value1
     * @param value2
     * @param needFormat
     * @return
     */

    public static BigDecimal mul(BigDecimal value1, BigDecimal value2,
                                 boolean needFormat) {
        //	if (needFormat) {
        DecimalFormat mulFormat = format;
        if (!TextUtils.isEmpty(currencySymbol)) {
            if (currencySymbol.equals("Rp")) {
                mulFormat = intFormat;
            }
        }
        return new BigDecimal(mulFormat.format(value1.multiply(value2)));
//		} else {
//			return value1.multiply(value2);
//		}
    }

    /**
     * 除法
     *
     * @param value1
     * @param value2
     * @param needFormat 为true时保留小数点后两位，
     * @return
     */
    public static BigDecimal div(BigDecimal value1, BigDecimal value2,
                                 boolean needFormat) {
        //	if (needFormat) {
        return new BigDecimal(format.format(value1.divide(value2, 5)));
//		} else {
//			return value1.divide(value2, 5, BigDecimal.ROUND_HALF_UP);
//		}
    }

    /**
     * 除法
     *
     * @param value1
     * @param value2
     * @param needFormat 为true时保留小数点后三位，
     * @return
     */
    public static BigDecimal divThirdFormat(BigDecimal value1, BigDecimal value2,
                                            boolean needFormat) {
//		if (needFormat) {
        return value1.divide(value2, 3, BigDecimal.ROUND_HALF_UP);
//		} else {
//			return value1.divide(value2, 5, BigDecimal.ROUND_HALF_UP);
//		}
    }
//
//	public static BigDecimal getBD(Integer integer) {
//		if (CommonUtil.isNull(integer))
//			return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
//		return new BigDecimal(format.format(new BigDecimal(integer)));
//	}
//
//	public static BigDecimal getBD(String string) {
//		if (CommonUtil.isNull(string))
//			return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
//		return new BigDecimal(format.format(new BigDecimal(string)));
//	}
//
//	public static BigDecimal getBDNoFormat(String string){
//		if (CommonUtil.isNull(string))
//			return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
//		return new BigDecimal(string);
//	}
//
//	public static BigDecimal getBDThirdFormat(String string){
//		if (CommonUtil.isNull(string))
//			return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
//		return new BigDecimal(threeFormat.format(new BigDecimal(string)));
//	}
//
//	public static BigDecimal getBD(Double string) {
//		if (CommonUtil.isNull(string))
//			return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
//		return new BigDecimal(format.format(new BigDecimal(string)));
//	}
//	public static BigDecimal getBD(BigDecimal bigDecimal) {
//		if (CommonUtil.isNull(bigDecimal))
//			return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
//		return new BigDecimal(format.format(bigDecimal));
//	}


    public static BigDecimal getBD(Integer integer) {
        try
        {
            if (CommonUtil.isNull(integer))
            {
                return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
            }
            else
            {
                return new BigDecimal(format.format(new BigDecimal(integer)));
            }
        }
        catch(Exception e)
        {
            Log.e("Null Integer Error", String.valueOf(e));
            return null;
        }
    }

    /**
     * 格式后金额 转为数额
     *
     * @param 1,000
     * @param 1000
     * @param ，
     * @return
     */

    public static BigDecimal getReplace(String string) {
        if (CommonUtil.isNull(string))
            return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
        return new BigDecimal(format.format(new BigDecimal(string.replace(",", ""))));
    }

    public static BigDecimal getBD(String string) {
        if (CommonUtil.isNull(string))
            return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
        return new BigDecimal(format.format(new BigDecimal(string)));
    }

    public static BigDecimal getBDNoFormat(String string) {
        if (CommonUtil.isNull(string))
            return new BigDecimal(ParamConst.DOUBLE_ZERO);
        return new BigDecimal(format.format(new BigDecimal(string)));
    }

    public static BigDecimal getBDNoFormatz(String string) {
        if (CommonUtil.isNull(string))
            return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
        return new BigDecimal(doubleFormat.format(new BigDecimal(string)));
    }


    public static BigDecimal getBDThirdFormat(String string) {
        if (CommonUtil.isNull(string))
            return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
        return new BigDecimal(format.format(new BigDecimal(string)));
    }

    public static BigDecimal getBD(Double string) {
        if (CommonUtil.isNull(string)) {
            return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
        } else {
            return new BigDecimal(format.format(new BigDecimal(string)));
        }
    }

    public static BigDecimal getBD(BigDecimal bigDecimal) {
        if (CommonUtil.isNull(bigDecimal))
            return new BigDecimal(isDouble ? ParamConst.DOUBLE_ZERO : ParamConst.INT_ZERO);
        return new BigDecimal(format.format(bigDecimal));
    }

    /**
     * 求得绝对值
     *
     * @param value1
     * @param needFormat
     * @return
     */
    public static BigDecimal abs(BigDecimal value1, boolean needFormat) {
//		if (needFormat) {
        return new BigDecimal(format.format(value1.abs()));
//		} else {
//			return value1.abs();
//		}
    }

    /**
     * 比较大小
     * 为true时 前比后大
     * 为false是 前小于等于后
     *
     * @param value1
     * @param value2
     * @return
     */
    public static boolean compare(BigDecimal value1, BigDecimal value2) {
        if (value1.compareTo(value2) == 1) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean IsDouble() {
        return isDouble;
    }
//
//	/**
//	 * 保留小数点后两位
//	 * @param value1
//	 * @param needFormat
//	 * @return
//	 */
//	public static BigDecimal formatDouble(BigDecimal value1, boolean needFormat) {
//		if (needFormat) {
//			return new BigDecimal(format.format(value1));
//		} else {
//			return value1;
//		}
//	}
}
