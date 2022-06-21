package com.tjhello.lib.billing.base.handler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;


import com.tjhello.lib.billing.base.anno.BillingName;
import com.tjhello.lib.billing.base.info.PurchaseParam;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;
import com.tjhello.lib.billing.base.utils.BillingEasyLog;

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
public class EmptyHandler extends  BillingHandler {

    public EmptyHandler(BillingEasyListener mBillingEasyListener) {
        super(mBillingEasyListener);
        BillingEasyLog.e("没有检测到任何内购库");
    }

    @NonNull
    @Override
    public String getProductType(String type) {
        return "";
    }


    @Override
    public void onInit(@NonNull Activity activity) {

    }

    @Override
    public boolean connection(@NonNull BillingEasyListener listener) {
        return false;
    }

    @Override
    public void queryProduct(@NonNull List<String> productCodeList, @NonNull String type, @Nullable BillingEasyListener listener) {
        
    }

    @Override
    public void purchase(@NonNull Activity activity, @NonNull PurchaseParam param, @NonNull String type) {

    }

    @Override
    public void consume(@NonNull String purchaseToken, @NonNull BillingEasyListener listener) {

    }

    @Override
    public void acknowledge(@NonNull String purchaseToken, @NonNull BillingEasyListener listener) {

    }

    @Override
    public void queryOrderAsync(@NonNull String type, @NonNull BillingEasyListener listener) {

    }

    @Override
    public void queryOrderLocal(@NonNull String type, @NonNull BillingEasyListener listener) {

    }

    @Override
    public void queryOrderHistory(@NonNull String type, @NonNull BillingEasyListener listener) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    @Override
    @BillingName
    public String getBillingName() {
        return BillingName.EMPTY;
    }


}
