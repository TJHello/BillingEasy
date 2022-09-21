package com.tjhello.lib.billing.base.listener;

import androidx.annotation.NonNull;

import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.lib.billing.base.info.BillingEasyResult;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;
import com.tjhello.lib.billing.base.info.PurchaseHistoryInfo;

import java.util.List;

/**
 * BillingEasy
 *一款全新设计的内购聚合，同时支持华为内购与谷歌内购。
 *=========================================
 * 作者:TJHello
 * 日期:2021-07-17
 * qq群:425219113
 * 仓库地址:https://gitee.com/TJHello/BillingEasy
 * ========================================
 * 使用该库请遵循Apache License2.0协议，莫要寒了广大开源者的心
 */
public interface BillingEasyListener {

    default void onConnection(@NonNull BillingEasyResult result){}

    default void onDisconnected(){}

    default void onQueryProduct(@NonNull BillingEasyResult result,@NonNull List<ProductInfo> productInfoList){}

    default void onPurchases(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList){}

    default void onConsume(@NonNull BillingEasyResult result,@NonNull String purchaseToken){}

    default void onAcknowledge(@NonNull BillingEasyResult result,@NonNull String purchaseToken){}

    default void onQueryOrder(@NonNull BillingEasyResult result,@ProductType String type, @NonNull List<PurchaseInfo> purchaseInfoList){}

    default void onQueryOrderHistory(@NonNull BillingEasyResult result, @ProductType String type, @NonNull List<PurchaseHistoryInfo> purchaseInfoList){}
}
