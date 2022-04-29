package com.tjhello.lib.billing.base.imp;

import android.app.Activity;
import android.content.Context;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjhello.lib.billing.base.anno.BillingName;
import com.tjhello.lib.billing.base.info.PurchaseParam;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;

import java.util.List;

public interface BillingHandlerImp {

    void onInit(@NonNull Context context);

    boolean connection(@NonNull BillingEasyListener listener);

    void queryProduct(@NonNull List<String> productCodeList, @NonNull String type, @NonNull BillingEasyListener listener);

    void purchase(@NonNull Activity activity,@NonNull String type,@NonNull PurchaseParam purchaseParam);

    void consume(@NonNull String purchaseToken,@NonNull BillingEasyListener listener);

    void acknowledge(@NonNull String purchaseToken,@NonNull BillingEasyListener listener);

    void queryOrderAsync(@NonNull List<String> typeList,@NonNull BillingEasyListener listener);

    void queryOrderLocal(@NonNull List<String> typeList,@NonNull BillingEasyListener listener);

    void queryOrderHistory(@NonNull List<String> typeList,@NonNull BillingEasyListener listener);

    @BillingName
    String getBillingName();
}
