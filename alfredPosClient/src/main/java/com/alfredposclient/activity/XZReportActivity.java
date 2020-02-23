package com.alfredposclient.activity;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alfredbase.BaseActivity;
import com.alfredbase.LoadingDialog;
import com.alfredbase.ParamConst;
import com.alfredbase.VerifyDialog;
import com.alfredbase.http.ResultCode;
import com.alfredbase.javabean.ItemCategory;
import com.alfredbase.javabean.ItemMainCategory;
import com.alfredbase.javabean.PrinterTitle;
import com.alfredbase.javabean.ReportDayPayment;
import com.alfredbase.javabean.ReportDaySales;
import com.alfredbase.javabean.ReportDayTax;
import com.alfredbase.javabean.ReportHourly;
import com.alfredbase.javabean.ReportPluDayComboModifier;
import com.alfredbase.javabean.ReportPluDayItem;
import com.alfredbase.javabean.ReportPluDayModifier;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.model.PrinterDevice;
import com.alfredbase.javabean.model.SessionStatus;
import com.alfredbase.javabean.temporaryforapp.ReportUserOpenDrawer;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.CashInOutSQL;
import com.alfredbase.store.sql.ItemCategorySQL;
import com.alfredbase.store.sql.ItemMainCategorySQL;
import com.alfredbase.store.sql.UserOpenDrawerRecordSQL;
import com.alfredbase.utils.BH;
import com.alfredbase.utils.CommonUtil;
import com.alfredbase.utils.DialogFactory;
import com.alfredbase.utils.ObjectFactory;
import com.alfredbase.utils.TimeUtil;
import com.alfredposclient.Calendar.CalendarCard;
import com.alfredposclient.Calendar.CardGridItem;
import com.alfredposclient.Calendar.OnCellItemClick;
import com.alfredposclient.R;
import com.alfredposclient.adapter.XZReportDetailAdapter;
import com.alfredposclient.adapter.XZReportHourlyAdapter;
import com.alfredposclient.adapter.XZReportSumaryAdapter;
import com.alfredposclient.global.App;
import com.alfredposclient.global.ReportObjectFactory;
import com.alfredposclient.global.SyncCentre;
import com.alfredposclient.global.UIHelp;
import com.alfredposclient.javabean.ReportDetailAnalysisItem;
import com.alfredposclient.utils.AlertToDeviceSetting;
import com.alfredposclient.utils.DialogSelectReportPrint;
import com.alfredposclient.view.ReportDaySalesItem;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

public class XZReportActivity extends BaseActivity {

    private CalendarCard calendarCard;
    private TextView tv_title_name;
    private LinearLayout ll_print;
    private LinearLayout ll_sales_total;
    private ImageButton btn_back;
    private LinearLayout ll_xz_analsis;
    private long businessDate;
    private SessionStatus session;
    private Calendar calendar;
    private Map<String, Object> data = new HashMap<String, Object>();
    private RevenueCenter revenueCenter;
    private LoadingDialog loadingDialog;
    private ListView lv_hourly_analsis;
    private SimpleDateFormat yearMonthDayFormater = new SimpleDateFormat("yyyy年MM月dd日");
    private String curStr;
    private long date;
    private ReportDaySales reportDaySales;
    private List<ReportDayTax> reportDayTaxs;
    private List<ReportDayPayment> reportDayPayments;
    private List<ReportPluDayItem> reportPluDayItems;
    private List<ReportDetailAnalysisItem> reportDetailAnalysisItems;
    private List<ReportDetailAnalysisItem> reportDetailAnalysisItemList;
    private List<ReportPluDayModifier> reportPluDayModifiers;
    private List<ReportHourly> reportHourlys;
    private List<ItemCategory> itemCategorys;
    private List<ItemMainCategory> itemMainCategorys;
    private List<ReportPluDayComboModifier> reportPluDayComboModifiers;
    private Map<String, Object> map = new HashMap<String, Object>();
    private long showBusinessDate;
    private BigDecimal detailTotalAmount;
    private int detailTotalQty;
    private BigDecimal hourlyTotalAmount;
    private int hourlyTotalQty;

    @Override
    protected void initView() {
        super.initView();
        setContentView(R.layout.activity_xzrerort);
        VerifyDialog verifyDialog = new VerifyDialog(context, handler);
        verifyDialog.show("initData", null);
        findViewById(R.id.tv_print).setOnClickListener(this);
        showBusinessDate = 0;
        reportDetailAnalysisItems = new ArrayList<>();
        reportDetailAnalysisItemList = new ArrayList<>();
        detailTotalAmount = BH.getBD(ParamConst.DOUBLE_ZERO);
        hourlyTotalAmount = BH.getBD(ParamConst.DOUBLE_ZERO);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case VerifyDialog.DIALOG_RESPONSE:
                    init();
                    break;
                case VerifyDialog.DIALOG_DISMISS:
                    XZReportActivity.this.finish();
                    break;
                case XZReportHtml.LOAD_CLOUD_REPORT_COMPLETE:
                    Map<String, Object> param = (Map<String, Object>) msg.obj;
                    Long date = (Long) param.get("bizDate");
                    showBusinessDate = date.longValue();
                    loadOldReport(date);
                    if (loadingDialog != null && loadingDialog.isShowing())
                        loadingDialog.dismiss();
                    initData(businessDate);

                    break;
                case ResultCode.CONNECTION_FAILED:
                    if (loadingDialog != null && loadingDialog.isShowing()) {
                        loadingDialog.dismiss();
                    }
                    UIHelp.showShortToast(XZReportActivity.this, XZReportActivity.this.getString(R.string.no_data));
                    break;
            }
        }
    };

    private void loadNewReport(long businessDate) {
        reportDaySales = ReportObjectFactory.getInstance().loadShowReportDaySales(businessDate);
        if (reportDaySales != null) {
            itemCategorys = ItemCategorySQL.getAllItemCategoryForReport();
            itemMainCategorys = ItemMainCategorySQL
                    .getAllItemMainCategoryForReport();
            reportPluDayItems = ReportObjectFactory.getInstance().loadShowReportPluDayItem(businessDate);
            Map<String, Object> map = ReportObjectFactory.getInstance().loadShowReportPluDayModifierInfo(businessDate);
            reportPluDayModifiers = (List<ReportPluDayModifier>) map.get("reportPluDayModifiers");
            reportPluDayComboModifiers = (List<ReportPluDayComboModifier>) map.get("reportPluDayComboModifiers");
            reportHourlys = ReportObjectFactory.getInstance().loadShowReportHourlys(businessDate);
            reportDayTaxs = ReportObjectFactory.getInstance().loadShowReportDayTax(businessDate);
            reportDayPayments = ReportObjectFactory.getInstance().loadShowReportDayPayment(businessDate);
            loadModel();
        } else {
            reportDayTaxs = new ArrayList<>();
            reportDayPayments = new ArrayList<>();
            reportPluDayItems = new ArrayList<>();
            reportPluDayModifiers = new ArrayList<>();
            reportHourlys = new ArrayList<>();
            itemCategorys = new ArrayList<>();
            itemMainCategorys = new ArrayList<>();
            reportDetailAnalysisItems = new ArrayList<>();
            reportDetailAnalysisItemList = new ArrayList<>();

        }

    }


    private void loadOldReport(long businessDate) {
        reportDaySales = ReportObjectFactory.getInstance().loadReportDaySales(businessDate);
        if (reportDaySales != null) {
            itemCategorys = ItemCategorySQL.getAllItemCategoryForReport();
            itemMainCategorys = ItemMainCategorySQL
                    .getAllItemMainCategoryForReport();
            reportPluDayItems = ReportObjectFactory.getInstance().loadReportPluDayItem(businessDate);
            Map<String, Object> map = ReportObjectFactory.getInstance().loadReportPluDayModifierInfo(businessDate);

            reportPluDayComboModifiers = (ArrayList<ReportPluDayComboModifier>) map.get("reportPluDayComboModifiers");
            reportPluDayModifiers = (ArrayList<ReportPluDayModifier>) map.get("reportPluDayModifiers");
            reportHourlys = ReportObjectFactory.getInstance().loadReportHourlys(businessDate);
            reportDayTaxs = ReportObjectFactory.getInstance().loadReportDayTax(businessDate);
            reportDayPayments = ReportObjectFactory.getInstance().loadReportDayPayment(businessDate);
            loadModel();
        } else {
            reportDayTaxs = new ArrayList<>();
            reportDayPayments = new ArrayList<>();
            reportPluDayItems = new ArrayList<ReportPluDayItem>();
            reportPluDayModifiers = new ArrayList<>();
            reportHourlys = new ArrayList<>();
            itemCategorys = new ArrayList<>();
            itemMainCategorys = new ArrayList<>();
            reportDetailAnalysisItems = new ArrayList<>();
            reportDetailAnalysisItemList = new ArrayList<>();
        }
    }

    private void loadModel() {
        reportDetailAnalysisItems.clear();
        reportDetailAnalysisItemList.clear();
        detailTotalAmount = BH.getBD(ParamConst.DOUBLE_ZERO);
        hourlyTotalAmount = BH.getBD(ParamConst.DOUBLE_ZERO);
        detailTotalQty = 0;
        hourlyTotalQty = 0;
        if (reportPluDayItems != null && reportPluDayItems.size() > 0) {
            Collections.sort(reportPluDayItems);
            int mainId = 0;
            int id = 0;
            for (ReportPluDayItem reportPluDayItem : reportPluDayItems) {
                if (mainId == reportPluDayItem.getItemMainCategoryId().intValue()) {
                    ReportDetailAnalysisItem reportDetailAnalysisItem = new ReportDetailAnalysisItem();
                    reportDetailAnalysisItem.setName(reportPluDayItem.getItemName());
                    reportDetailAnalysisItem.setQty(reportPluDayItem.getItemCount());
                    reportDetailAnalysisItem.setAmount(BH.getBD(reportPluDayItem.getItemAmount()));
                    reportDetailAnalysisItem.setShowOther(true);
                    reportDetailAnalysisItems.add(reportDetailAnalysisItem);
                } else {
                    mainId = reportPluDayItem.getItemMainCategoryId().intValue();
                    ReportDetailAnalysisItem reportDetailAnalysisItem1 = new ReportDetailAnalysisItem();
                    reportDetailAnalysisItem1.setName(reportPluDayItem.getItemMainCategoryName());
                    reportDetailAnalysisItem1.setShowOther(false);
                    ReportDetailAnalysisItem reportDetailAnalysisItem = new ReportDetailAnalysisItem();
                    reportDetailAnalysisItem.setName(reportPluDayItem.getItemName());
                    reportDetailAnalysisItem.setQty(reportPluDayItem.getItemCount());
                    reportDetailAnalysisItem.setAmount(BH.getBD(reportPluDayItem.getItemAmount()));
                    reportDetailAnalysisItem.setShowOther(true);

                    reportDetailAnalysisItems.add(reportDetailAnalysisItem1);
                    reportDetailAnalysisItems.add(reportDetailAnalysisItem);
                }
                if (id == reportPluDayItem.getItemCategoryId().intValue()) {
                    ReportDetailAnalysisItem reportDetailAnalysisItem = reportDetailAnalysisItemList.get(reportDetailAnalysisItemList.size() - 1);
                    reportDetailAnalysisItem.setQty(reportDetailAnalysisItem.getQty() + reportPluDayItem.getItemCount());
                    reportDetailAnalysisItem.setAmount(BH.add(BH.getBD(reportPluDayItem.getItemAmount()), reportDetailAnalysisItem.getAmount(), true));
                } else {
                    mainId = reportPluDayItem.getItemMainCategoryId().intValue();
                    ReportDetailAnalysisItem reportDetailAnalysisItem1 = new ReportDetailAnalysisItem();
                    reportDetailAnalysisItem1.setName(reportPluDayItem.getItemMainCategoryName());
                    reportDetailAnalysisItem1.setShowOther(false);

                    ReportDetailAnalysisItem reportDetailAnalysisItem = new ReportDetailAnalysisItem();
                    reportDetailAnalysisItem.setName(reportPluDayItem.getItemCategoryName());
                    reportDetailAnalysisItem.setQty(reportPluDayItem.getItemCount());
                    reportDetailAnalysisItem.setAmount(BH.getBD(reportPluDayItem.getItemAmount()));
                    reportDetailAnalysisItem.setShowOther(true);
                    reportDetailAnalysisItemList.add(reportDetailAnalysisItem1);
                    reportDetailAnalysisItemList.add(reportDetailAnalysisItem);
                }
                detailTotalAmount = BH.add(detailTotalAmount, BH.getBD(reportPluDayItem.getItemAmount()), true);
                detailTotalQty += reportPluDayItem.getItemCount().intValue();
            }
        }
        if (reportHourlys != null && reportHourlys.size() > 0) {
            for (ReportHourly reportHourly : reportHourlys) {
                hourlyTotalAmount = BH.add(hourlyTotalAmount, BH.getBD(reportHourly.getAmountPrice()), true);
                hourlyTotalQty += reportHourly.getAmountQty().intValue();
            }
        }
    }

    private void init() {
        calendarCard = (CalendarCard) findViewById(R.id.calendarCard);
        tv_title_name = (TextView) findViewById(R.id.tv_title_name);
        ll_print = (LinearLayout) findViewById(R.id.ll_print);
        btn_back = (ImageButton) findViewById(R.id.btn_back);
        ll_xz_analsis = (LinearLayout) findViewById(R.id.ll_xz_analsis);
        lv_hourly_analsis = (ListView) findViewById(R.id.lv_hourly_analsis);
        tv_title_name.setText(getString(R.string.report_day_sale));
        ll_print.setVisibility(View.INVISIBLE);
        loadingDialog = new LoadingDialog(this);

        calendar = Calendar.getInstance(Locale.US);
        businessDate = App.instance.getBusinessDate();
        Date date = new Date(businessDate);
        curStr = yearMonthDayFormater.format(date);
//        session = App.instance.getSessionStatus();
        revenueCenter = App.instance.getRevenueCenter();

        Calendar cal = Calendar.getInstance(Locale.US);
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int firstDay = cal.get(Calendar.DAY_OF_MONTH);
        cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
        int lastDay = cal.get(Calendar.DAY_OF_MONTH)+1;
//        ArrayList<String> grossTotals = new ArrayList<>();
        Map<Date, String> grossTotals = new HashMap<>();
        for(int i=firstDay; i<lastDay; i++)
        {
            cal.set(Calendar.DAY_OF_MONTH, i);
            Calendar curDate = (Calendar)cal.clone();
            curDate.set(Calendar.HOUR, 0);
            curDate.set(Calendar.MINUTE, 0);
            curDate.set(Calendar.SECOND, 0);
            curDate.set(Calendar.MILLISECOND, 0);
            curDate.set(Calendar.HOUR_OF_DAY, 0);
            Date d = curDate.getTime();

            reportDaySales = ReportObjectFactory.getInstance().loadShowReportDaySales(d.getTime());
            reportDayPayments = ReportObjectFactory.getInstance().loadReportDayPayment(d.getTime());

            if (reportDaySales != null)
            {
                if (reportDayPayments != null)
                {
                    BigDecimal totalCustomPayment = BH.getBD(ParamConst.DOUBLE_ZERO);
                    for (ReportDayPayment reportDayPayment : reportDayPayments) {
                        totalCustomPayment = BH.add(totalCustomPayment, BH.getBD(reportDayPayment.getPaymentAmount()), false);
                    }
                    BigDecimal totals = BH.add(totalCustomPayment, BH.getBD(reportDaySales.getNettSales()), false);
                    //  String nettsSales = reportDaySales.getNettSales();
                    calendarCard.setAmount(totals.toString());
//                    grossTotals.add(totals.toString());
                    grossTotals.put(d, totals.toString());
                }
                else
                {
                    String nettsSales = reportDaySales.getNettSales();
                    calendarCard.setAmount(nettsSales);
//                    grossTotals.add(nettsSales);
                    grossTotals.put(d, nettsSales);
                }
            }
        }
        calendarCard.setGrossTotals(grossTotals);
        calendarCard.setDateDisplay(calendar);
        calendarCard.notifyChanges();
        calendarCard.setOnCellItemClick(onCellItemClick);
        btn_back.setOnClickListener(onClickListener);
    }


    private void initData(long time) {
        if (reportDaySales != null) {
            calendarCard.setVisibility(View.GONE);
            ll_xz_analsis.setVisibility(View.VISIBLE);
            ll_print.setVisibility(View.VISIBLE);
            ll_sales_total = (LinearLayout) findViewById(R.id.ll_sales_total);
            ll_sales_total.removeAllViews();
            // Item Sales
            ReportDaySalesItem salesTotal = new ReportDaySalesItem(context);
            salesTotal.setData("Item Sales", reportDaySales.getItemSalesQty() + "", App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getItemSales()).toString(), true);
            ll_sales_total.addView(salesTotal);
            // Stored-Card Sales
            ReportDaySalesItem storedCardSales = new ReportDaySalesItem(context);
            storedCardSales.setData("Stored-Card Sales", reportDaySales.getTopUpsQty() + "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getTopUps()).toString(), true);
            ll_sales_total.addView(storedCardSales);
            // ENT Items
            ReportDaySalesItem entItems = new ReportDaySalesItem(context);
            entItems.setData("ENT items", reportDaySales.getFocItemQty() + "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getFocItem()).toString(), true);
            ll_sales_total.addView(entItems);
            // ENT Bills
            ReportDaySalesItem entBills = new ReportDaySalesItem(context);
            entBills.setData("ENT Bills/$", reportDaySales.getFocBillQty() + "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getFocBill()).toString(), true);
            ll_sales_total.addView(entBills);
            // VOID/Items
            ReportDaySalesItem voidItem = new ReportDaySalesItem(context);
            voidItem.setData("VOID items", reportDaySales.getItemVoidQty() + "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getItemVoid()).toString(), true);
            ll_sales_total.addView(voidItem);
            // VOID Bills
            ReportDaySalesItem voidBill = new ReportDaySalesItem(context);
            voidBill.setData("VOID Bills", reportDaySales.getBillVoidQty() + "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getBillVoid()).toString(), true);
            ll_sales_total.addView(voidBill);
            // REFUND Bills
            ReportDaySalesItem refundBill = new ReportDaySalesItem(context);
            refundBill.setData("REFUND Bills", reportDaySales.getBillRefundQty() + "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getBillRefund()).toString(), true);
            ll_sales_total.addView(refundBill);
            // REFUND Taxes
            ReportDaySalesItem refundTax = new ReportDaySalesItem(context);
            refundTax.setData("REFUND Taxes", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getRefundTax()).toString(), true);
            ll_sales_total.addView(refundTax);
            // Discount on%
            ReportDaySalesItem discountA = new ReportDaySalesItem(context);
            discountA.setData("Discount on %", reportDaySales.getDiscountPerQty() + "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getDiscountPer()).toString(), true);
            ll_sales_total.addView(discountA);
            // Discount on$
            ReportDaySalesItem discountB = new ReportDaySalesItem(context);
            discountB.setData("Discount on $", reportDaySales.getDiscountQty() + "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getDiscount()).toString(), true);
            ll_sales_total.addView(discountB);
            ReportDaySalesItem promotionTotal = new ReportDaySalesItem(context);
            promotionTotal.setData("Promotion Total", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getPromotionTotal()).toString(), true)
            ;
            ll_sales_total.addView(promotionTotal);
            double nSales = parseDoubleNullCheck(reportDaySales.getItemSales()) +
                    parseDoubleNullCheck(reportDaySales.getTopUps()) -
                    parseDoubleNullCheck(reportDaySales.getFocItem()) -
                    parseDoubleNullCheck(reportDaySales.getFocBill()) -
                    parseDoubleNullCheck(reportDaySales.getItemVoid()) -
                    parseDoubleNullCheck(reportDaySales.getBillVoid()) -
                    parseDoubleNullCheck(reportDaySales.getBillRefund()) -
                    parseDoubleNullCheck(reportDaySales.getDiscount()) -
                    parseDoubleNullCheck(reportDaySales.getDiscountPer()) -
                    parseDoubleNullCheck(reportDaySales.getPromotionTotal());
            ReportDaySalesItem nettsSales = new ReportDaySalesItem(context);
            nettsSales.setData("NETT Sales", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(nSales + "").toString(), true)
            ;
            ll_sales_total.addView(nettsSales);

            ReportDaySalesItem nettSalesSvg = new ReportDaySalesItem(context);

            if (reportDayTaxs != null) {

                BigDecimal taxSvg = BH.getBD("0.00");
                for (int i = 0; i < reportDayTaxs.size(); i++) {
                    //服务税
                    if (reportDayTaxs.get(i).getTaxType() != null) {
                        if (reportDayTaxs.get(i).getTaxType().intValue() == 1) {
                            ReportDayTax reportDayTax = reportDayTaxs.get(i);

                            taxSvg = BH.add(taxSvg, BH.getBD(reportDayTax.getTaxAmount()), true);
                        } else if (reportDayTaxs.get(i).getTaxType().intValue() == 2) {
                            if (!reportDayTaxs.get(i).getTaxPercentage().equals(0.07)) {
                                ReportDayTax reportDayTax = reportDayTaxs.get(i);
                                taxSvg = BH.add(taxSvg, BH.getBD(reportDayTax.getTaxAmount()), true);
                            }
                        }
                    }
                }

                nettSalesSvg.setData("NETT Sales + SVG", "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(BH.add(taxSvg, BH.getBD(nSales), true).toString()), true);
            } else {
                nettSalesSvg.setData("NETT Sales + SVG", "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(nSales + "").toString(), true)
                ;
            }

            ll_sales_total.addView(nettSalesSvg);
            // Exlusive Tax
            ReportDaySalesItem exclusiveTax = new ReportDaySalesItem(context);
            exclusiveTax.setData("Exclusive Tax", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getTotalTax()).toString(), true);
            ll_sales_total.addView(exclusiveTax);
            // Inclusive Tax
            ReportDaySalesItem inclusiveTax = new ReportDaySalesItem(context);
            inclusiveTax.setData("Exclusive Tax", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getInclusiveTaxAmt()).toString(), true);
            ll_sales_total.addView(inclusiveTax);
            BigDecimal overPaymentAmount = BH.getBD(ParamConst.DOUBLE_ZERO);
            if (reportDayPayments != null && reportDayPayments.size() > 0) {
                for (ReportDayPayment reportDayPayment : reportDayPayments) {
                    overPaymentAmount = BH.add(overPaymentAmount, BH.getBD(reportDayPayment.getOverPaymentAmount()), false);
                }
                if (overPaymentAmount.compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) > 0) {
                    ReportDaySalesItem other = new ReportDaySalesItem(context);
                    other.setData("Custom Change", "",
                            App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(overPaymentAmount.toString()), true);
                    ll_sales_total.addView(other);
                }
            }
            ReportDaySalesItem rounding = new ReportDaySalesItem(context);
            rounding.setData("Rounding", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getTotalBalancePrice()), true);
            ll_sales_total.addView(rounding);

            // Total/sales
            double grossTotal;
            if (reportDaySales.getPromotionTotal() != null) {
                grossTotal = nSales + parseDoubleNullCheck(reportDaySales.getTotalTax()) + parseDoubleNullCheck(reportDaySales.getTotalBalancePrice()) + overPaymentAmount.doubleValue();
            } else {
                grossTotal = nSales + parseDoubleNullCheck(reportDaySales.getTotalTax()) + parseDoubleNullCheck(reportDaySales.getTotalBalancePrice()) + overPaymentAmount.doubleValue();
            }
            //   double grossTotal = nSales + parseDoubleNullCheck(reportDaySales.getTotalTax()) + parseDoubleNullCheck(reportDaySales.getTotalBalancePrice()) + overPaymentAmount.doubleValue();
            ReportDaySalesItem totalSales = new ReportDaySalesItem(context);
            totalSales.setData("Gross Total Sales", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(grossTotal + "").toString(), true);
            ll_sales_total.addView(totalSales);


            ReportDaySalesItem mediaView = new ReportDaySalesItem(context);
            mediaView.setTitle("-----MEDIA-----");
            mediaView.setData("", "", "", false);
            ll_sales_total.addView(mediaView);
            // CASH

            if (BH.getBD(reportDaySales.getCash()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem cashView = new ReportDaySalesItem(context);


                cashView.setData("CASH", reportDaySales.getCashQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getCash()), true);

                ll_sales_total.addView(cashView);
            }

            // Diner App
            if (BH.getBD(reportDaySales.getPaypalpay()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem dinerApp = new ReportDaySalesItem(context);
                dinerApp.setData("Diner App", reportDaySales.getPaypalpayQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getPaypalpay()), true);
                ll_sales_total.addView(dinerApp);
            }
            // Stored-Card Use
            if (BH.getBD(reportDaySales.getStoredCard()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem storedCardUse = new ReportDaySalesItem(context);
                storedCardUse.setData("Stored_Card Use", reportDaySales.getStoredCardQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getStoredCard()), true);
                ll_sales_total.addView(storedCardUse);
            }
            // Stored-Card Charge
            if (BH.getBD(reportDaySales.getTopUps()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem storedCardCharge = new ReportDaySalesItem(context);
                storedCardCharge.setData("Store_Card Charge", reportDaySales.getTopUpsQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getTopUps()), true);
                ll_sales_total.addView(storedCardCharge);
            }
            // NETS
            if (BH.getBD(reportDaySales.getNets()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem nets = new ReportDaySalesItem(context);
                nets.setData("NETS", reportDaySales.getNetsQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getNets()), true);
                ll_sales_total.addView(nets);
            }
            // VISA

            if (BH.getBD(reportDaySales.getVisa()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem visa = new ReportDaySalesItem(context);
                visa.setData("VISA", reportDaySales.getVisaQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getVisa()), true);
                ll_sales_total.addView(visa);
            }
            // MC
            if (BH.getBD(reportDaySales.getMc()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem mc = new ReportDaySalesItem(context);
                mc.setData("MC", reportDaySales.getMcQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getMc()), true);
                ll_sales_total.addView(mc);
            }
            // AMEX
            if (BH.getBD(reportDaySales.getAmex()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem amex = new ReportDaySalesItem(context);
                amex.setData("AMEX", reportDaySales.getAmexQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getAmex()), true);
                ll_sales_total.addView(amex);
            }
            // JBL
            if (BH.getBD(reportDaySales.getJbl()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem jbl = new ReportDaySalesItem(context);
                jbl.setData("JBL", reportDaySales.getJblQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getJbl()), true);
                ll_sales_total.addView(jbl);
            }
            // unionPAYPayQty
            if (BH.getBD(reportDaySales.getUnionPay()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem unionPay = new ReportDaySalesItem(context);
                unionPay.setData("union Pay", reportDaySales.getUnionPayQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getUnionPay()), true);
                ll_sales_total.addView(unionPay);
            }
            // Diner
            if (BH.getBD(reportDaySales.getDiner()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem diner = new ReportDaySalesItem(context);
                diner.setData("Diner", reportDaySales.getDinerQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getDiner()), true);
                ll_sales_total.addView(diner);
            }
            // BOH
            if (BH.getBD(reportDaySales.getHoldld()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem boh = new ReportDaySalesItem(context);
                boh.setData("BOH", reportDaySales.getHoldldQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getHoldld()), true);
                ll_sales_total.addView(boh);
            }
            // DELIVEROO
            if (BH.getBD(reportDaySales.getDeliveroo()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem deliveroo = new ReportDaySalesItem(context);
                deliveroo.setData("DELIVEROO", reportDaySales.getDeliverooQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getDeliveroo()), true);
                ll_sales_total.addView(deliveroo);
            }
//            // UBEREATS
//            ((TextView) findViewById(R.id.tv_ubereats_item_num)).setText(reportDaySales.getUbereatsQty() + "");
//            ((TextView) findViewById(R.id.tv_ubereats_item)).setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + reportDaySales.getUbereats());
            // FOODPANDA
            if (BH.getBD(reportDaySales.getFoodpanda()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem foodpanda = new ReportDaySalesItem(context);
                foodpanda.setData("FOODPANDA", reportDaySales.getFoodpandaQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getFoodpanda()), true);
                ll_sales_total.addView(foodpanda);
            }

            if (BH.getBD(reportDaySales.getPayHalal()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem payhalal = new ReportDaySalesItem(context);
                payhalal.setData("PayHalal", reportDaySales.getPayHalalQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + reportDaySales.getPayHalal(), true);
                ll_sales_total.addView(payhalal);
            }
            if (reportDayPayments != null && reportDayPayments.size() > 0) {
                int totalCustomPaymentQty = 0;
                BigDecimal totalCustomPayment = BH.getBD(ParamConst.DOUBLE_ZERO);
                for (ReportDayPayment reportDayPayment : reportDayPayments) {
                    totalCustomPaymentQty += reportDayPayment.getPaymentQty();
                    totalCustomPayment = BH.add(totalCustomPayment, BH.getBD(reportDayPayment.getPaymentAmount()), false);
                    ReportDaySalesItem other = new ReportDaySalesItem(context);
                    other.setData(reportDayPayment.getPaymentName(), reportDayPayment.getPaymentQty() + "",
                            App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDayPayment.getPaymentAmount()), true);
                    ll_sales_total.addView(other);
                }
                ReportDaySalesItem totalOther = new ReportDaySalesItem(context);
                totalOther.setData("TOTAL CUSTOM PAYMENT", totalCustomPaymentQty + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(totalCustomPayment.toString()), true);
                ll_sales_total.addView(totalOther);
            }
//            // VOUCHER
//            ((TextView) findViewById(R.id.tv_voucher_item_num)).setText(reportDaySales.getVoucherQty() + "");
//            ((TextView) findViewById(R.id.tv_voucher_item)).setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + reportDaySales.getVoucher());
            // TOTAL DELIVERY

            BigDecimal f = BH.add(BH.add(BH.getBD(reportDaySales.getDeliveroo()), BH.getBD(reportDaySales.getUbereats()), false), BH.getBD(reportDaySales.getFoodpanda()), true);
            // float f = Float.parseFloat(reportDaySales.getDeliveroo()) + Float.parseFloat(reportDaySales.getUbereats()) + Float.parseFloat(reportDaySales.getFoodpanda());
            if (f.compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem totalDelivery = new ReportDaySalesItem(context);
                totalDelivery.setData("TOTAL DELIVERY", reportDaySales.getDeliverooQty().intValue() + reportDaySales.getUbereatsQty().intValue() + reportDaySales.getFoodpandaQty().intValue() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(f + ""), true);
                ll_sales_total.addView(totalDelivery);
            }
            // TOTAL/CARD
            if (BH.getBD(reportDaySales.getTotalCard()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem totalCard = new ReportDaySalesItem(context);
                totalCard.setData("TOTAL CARD", reportDaySales.getTotalCardQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getTotalCard()).toString(), true);
                ll_sales_total.addView(totalCard);
            }
            // TOTAL/NETS
            if (BH.getBD(reportDaySales.getNets()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem totalNets = new ReportDaySalesItem(context);
                totalNets.setData("TOTAL NETS", reportDaySales.getNetsQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getNets()).toString(), true);
                ll_sales_total.addView(totalNets);
            }
            // TOTAL/CASH

            if (BH.getBD(reportDaySales.getTotalCash()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem totalCash = new ReportDaySalesItem(context);
                totalCash.setData("TOTAL CASH", reportDaySales.getTotalCashQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getTotalCash()).toString(), true);
                ll_sales_total.addView(totalCash);
            }
            // TOTAL/BOH
            if (BH.getBD(reportDaySales.getHoldld()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem totalBoh = new ReportDaySalesItem(context);
                totalBoh.setData("TOTAL BOH", reportDaySales.getHoldldQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getHoldld()).toString(), true);
                ll_sales_total.addView(totalBoh);
            }
            // Netts/Sales
//            ReportDaySalesItem nettSales = new ReportDaySalesItem(context);
//            nettSales.setData("Nett Sales", "",
//                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + reportDaySales.getNettSales(), true);
//            ll_sales_total.addView(nettSales);
            // VOID/Items


            if (BH.getBD(reportDaySales.getBillVoid()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0
                    || BH.getBD(reportDaySales.getItemVoid()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0
                    || BH.getBD(reportDaySales.getBillRefund()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0
                    || BH.getBD(reportDaySales.getRefundTax()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {

                ReportDaySalesItem voidItemView = new ReportDaySalesItem(context);
                voidItemView.setTitle("-----VOID/REFUND/SUMMARY-----");
                voidItemView.setData("VOID Items", reportDaySales.getItemVoidQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getItemVoid()).toString(), true);
                ll_sales_total.addView(voidItemView);
                // VOID Bills
                ReportDaySalesItem voidBillView = new ReportDaySalesItem(context);
                voidBillView.setData("VOID Bills", reportDaySales.getBillVoidQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getBillVoid()).toString(), true);
                ll_sales_total.addView(voidBillView);
                // TOTAL VOID
                BigDecimal f1 = BH.add(BH.getBD(reportDaySales.getItemVoid()), BH.getBD(reportDaySales.getBillVoid()), true);

                // float f1 = Float.parseFloat(reportDaySales.getItemVoid()) + Float.parseFloat(reportDaySales.getBillVoid());
                ReportDaySalesItem totalVoid = new ReportDaySalesItem(context);
                totalVoid.setData("TOTAL VOID", "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(f1 + "").toString(), true);
                ll_sales_total.addView(totalVoid);
                // REFUND Bills
                ReportDaySalesItem refundBillView = new ReportDaySalesItem(context);
                refundBillView.setData("REFUND Bills", reportDaySales.getBillRefundQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getBillRefund()).toString(), true);
                ll_sales_total.addView(refundBillView);
                // REFUND Taxes
                ReportDaySalesItem refundTaxView = new ReportDaySalesItem(context);
                refundTaxView.setData("REFUND Taxes", "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getRefundTax()).toString(), true);
                ll_sales_total.addView(refundTaxView);
                // TOTAL REFUND
                BigDecimal f2 = BH.add(BH.getBD(reportDaySales.getBillRefund()), BH.getBD(reportDaySales.getRefundTax()), true);

//                float f2 = Float.parseFloat(reportDaySales.getBillRefund()) + Float.parseFloat(reportDaySales.getRefundTax());
                ReportDaySalesItem totalRefundView = new ReportDaySalesItem(context);
                totalRefundView.setData("TOTAL REFUND", "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(f2 + "").toString(), true);
                ll_sales_total.addView(totalRefundView);
            }

            // Discount on%


            if (BH.getBD(reportDaySales.getDiscountPer()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0
                    || BH.getBD(reportDaySales.getDiscount()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
                ReportDaySalesItem disncountViewA = new ReportDaySalesItem(context);
                disncountViewA.setTitle("-----DISCOUNTS-----");
                disncountViewA.setData("Discount on %", reportDaySales.getDiscountPerQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getDiscountPer()).toString(), true);
                ll_sales_total.addView(disncountViewA);
                // Discount on$

                ReportDaySalesItem disncountViewB = new ReportDaySalesItem(context);
                disncountViewB.setData("Discount on $", reportDaySales.getDiscountQty() + "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getDiscount()).toString(), true);
                ll_sales_total.addView(disncountViewB);
                // Total Discount
                BigDecimal f3 = BH.add(BH.getBD(reportDaySales.getDiscountPer()), BH.getBD(reportDaySales.getDiscount()), true);

                //  float f3 = Float.parseFloat(reportDaySales.getDiscountPer()) + Float.parseFloat(reportDaySales.getDiscount());
                ReportDaySalesItem totalDiscount = new ReportDaySalesItem(context);
                totalDiscount.setData("Total Discount", "",
                        App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(f3 + "").toString(), true);
                ll_sales_total.addView(totalDiscount);
            }

            // Svc Charge
//        ((TextView)findViewById(R.id.tv_svc_charge_item_num)).setText(reportDaySales.getDiscountPerQty() + "");
//        ((TextView)findViewById(R.id.tv_svc_charge_item)).setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + reportDaySales.getDiscountPer());
            // GST
//        ((TextView)findViewById(R.id.tv_gst_item_num)).setText(reportDaySales.getDiscountPerQty() + "");
//        ((TextView)findViewById(R.id.tv_gst_item)).setText(App.instance.getLocalRestaurantConfig().getCurrencySymbol() + reportDaySales.getDiscountPer());
            // Inclusive Tax
            ReportDaySalesItem titleView = new ReportDaySalesItem(context);
            titleView.setTitle("-----TAX/SVC-----");
            titleView.setData("", "", "", false);
            ll_sales_total.addView(titleView);
            if (reportDayTaxs != null) {

                BigDecimal taxSvc = BH.getBD("0.00");
                for (int i = 0; i < reportDayTaxs.size(); i++) {
                    ReportDaySalesItem TaxView = new ReportDaySalesItem(context);
                    ReportDayTax reportDayTax = reportDayTaxs.get(i);
//                    this.addItem(reportDayTax.getTaxName(), "", reportDayTax.getTaxAmount(), 1);
//                    taxSvc = BH.add(taxSvc, BH.getBD(reportDayTax.getTaxAmount()), true);

                    TaxView.setData(reportDayTax.getTaxName(), "",
                            App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDayTax.getTaxAmount()).toString(), true);
                    ll_sales_total.addView(TaxView);
                }
            }


            // Inclusive Tax
            ReportDaySalesItem inclusiveTaxView = new ReportDaySalesItem(context);
            inclusiveTaxView.setData("Inclusive Tax", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getInclusiveTaxAmt()).toString(), true);
            ll_sales_total.addView(inclusiveTaxView);
            // Total Tax

            BigDecimal f4 = BH.add(BH.getBD(reportDaySales.getTotalTax()), BH.getBD(reportDaySales.getInclusiveTaxAmt()), true);

            // float f4 = Float.parseFloat(reportDaySales.getTotalTax()) + Float.parseFloat(reportDaySales.getInclusiveTaxAmt());
            ReportDaySalesItem totalTaxView = new ReportDaySalesItem(context);
            totalTaxView.setData(getString(R.string.total_tax_tvc), "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(f4 + ""), true);
            ll_sales_total.addView(totalTaxView);
            // Start Drawer
            ReportDaySalesItem startDrawer = new ReportDaySalesItem(context);
            startDrawer.setTitle("-----DRAWER-----");
            SessionStatus sessionStatus = Store.getObject(context,
                    Store.SESSION_STATUS, SessionStatus.class);
            String start;
            String startNum = BH.getBD(CashInOutSQL.getStartDrawerSUMBySession(App.instance.getBusinessDate(), sessionStatus)) + "";
            if (reportDaySales.getBusinessDate().equals(App.instance.getBusinessDate())) {
                if (reportDaySales.getStartDrawerAmount().equals("0.00")) {
                    if (!TextUtils.isEmpty(startNum)) {
                        start = startNum;
                    } else {
                        start = "0.00";
                    }
                } else {
                    if (!TextUtils.isEmpty(startNum)) {
                        start = BH.add(BH.getBD(startNum), BH.getBD(reportDaySales.getStartDrawerAmount()), true).toString();

                        // start = (Float.parseFloat(startNum) + Float.parseFloat(reportDaySales.getStartDrawerAmount())) + "";
                    } else {
                        start = reportDaySales.getStartDrawerAmount();
                    }
                }
            } else {
                start = reportDaySales.getStartDrawerAmount();
            }
            startDrawer.setData(getString(R.string.start_drawer), "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(start), true);
            ll_sales_total.addView(startDrawer);
            // TOTAL CASH
            ReportDaySalesItem totalCASHView = new ReportDaySalesItem(context);
            totalCASHView.setData("Total Cash", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getTotalCash()), true);
            ll_sales_total.addView(totalCASHView);
            // Stored-card Cash Charge
            ReportDaySalesItem storedCardCashCharge = new ReportDaySalesItem(context);
            storedCardCashCharge.setData("Stored-Card Cash Charge", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getCashTopUp()), true);
            ll_sales_total.addView(storedCardCashCharge);
            // Cash In
            ReportDaySalesItem cashIN = new ReportDaySalesItem(context);
            cashIN.setData("Cash In", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getCashInAmt()), true);
            ll_sales_total.addView(cashIN);
            // Cash Out
            ReportDaySalesItem cashOUT = new ReportDaySalesItem(context);
            cashOUT.setData("Cash Out", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getCashOutAmt()), true);
            ll_sales_total.addView(cashOUT);
            // Expected In Drawer
            ReportDaySalesItem expected = new ReportDaySalesItem(context);
            expected.setData("Expected in Drawer", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getExpectedAmount()), true);
            ll_sales_total.addView(expected);
            // Actual In Drawer
            ReportDaySalesItem actual = new ReportDaySalesItem(context);
            actual.setData(context.getString(R.string.start_drawer), "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getWaiterAmount()), true);
            ll_sales_total.addView(actual);
            // Difference
            ReportDaySalesItem difference = new ReportDaySalesItem(context);
            difference.setData("Difference", "",
                    App.instance.getLocalRestaurantConfig().getCurrencySymbol() + BH.formatMoney(reportDaySales.getDifference()), true);
            ll_sales_total.addView(difference);

            ((TextView) findViewById(R.id.tv_total_bill)).setText(reportDaySales.getTotalBills().toString());
            ((TextView) findViewById(R.id.tv_total_temp_menu)).setText(reportDaySales.getOpenCount().toString());
            ((TextView) findViewById(R.id.tv_detail_total_num)).setText(detailTotalQty + "");
            ((TextView) findViewById(R.id.tv_detail_total)).setText(BH.formatMoney(detailTotalAmount.toString()).toString());
            ((TextView) findViewById(R.id.tv_summary_total_num)).setText(detailTotalQty + "");
            ((TextView) findViewById(R.id.tv_summary_total)).setText(BH.formatMoney(detailTotalAmount.toString()).toString());
            ((TextView) findViewById(R.id.tv_hourly_total_num)).setText(hourlyTotalQty + "");
            ((TextView) findViewById(R.id.tv_hourly_total)).setText(BH.formatMoney(hourlyTotalAmount.toString()).toString());
            if (reportHourlys != null && reportHourlys.size() > 0) {
                XZReportHourlyAdapter hourlyAdapter = new XZReportHourlyAdapter(reportHourlys, XZReportActivity.this);
                lv_hourly_analsis.setAdapter(hourlyAdapter);
            }
            if (reportPluDayItems != null && reportPluDayItems.size() > 0) {
                XZReportDetailAdapter xzReportDetailAdapter = new XZReportDetailAdapter(reportDetailAnalysisItems, context);
                ((ListView) findViewById(R.id.lv_detail_analsis)).setAdapter(xzReportDetailAdapter);
                XZReportSumaryAdapter xzReportSumaryAdapter = new XZReportSumaryAdapter(reportDetailAnalysisItemList, context);
                ((ListView) findViewById(R.id.lv_summary_sales)).setAdapter(xzReportSumaryAdapter);
            }
        } else {
            UIHelp.showShortToast(XZReportActivity.this, XZReportActivity.this.getString(R.string.no_data));
            calendarCard.setVisibility(View.VISIBLE);
            ll_xz_analsis.setVisibility(View.GONE);
            ll_print.setVisibility(View.GONE);
        }

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_back:
                    if (calendarCard.getVisibility() == View.VISIBLE && ll_xz_analsis.getVisibility() == View.GONE) {
                        XZReportActivity.this.finish();
                    } else if (calendarCard.getVisibility() == View.GONE && ll_xz_analsis.getVisibility() == View.VISIBLE) {
                        ll_xz_analsis.setVisibility(View.GONE);
                        calendarCard.setVisibility(View.VISIBLE);
                        ll_print.setVisibility(View.INVISIBLE);
                        tv_title_name.setText(getString(R.string.report_day_sale));
                    }
                    break;
                case R.id.ll_print:

                    break;
            }
        }
    };

    OnCellItemClick onCellItemClick = new OnCellItemClick() {
        @Override
        public void onCellClick(View v, CardGridItem item) {
            date = item.getDate().getTimeInMillis();
            Date date1 = new Date(item.getDate().getTimeInMillis());
            SimpleDateFormat yearMonthDayFormater = new SimpleDateFormat("yyyy年MM月dd日");
            String str = yearMonthDayFormater.format(date1);
            loadingDialog.setTitle(context.getString(R.string.loading));
            loadingDialog.show();
            if (str.equals(curStr)) {
                date = businessDate;
                showBusinessDate = date;
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        if (App.instance.getSessionStatus() != null) {
                            loadNewReport(showBusinessDate);
                        } else {
                            loadOldReport(showBusinessDate);
                        }
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (loadingDialog != null && loadingDialog.isShowing())
                                    loadingDialog.dismiss();
                                initData(businessDate);
                            }
                        });
                    }
                }).start();


            } else if (revenueCenter != null) {

                Map<String, Object> param = new HashMap<String, Object>();
                param.put("businessDate", TimeUtil.getMDY(date));
                param.put("bizDate", TimeUtil.getBusinessDateByDay(date, 0));
                param.put("revenueId", revenueCenter.getId().intValue());
                SyncCentre.getInstance().loadCloudDaySalesReport(
                        context, param, handler);
            }
        }
    };


    @Override
    protected void handlerClickEvent(View v) {
        switch (v.getId()) {
            case R.id.tv_print:
                showPrintDialog(showBusinessDate, true);
                break;
        }
        super.handlerClickEvent(v);
    }


    private void showPrintDialog(final Long bizDate, final boolean zPrint) {
        if (reportDaySales != null) {
            DialogSelectReportPrint.show(context,
                    new View.OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            switch (v.getId()) {
                                case R.id.btn_report_all: {
                                    sendPrintData(
                                            XZReportHtml.REPORT_PRINT_ALL,
                                            zPrint, bizDate);
                                }
                                break;
                                case R.id.btn_report_sales: {
                                    sendPrintData(
                                            XZReportHtml.REPORT_PRINT_SALES,
                                            zPrint, bizDate);
                                }
                                break;
                                case R.id.btn_report_detail_analysis: {
                                    sendPrintData(
                                            XZReportHtml.REPORT_PRINT_DETAILS,
                                            zPrint, bizDate);
                                }
                                break;
                                case R.id.btn_report_summary_analysis: {
                                    sendPrintData(
                                            XZReportHtml.REPORT_PRINT_SUMMARY,
                                            zPrint, bizDate);
                                }
                                break;
                            }
                        }
                    });
        } else {
            DialogFactory.alertDialog(
                    context,
                    context.getResources().getString(
                            R.string.warning),
                    context.getResources().getString(
                            R.string.no_sales_print));
        }
    }

    private void sendPrintData(int type, boolean zPrint, long bzDate) {
        String bizDate = TimeUtil.getPrintingDate(bzDate);
        String rptType = CommonUtil.getReportType(context, 999);

        SessionStatus session = App.instance.getSessionStatus();

        String label = "YX";
        if (zPrint) {
            label = "YZ";
        } else {
            rptType = CommonUtil.getReportType(context,
                    session.getSession_status());
            bizDate = TimeUtil.getPrintingDate(businessDate);
        }

        PrinterTitle title = ObjectFactory.getInstance()
                .getPrinterTitleForReport(
                        App.instance.getRevenueCenter().getId(),
                        label + reportDaySales.getReportNoStr(),
                        App.instance.getUser().getFirstName()
                                + App.instance.getUser().getLastName(), null,
                        bizDate, App.instance.getSystemSettings().getTrainType());

        PrinterDevice cashierPrinter = App.instance.getCahierPrinter();
        List<ReportUserOpenDrawer> reportUserOpenDrawers = new ArrayList<ReportUserOpenDrawer>();
        if (zPrint) {
            reportUserOpenDrawers = UserOpenDrawerRecordSQL.getReportUserOpenDrawerByTime(businessDate);
        } else {
            reportUserOpenDrawers = UserOpenDrawerRecordSQL.getReportUserOpenDrawer(session.getSession_status(), bzDate);
        }
        if (cashierPrinter == null) {
            AlertToDeviceSetting.noKDSorPrinter(context, context.getResources()
                    .getString(R.string.no_printer_devices));
        } else {
            if (type == XZReportHtml.REPORT_PRINT_ALL) {

                // sales report
                App.instance.remotePrintDaySalesReport(rptType, cashierPrinter,
                        title, reportDaySales, reportDayTaxs, reportDayPayments, reportUserOpenDrawers, null);
                // detail analysis
                App.instance.remotePrintDetailAnalysisReport(rptType,
                        cashierPrinter, title, reportDaySales,
                        reportPluDayItems, reportPluDayModifiers,
                        reportPluDayComboModifiers, itemMainCategorys,
                        itemCategorys);
                // summary
                App.instance
                        .remotePrintSummaryAnalysisReport(rptType,
                                cashierPrinter, title, reportPluDayItems,
                                reportPluDayModifiers, itemMainCategorys,
                                itemCategorys);
                // hourly sales
                App.instance.remotePrintHourlyReport(rptType, cashierPrinter,
                        title, reportHourlys);
            }
            if (type == XZReportHtml.REPORT_PRINT_SALES) {
                // sales report
                App.instance.remotePrintDaySalesReport(rptType, cashierPrinter,
                        title, reportDaySales, reportDayTaxs, reportDayPayments, reportUserOpenDrawers, null);
            }
            if (type == XZReportHtml.REPORT_PRINT_DETAILS) {
                if (zPrint)
                    App.instance.remotePrintDetailAnalysisReport(rptType,
                            cashierPrinter, title, reportDaySales,
                            reportPluDayItems, reportPluDayModifiers,
                            reportPluDayComboModifiers, itemMainCategorys,
                            itemCategorys);
                else
                    App.instance.remotePrintDetailAnalysisReport(rptType,
                            cashierPrinter, title, null, reportPluDayItems,
                            reportPluDayModifiers, reportPluDayComboModifiers,
                            itemMainCategorys, itemCategorys);

                // if(reportPluDayComboModifiers != null &&
                // reportPluDayComboModifiers.size() > 0) {
                // App.instance.remotePrintComboDetailAnalysisReport(rptType,
                // cashierPrinter, title,
                // filteredPluDayItems, reportPluDayComboModifiers);
                // }

            }
            if (type == XZReportHtml.REPORT_PRINT_SUMMARY) {
                // sales report
                App.instance
                        .remotePrintSummaryAnalysisReport(rptType,
                                cashierPrinter, title, reportPluDayItems,
                                reportPluDayModifiers, itemMainCategorys,
                                itemCategorys);
            }
            if (type == XZReportHtml.REPORT_PRINT_HOURLY) {
                // hourly sales
                App.instance.remotePrintHourlyReport(rptType, cashierPrinter,
                        title, reportHourlys);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if (calendarCard != null && ll_xz_analsis != null
                    && calendarCard.getVisibility() == View.GONE && ll_xz_analsis.getVisibility() == View.VISIBLE) {
                calendarCard.setVisibility(View.VISIBLE);
                ll_xz_analsis.setVisibility(View.GONE);
                ll_print.setVisibility(View.INVISIBLE);
                tv_title_name.setText(getString(R.string.report_day_sale));
            } else {
                this.finish();
            }
        }
        return false;
    }

    private double parseDoubleNullCheck(String string){
        try {
            if(!TextUtils.isEmpty(string)) {
                return Double.parseDouble(string);
            }else{
                return 0;
            }
        }catch (Exception e){
            return 0;
        }
    }
}
