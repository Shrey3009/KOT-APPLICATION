package com.alfredbase.utils;

import android.text.TextUtils;
import android.util.Log;

import com.alfredbase.BaseApplication;
import com.alfredbase.ParamConst;
import com.alfredbase.global.CoreData;
import com.alfredbase.javabean.HappyHourWeek;
import com.alfredbase.javabean.ItemDetail;
import com.alfredbase.javabean.ItemHappyHour;
import com.alfredbase.javabean.ItemModifier;
import com.alfredbase.javabean.ItemPromotion;
import com.alfredbase.javabean.Modifier;
import com.alfredbase.javabean.Order;
import com.alfredbase.javabean.OrderDetail;
import com.alfredbase.javabean.OrderDetailTax;
import com.alfredbase.javabean.OrderModifier;
import com.alfredbase.javabean.OrderPromotion;
import com.alfredbase.javabean.OrderSplit;
import com.alfredbase.javabean.Printer;
import com.alfredbase.javabean.Promotion;
import com.alfredbase.javabean.PromotionOrder;
import com.alfredbase.javabean.PromotionWeek;
import com.alfredbase.javabean.RevenueCenter;
import com.alfredbase.javabean.RoundAmount;
import com.alfredbase.javabean.Tax;
import com.alfredbase.javabean.TaxCategory;
import com.alfredbase.store.Store;
import com.alfredbase.store.sql.OrderDetailSQL;
import com.alfredbase.store.sql.OrderDetailTaxSQL;
import com.alfredbase.store.sql.OrderModifierSQL;
import com.alfredbase.store.sql.OrderSQL;
import com.alfredbase.store.sql.OrderSplitSQL;
import com.alfredbase.store.sql.PromotionDataSQL;
import com.alfredbase.store.sql.PromotionOrderSQL;
import com.alfredbase.store.sql.PromotionSQL;
import com.alfredbase.store.sql.PromotionWeekSQL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;



public class OrderHelper {
	/**
	 * 获取订单详情的Tax
	 *
	 * @param orderDetail
	 * @return
	 */
	public static BigDecimal getOrderDetailTax(Order order,
			OrderDetail orderDetail) {
		BigDecimal preTaxPrice = BH.sub(BH.getBD(orderDetail.getRealPrice()),
				getOrderDetailDiscountPrice(orderDetail), false);

		ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(orderDetail.getItemId(), orderDetail.getItemName());
		BigDecimal taxTotal = BH.getBD(ParamConst.DOUBLE_ZERO);
		if(itemDetail == null || itemDetail.getTaxCategoryId() == null)
		{
			return taxTotal;
		}
		List<TaxCategory> taxCategories = CoreData.getInstance().getTaxCategorys(itemDetail.getTaxCategoryId());
		if (taxCategories.size() > 0)
		{
			for (TaxCategory taxCategory : taxCategories)
			{
				Tax tax = CoreData.getInstance().getTax(taxCategory.getTaxId());
				if (tax == null)
				{
					continue;
				}
				if(tax.getBeforeDiscount()==0)
				{
					OrderDetailTax orderDetailTax = ObjectFactory.getInstance().getOrderDetailTax(order, orderDetail, tax, taxCategory.getIndex());
					Boolean completedOrder = false;
					for(OrderSplit finishedOrder : OrderSplitSQL.getFinishedOrderSplits(order.getId()))
					{
						if(orderDetailTax.getOrderSplitId().equals(finishedOrder.getId()))
						{
							if(finishedOrder.getOrderStatus() == ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED)
							{
								completedOrder = true;
							}
						}
					}
					if (taxCategory.getTaxOn() == ParamConst.TAX_ON_TAX_1 || taxCategory.getTaxOn() == ParamConst.TAX_ON_TAX_2)
					{
						TaxCategory temp = CoreData.getInstance().getTaxCategory(taxCategory.getTaxOnId());
						Tax taxOn = CoreData.getInstance().getTax(temp.getTaxId());
						if(taxOn.getBeforeDiscount()==0)
						{
							if (orderDetail.getIsTakeAway() == ParamConst.TAKE_AWAY)
							{
								if (orderDetailTax.getTaxType() == ParamConst.TAX_TYPE_SERVICE) {
									orderDetailTax.setTaxPrice(ParamConst.DOUBLE_ZERO);
								} else if (taxOn.getTaxType() == ParamConst.TAX_TYPE_SERVICE
										&& tax.getTaxType() == ParamConst.TAX_TYPE_SERVICE) {
									taxTotal = BH.getBD(ParamConst.DOUBLE_ZERO);
									orderDetailTax.setTaxPrice(ParamConst.DOUBLE_ZERO);
								} else if (taxOn.getTaxType() == ParamConst.TAX_TYPE_SERVICE) {
									taxTotal = BH.add(taxTotal, BH.mul(preTaxPrice,
											BH.getBDNoFormat(tax.getTaxPercentage()), false), false);
									orderDetailTax.setTaxPrice(BH.mul(preTaxPrice,
											BH.getBDNoFormat(tax.getTaxPercentage()), false).toString());
								} else if (tax.getTaxType() == ParamConst.TAX_TYPE_SERVICE) {
									taxTotal = BH.add(taxTotal, BH.mul(preTaxPrice,
											BH.getBDNoFormat(taxOn.getTaxPercentage()), false), false);
									orderDetailTax.setTaxPrice(BH.mul(preTaxPrice,
											BH.getBDNoFormat(taxOn.getTaxPercentage()), false).toString());
								} else {
									BigDecimal priceOntax = BH.add(
											preTaxPrice,
											BH.mul(preTaxPrice,
													BH.getBDNoFormat(taxOn.getTaxPercentage()), false),
											false);

									taxTotal = BH.add(taxTotal, BH.mul(priceOntax,
											BH.getBDNoFormat(tax.getTaxPercentage()), false), false);
									orderDetailTax.setTaxPrice(BH.mul(priceOntax,
											BH.getBDNoFormat(tax.getTaxPercentage()), false).toString());
								}
							}
							else
							{
								BigDecimal priceOntax = BH.add(
										preTaxPrice,
										BH.mul(preTaxPrice,
												BH.getBDNoFormat(taxOn.getTaxPercentage()), false),
										false);

								taxTotal = BH.add(taxTotal, BH.mul(priceOntax,
										BH.getBDNoFormat(tax.getTaxPercentage()), false), false);
								orderDetailTax.setTaxPrice(BH.mul(priceOntax,
										BH.getBDNoFormat(tax.getTaxPercentage()), false).toString());
							}
						}
					}
					else
					{
						if(!completedOrder)
						{
							if (order.getIsTakeAway() == ParamConst.TAKE_AWAY || orderDetail.getIsTakeAway() == ParamConst.TAKE_AWAY)
							{
								if (tax.getTaxType() == ParamConst.TAX_TYPE_SERVICE)
								{
									orderDetailTax.setTaxPrice(ParamConst.DOUBLE_ZERO);
								}
								else
								{
									taxTotal = BH.add(taxTotal, BH.mul(preTaxPrice, BH.getBDNoFormat(tax.getTaxPercentage()), false), false);
									orderDetailTax.setTaxPrice(BH.mul(preTaxPrice, BH.getBDNoFormat(tax.getTaxPercentage()), false).toString());
								}
							}
							else
							{
								taxTotal = BH.add(taxTotal, BH.mul(preTaxPrice, BH.getBDNoFormat(tax.getTaxPercentage()), false), false);
								orderDetailTax.setTaxPrice(BH.mul(preTaxPrice, BH.getBDNoFormat(tax.getTaxPercentage()), false).toString());
							}
						}
					}
					OrderDetailTaxSQL.updateOrderDetailTax(orderDetailTax);
				}
			}
		}
		return taxTotal;
	}

	/**
	 * 获取订单详情的Tax“
	 *
	 * @param orderDetail
	 * @return
	 */
	public static BigDecimal getOrderDetailBeforTax(Order order,
											   OrderDetail orderDetail) {
		BigDecimal preTaxPrice = BH.getBD(orderDetail.getRealPrice());


		ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
				orderDetail.getItemId(), orderDetail.getItemName());
		BigDecimal taxTotal = BH.getBD(ParamConst.DOUBLE_ZERO);
		if(itemDetail == null || itemDetail.getTaxCategoryId() == null)
			return taxTotal;
		List<TaxCategory> taxCategories = CoreData.getInstance()
				.getTaxCategorys(itemDetail.getTaxCategoryId());
		if (taxCategories.size() > 0) {
			for (TaxCategory taxCategory : taxCategories) {
				Tax tax = CoreData.getInstance().getTax(taxCategory.getTaxId());
				if (tax == null) {
					continue;
				}

				if(tax.getBeforeDiscount()==1) {

					OrderDetailTax orderDetailTax = ObjectFactory.getInstance().getOrderDetailTax(order, orderDetail, tax, taxCategory.getIndex());
					if ((taxCategory.getTaxOn() == ParamConst.TAX_ON_TAX_1 || taxCategory.getTaxOn() == ParamConst.TAX_ON_TAX_2))
					{
						TaxCategory temp = CoreData.getInstance().getTaxCategory(taxCategory.getTaxOnId());

						Tax taxOn = CoreData.getInstance().getTax(temp.getTaxId());
						if(taxOn.getBeforeDiscount()==1)
						{
							if (order.getIsTakeAway() == ParamConst.TAKE_AWAY || orderDetail.getIsTakeAway() == ParamConst.TAKE_AWAY)
							{
								if (orderDetailTax.getTaxType() == ParamConst.TAX_TYPE_SERVICE)
								{
									orderDetailTax.setTaxPrice(ParamConst.DOUBLE_ZERO);
								}
								else if (taxOn.getTaxType() == ParamConst.TAX_TYPE_SERVICE && tax.getTaxType() == ParamConst.TAX_TYPE_SERVICE)
								{
									taxTotal = BH.getBD(ParamConst.DOUBLE_ZERO);
									orderDetailTax.setTaxPrice(ParamConst.DOUBLE_ZERO);
								}
								else if (taxOn.getTaxType() == ParamConst.TAX_TYPE_SERVICE)
								{
									taxTotal = BH.add(taxTotal, BH.mul(preTaxPrice, BH.getBDNoFormat(tax.getTaxPercentage()), false), false);
									orderDetailTax.setTaxPrice(BH.mul(preTaxPrice, BH.getBDNoFormat(tax.getTaxPercentage()), false).toString());
								}
								else if (tax.getTaxType() == ParamConst.TAX_TYPE_SERVICE)
								{
									taxTotal = BH.add(taxTotal, BH.mul(preTaxPrice, BH.getBDNoFormat(taxOn.getTaxPercentage()), false), false);
									orderDetailTax.setTaxPrice(BH.mul(preTaxPrice, BH.getBDNoFormat(taxOn.getTaxPercentage()), false).toString());
								}
								else
								{
								BigDecimal priceOntax = BH.add(
										preTaxPrice,
										BH.mul(preTaxPrice,
												BH.getBDNoFormat(taxOn.getTaxPercentage()), false),
										false);

								taxTotal = BH.add(taxTotal, BH.mul(priceOntax,
										BH.getBDNoFormat(tax.getTaxPercentage()), false), false);
								orderDetailTax.setTaxPrice(BH.mul(priceOntax,
										BH.getBDNoFormat(tax.getTaxPercentage()), false).toString());
								}
							}
							else
							{
								BigDecimal priceOntax = BH.add(
										preTaxPrice,
										BH.mul(preTaxPrice,
												BH.getBDNoFormat(taxOn.getTaxPercentage()), false),
										false);

								taxTotal = BH.add(taxTotal, BH.mul(priceOntax,
										BH.getBDNoFormat(tax.getTaxPercentage()), false), false);
								orderDetailTax.setTaxPrice(BH.mul(priceOntax,
										BH.getBDNoFormat(tax.getTaxPercentage()), false).toString());
							}
						}

					}
					else
					{
						if (order.getIsTakeAway() == ParamConst.TAKE_AWAY
								|| orderDetail.getIsTakeAway() == ParamConst.TAKE_AWAY) {
							if (tax.getTaxType() == ParamConst.TAX_TYPE_SERVICE) {
								orderDetailTax.setTaxPrice(ParamConst.DOUBLE_ZERO);
							} else {
								taxTotal = BH.add(
										taxTotal,
										BH.mul(preTaxPrice,
												BH.getBDNoFormat(tax.getTaxPercentage()), false),
										false);
								orderDetailTax.setTaxPrice(BH.mul(preTaxPrice,
										BH.getBDNoFormat(tax.getTaxPercentage()), false).toString());
							}
						} else {
							taxTotal = BH.add(
									taxTotal,
									BH.mul(preTaxPrice,
											BH.getBDNoFormat(tax.getTaxPercentage()), false),
									false);
							orderDetailTax.setTaxPrice(BH.mul(preTaxPrice,
									BH.getBDNoFormat(tax.getTaxPercentage()), false).toString());
						}

					}
					OrderDetailTaxSQL.updateOrderDetailTax(orderDetailTax);
				}
			}
		}
		return taxTotal;
	}

	/**
	 * 获取订单详情的基础价格，计算方式：实收金额=(菜单金额-HappyHour金额+配料金额)*数量，也就是UI中的subTotal
	 *
	 * @param order
	 * @param orderDetail
	 * @return
	 */
	public static BigDecimal getOrderDetailRealPrice(Order order,
			OrderDetail orderDetail) {
		BigDecimal price = BH.getBD(ParamConst.DOUBLE_ZERO);
		ItemHappyHour itemHappyHour = getItemHappyHour(order, orderDetail);
		if (itemHappyHour != null) {
			if (BH.getBD(itemHappyHour.getDiscountPrice()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
				price = BH.sub(BH.getBD(orderDetail.getItemPrice()),
						BH.getBDNoFormat(itemHappyHour.getDiscountPrice()), false);
			}  else if (Double.parseDouble(itemHappyHour.getDiscountRate()) > 0) {
				orderDetail.setDiscountRate(itemHappyHour.getDiscountRate());
				price = BH.sub(BH.getBD(orderDetail.getItemPrice()), BH
						.mul(BH.getBD(orderDetail.getItemPrice()),
								BH.getBDNoFormat(itemHappyHour.getDiscountRate()),
								false), false);
			} else if (itemHappyHour.getFreeNum().intValue() > 0) {
				price = BH.getBD(orderDetail.getItemPrice());
			}
		} else {
			price = BH.getBD(orderDetail.getItemPrice());
		}
//		BigDecimal promotionPrice = BH.getBD(ParamConst.DOUBLE_ZERO);
//		ItemPromotion itemPromotion = getItemPromotion(order, orderDetail);
//		if (itemPromotion != null&&hasWeek(itemPromotion.getPromotionId())) {
//			if (BH.getBD(itemPromotion.getDiscountPrice()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
//				price = BH.sub(BH.getBD(orderDetail.getItemPrice()),
//						BH.getBD(itemPromotion.getDiscountPrice()), false);
//                promotionPrice=BH.add(promotionPrice,BH.getBD(itemPromotion.getDiscountPrice()), true);
//			}
//			else if (Double.parseDouble(itemPromotion.getDiscountPercentage()) > 0) {
//				orderDetail.setDiscountRate(itemPromotion.getDiscountPercentage());
//
//				 if(price.compareTo(BH.getBD("0")) > 0) {
//					 price = BH.sub(BH.getBD(orderDetail.getItemPrice()), BH
//							 .mul(BH.getBD(price),
//									 BH.getBD(itemPromotion.getDiscountPercentage()),
//									 false), false);
//				 }else {
//					 price = BH.sub(BH.getBD(orderDetail.getItemPrice()), BH
//							 .mul(BH.getBD(orderDetail.getItemPrice()),
//									 BH.getBD(itemPromotion.getDiscountPercentage()),
//									 false), false);
//				 }
//				promotionPrice=BH.add(promotionPrice,BH.mul(BH.getBD(orderDetail.getItemPrice()),
//                        BH.getBD(itemPromotion.getDiscountPercentage()),
//                        false),false) ;
//			}
//			if (itemPromotion.getFreeNum().intValue() > 0) {
//				//promotionPrice = BH.getBD(orderDetail.getItemPrice());
//			}
//		} else {
//			price = BH.getBD(orderDetail.getItemPrice());
//		}

//		promotionPrice = BH.mul(promotionPrice, BH.getBD(orderDetail.getItemNum()), false);
//		if(promotionPrice.compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) > 0){
//			long nowTime = System.currentTimeMillis();
//
//			OrderPromotion promotionData=PromotionDataSQL.getPromotionData(order.getId(),orderDetail.getId());
//			if(promotionData==null){
//				List<Promotion> promotionList= PromotionSQL.getAllPromotion();
//				Promotion promotion= PromotionSQL.getPromotion(itemPromotion.getPromotionId());
//                promotionData=new OrderPromotion();
//				promotionData.setPromotionType(0);
//				promotionData.setPromotionAmount(promotionPrice.toString());
//				promotionData.setItemId(orderDetail.getItemId());
//				promotionData.setItemName(orderDetail.getItemName());
//				promotionData.setFreeItemId(itemPromotion.getFreeItemId());
//				promotionData.setFreeNum(itemPromotion.getFreeNum());
//				promotionData.setFreeItemName(itemPromotion.getFreeItemName());
//				promotionData.setOrderDetailId(orderDetail.getId());
//				promotionData.setOrderId(orderDetail.getOrderId());
//				promotionData.setItemNum(orderDetail.getItemNum());
//				promotionData.setCreateTime(nowTime);
//				promotionData.setPromotionId(itemPromotion.getPromotionId());
//				promotionData.setPromotionName(promotion.getPromotionName());
//				promotionData.setBusinessDate( order.getBusinessDate());
//				PromotionDataSQL.addPromotionData(promotionData);
//			}else {
//				promotionData.setPromotionAmount(promotionPrice.toString());
//				promotionData.setItemNum(orderDetail.getItemNum());
//				promotionData.setUpdateTime(nowTime);
//				PromotionDataSQL.updatePromotionData(promotionData);
//			}
	//	}

		price = BH.mul(price, BH.getBD(orderDetail.getItemNum()), false);
		if(BH.getBDNoFormat(orderDetail.getWeight()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) > 0){
			price = BH.mul(price, BH.getBDNoFormat(orderDetail.getWeight()), false);
		}
		price = BH.add(price, BH.getBD(orderDetail.getModifierPrice()), true);
		return price;
	}

	/**
	 * 得到所有配料的价格
	 *
	 * @param order
	 * @param orderDetail
	 * @return
	 */
	public static BigDecimal getOrderDetailModifierPrice(final Order order,
			OrderDetail orderDetail) {
		ArrayList<OrderModifier> orderModifiers = OrderModifierSQL
				.getOrderModifiers(order, orderDetail);
		BigDecimal modifierPrice = BH.getBD(ParamConst.DOUBLE_ZERO);
		for (OrderModifier orderModifier : orderModifiers) {
			if (orderModifier.getStatus().intValue() == ParamConst.ORDER_MODIFIER_STATUS_NORMAL) {
				modifierPrice = BH.add(modifierPrice,
						BH.getBD(orderModifier.getModifierPrice()), true);
			}
		}
		return modifierPrice;
	}

	/**
	 * 得到当前orderDetail的折扣信息
	 *
	 * @param orderDetail
	 * @return
	 */
	public static BigDecimal getOrderDetailDiscountPrice(OrderDetail orderDetail) {
		BigDecimal discountPrice = BH.getBD(ParamConst.DOUBLE_ZERO);
		if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
				|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_SUB
				|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_RATE
				|| orderDetail.getDiscountType().intValue() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_RATE
				|| orderDetail.getDiscountType().intValue() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_SUB) {
			discountPrice = BH.mul(BH.getBD(orderDetail.getRealPrice()),
					BH.getBDNoFormat(orderDetail.getDiscountRate()), true);
			return discountPrice;
		}
		if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB) {
			discountPrice = BH.getBD(orderDetail.getDiscountPrice());
			return discountPrice;
		}
		return discountPrice;
	}
	// 当修改Order的discount的时候才能调用
//	public static void setOrderDetailDiscount(Order order,
//			OrderDetail orderDetail) {
//		// 0不打折、10主订单按照比率打折、11主订单直接减、12子订单打折
//		
//			if (order.getDiscountType() == ParamConst.ORDER_DISCOUNT_TYPE_RATE_BY_ORDER) {
//				orderDetail.setDiscountRate(order.getDiscountRate());
//				orderDetail
//						.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_RATE);
//			} else if(order.getDiscountType().intValue() == ParamConst.ORDER_DISCOUNT_TYPE_SUB_BY_ORDER){
//				String discount_rate = BH.div(BH.getBD(order.getDiscountAmount()),
//						BH.getBD(order.getSubTotal()), true).toString();
//				orderDetail.setDiscountRate(discount_rate);
//				orderDetail
//						.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_SUB);
//			} else {
//				orderDetail.setDiscountRate(ParamConst.DOUBLE_ZERO);
//				orderDetail
//						.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
//			}
//	}

	public static BigDecimal getOrderDiscount(Order order,
			List<OrderDetail> orderDetails) {
		BigDecimal discount = BH.getBD(ParamConst.DOUBLE_ZERO);
		for (OrderDetail orderDetail : orderDetails) {
			if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB) {
				discount = BH.add(discount,
						BH.getBD(orderDetail.getDiscountPrice()), true);
			} else if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
					|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_RATE
					|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_SUB) {
				discount = BH
						.add(discount,
								BH.mul(BH.getBD(orderDetail.getRealPrice()),
										BH.getBDNoFormat(orderDetail.getDiscountRate()),
										false), true);
			}
		}
		return discount;
	}

	public static void setOrderDiscount(Order order, List<OrderDetail> orderDetails)
	{
		BigDecimal discount = BH.getBD(ParamConst.DOUBLE_ZERO);
		if (order.getDiscountType() == ParamConst.ORDER_DISCOUNT_TYPE_SUB_BY_ORDER
				|| order.getDiscountType() == ParamConst.ORDER_DISCOUNT_TYPE_SUB_BY_CATEGORY)
		{
			for (OrderDetail orderDetail : orderDetails)
			{
				Boolean completedOrder = false;
				for(OrderSplit finishedOrder : OrderSplitSQL.getFinishedOrderSplits(order.getId()))
				{
					if(orderDetail.getOrderSplitId().equals(finishedOrder.getId()))
					{
						if(finishedOrder.getOrderStatus() == ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED)
						{
							completedOrder = true;
						}
					}
				}
				if(!completedOrder)
				{
					if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
							|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_RATE
							|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_RATE)
					{
						discount = BH.add(discount, BH.mul(
								BH.getBD(orderDetail.getRealPrice()),
								BH.getBDNoFormat(orderDetail.getDiscountRate()), false),
								true);
					}
					else if(orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB
							|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_SUB
							|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_SUB)
					{
						discount = BH.add(discount, BH.getBD(orderDetail.getDiscountPrice()), true);
					}
				}
			}
		}
		else
		{
			for (OrderDetail orderDetail : orderDetails)
			{
				Boolean completedOrder = false;
				for(OrderSplit finishedOrder : OrderSplitSQL.getFinishedOrderSplits(order.getId()))
				{
					if(orderDetail.getOrderSplitId().equals(finishedOrder.getId()))
					{
						if(finishedOrder.getOrderStatus() == ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED)
						{
							completedOrder = true;
                            discount = BH.sub(discount, BH.getBD(finishedOrder.getDiscountAmount()), false);
						}
					}
				}
				if(!completedOrder)
				{
					if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB)
					{
						discount = BH.add(discount, BH.getBD(orderDetail.getDiscountPrice()), true);
					}
					else if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
							|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_RATE
//									|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_SUB
							|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_RATE
					)
					{
						discount = BH.add(discount, BH.mul(
								BH.getBD(orderDetail.getRealPrice()),
								BH.getBDNoFormat(orderDetail.getDiscountRate()), false),
								true);
					}
				}
			}
		}

		order.setOrderRound(BH.formatRound(BH.getBD(discount)).toString());
		order.setDiscountAmount(BH.getBD(discount).toString());
	}
	// 只有修改orderDiscount的时候才能调用这个方法
	public static void setOrderDiscountByOrder(Order order,
			List<OrderDetail> orderDetails) {
//		if (order.getDiscountType().intValue() == ParamConst.ORDER_DISCOUNT_TYPE_RATE_BY_ORDER) {
//			order.setDiscountAmount(BH.mul(BH.getBD(order.getSubTotal()),
//					BH.getBD(order.getDiscountRate()), true).toString());
//		} else if (order.getDiscountType().intValue() == ParamConst.ORDER_DISCOUNT_TYPE_SUB_BY_ORDER) {
////			order.setDiscountRate(BH.div(BH.getBD(order.getDiscountPrice()),
////					BH.getBD(order.getSubTotal()), true).toString());
//			order.setDiscountAmount(order.getDiscountPrice());
//		} else if (order.getDiscountType().intValue() == ParamConst.ORDER_DISCOUNT_TYPE_NULL) {
//			order.setDiscountRate(ParamConst.DOUBLE_ZERO);
//			order.setDiscountPrice(ParamConst.INT_ZERO);
//			order.setDiscountAmount(ParamConst.INT_ZERO);
//		} else {
			BigDecimal discount = BH.getBD(ParamConst.DOUBLE_ZERO);
			for (OrderDetail orderDetail : orderDetails) {
				if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB) {
					discount = BH.add(discount,
							BH.getBD(orderDetail.getDiscountPrice()), true);
				} else if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
								|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_RATE
								|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_SUB) {
					discount = BH.add(discount, BH.mul(
							BH.getBD(orderDetail.getRealPrice()),
							BH.getBDNoFormat(orderDetail.getDiscountRate()), false),
							true);
				}
			}
			order.setDiscountAmount(BH.getBD(discount).toString());
//			if (Double.parseDouble(order.getSubTotal()) > 0) {
//				order.setDiscountRate(BH.div(discount,
//						BH.getBD(order.getSubTotal()), true).toString());
//			} else {
//				order.setDiscountRate(ParamConst.INT_ZERO);
//			}
	}

	public static void setOrderSubTotal(Order order,
			List<OrderDetail> orderDetails) {

        BigDecimal subTotal = BH.getBD(ParamConst.DOUBLE_ZERO);
        if (orderDetails.size() > 0) {
            for (OrderDetail orderDetail : orderDetails) {
                subTotal = BH.add(subTotal,
                        BH.getBD(orderDetail.getRealPrice()), false);
            }
        }
      //  PromotionDataSQL.deleteAllPromotionData();
        BigDecimal promotionPrice = BH.getBD(ParamConst.DOUBLE_ZERO);
		BigDecimal maxPrice = BH.getBD(ParamConst.DOUBLE_ZERO);

//        List<PromotionOrder> promotionOrders = getOrderPromotion();
//        if (promotionOrders != null) {
//
//			for (int i = 0; i < promotionOrders.size(); i++) {
//				PromotionOrder promotionOrder = promotionOrders.get(i);
//				if(!hasWeek(promotionOrder.getPromotionId())){
//                         break;
//				}
////				if(maxPrice.compareTo(BH.getBD(promotionOrder.getBasePrice()))>0)
////                {
////                    maxPrice=BH.getBD(promotionOrder.getBasePrice());
////                }
//				if (subTotal.compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0&&subTotal.compareTo(BH.getBD(promotionOrder.getBasePrice())) >= 0) {
//					if (BH.getBD(promotionOrder.getDiscountPrice()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) > 0) {
//
//						subTotal = BH.sub(BH.getBD(subTotal),
//								BH.getBDNoFormat(promotionOrder.getDiscountPrice()), false);
//
//						if (subTotal.compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) < 0) {
//							subTotal = BH.getBD(ParamConst.DOUBLE_ZERO);
//						}
//						promotionPrice = BH.add(promotionPrice, BH.getBDNoFormat(promotionOrder.getDiscountPrice()), false);
//					}
//					else if (Double.parseDouble(promotionOrder.getDiscountPercentage()) > 0) {
//
//
//						if (subTotal.compareTo(BH.getBD("0")) > 0) {
//							subTotal = BH.sub(BH.getBD(subTotal), BH
//									.mul(BH.getBD(subTotal),
//											BH.getBDNoFormat(promotionOrder.getDiscountPercentage()),
//											false), false);
//						}
//						promotionPrice = BH.add(promotionPrice, BH.mul(BH.getBD(subTotal),
//								BH.getBDNoFormat(promotionOrder.getDiscountPercentage()),
//								false), false);
//					}
//
//					order.setSubTotal(BH.getBD(subTotal).toString());
//					if (subTotal.compareTo(BH.getBD(promotionOrder.getBasePrice())) >= 0) {
//						//  orderPromotion 获取basePrice进行优惠（满足多个添加多个免费菜）
//
//						if (promotionOrder != null && promotionOrder.getFreeNum().intValue() > 0) {
//							ItemDetail itemDetail = CoreData.getInstance()
//									.getItemDetailByTemplateId(promotionOrder.getFreeItemId());
//							if (itemDetail == null) {
//								return;
//
//							}
//							OrderDetail freeOrderDetail = ObjectFactory.getInstance()
//									.getOrderFreeOrderDetail(order, itemDetail,
//											promotionOrder);
//							OrderDetailSQL.updateOrderDetail(freeOrderDetail);
//						}
//					}
//
//					if(promotionOrder!=null) {
//						long nowTime = System.currentTimeMillis();
//
//						OrderPromotion promotionData = PromotionDataSQL.getPromotionDataOrType(order.getId(), 1);
//
//						if (promotionData == null) {
//							List<Promotion> promotionList = PromotionSQL.getAllPromotion();
//							Promotion promotion = PromotionSQL.getPromotion(promotionOrder.getPromotionId());
//							promotionData = new OrderPromotion();
//							promotionData.setPromotionType(1);
//							promotionData.setPromotionAmount(promotionPrice.toString());
//							promotionData.setOrderId(order.getId());
//							promotionData.setCreateTime(nowTime);
//							promotionData.setPromotionId(promotionOrder.getPromotionId());
//							promotionData.setPromotionName(promotion.getPromotionName());
//							promotionData.setBusinessDate(order.getBusinessDate());
//							promotionData.setFreeItemName(promotionOrder.getFreeItemName());
//							promotionOrder.setFreeItemId(promotionOrder.getFreeItemId());
//							promotionData.setFreeNum(promotionOrder.getFreeNum());
//							PromotionDataSQL.addPromotionData(promotionData);
//						} else {
//							promotionData.setPromotionAmount(promotionPrice.toString());
//							promotionData.setUpdateTime(nowTime);
//							PromotionDataSQL.updatePromotionData(promotionData);
//						}
//					}
//				}

//			}
  //  orderPromotion 获取最大的basePrice进行优惠（满足多个选最大的）
//			if(maxPrice.compareTo(BH.getBD(ParamConst.DOUBLE_ZERO))>0) {
//				PromotionOrder promotionOrder = PromotionOrderSQL.getPromotionOrderOrBasePrice(maxPrice.toString());
//
//			}

	//	}

		order.setSubTotal(BH.getBD(subTotal).toString());
		}

	// Buy X Item, get Y Price
	// If you buy X Quantity you will get Y price.
	// This part has no issues, but can be refined further for shorter code, to be done when we have the time.
	private static BigDecimal  promotionAndItemPrice(Order order,List<OrderDetail> orderDetails,Promotion promotion,BigDecimal total)
	{
		int itemNum = 0;
		int totalNumOfItem = 0;

		List<OrderDetail> beforeDiscOrderDetails = new ArrayList<>();
		List<OrderDetail> discOrderDetails = new ArrayList<>();

		for(OrderDetail orderDetail : orderDetails)
		{
			ItemDetail itemDetail= CoreData.getInstance().getItemDetailById(orderDetail.getItemId(), null);
			if (promotion.getItemId()>0)
			{
				if (promotion.getItemId().intValue() == itemDetail.getItemTemplateId().intValue() && orderDetail.getIsFree() != ParamConst.FREE)
				{
					totalNumOfItem = totalNumOfItem + orderDetail.getItemNum();
					if(orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL)
					{
						itemNum = itemNum + orderDetail.getItemNum();
						beforeDiscOrderDetails.add(orderDetail);
					}
					else
					{
						discOrderDetails.add(orderDetail);
					}
				}
			}
			if (promotion.getItemId()<=0&&promotion.getItemCategoryId()>0)
			{
				if (promotion.getItemCategoryId().intValue() ==itemDetail.getItemCategoryId()&&orderDetail.getIsFree()!=ParamConst.FREE)
				{
					if(orderDetail.getDiscountPrice() == null)
					{
						CoreData.getInstance().getItemDetails(promotion.getItemCategoryId());
						itemNum = itemNum + orderDetail.getItemNum();
						totalNumOfItem = totalNumOfItem + orderDetail.getItemNum();
						beforeDiscOrderDetails.add(orderDetail);
					}
				}
			}
			// 最后按照主分类找
			if (promotion.getItemId()<=0&& promotion.getItemCategoryId()<=0)
			{
				if (promotion.getItemMainCategoryId().intValue() == itemDetail.getItemMainCategoryId().intValue()&&orderDetail.getIsFree()!=ParamConst.FREE)
				{
					if(orderDetail.getDiscountPrice() == null)
					{
						itemNum = itemNum + orderDetail.getItemNum();
						totalNumOfItem = totalNumOfItem + orderDetail.getItemNum();
						beforeDiscOrderDetails.add(orderDetail);
					}
				}
			}
		}

		Boolean alteredPrice = false;

		if(discOrderDetails.size() > promotion.getItemNum())
		{
			OrderDetail firstOrder = discOrderDetails.get(0);
			BigDecimal DiscountPerItem = BH.div(BH.getBD(promotion.getDiscountPrice()), new BigDecimal(promotion.getItemNum()), false);
			BigDecimal ItemPrice = BH.mul(BH.getBD(firstOrder.getItemNum()), BH.div(BH.getBD(promotion.getDiscountPrice()), new BigDecimal(promotion.getItemNum()), false), false);
			BigDecimal RealPrice = BH.mul(BH.getBD(firstOrder.getItemNum()), new BigDecimal(firstOrder.getItemPrice()), false);
			BigDecimal DiscountPrice = BH.sub(RealPrice, ItemPrice, false);
			int tempItemCount = totalNumOfItem;
			int remainder = 0;
			for(int j = promotion.getItemNum(); j < tempItemCount; j = promotion.getItemNum())
			{
				tempItemCount = tempItemCount - promotion.getItemNum();
				remainder = tempItemCount;
				if(remainder == promotion.getItemNum())
				{
					remainder = 0;
				}
			}
			while(remainder > 0)
			{
				DiscountPrice = BH.sub(DiscountPrice, new BigDecimal(firstOrder.getItemPrice()), false);
				DiscountPrice = BH.add(DiscountPrice, DiscountPerItem, false);
				remainder--;
			}

			if(DiscountPrice.compareTo(new BigDecimal(firstOrder.getDiscountPrice())) != 0)
			{
				alteredPrice = true;
			}
		}

		if (itemNum >= promotion.getItemNum() || alteredPrice)
		{
			OrderDetail orderToAppend;
			if(beforeDiscOrderDetails.size() != 0)
			{
				orderToAppend = beforeDiscOrderDetails.get(0);
			}
			else
			{
				orderToAppend = discOrderDetails.get(0);
			}

			for(OrderDetail tempOrderDetail : beforeDiscOrderDetails)
			{
				OrderDetailSQL.deleteOrderDetail(tempOrderDetail);
			}

			if(discOrderDetails.size() != 0)
			{
				orderToAppend = discOrderDetails.get(0);
				orderToAppend.setItemNum(orderToAppend.getItemNum() + itemNum);
			}
			else
			{
				orderToAppend.setItemNum(beforeDiscOrderDetails.get(0).getItemNum());
				orderToAppend.setItemNum(itemNum);
			}
			BigDecimal DiscountPerItem = BH.div(BH.getBD(promotion.getDiscountPrice()), new BigDecimal(promotion.getItemNum()), false);
			BigDecimal ItemPrice = BH.mul(BH.getBD(orderToAppend.getItemNum()), DiscountPerItem, false);
			BigDecimal RealPrice = BH.mul(BH.getBD(orderToAppend.getItemNum()), new BigDecimal(orderToAppend.getItemPrice()), false);
			BigDecimal DiscountPrice = BH.sub(RealPrice, ItemPrice, false);

			int tempItemCount = totalNumOfItem;
			int remainder = 0;
			for(int j = promotion.getItemNum(); j < tempItemCount; j = promotion.getItemNum())
			{
				tempItemCount = tempItemCount - promotion.getItemNum();
				remainder = tempItemCount;
				if(remainder == promotion.getItemNum())
				{
					remainder = 0;
				}
			}
			while(remainder > 0)
			{
				DiscountPrice = BH.sub(DiscountPrice, new BigDecimal(orderToAppend.getItemPrice()), false);
				DiscountPrice = BH.add(DiscountPrice, DiscountPerItem, false);
				remainder--;
			}

			orderToAppend.setDiscountPrice(String.valueOf(DiscountPrice));
			orderToAppend.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB);
			OrderDetailSQL.updateOrderDetailAndOrderByDiscount(orderToAppend);
			deletePromotion(order, promotion);
			addPromotion(order, promotion, null, promotion.getDiscountPrice());
			OrderSQL.updateOrder(order);
            total = new BigDecimal(order.getTotal());
		}
		if(totalNumOfItem < promotion.getItemNum() || totalNumOfItem == 0)
		{
			if(discOrderDetails.size() != 0)
			{
				discOrderDetails.get(0).setDiscountPrice(ParamConst.INT_ZERO);
				discOrderDetails.get(0).setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
				OrderDetailSQL.updateOrderDetailAndOrderByDiscount(discOrderDetails.get(0));
				OrderSQL.updateOrder(order);
                total = new BigDecimal(order.getTotal());
				deletePromotion(order, promotion);
			}
		}

		return total;
	}

	// Buy X Total Price, get Y%
	// If you purchase the item Xs exceeding Base Price, Discount will be applied onto Grand Total.
	// This part has no issues.
	private static BigDecimal promotionAndTotalDiscount(Order order, List<OrderDetail> orderDetails, Promotion promotion, BigDecimal total)
	{
		int itemNum = 0;
		int totalNumOfItem = 0;
		BigDecimal totalPrice = BH.getBD(ParamConst.DOUBLE_ZERO);
		BigDecimal beforePromotionTotal = BH.getBD(ParamConst.DOUBLE_ZERO);
		BigDecimal afterPromotionTotal = BH.getBD(ParamConst.DOUBLE_ZERO);

		List<OrderDetail> beforeDiscOrderDetails = new ArrayList<>();
		List<OrderDetail> discOrderDetails = new ArrayList<>();

		for(OrderDetail orderDetail : orderDetails)
		{
			ItemDetail itemDetail= CoreData.getInstance().getItemDetailById(orderDetail.getItemId(), null);
			if (promotion.getItemId()>0)
			{
				if (promotion.getItemId().intValue() == itemDetail.getItemTemplateId().intValue() && orderDetail.getIsFree() != ParamConst.FREE)
				{
					totalNumOfItem = totalNumOfItem + orderDetail.getItemNum();
					totalPrice=BH.add(totalPrice,BH.getBD(orderDetail.getRealPrice()),false);
					if(orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL)
					{
						beforePromotionTotal = BH.add(beforePromotionTotal,BH.getBD(orderDetail.getRealPrice()),false);
						itemNum = itemNum + orderDetail.getItemNum();
						beforeDiscOrderDetails.add(orderDetail);
					}
					else
					{
						discOrderDetails.add(orderDetail);
						afterPromotionTotal = BH.add(beforePromotionTotal,BH.getBD(orderDetail.getRealPrice()),false);
					}
				}
			}
			if (promotion.getItemId()<=0&&promotion.getItemCategoryId()>0)
			{
				if (promotion.getItemCategoryId().intValue() ==itemDetail.getItemCategoryId()&&orderDetail.getIsFree()!=ParamConst.FREE)
				{
					if(orderDetail.getDiscountPrice() == null)
					{
						totalNumOfItem = totalNumOfItem + orderDetail.getItemNum();
						totalPrice=BH.add(totalPrice,BH.getBD(orderDetail.getRealPrice()),false);
						CoreData.getInstance().getItemDetails(promotion.getItemCategoryId());
						itemNum = itemNum + orderDetail.getItemNum();
						beforeDiscOrderDetails.add(orderDetail);
					}
				}
			}
			// 最后按照主分类找
			if (promotion.getItemId()<=0&& promotion.getItemCategoryId()<=0)
			{
				if (promotion.getItemMainCategoryId().intValue() == itemDetail.getItemMainCategoryId().intValue()&&orderDetail.getIsFree()!=ParamConst.FREE)
				{
					if(orderDetail.getDiscountPrice() == null)
					{
						totalNumOfItem = totalNumOfItem + orderDetail.getItemNum();
						totalPrice=BH.add(totalPrice,BH.getBD(orderDetail.getRealPrice()),false);
						itemNum = itemNum + orderDetail.getItemNum();
						beforeDiscOrderDetails.add(orderDetail);
					}
				}
			}
		}

		Boolean alteredPrice = false;

		if(afterPromotionTotal.compareTo(new BigDecimal(promotion.getBasePrice())) >= 0)
		{
			OrderDetail firstOrder = discOrderDetails.get(0);
			BigDecimal discountedPrice = BH.mul(new BigDecimal(firstOrder.getItemPrice()), BH.sub(new BigDecimal("1"), BH.divThirdFormat(BH.getBD(promotion.getDiscountPercentage()), BH.getBD(100), false), false), false);
			int promoCount = 0;
			for(BigDecimal x = new BigDecimal(promotion.getBasePrice()); x.compareTo(new BigDecimal(firstOrder.getItemPrice())) >= 0; x = BH.sub(x, new BigDecimal(firstOrder.getItemPrice()), false))
			{
				promoCount++;
			}
			BigDecimal DiscountPerItem = BH.div(BH.getBD(discountedPrice), new BigDecimal(promoCount), false);
			BigDecimal ItemPrice = BH.mul(BH.getBD(firstOrder.getItemNum()), DiscountPerItem, false);
			BigDecimal RealPrice = BH.mul(BH.getBD(firstOrder.getItemNum()), new BigDecimal(firstOrder.getItemPrice()), false);
			BigDecimal DiscountPrice = BH.sub(RealPrice, ItemPrice, false);
			int tempItemCount = totalNumOfItem;
			int remainder = 0;
			for(int j = promoCount; j < tempItemCount; j = promoCount)
			{
				tempItemCount = tempItemCount - promoCount;
				remainder = tempItemCount;
				if(remainder == promoCount)
				{
					remainder = 0;
				}
			}
			while(remainder > 0)
			{
				DiscountPrice = BH.sub(DiscountPrice, new BigDecimal(firstOrder.getItemPrice()), false);
				DiscountPrice = BH.add(DiscountPrice, DiscountPerItem, false);
				remainder--;
			}

			if(DiscountPrice.compareTo(new BigDecimal(firstOrder.getDiscountPrice())) != 0)
			{
				alteredPrice = true;
			}
		}

		if(beforePromotionTotal.compareTo(new BigDecimal(promotion.getBasePrice())) >= 0 || alteredPrice)
		{
			OrderDetail orderToAppend;
			if(beforeDiscOrderDetails.size() != 0)
			{
				orderToAppend = beforeDiscOrderDetails.get(0);
			}
			else
			{
				orderToAppend = discOrderDetails.get(0);
			}

			for(OrderDetail tempOrderDetail : beforeDiscOrderDetails)
			{
				OrderDetailSQL.deleteOrderDetail(tempOrderDetail);
			}

			if(discOrderDetails.size() != 0)
			{
				orderToAppend = discOrderDetails.get(0);
				orderToAppend.setItemNum(orderToAppend.getItemNum() + itemNum);
			}
			else
			{
				orderToAppend.setItemNum(beforeDiscOrderDetails.get(0).getItemNum());
				orderToAppend.setItemNum(itemNum);
			}

			BigDecimal discountedPrice = BH.mul(new BigDecimal(orderToAppend.getItemPrice()), BH.sub(new BigDecimal("1"), BH.divThirdFormat(BH.getBD(promotion.getDiscountPercentage()), BH.getBD(100), false), false), false);
			int promoCount = 0;
			for(BigDecimal x = new BigDecimal(promotion.getBasePrice()); x.compareTo(new BigDecimal(orderToAppend.getItemPrice())) >= 0; x = BH.sub(x, new BigDecimal(orderToAppend.getItemPrice()), false))
			{
				promoCount++;
			}
			BigDecimal DiscountPerItem = BH.div(BH.getBD(discountedPrice), new BigDecimal(promoCount), false);
			BigDecimal ItemPrice = BH.mul(BH.getBD(orderToAppend.getItemNum()), DiscountPerItem, false);
			BigDecimal RealPrice = BH.mul(BH.getBD(orderToAppend.getItemNum()), new BigDecimal(orderToAppend.getItemPrice()), false);
			BigDecimal DiscountPrice = BH.sub(RealPrice, ItemPrice, false);
			int tempItemCount = totalNumOfItem;
			int remainder = 0;
			for(int j = promoCount; j < tempItemCount; j = promoCount)
			{
				tempItemCount = tempItemCount - promoCount;
				remainder = tempItemCount;
				if(remainder == promoCount)
				{
					remainder = 0;
				}
			}
			while(remainder > 0)
			{
				DiscountPrice = BH.sub(DiscountPrice, new BigDecimal(orderToAppend.getItemPrice()), false);
				DiscountPrice = BH.add(DiscountPrice, DiscountPerItem, false);
				remainder--;
			}

			orderToAppend.setDiscountPrice(String.valueOf(DiscountPrice));
			orderToAppend.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB);
			OrderDetailSQL.updateOrderDetailAndOrderByDiscount(orderToAppend);
			deletePromotion(order, promotion);
			addPromotion(order, promotion, null, promotion.getDiscountPrice());
			OrderSQL.updateOrder(order);
            total = new BigDecimal(order.getTotal());
        }

		if(totalPrice.compareTo(new BigDecimal(promotion.getBasePrice())) < 0)
		{
			if(discOrderDetails.size() != 0)
			{
				discOrderDetails.get(0).setDiscountPrice(ParamConst.INT_ZERO);
				discOrderDetails.get(0).setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
				OrderDetailSQL.updateOrderDetailAndOrderByDiscount(discOrderDetails.get(0));
				OrderSQL.updateOrder(order);
				deletePromotion(order, promotion);
                total = new BigDecimal(order.getTotal());
			}
		}
		return total;
	}

	// Buy X Total Price, get Y Item Free
	// If you purchase item Xs exceeding Base Price, you will get FreeItem.
	// This part has no issues.
	private static BigDecimal promotionAndTotalFree(Order order, List<OrderDetail> orderDetails, Promotion promotion, BigDecimal total)
	{
		BigDecimal promotionTotal = BH.getBD(ParamConst.DOUBLE_ZERO);
		BigDecimal promotionPrice = BH.getBD(ParamConst.DOUBLE_ZERO);

		for(OrderDetail orderDetail : orderDetails)
		{
			ItemDetail itemDetail= CoreData.getInstance()
					.getItemDetailById(orderDetail.getItemId().intValue(), orderDetail.getItemName());

			if (promotion.getItemId()>0) {
				if (promotion.getItemId().intValue() ==itemDetail.getItemTemplateId().intValue()&&orderDetail.getIsFree()!=ParamConst.FREE) {
					promotionTotal=BH.add(promotionTotal,BH.getBD(orderDetail.getRealPrice()),false);
				}
			}

			if (promotion.getItemId()<=0&&promotion.getItemCategoryId()>0) {
				if (promotion.getItemCategoryId().intValue() ==itemDetail.getItemCategoryId()&&orderDetail.getIsFree()!=ParamConst.FREE) {
					CoreData.getInstance().getItemDetails(promotion.getItemCategoryId());
					promotionTotal=BH.add(promotionTotal,BH.getBD(orderDetail.getRealPrice()),false);
				}
			}
			// 最后按照主分类找
			if (promotion.getItemId()<=0&& promotion.getItemCategoryId()<=0) {
				if (promotion.getItemMainCategoryId().intValue() == itemDetail
						.getItemMainCategoryId().intValue()&&orderDetail.getIsFree()!=ParamConst.FREE) {
					promotionTotal=BH.add(promotionTotal,BH.getBD(orderDetail.getRealPrice()),false);
				}
			}

			if(promotion.getFreeItemId().intValue() == itemDetail.getItemTemplateId().intValue() && orderDetail.getIsFree()==ParamConst.FREE)
			{
				deletePromotion(order, promotion);
			}
		}

		int promoCount = 0;
		for(BigDecimal tempValue = promotionTotal; tempValue.compareTo(new BigDecimal(promotion.getBasePrice())) >= 0; tempValue = tempValue.subtract(BH.getBD(promotion.getBasePrice())))
		{
			deletePromotion(order, promotion);
			promoCount++;
		}

		if(promotion.getItemMainCategoryId()>0) {
			if (promotionTotal.compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) != 0) {
				for(int x = 0; x < promoCount; x++)
				{
					ItemDetail itemDetailFree = CoreData.getInstance().getItemDetailByTemplateId(promotion.getFreeItemId());
					if (promotion.getFreeNum() > 0)
					{
						if (itemDetailFree == null)
						{
							return total;
						}
						OrderDetail proOrderDetail = ObjectFactory.getInstance().getOrderDetailAndPromotion(order, itemDetailFree, 0, promotion);
						proOrderDetail.setItemNum(promotion.getFreeNum());
						proOrderDetail.setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_ADDED);
						OrderDetailSQL.updateOrderDetail(proOrderDetail);
						total = BH.sub(BH.getBD(total), promotionPrice, false);
						addPromotion(order, promotion, proOrderDetail, promotionPrice.toString());
					}
				}
			}
			else  if (promoCount == 0 || promotionTotal.compareTo(BigDecimal.valueOf(Long.parseLong(promotion.getBasePrice()))) == 1)
			{
				deletePromotion(order, promotion);
			}
		}
		return total;
	}

	// Buy X Item and get Y Item Free
	// If you buy X items, you will get FreeNum number of FreeItem
	// This part has no issues.
    private static BigDecimal promotionAndItemFree(Order order, List<OrderDetail> orderDetails, Promotion promotion, BigDecimal total)
	{
		int itemNum = 0;

		BigDecimal promotionPrice=BH.getBD(ParamConst.DOUBLE_ZERO);
		for(OrderDetail orderDetail : orderDetails)
		{
			ItemDetail itemDetail= CoreData.getInstance()
					.getItemDetailById(orderDetail.getItemId(), orderDetail.getItemName());

			if (promotion.getItemId()>0) {
				if (promotion.getItemId().intValue() == itemDetail.getItemTemplateId().intValue() && orderDetail.getIsFree()!=ParamConst.FREE)
				{
					CoreData.getInstance().getItemDetails(promotion.getItemCategoryId());
					itemNum = itemNum + orderDetail.getItemNum();
				}
			}

			if (promotion.getItemId()<=0&&promotion.getItemCategoryId()>0) {
				if (promotion.getItemCategoryId().intValue() ==itemDetail.getItemCategoryId()&&orderDetail.getIsFree()!=ParamConst.FREE) {
					CoreData.getInstance().getItemDetails(promotion.getItemCategoryId());
					itemNum = itemNum + orderDetail.getItemNum();
				}
			}

			// 最后按照主分类找
			if (promotion.getItemId()<=0&& promotion.getItemCategoryId()<=0) {
				if (promotion.getItemMainCategoryId().intValue() == itemDetail
						.getItemMainCategoryId().intValue()&&orderDetail.getIsFree()!=ParamConst.FREE) {
					itemNum = itemNum + orderDetail.getItemNum();
				}
			}

			if(promotion.getFreeItemId().intValue() == itemDetail.getItemTemplateId().intValue() && orderDetail.getIsFree()==ParamConst.FREE)
			{
				deletePromotion(order, promotion);
			}
       }

		int promoCount = 0;
		for(int tempValue = itemNum; tempValue >= promotion.getItemNum(); tempValue = tempValue - promotion.getItemNum())
		{
			deletePromotion(order, promotion);
			promoCount++;
		}

		if (itemNum >= promotion.getItemNum())
		{
			for(int x = 0; x < promoCount; x++)
			{
				if (promotion.getFreeNum() > 0)
				{
					ItemDetail itemDetails = CoreData.getInstance().getItemDetailByTemplateId(promotion.getFreeItemId());
					if (itemDetails == null) {
						return total;
					}
					OrderDetail proOrderDetail = ObjectFactory.getInstance().getOrderDetailAndPromotion(order, itemDetails,0,promotion);
					proOrderDetail.setItemNum(promotion.getFreeNum());
					proOrderDetail.setOrderDetailStatus(ParamConst.ORDERDETAIL_STATUS_ADDED);
					OrderDetailSQL.updateOrderDetail(proOrderDetail);
					total = BH.sub(BH.getBD(total),promotionPrice, false);
					addPromotion(order, promotion, proOrderDetail,promotionPrice.toString());
				}
			}
		}

		if (itemNum < promotion.getItemNum() || promoCount == 0 )
		{
			for(int x = 0; x < promoCount; promoCount--)
			{
				deletePromotion(order, promotion);
			}
		}

        return total;
    }

	// Buy X Item and get Cheapest Item Free
	// If you buy X items, cheapest food will be free
	// This part has no issues.
	private static BigDecimal promotionAndFree(Order order, List<OrderDetail> orderDetails, Promotion promotion, BigDecimal total)
	{
        BigDecimal promotionPrice=BH.getBD(ParamConst.DOUBLE_ZERO);
        int itemNum=0;
        OrderDetail orderDetailMin = null;
		OrderDetail prevOrderDetailMin = null;
        for(OrderDetail orderDetail : orderDetails)
        {
        	ItemDetail itemDetail= CoreData.getInstance().getItemDetailById(orderDetail.getItemId(), orderDetail.getItemName());

            if (promotion.getItemId()<=0&&promotion.getItemCategoryId()>0) {
                if (promotion.getItemCategoryId().intValue() ==itemDetail.getItemCategoryId()&&orderDetail.getIsFree()!=ParamConst.FREE) {
                    CoreData.getInstance().getItemDetails(promotion.getItemCategoryId());
                    itemNum = itemNum + orderDetail.getItemNum();
                    if((promotionPrice.compareTo(BH.getBD(ParamConst.DOUBLE_ZERO))==0 || promotionPrice.compareTo(BH.getBD(orderDetail.getItemPrice()))>0) && orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL)
                    {
                    	if(orderDetailMin == null || orderDetail.getItemPrice().compareTo(orderDetailMin.getItemPrice()) < 0)
						{
							orderDetailMin=orderDetail;
						}
                    }
                }
            }
            // 最后按照主分类找
            if (promotion.getItemId()<=0&& promotion.getItemCategoryId()<=0)
            {
                if (promotion.getItemMainCategoryId().intValue() == itemDetail
                        .getItemMainCategoryId().intValue()&&orderDetail.getIsFree()!=ParamConst.FREE)
                {
                    itemNum = itemNum + orderDetail.getItemNum();
					if((promotionPrice.compareTo(BH.getBD(ParamConst.DOUBLE_ZERO))==0 || promotionPrice.compareTo(BH.getBD(orderDetail.getItemPrice()))>0) && orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL)
					{
						if(orderDetailMin == null || orderDetail.getItemPrice().compareTo(orderDetailMin.getItemPrice()) < 0)
						{
							orderDetailMin=orderDetail;
						}
					}
                }
            }

            if (promotion.getItemId()<=0&&promotion.getItemCategoryId()>0)
            {
                if (promotion.getItemCategoryId().intValue() ==itemDetail.getItemCategoryId()&&orderDetail.getIsFree()!=ParamConst.FREE)
                {
                    if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB)
                    {
                        prevOrderDetailMin = orderDetail;
                    }
                }
            }
        }

		if(prevOrderDetailMin != null && orderDetailMin != null && prevOrderDetailMin != orderDetailMin)
		{
			if(orderDetailMin.getItemPrice().compareTo(prevOrderDetailMin.getItemPrice()) >= 0)
			{
				orderDetailMin = prevOrderDetailMin;
			}
			else
			{
				prevOrderDetailMin.setDiscountPrice(ParamConst.INT_ZERO);
				prevOrderDetailMin.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
				OrderDetailSQL.updateOrderDetailAndOrderByDiscount(prevOrderDetailMin);
			}
		}

        // 然后按照分类来找
        if(orderDetailMin != null || prevOrderDetailMin != null)
        {
			if(itemNum>=promotion.getItemNum() && orderDetailMin != null)
			{
				if(orderDetailMin != prevOrderDetailMin)
				{
					orderDetailMin.setDiscountPrice(orderDetailMin.getItemPrice());
					orderDetailMin.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB);
					OrderDetailSQL.updateOrderDetailAndOrderByDiscount(orderDetailMin);
					deletePromotion(order, promotion);
					addPromotion(order, promotion, null, orderDetailMin.getItemPrice());
                	OrderSQL.updateOrder(order);
					total = new BigDecimal(order.getTotal());
				}
			}
			else if(prevOrderDetailMin != null && itemNum<promotion.getItemNum())
			{
				if(prevOrderDetailMin.getDiscountType() != ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL)
				{
					prevOrderDetailMin.setDiscountPrice(ParamConst.INT_ZERO);
					prevOrderDetailMin.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
					OrderDetailSQL.updateOrderDetailAndOrderByDiscount(prevOrderDetailMin);
					OrderSQL.updateOrder(order);
					deletePromotion(order, promotion);
					total = new BigDecimal(order.getTotal());
				}
			}
			else if(orderDetailMin != null && itemNum<promotion.getItemNum())
			{
				if(orderDetailMin.getDiscountType() != ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL)
				{
					orderDetailMin.setDiscountPrice(ParamConst.INT_ZERO);
					orderDetailMin.setDiscountType(ParamConst.ORDERDETAIL_DISCOUNT_TYPE_NULL);
					OrderDetailSQL.updateOrderDetailAndOrderByDiscount(orderDetailMin);
                	OrderSQL.updateOrder(order);
					deletePromotion(order, promotion);
					total = new BigDecimal(order.getTotal());
				}
			}
        }
        return total;
	}

	// Pax Promotion
	// If pax in backend set to 5, total price will have Discount Rate
	// If set pax to 5 then set pax to 6, discount will stack. However, can be circumvented by resetting the discount by setting pax below promotion then set again on correct amount of pax.
	private static BigDecimal promotionAndPax(Order order, List<OrderDetail> orderDetails, Promotion promotion, BigDecimal total, int pax)
	{
		BigDecimal currentDiscAmount = new BigDecimal(order.getDiscountAmount());
		BigDecimal discount = BH.divThirdFormat(BH.getBD(promotion.getDiscountPercentage()), BH.getBD(100), false);
		BigDecimal promotionPrice = BH.mul(total, discount, false);
		if(total.compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) >0&&pax>=promotion.getGuestNum())
		{
//            currentDiscAmount = new BigDecimal("0");
//            for(OrderDetail orderDetail : orderDetails)
//            {
//                if(orderDetail.getDiscountPrice() != null)
//                {
//                    currentDiscAmount = BH.add(currentDiscAmount, new BigDecimal(orderDetail.getDiscountPrice()), false);
//                }
//            }
//            order.setDiscountAmount(String.valueOf(currentDiscAmount));
//            order.setTotal(String.valueOf(BH.add(BH.getBD(total),promotionPrice, false)));
//            promotionPrice = BH.mul(total, discount, false);

            order.setDiscountAmount(String.valueOf(BH.add(new BigDecimal(order.getDiscountAmount()), promotionPrice, false)));
            total = BH.sub(BH.getBD(total),promotionPrice, false);
            deletePromotion(order, promotion);
            addPromotion(order, promotion, null,promotionPrice.toString());
            order.setTotal(String.valueOf(total));
            OrderSQL.update(order);
            total = new BigDecimal(order.getTotal());
		}
		else
		{
			if(BH.sub(currentDiscAmount, promotionPrice, false).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO)) > 0)
			{
				if(PromotionDataSQL.getPromotionDataOrId(order.getId(), promotion.getId()) != null)
				{
					order.setDiscountAmount("0");
					deletePromotion(order, promotion);
					OrderSQL.updateOrder(order);
					total = new BigDecimal(order.getTotal());
				}
			}
		}

		return total;
	}

    private static void addPromotion(Order order,Promotion promotion,OrderDetail freeOrderDetail,String price)
	{
		long nowTime = System.currentTimeMillis();
		OrderPromotion promotionData = PromotionDataSQL.getPromotionDataOrId(order.getId(), promotion.getId());
		if (promotionData == null || freeOrderDetail!=null)
		{
			promotionData = new OrderPromotion();
			promotionData.setPromotionType(0);
			if(!TextUtils.isEmpty(price)){
				promotionData.setPromotionAmount(price);
			}

			if(freeOrderDetail!=null){
				promotionData.setOrderDetailId(freeOrderDetail.getId());
			}

			promotionData.setOrderId(order.getId());
			promotionData.setCreateTime(nowTime);
			promotionData.setPromotionId(promotion.getId());
			promotionData.setPromotionName(promotion.getPromotionName());
			promotionData.setBusinessDate(order.getBusinessDate());
			promotionData.setFreeItemName(promotion.getFreeItemName());
			promotionData.setFreeItemId(promotion.getFreeItemId());
			promotionData.setFreeNum(promotion.getFreeNum());
			promotionData.setDiscountPrice(promotion.getDiscountPrice());
			promotionData.setDiscountPercentage(promotion.getDiscountPercentage());
			promotionData.setSessionStatus(order.getSessionStatus());
			promotionData.setUpdateTime(nowTime);
			//promotionData.setPromotionAmount(promotion.getDiscountPrice());
			PromotionDataSQL.addPromotionData(promotionData);
		}
		else
		{
			if(!TextUtils.isEmpty(price)){
				promotionData.setPromotionAmount(price);
			}


			//	promotionData.setPromotionAmount(promotionPrice.toString());
			promotionData.setUpdateTime(nowTime);
			PromotionDataSQL.updatePromotionData(promotionData);
		}
	}

	private static void deletePromotion(Order order,Promotion promotion)
 	{
		OrderPromotion promotionData = PromotionDataSQL.getPromotionDataOrId(order.getId(), promotion.getId());
		if (promotionData != null)
		{
			PromotionDataSQL.deletePromotionAndFree(promotionData);
			//PromotionDataSQL.deletePromotionData(promotionData);
		}
	}

	public static void setPromotion(Order order,List<OrderDetail> orderDetails)
	{
		BigDecimal promotionPrice = BH.getBD(ParamConst.DOUBLE_ZERO);
		BigDecimal total =BH.getBD(order.getTotal());

	List<OrderSplit> orderSplits =OrderSplitSQL.getUnFinishedOrderSplits(order.getId());
	if(orderSplits== null||orderSplits.size()==0){
		List<Promotion>  promotionList =PromotionSQL.getAllPromotion();
        Comparator<Promotion> compareByWeight = new Comparator<Promotion>() {
            @Override
            public int compare(Promotion o1, Promotion o2) {
                return o2.getPromotionWeight().compareTo(o1.getPromotionWeight());
            }
        };

        Collections.sort(promotionList, compareByWeight);

        Boolean PaxPromo = false;
		Boolean ItemPromo = false;

		if(promotionList!=null&&promotionList.size()>0) {
			for (int i = 0; i < promotionList.size(); i++) {
				Promotion promotion = promotionList.get(i);
				if (hasWeek(promotion.getPromotionDateInfoId())) {
					switch (promotion.getType()) {
						case 1:
							if(!ItemPromo)
							{
								total = promotionAndItemPrice(order, orderDetails, promotion, total);
								ItemPromo = true;
							}
							break;
						case 2:
							total = promotionAndItemFree(order, orderDetails, promotion, total);
							break;
						case 3:
							total = promotionAndTotalDiscount(order, orderDetails, promotion, total);
							break;
						case 4:
							total = promotionAndTotalFree(order, orderDetails, promotion, total);
							break;
						case 5:
							total = promotionAndFree(order, orderDetails, promotion, total);
							break;
						case 6:
							if(!PaxPromo)
							{
								total = promotionAndPax(order, orderDetails, promotion, total, order.getPersons());
								if(promotion.getGuestNum() <= order.getPersons())
								{
									PaxPromo = true;
								}
							}
							break;
					}
				}

			}
		}
	}
//	 List<OrderPromotion> promotionDatas=PromotionDataSQL.getPromotionDataOrOrderid(order.getId());
//
//		if (promotionDatas.size() > 0) {
//			for (OrderPromotion promotionData : promotionDatas) {
//				if(promotionData.getPromotionType()!= 5){
//					if(BH.getBD(promotionData.getDiscountPrice()).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO))!=0) {
//						BigDecimal discount = BH.divThirdFormat(BH.getBD(promotionData.getDiscountPercentage()),BH.getBD(100),false);
//
//						total=BH.sub(total,
//								BH.getBD(promotionData.getDiscountPrice()), false);
//						promotionPrice = BH.add(promotionPrice,
//								BH.getBD(promotionData.getDiscountPrice()), false);
//
//					}else {
//
//						BigDecimal discount = BH.divThirdFormat(BH.getBD(promotionData.getDiscountPercentage()),BH.getBD(100),false);
//						BigDecimal price = BH.mul(total,BH.getBD(discount),false);
//
//						total=BH.sub(total,price,false);
//						promotionPrice = BH.add(promotionPrice,
//								price, false);
//						promotionData.setPromotionAmount(BH.getBD(promotionPrice).toString());
//						PromotionDataSQL.updatePromotionData(promotionData);
//					}
//				}
//
//			}
//		}
         String  promotionTotal= PromotionDataSQL.getPromotionDataSum(order);
	if(BH.getBD(promotionTotal).compareTo(BH.getBD(ParamConst.DOUBLE_ZERO))==0){
		order.setPromotion(order.getPromotion());
	}else {
		order.setPromotion(promotionTotal);
	}

		order.setTotal(total.toString());

	}

	public static void setOrderBeforTax(Order order, List<OrderDetail> orderDetails) {
		BigDecimal tax = BH.getBD(ParamConst.DOUBLE_ZERO);
		if (orderDetails.size() > 0) {
			for (OrderDetail orderDetail : orderDetails) {
				if(!IntegerUtils.isEmptyOrZero(orderDetail.getAppOrderDetailId())){
					tax = BH.add(tax,
							BH.getBD(orderDetail.getTaxPrice()),
							false);
				}else {
					BigDecimal orderDetailTax = OrderHelper.getOrderDetailBeforTax(order, orderDetail);
					tax = BH.add(tax,
							orderDetailTax,
							false);
					OrderDetailSQL.updateOrderDetailTaxById(orderDetailTax.toString(), orderDetail.getId().intValue());
				}
			}
		}

		order.setOrderRound(BH.add(BH.getBD(order.getOrderRound()),BH.formatRound(BH.getBD(tax)),false).toString());
		order.setTaxAmount(BH.getBD(tax).toString());
	}

	public static void setOrderTax(Order order, List<OrderDetail> orderDetails)
	{
		BigDecimal tax = BH.getBD(ParamConst.DOUBLE_ZERO);
		if (orderDetails.size() > 0) {
			for (OrderDetail orderDetail : orderDetails) {
				if(!IntegerUtils.isEmptyOrZero(orderDetail.getAppOrderDetailId())){
					tax = BH.add(tax,
							BH.getBD(orderDetail.getTaxPrice()),
							false);
				}else {
					BigDecimal orderDetailTax = OrderHelper.getOrderDetailTax(order, orderDetail);
					tax = BH.add(tax,
							orderDetailTax,
							false);

					OrderDetailSQL.updateOrderDetailTaxById(orderDetailTax.toString(), orderDetail.getId().intValue());
				}
			}
		}
		//tax= BH.add(tax,BH.getBD(order.getTaxAmount()),false);
		order.setOrderRound(BH.add(BH.getBD(order.getOrderRound()),BH.formatRound(BH.getBD(tax)),false).toString());
		order.setTaxAmount(BH.getBD(tax).toString());
	}

	public static void setOrderTotal(Order order, List<OrderDetail> orderDetails) {
		order.setTotal(BH.add(
				BH.sub(BH.getBD(order.getSubTotal()),
						BH.getBD(order.getDiscountAmount()), false),
				BH.getBD(order.getTaxAmount()), true).toString());
	}

	public static void setOrderInclusiveTaxPrice(Order order){
		if(order.getInclusiveTaxPercentage() != null){
			order.setInclusiveTaxPrice(BH
					.mul(BH.getBD(order.getInclusiveTaxPercentage()),
							BH.div(BH.sub(BH.getBD(order.getSubTotal()),
									BH.getBD(order.getDiscountAmount()), false),
									BH.add(BH.getBD(1), BH.getBD(order
											.getInclusiveTaxPercentage()),
											false), false), true).toString());
//			order.setInclusiveTaxPrice( BH.mul(
//							BH.getBD(order.getInclusiveTaxPercentage()),
//							BH.sub(BH.getBD(order.getSubTotal()),
//											BH.getBD(order.getDiscountAmount()),
//											false),
//									true).toString());
		}
	}

	public static void setOrderTotalAlfterRound(Order order, RoundAmount roundAmount){
		if(order != null && roundAmount != null){
			order.setTotal(BH.add(BH.getBD(order.getTotal()), BH.getBD(roundAmount.getRoundBalancePrice()), true).toString());
		}

	}

	public static void setOrderSplitTotalAlfterRound(OrderSplit orderSplit, RoundAmount roundAmount){
		if(orderSplit != null && roundAmount != null){
			orderSplit.setTotal(BH.add(BH.getBD(orderSplit.getTotal()), BH.getBD(roundAmount.getRoundBalancePrice()), true).toString());
		}

	}

	public static void setOrderSplitInclusiveTaxPrice(OrderSplit orderSplit){
		if(orderSplit.getInclusiveTaxPercentage() != null){
//			orderSplit.setInclusiveTaxPrice(BH
//					.mul(BH.getBD(orderSplit.getInclusiveTaxPercentage()),
//							BH.sub(BH.getBD(orderSplit.getSubTotal()),
//									BH.getBD(orderSplit.getDiscountAmount()), false)
//									, true).toString());

			orderSplit.setInclusiveTaxPrice(BH
					.mul(BH.getBD(orderSplit.getInclusiveTaxPercentage()),
							BH.div(BH.sub(BH.getBD(orderSplit.getSubTotal()),
									BH.getBD(orderSplit.getDiscountAmount()), false),
									BH.add(BH.getBD(1), BH.getBD(orderSplit
													.getInclusiveTaxPercentage()),
											false), false), true).toString());

		}
	}

	public static void setOrderSplitDiscount(Order order, OrderSplit orderSplit, List<OrderDetail> orderDetails)
    {
//			BigDecimal discount = BH.getBD(ParamConst.DOUBLE_ZERO);
//			for (OrderDetail orderDetail : orderDetails) {
//					if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB) {
//						if(orderSplit.getGroupId().intValue() == orderDetail.getGroupId().intValue()){
//							discount = BH.add(discount, BH.getBD(orderDetail.getDiscountPrice()), false);
//						}
//					} else if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
//							|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_RATE
//							|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_SUB) {
//						if(orderSplit.getGroupId().intValue() == orderDetail.getGroupId().intValue()){
//							discount = BH.add(discount, BH.mul( BH.getBD(orderDetail.getRealPrice()),
//									BH.getBDNoFormat(orderDetail.getDiscountRate()), false),false);
//						}
//					}
//				}
//	    Discount via setting a number on order menu
		BigDecimal discount = BH.getBD(ParamConst.DOUBLE_ZERO);
		if (order.getDiscountType() == ParamConst.ORDER_DISCOUNT_TYPE_SUB_BY_ORDER || order.getDiscountType() == ParamConst.ORDER_DISCOUNT_TYPE_SUB_BY_CATEGORY)
		{
			for (OrderDetail orderDetail : orderDetails)
			{
				if(orderSplit.getOrderStatus() != ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED)
				{
					if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
                    || orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_RATE
                    || orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_RATE)
					{
						if(orderSplit.getGroupId().intValue() == orderDetail.getGroupId().intValue())
						{
							discount = BH.add(discount, BH.mul(
									BH.getBD(orderDetail.getRealPrice()),
									BH.getBDNoFormat(orderDetail.getDiscountRate()), false),
									true);
						}
					}
					else if(orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB
                            || orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_SUB
                            || orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_SUB)
					{
						if(orderSplit.getGroupId().intValue() == orderDetail.getGroupId().intValue())
						{
							discount = BH.add(discount, BH.getBD(orderDetail.getDiscountPrice()), true);
						}
					}
				}
			}
		}
//		Discount via setting percentage
		else
		{
			for (OrderDetail orderDetail : orderDetails)
			{
				if(orderSplit.getOrderStatus() != ParamConst.ORDERSPLIT_ORDERSTATUS_FINISHED)
				{
					if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_SUB)
					{
						if(orderSplit.getGroupId().intValue() == orderDetail.getGroupId().intValue())
						{
							discount = BH.add(discount,
									BH.getBD(orderDetail.getDiscountPrice()), true);
						}
					}
					else if (orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_TYPE_RATE
							|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_RATE
//									|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYORDER_TYPE_SUB
							|| orderDetail.getDiscountType() == ParamConst.ORDERDETAIL_DISCOUNT_BYCATEGORY_TYPE_RATE)
					{
						if(orderSplit.getGroupId().intValue() == orderDetail.getGroupId().intValue())
						{
							discount = BH.add(discount, BH.mul(
									BH.getBD(orderDetail.getRealPrice()),
									BH.getBDNoFormat(orderDetail.getDiscountRate()), false),
									true);
						}
					}

				}
			}
		}
		discount = discount.setScale(2, BigDecimal.ROUND_HALF_UP);
		orderSplit.setDiscountAmount(BH.getBD(discount).toString());
	}

	public static void setOrderSplitSubTotal(Order order, OrderSplit orderSplit,
			List<OrderDetail> orderDetails) {
		BigDecimal subTotal = BH.getBD(ParamConst.DOUBLE_ZERO);
		if (!orderDetails.isEmpty()) {
			for (OrderDetail orderDetail : orderDetails) {
				if(orderSplit.getGroupId().intValue() == orderDetail.getGroupId().intValue()){
					subTotal = BH.add(subTotal, BH.getBD(orderDetail.getRealPrice()), false);
				}
			}
		}
		orderSplit.setSubTotal(BH.getBD(subTotal).toString());
	}

	public static void setOrderSplitTax(Order order, OrderSplit orderSplit, List<OrderDetail> orderDetails){
		/*
		BigDecimal tax = BH.getBD(ParamConst.DOUBLE_ZERO);
		if (orderDetails.size() > 0) {
			for (OrderDetail orderDetail : orderDetails) {
				if(!IntegerUtils.isEmptyOrZero(orderDetail.getAppOrderDetailId())){
					tax = BH.add(tax,
							BH.getBD(orderDetail.getTaxPrice()),
							false);
				}else {
					BigDecimal orderDetailTax = OrderHelper.getOrderDetailTax(order, orderDetail);
					tax = BH.add(tax,
							orderDetailTax,
							false);
					OrderDetailSQL.updateOrderDetailTaxById(orderDetailTax.toString(), orderDetail.getId().intValue());
				}
			}
		}
		order.setTaxAmount(BH.doubleFormat.format(tax));
		 */
		BigDecimal orderSplitTax = BH.getBD(ParamConst.DOUBLE_ZERO);
		if(!orderDetails.isEmpty()){
			for (OrderDetail orderDetail : orderDetails) {
				if(orderSplit.getGroupId().intValue() == orderDetail.getGroupId().intValue()){
					if(!IntegerUtils.isEmptyOrZero(orderDetail.getAppOrderDetailId())){
						orderSplitTax = BH.add(orderSplitTax,
							BH.getBD(orderDetail.getTaxPrice()),false);
					}
//					orderSplitTax = BH.add(orderSplitTax,
//							OrderHelper.getOrderDetailTax(order, orderDetail),
//							false);
					BigDecimal orderDetailTax = OrderHelper.getOrderDetailTax(order, orderDetail);
					orderSplitTax = BH.add(orderSplitTax,
							orderDetailTax,
							false);
					OrderDetailSQL.updateOrderDetailTaxById(orderDetailTax.toString(), orderDetail.getId().intValue());
				}
			}
		}
		orderSplit.setTaxAmount(BH.getBD(orderSplitTax).toString());
	}

	public static void setOrderSplitTotal(Order order, OrderSplit orderSplit, List<OrderDetail> orderDetails){
		orderSplit.setTotal(BH.add(
				BH.sub(BH.getBD(orderSplit.getSubTotal()),
						BH.getBD(orderSplit.getDiscountAmount()), false),
				BH.getBD(orderSplit.getTaxAmount()), true).toString());
	}

	public static ItemHappyHour getItemHappyHour(Order order,
			OrderDetail orderDetail) {
		ItemHappyHour itemHappyHour = null;
		RevenueCenter revenueCenter = CoreData.getInstance().getRevenueCenter(
				order);
		// 先从RevenueCenter判断，是否存在合法的HappyHour，如果不存在，则认为没有ItemHappyHour
		if (revenueCenter == null
				|| CommonUtil.isNull(revenueCenter.getHappyHourId())) {
			return itemHappyHour;
		}
		if (CommonUtil.isNull(revenueCenter.getHappyEndTime())
				|| CommonUtil.isNull(revenueCenter.getHappyStartTime())) {
			return itemHappyHour;
		}
		long currentTimeMillis = System.currentTimeMillis();
		if (currentTimeMillis < revenueCenter.getHappyStartTime()
				|| currentTimeMillis > revenueCenter.getHappyEndTime()) {
			return itemHappyHour;
		}

		// 再从HappyHourWeek判断，是否存在合法的HappyHourWeek，如果不存在，则认为没有ItemHappyHour
		boolean isHappy = hasHappyWeek(revenueCenter.getHappyHourId());
		if (!isHappy)
			return itemHappyHour;

		// 最后从ItemHappyHour判断，是否存在合法的ItemHappyHour，如果不存在，则认为没有ItemHappyHour
		ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
				orderDetail.getItemId(), orderDetail.getItemName());
		itemHappyHour = CoreData.getInstance().getItemHappyHour(revenueCenter,
				itemDetail);
		return itemHappyHour;
	}

	public static ItemPromotion getItemPromotion(Order order,
												 OrderDetail orderDetail) {
		ItemPromotion itemPromotion = null;
		RevenueCenter revenueCenter = CoreData.getInstance().getRevenueCenter(
				order);
		// 先从RevenueCenter判断，是否存在合法的HappyHour，如果不存在，则认为没有ItemHappyHour
		if (revenueCenter == null
				|| CommonUtil.isNull(revenueCenter.getHappyHourId())) {
			return itemPromotion;
		}
		if (CommonUtil.isNull(revenueCenter.getHappyEndTime())
				|| CommonUtil.isNull(revenueCenter.getHappyStartTime())) {
			return itemPromotion;
		}
		long currentTimeMillis = System.currentTimeMillis();
//		if (currentTimeMillis < revenueCenter.getHappyStartTime()
//				|| currentTimeMillis > revenueCenter.getHappyEndTime()) {
//			return itemPromotion;
//		}

		// 再从HappyHourWeek判断，是否存在合法的HappyHourWeek，如果不存在，则认为没有ItemHappyHour
//		boolean isHappy = hasHappyWeek(revenueCenter.getHappyHourId());
//		if (!isHappy)
//			return itemPromotion;

		// 最后从ItemHappyHour判断，是否存在合法的ItemHappyHour，如果不存在，则认为没有ItemHappyHour
		ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
				orderDetail.getItemId(), orderDetail.getItemName());
		itemPromotion = CoreData.getInstance().getItemPromotion(revenueCenter,
				itemDetail);
		return itemPromotion;
	}

	public static List<PromotionOrder> getOrderPromotion() {
		PromotionOrder promotionOrder = null;
	    List<PromotionOrder> promotionOrders=PromotionOrderSQL.getAllpromotionOrder();
//	    if(promotionOrders!=null&&promotionOrders.size()>0){
//	    	promotionOrder=promotionOrders.get(0);
//		}
		return promotionOrders;
	}

	public static boolean hasWeek(Integer id) {
		long time = System.currentTimeMillis();
		List<PromotionWeek>	promotionWeeks= PromotionWeekSQL.getAllPromotionWeek();
		if(promotionWeeks!=null) {
			for (PromotionWeek promotionWeek : promotionWeeks) {
				if (promotionWeek.getPromotionDateInfoId().intValue() == id
						.intValue()) {

					if (promotionWeek.getWeek() == TimeUtil
							.getWeek(time)) {

						if (time >= getTimes(promotionWeek.getStartTime()) && getTimes(promotionWeek.getEndTime()) >= time) {
							return true;
						}


					}
				}
			}
		}
		return false;
	}

	private static long getTimes(String time) {
		 long hourTime = 60 * 60 * 1000;
		String[] times = time.split(":");
       long timeAll =TimeUtil.getTimeInMillsByZero(0)+ Integer.valueOf(times[0]).intValue()*hourTime+Integer.valueOf(times[1]).intValue()*60*1000;
           return timeAll;
	}

	private static boolean hasHappyWeek(Integer happy_hour_id) {
		long time = TimeUtil.getTimeInMillis();
		for (HappyHourWeek happyHourWeek : CoreData.getInstance()
				.getHappyHourWeeks()) {
			if (happyHourWeek.getHappyHourId().intValue() == happy_hour_id
					.intValue()) {
				if (happyHourWeek.getStartTime() < time
						&& happyHourWeek.getEndTime() > time) {
					if (Integer.parseInt(happyHourWeek.getWeek()) == TimeUtil
							.dayForWeek()) {
						return true;
					}
				}
			}
		}
		return false;
	}

	// 只有添加默认modifier
	public static void addDefaultModifiers(Order order, OrderDetail orderDetail) {
		ItemDetail itemDetail = CoreData.getInstance().getItemDetailById(
				orderDetail.getItemId(), orderDetail.getItemName());
		if (itemDetail == null || itemDetail.getItemTemplateId() == null){
			return;
		}
		for(ItemModifier itemModifier : CoreData.getInstance().getItemModifiers()){
			if (itemModifier == null
					|| itemModifier.getModifierCategoryId() == null) {
				continue;
			}
			if (itemModifier.getItemId().intValue() == itemDetail
					.getItemTemplateId().intValue()) {
				for (Modifier modifier : CoreData.getInstance().getModifierList()) {
					if (modifier == null || modifier.getId() == null){
						continue;
					}
					if (modifier.getCategoryId().intValue() == itemModifier
							.getModifierCategoryId().intValue()
							&& modifier.getType().intValue() == 1
							&& modifier.getIsDefault().intValue() == 1) {
						ItemDetail printItemDetail = CoreData.getInstance().getItemDetailByTemplateId(modifier.getItemId());
						int printId = 0;
						if(printItemDetail != null){
							ArrayList<Printer> prints = CoreData.getInstance().getPrintersInGroup(printItemDetail.getPrinterId().intValue());
							if(prints.size() == 0){
								printId = 0;
							}else{
								printId = prints.get(0).getId().intValue();
							}

						}
						OrderModifier orderModifier = ObjectFactory.getInstance().getOrderModifier(order, orderDetail, modifier, printId);
						OrderModifierSQL.addOrderModifier(orderModifier);
					}
				}
			}
		}

	}


	public static void setOrderModifierPirceAndNum(OrderDetail orderDetail, int num){
		List<OrderModifier> orderModifiers = OrderModifierSQL.getOrderModifiers(orderDetail);
		for(OrderModifier orderModifier : orderModifiers){
			Modifier modifier = CoreData.getInstance().getModifier(orderModifier.getModifierId().intValue());
			int modifierNum = 1;
			if(modifier == null){
				modifierNum = num;
			}else{
				modifierNum = modifier.getQty() * num;
			}
			orderModifier.setModifierNum(modifierNum);
			orderModifier.setModifierPrice(BH.mul(BH.getBD(orderModifier.getModifierItemPrice()), BH.getBD(modifierNum), true).toString());
			OrderModifierSQL.updateOrderModifier(orderModifier);
		}

	}


	/*:加入流水号 */
	public static int calculateOrderNo(long bizDate) {
		int maxOrderNo = Store.getInt(BaseApplication.instance, Store.MAX_ORDER_NO, 0);
		int sid = 0;
		sid = OrderSQL.getSumCountByBizDate(bizDate);
		if(maxOrderNo > 0 && sid >= maxOrderNo){
			return 1;
		}
		return sid+1;
	}
}
