package com.alfredbase;

public class ParamConst {
    // APP

    public static final int APP_QUIT = -1;
    // network
    public static final int CLOUD_NETWORK_ERROR = 0xff;
    public static final int CLOUD_NETWORK_CONNECTED = 0xa0;
    public static final int CLOUD_NETWORK_DISCONNECTED = 0xa1;
    public static final int CLOUD_DATA_SYNC_NOTIFICATION = 0xb0;
    public static final String THREE_ZERO = "0.000";
    public static final String DOUBLE_ZERO = "0.00";
    public static final String INT_ZERO = "0";

    // 标记orderDetail或者itemDetail是否可以手动打折
    public static final int ITEM_DISCOUNT = 1;
    public static final int ITEM_NO_DISCOUNT = 0;

    // 订单状态(1打开 从订单生成到打印凭条、10 waiter发送到pos机、11待支付(包括支付部分 未支付完成) 、12已支付、13挂单,14EMenu快餐未下单
    // 赊账、20拆单、21拆单结算中、30已完成)
    public static final int ORDER_STATUS_OPEN_IN_WAITER = 1;
    public static final int ORDER_STATUS_OPEN_IN_POS = 10;
    public static final int ORDER_STATUS_UNPAY = 11;
    public static final int ORDER_STATUS_PAYED = 12; //not used
    public static final int ORDER_STATUS_HOLD = 13;//not
    public static final int ORDER_STATUS_KIOSK = 14;//not
    public static final int ORDER_STATUS_HOLD_KITCHEN = 15;//not
    public static final int ORDER_STATUS_UNPACK = 20; // split order
    public static final int ORDER_STATUS_UNPACK_CLOSING = 21;
    public static final int ORDER_STATUS_FINISHED = 30;

    // 拆单的OrderSplit 订单状态 10打开、11待支付、12已支付、30已完成
    public static final int ORDERSPLIT_ORDERSTATUS_OPEN = 10;
    public static final int ORDERSPLIT_ORDERSTATUS_UNPAY = 11;
    public static final int ORDERSPLIT_ORDERSTATUS_PAYED = 12;
    public static final int ORDERSPLIT_ORDERSTATUS_FINISHED = 30;

    // 桌子状态 0 空的、 1 占用、 2 正在结账
    public static final int TABLE_STATUS_IDLE = 0;
    public static final int TABLE_STATUS_DINING = 1;
    public static final int TABLE_STATUS_INCHECKOUT = 2;

    //split by pax 1 true, 0 false
    public static final int SPLIT_BY_PAX_FALSE = 0;

    // revenueCenter isKiosk
    public static final int REVENUECENTER_ISNOT_KIOSK = 0;
    public static final int REVENUECENTER_IS_KIOSK = 1;

    // MainPosInfo isKiosk
    public static final int MAINPOSINFO_ISNOT_KIOSK = 0;
    public static final int MAINPOSINFO_IS_KIOSK = 1;

    // 默认OrderDetail groupId
    public static final int ORDERDETAIL_DEFAULT_GROUP_ID = 0;

    // 订单来源(1pos、2服务员、3桌子自助、4手机app)

    public static final int ORDER_ORIGIN_POS = 1;
    public static final int ORDER_ORIGIN_WAITER = 2;
    public static final int ORDER_ORIGIN_TABLE = 3;
    public static final int ORDER_ORIGIN_APP = 4;
    public static final int ORDER_ORIGIN_SUB_POS = 0;

    // 配料订单状态(-1删除，0正常)
    public static final int ORDER_MODIFIER_STATUS_DELETE = -1;
    public static final int ORDER_MODIFIER_STATUS_NORMAL = 0;

    // 配料数据的type(0类型，1具体食材)
    public static final int ORDER_MODIFIER_TYPE_CATEGORY = 1;
    public static final int ORDER_MODIFIER_TYPE_CATEGORYDETAIL = 0;
    // 打印状态(0未打印，1打印)
    public static final int PRINT_STATUS_UNDONE = 0;
    public static final int PRINT_STATUS_DONE = 1;

    // 0:onvalue,1ontax
    public static final int TAX_ON_VALUE = 0;
    public static final int TAX_ON_TAX_1 = 1;
    public static final int TAX_ON_TAX_2 = 2;

    // taxType 0消费税， 1服务税
    public static final int TAX_TYPE_GST = 0;
    public static final int TAX_TYPE_SERVICE = 1;

    public static final int FIRE_STATUS_DEFAULT = 0;
    public static final int FIRE_STATUS_NEXT = 1;


    public static final String POS_PRINT_BROADCASR = "pos_print_broadcasr";

    // (0不打折、10主订单按照比率打折、11主订单直接减、12子订单打折)
    public static final int ORDER_DISCOUNT_TYPE_NULL = 0;
    public static final int ORDER_DISCOUNT_TYPE_RATE_BY_ORDER = 10;
    public static final int ORDER_DISCOUNT_TYPE_SUB_BY_ORDER = 11;
    public static final int ORDER_DISCOUNT_TYPE_BY_ORDERDETAIL = 12;
    public static final int ORDER_DISCOUNT_TYPE_RATE_BY_CATEGORY = 21;
    public static final int ORDER_DISCOUNT_TYPE_SUB_BY_CATEGORY = 22;

    // 打折类型(0不打折、1根据比例打折、2直接减)
    public static final int ORDERDETAIL_DISCOUNT_TYPE_NULL = 0;
    public static final int ORDERDETAIL_DISCOUNT_TYPE_RATE = 1;
    public static final int ORDERDETAIL_DISCOUNT_TYPE_SUB = 2;
    public static final int ORDERDETAIL_DISCOUNT_BYORDER_TYPE_RATE = 3;
    public static final int ORDERDETAIL_DISCOUNT_BYORDER_TYPE_SUB = 4;
    public static final int ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_RATE = 5;
    public static final int ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_SUB = 6;

    // OrderDetail的订单详情的状态 1added、2Kotprinterd、发送到厨房 3prepared、 厨房已经做好了
    // 4served、 让做的菜
    // * 5removed、 还没有送到厨房之前退单 6cancelled 退单
    public static final int ORDERDETAIL_STATUS_WAITER_ADD = -1;
    public static final int ORDERDETAIL_STATUS_WAITER_CREATE = 0;
    public static final int ORDERDETAIL_STATUS_ADDED = 1;
    public static final int ORDERDETAIL_STATUS_KOTPRINTERD = 2;
    public static final int ORDERDETAIL_STATUS_PREPARED = 3;
    public static final int ORDERDETAIL_STATUS_SERVED = 4;
    // public static final int ORDERDETAIL_STATUS_VOID_BEFORE_SETTLE = 10;
    // public static final int ORDERDETAIL_STATUS_ENT_BEFORE_SETTLE = 11;
    // public static final int ORDERDETAIL_STATUS_VOID_AFTER_SETTLE = 20;
    // public static final int ORDERDETAIL_STATUS_ENT_AFTER_SETTLE = 21;
    // public static final int ORDERDETAIL_STATUS_BOH_AFTER_SETTLE = 22;

    // OrderDetail订单详情类型 (0general,1void,2free)
    public static final int ORDERDETAIL_TYPE_GENERAL = 0;
    public static final int ORDERDETAIL_TYPE_VOID = 1;
    public static final int ORDERDETAIL_TYPE_FREE = 2;

    // 菜品类型(0菜单模板，1子菜单、2临时菜、3套餐)
    public static final int ITEMDETAIL_TEMPLATE = 0;
    public static final int ITEMDETAIL_SUB_ITEM = 1;
    public static final int ITEMDETAIL_TEMP_ITEM = 2;
    public static final int ITEMDETAIL_COMBO_ITEM = 3;

    // 是否可以打包(0不可以，1可以)
    public static final int CAN_PACK = 1;
    public static final int CANNOT_PACK = 0;
    // 是否可外卖(0不可以，1可以)
    public static final int CAN_TAKEOUT = 1;
    public static final int CANNOT_TAKEOUT = 0;
    // type是bill的类型 （0为不拆单，1为拆单）
    public static final int BILL_TYPE_UN_SPLIT = 0;
    public static final int BILL_TYPE_SPLIT = 1;

    // 是否是赠送(0非赠送、1赠送)
    public static final int NOT_FREE = 0;
    public static final int FREE = 1;


    //  0 堂吃, 1 打包, 2 外卖
    public static final int DINE_IN = 0;
    public static final int TAKE_AWAY = 1;
    public static final int APP_DELIVERY = 2;
    public static final int NOT_TAKE_AWAY=DINE_IN;

    // 0:没有发送，1：发送中，2：发送成功, 3没有成功
    public static final int SYNC_MSG_UN_SEND = 0; // initial state create a new
    // msg
    public static final int SYNC_MSG_QUEUED = 1; // Queued in syncing JOB task
    public static final int SYNC_MSG_SUCCESS = 2; // success processed in
    // backend
    public static final int SYNC_MSG_MALDATA = 3; // data failed processed at
    // backend, no need send any
    // more

    // 0:没有发送，1：发送了，但是没有成功，2：发送成功
    public static final int PRINTQUEUE_MSG_UN_SEND = 0; // initial state create a new
    // msg
    public static final int PRINTQUEUE_MSG_QUEUED = 1; // Queued in syncing JOB task
    public static final int PRINTQUEUE_MSG_SUCCESS = 2; // success processed in
    // backend
    public static final int PRINTQUEUE_MSG_MALDATA = 3; // data failed processed at
    // backend, no need send any
    // more

    // 支付类型
    public static final int SETTLEMENT_TYPE_CASH = 0;
    public static final int SETTLEMENT_TYPE_MASTERCARD = 1;
    public static final int SETTLEMENT_TYPE_UNIPAY = 2;
    public static final int SETTLEMENT_TYPE_VISA = 3;
    public static final int SETTLEMENT_TYPE_AMEX = 5;
    public static final int SETTLEMENT_TYPE_JCB = 6;
    public static final int SETTLEMENT_TYPE_DINNER_INTERMATIONAL = 7;
    public static final int SETTLEMENT_TYPE_NETS = 1000;
    public static final int SETTLEMENT_TYPE_ALIPAY = 1001;
    public static final int SETTLEMENT_TYPE_EZLINK = 1002;
    public static final int SETTLEMENT_TYPE_PAYPAL = 1003;
    public static final int SETTLEMENT_TYPE_STORED_CARD = 1004;
    public static final int SETTLEMENT_TYPE_STORED_CARD_SALES = 1005;
    public static final int SETTLEMENT_TYPE_BILL_ON_HOLD = 101;
    public static final int SETTLEMENT_TYPE_COMPANY = 102;
    public static final int SETTLEMENT_TYPE_HOURS_CHARGE = 103;
    public static final int SETTLEMENT_TYPE_VOID = 2000;
    public static final int SETTLEMENT_TYPE_REFUND = 2001;
    public static final int SETTLEMENT_TYPE_ENTERTAINMENT = 105;
    public static final int SETTLEMENT_TYPE_THIRDPARTY = 106;
    public static final int SETTLEMENT_TYPE_GROUPON = 3000;
    public static final int SETTLEMENT_TYPE_DELIVEROO = 3001;
    public static final int SETTLEMENT_TYPE_UBEREATS = 3002;
    public static final int SETTLEMENT_TYPE_FOODPANDA = 3003;
    public static final int SETTLEMENT_TYPE_VOUCHER = 4001;
    public static final int SETTLEMENT_TYPE_HALAL = 6001;// 马来西亚的QR支付
    public static final int SETTLEMENT_TYPE_PAYHALAL = 21000;

    public static final int SETTLEMENT_TYPE_IPAY88 = 20000;
    public static final int SETTLEMENT_TYPE_IPAY88_WEPAY = 20317;
    public static final int SETTLEMENT_TYPE_IPAY88_ALIPAY = 20233;
    public static final int SETTLEMENT_TYPE_IPAY88_BOOST = 20327;
    public static final int SETTLEMENT_TYPE_IPAY88_MCASH = 20328;
    public static final int SETTLEMENT_TYPE_IPAY88_TOUCHNGO = 20337;
    public static final int SETTLEMENT_TYPE_IPAY88_UNIONPAY = 20339;
    public static final int SETTLEMENT_TYPE_IPAY88_MBB = 20345;
    public static final int SETTLEMENT_TYPE_IPAY88_CIMB = 20346;
    public static final int SETTLEMENT_TYPE_IPAY88_GRABPAY = 20347;
    public static final int SETTLEMENT_TYPE_IPAY88_NETS = 20348;




    /**
     * 自定义支付选项，只用于页面展示 不做为数据存
     */
    // 主
    public static final int SETTLEMENT_CUSTOM_PAYMENT = 5000;
    // 副
    public static final int SETTLEMENT_CUSTOM_ALL = 5001;
    public static final int SETTLEMENT_CUSTOM_PART = 5002;
    public static final int SETTLEMENT_CUSTOM_PART_DEFAULT_VALUE = 5003;


    // BOH PaymentType
    public static final int BOH_PAYMENT_TYPE_CASH = 0;
    public static final int BOH_PAYMENT_TYPE_CARD = 1;

    // BOH 的status 0 未支付 1 已支付
    public static final int BOH_HOLD_STATUS_UNPLAY = 0;
    public static final int BOH_HOLD_STATUS_PLAY = 1;

    // User type(账户类型(10服务员，11厨房，12收银员，13 大堂经理，20后台用户，21企业管理员，999系统管理员))
    public static final int USER_TYPE_WAITER = 10;
    public static final int USER_TYPE_KOT = 11;
    public static final int USER_TYPE_POS = 12;
    public static final int USER_TYPE_MANAGER = 13;
    // KOTItemDetail 的 kot_status (0未发送、 1待完成、 2更新、 3已完成、 4已退单、 -1已删除)
    public static final int KOT_STATUS_UNSEND = 0;
    public static final int KOT_STATUS_UNDONE = 1;
    public static final int KOT_STATUS_UPDATE = 2;
    public static final int KOT_STATUS_DONE = 3;
    public static final int KOT_STATUS_VOID = 4;
    public static final int KOT_STATUS_DELETE = -1;
    public static final int KOT_STATUS_TMP = -2;

    // KotSummary的status(0-未完成，1-已完成)
    public static final int KOTS_STATUS_UNDONE = 0;
    public static final int KOTS_STATUS_DONE = 1;

    // KotItemDetail的categoryId
    public static final int KOTITEMDETAIL_CATEGORYID_MAIN = 0;
    public static final int KOTITEMDETAIL_CATEGORYID_SUB = 1;

    // Job用 标记placeOrder 发送给KDS的状态
    public static final String JOB_TMP_KOT = "tmp_kot";
    public static final String JOB_NEW_KOT = "new_kot";
    public static final String JOB_UPDATE_KOT = "update_kot";
    public static final String JOB_DELETE_KOT = "delete_kot";
    public static final String JOB_VOID_KOT = "void_kot";
    public static final String JOB_DELETE_KOT_SUMMARY = "delete_kot_summary";
    public static final String JOB_DELETE_TMP_ITEM_KOT = "delete_tmp_item_kot";
    public static final String JOB_KOT_SUMMARY = "kot_summary";
    public static final String JOB_KOT_UPDATE_ORDER_COUNT = "kot_summary_update_order_count";
    public static final String JOB_REFRESH_KOT = "refresh_kot";

    // Job用 标记transfer table 发送给KDS的装填
    public static final String JOB_MERGER_KOT = "merger_kot";
    public static final String JOB_TRANSFER_KOT = "transfer_kot";

    // device type
    public static final int DEVICE_TYPE_MAIN_POS = 0;
    public static final int DEVICE_TYPE_WAITER = 1;
    public static final int DEVICE_TYPE_KDS = 2;
    public static final int DEVICE_TYPE_PRINTER = 3;
    public static final int DEVICE_TYPE_CUSTOMER = 4;
    public static final int DEVICE_TYPE_CONSUMER = 5;

    // Notification from HTTP Request
    public static final int HTTP_REQ_CALLBACK_KDS_PAIRED = 20;
    public static final int HTTP_REQ_CALLBACK_WAITER_PAIRED = 21;
    public static final int HTTP_REQ_REFRESH_KDS_PAIRED = 22;
    public static final int HTTP_REQ_REFRESH_WAITER_PAIRED = 23;

    // kotNotification是否能删除的状态
    public static final int KOTNOTIFICATION_STATUS_DELETE = -1;
    public static final int KOTNOTIFICATION_STATUS_NORMAL = 0;

    // 四舍五入规则(ROUND_5CENTS精确小数点后两位四舍五入、ROUND_10CENTS精确小数点后一位四舍五入、ROUND_1DOLLAR精确到小数点前一位四舍五入、ROUND_NONE不做处理)

    public static final int PRINTER_TYPE_GROUP = 0;
    public static final int PRINTER_TYPE_UNGROUP = 1;

    // 用户 0 已登录未登出， 1已登录登出
    public static final int USERTIMESHEET_STATUS_LOGIN = 0;
    public static final int USERTIMESHEET_STATUS_LOGOUT = 1;

    // sync data details
    public static final int HAPPY_HOURS = 1;
    public static final int PRINTER = 2;
    public static final int ITEM = 3;
    public static final int MODIFIER = 4;
    public static final int USER = 5;
    public static final int RESTAURANT = 6;
    public static final int PLACE_TABLE = 7;
    public static final int TAX = 8;

    public static final int DEFAULT_TRUE = 1;
    public static final int DEFAULT_FALSE = 0;

    public static final int ORDER_SUMMARY_PRINT_TRUE = 1;
    public static final int ORDER_SUMMARY_PRINT_FALSE = 0;

    // cashInOut的type
    public static final int CASHINOUT_TYPE_IN = 0;
    public static final int CASHINOUT_TYPE_OUT = 1;
    public static final int CASHINOUT_TYPE_START = 2;

    // 网络job的类型
    public static final int JOB_TYPE_POS_PLACEORDER = 0;
    public static final int JOB_TYPE_WAITER_PLACEORDER = 1;
    public static final int JOB_TYPE_POS_TRANSFER_TABLE = 2;
    public static final int JOB_TYPE_POS_MERGER_TABLE = 3;

    // 支付方式是否可用
    public static final int PAYMENT_SETT_IS_ACTIVE = 0;
    public static final int PAYMENT_SETT_IS_NO_ACTIVE = -1;

    public static final int TEMP_ORDER_STATUS_UNUSED = 0;
    public static final int TEMP_ORDER_STATUS_USED = -1;

    public static final int ACTIVE_NOMAL = 1;
    public static final int ACTIVE_DISABLE = 0;
    public static final int ACTIVE_DELETE = -1;

    public static final int ACTIVE_REFUND = -2;
    /**
     * 精确小数点后两位四舍五入
     */
    public static final String ROUND_5CENTS = "ROUND_5CENTS";
    /**
     * 精确小数点后一位四舍五入
     */
    public static final String ROUND_10CENTS = "ROUND_10CENTS";
    /**
     * 精确到小数点前一位四舍五入
     */
    public static final String ROUND_1DOLLAR = "ROUND_1DOLLAR";

    /**
     * 精确到小数点前一位四舍五入
     */
    public static final String ROUND_50DOLLAR = "ROUND_50DOLLAR";

    /**
     * 精确到小数点前一位四舍五入
     */
    public static final String ROUND_100DOLLAR = "ROUND_100DOLLAR";

    /**
     * 精确到小数点前一位四舍五入
     */
    public static final String ROUND_500DOLLAR = "ROUND_500DOLLAR";
    /**
     * 精确到小数点前一位四舍五入
     */
    public static final String ROUND_1000DOLLAR = "ROUND_1000DOLLAR";
    /**
     * 精确小数点后两位,向上四舍五入
     */
    public static final String ROUND_5CENTS_UP = "ROUND_5CENTS_UP";
    /**
     * 精确小数点后一位,向上四舍五入
     */
    public static final String ROUND_10CENTS_UP = "ROUND_10CENTS_UP";
    /**
     * 精确到小数点前一位,向上四舍五入
     */
    public static final String ROUND_1DOLLAR_UP = "ROUND_1DOLLAR_UP";
    /**
     * 精确小数点后两位,向下四舍五入
     */
    public static final String ROUND_5CENTS_DOWN = "ROUND_5CENTS_DOWN";
    /**
     * 精确小数点后一位,向下四舍五入
     */
    public static final String ROUND_10CENTS_DOWN = "ROUND_10CENTS_DOWN";
    /**
     * 精确到小数点前一位,向下四舍五入
     */
    public static final String ROUND_1DOLLAR_DOWN = "ROUND_1DOLLAR_DOWN";
    /**
     * 不做处理
     */
    public static final String ROUND_NONE = "ROUND_NONE";

    // 调用js的参数
    public static final String JS_CONNECT_ANDROID = "javascript:JsConnectAndroid";


    /**
     * 配置内容分隔符
     */
    public final static String PARA_VALUE_SPLIT = ";;";

    /** ##################### 币种类 ######################## */
    /**
     * 币种类型
     * 直接用value1
     */
    public final static int CURRENCY_TYPE = 100200;

    /** ##################### 四舍五入规则类 ######################## */
    /**
     * 四舍五入规则类
     * 直接用value1
     */
    public final static int ROUND_RULE_TYPE = 100800;

    /* Item price is tax inclusive
     * */
    public final static int PRICE_TAX_INCLUSIVE = 1001000; //value1: 0(default) not included, value1 (YES):tax ID; Value2 如果是V1就是用税收组去算,其他的都是用单个税去算
    public final static String ITEM_PRICE_TYPE_VALUE2 = "V1"; // 从V1开始的所有的包含税都是以组存在，在B端那边需要用税收组去算

    /**
     * #####################   默认打折比例   ########################
     */
    public final static int DEF_DISCOUNT_TYPE = 1001100;

    /** ##################### 销售时段类 ######################## */
    /**
     * 餐厅销售时段类型，value1对应销售时段，以;;分隔。
     */
    public final static int SALE_SESSION_TYPE = 100400;

    /**
     * #####################   默认打折比例   ########################
     */
    public final static int SEND_FOOD_CARD_NUM = 1001900;

    // 早中午餐(0全天、1早上、2午餐、3晚餐、4夜店)

    public static final int SESSION_STATUS_ALL_DAY = 0;
    public static final int SESSION_STATUS_BREAKFAST = 1;
    public static final int SESSION_STATUS_LUNCH = 2;
    public static final int SESSION_STATUS_DINNER = 3;
    public static final int SESSION_STATUS_SUPPER = 4;


    public static final int TEMP_ORDER_NORMAL = 0;
    public static final int TEMP_ORDER_DELETE = -1;

    /**
     * country code
     */
    public static final int CHINA = 86;
    public static final int SINGAPORE = 65;

    public static final int TEMPORDER_STATUS_UN_CHECKED = 0;
    public static final int TEMPORDER_STATUS_CHECKED = 1;
    public static final int TEMPORDER_STATUS_UN_ACTIVTY = 2;

    public static final int TEMPORDER_UN_PAIED = 0;
    public static final int TEMPORDER_PAIED = 1;


    /**
     * 用于标记是否配置了套餐
     */
    public static final int IS_SET_COMBO = 1;
    public static final int NO_SET_COMBO = 0;

    /**
     * 0没有选择规则  1任意选择几
     */
    public static final int MODIFIER_MUST_DEFAULT_NORMAL = 0;
    public static final int MODIFIER_MUST_DEFAULT_SELECT = 1;


    public static final int APP_ORDER_STATUS_PAID = 0;
    public static final int APP_ORDER_STATUS_ACCEPTED = 1;
    public static final int APP_ORDER_STATUS_PREPARING = 2;
    public static final int APP_ORDER_STATUS_PREPARED = 3;
    public static final int APP_ORDER_STATUS_COMPLETED = 4;
    public static final int APP_ORDER_STATUS_REFUND = -2;


    public static final int APP_ORDER_TABLE_STATUS_DEFAULT = 0;
    public static final int APP_ORDER_TABLE_STATUS_NOT_USE = 1;
    public static final int APP_ORDER_TABLE_STATUS_USED = -1;

    public static final int STORED_CARD_ACTION_TOP_UP = 1;
    public static final int STORED_CARD_ACTION_PAY = 2;
    public static final int STORED_CARD_ACTION_REFUND = 3;


    public static final int APP_ORDER_EAT_IN = 1;
    public static final int APP_ORDER_TAKE_AWAY = 2;
    public static final int APP_ORDER_DELIVERY = 3;
    public static final int POS_TYPE_MAIN = 0;

    public static final int POS_TYPE_SUB = 1;

    public static final int SUB_POS_STATUS_OPEN = 1;

    public static final int SUB_POS_STATUS_CLOSE = 0;

    //promotion
    public static final int ITEM_PROMOTION = 0;
    public static final int ORDER_PROMOTION = 1;


    //ipay88
    public static final int PAY88_STATUS_NO_PROCESS = 0;
    public static final int PAY88_STATUS_NO_REAL_DATE = 2;
    public static final int PAY88_STATUS_PROCESSED = 1;
    public static final int PAY88_STATUS_PROCESSED_ORDER = -1;
    public static final int PAY88_STATUS_PROCESSED_ERROR = -2;


    public static final String PAYHALAL_PAYMENT_STATUS_SUCCESS = "SUCCESS";
    public static final String PAYHALAL_PAYMENT_STATUS_FAILED = "FAILED";
    public static final String PAYHALAL_PAYMENT_STATUS_PENDING = "PENDING";
    public static final String PAYHALAL_PAYMENT_STATUS_TIMEOUT = "TIMEOUT";


//     233 Alipay,  339 Union Pay, dan 347 GrabPay


    public static String getQRPaymentName(int paymentTypeId){
        String type = "";
        if(paymentTypeId == SETTLEMENT_TYPE_IPAY88_ALIPAY){
            type = "Alipay";
        }
        else if(paymentTypeId == SETTLEMENT_TYPE_IPAY88_BOOST){
            type = "Boost";
        }
        else if(paymentTypeId == SETTLEMENT_TYPE_IPAY88_TOUCHNGO){
            type = "Touch N Go";
        }
        else if(paymentTypeId == SETTLEMENT_TYPE_IPAY88_MCASH){
            type = "Mcash";
        }
        else if(paymentTypeId == SETTLEMENT_TYPE_IPAY88_UNIONPAY){
            type = "UnionPay";
        }
        else if(paymentTypeId == SETTLEMENT_TYPE_IPAY88_NETS){
            type = "NetsPay";
        }
        else if(paymentTypeId == SETTLEMENT_TYPE_IPAY88_CIMB){
            type = "Cimb Pay";
        }
        else if(paymentTypeId == SETTLEMENT_TYPE_IPAY88_MBB){
            type = "Maybank QRPay";
        }
        else if(paymentTypeId == SETTLEMENT_TYPE_IPAY88_GRABPAY){
            type = "GrabPay";
        }
        else if(paymentTypeId == SETTLEMENT_TYPE_IPAY88_WEPAY){
            type = "WePay";
        }
        else if(paymentTypeId == SETTLEMENT_TYPE_PAYHALAL){
            type = "PayHalal";
        }
        return type;
    }
    public static final int ENABLE_POS_TRAINING  = 1;
    public static final int DISABLE_POS_TRAINING = 0;



}
