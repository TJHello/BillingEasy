package com.tjhello.lib.billing.base.imp;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjhello.lib.billing.base.anno.BillingName;
import com.tjhello.lib.billing.base.info.PurchaseParam;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;

import java.util.List;

public interface BillingHandlerImp {

    void onInit(@NonNull Activity activity);

    boolean connection(@NonNull BillingEasyListener listener);

    void queryProduct(@NonNull List<String> productCodeList, @NonNull String type, @NonNull BillingEasyListener listener);

    void purchase(@NonNull Activity activity, @NonNull PurchaseParam param, @NonNull String type);

    void consume(@NonNull String purchaseToken,@NonNull BillingEasyListener listener);

    void acknowledge(@NonNull String purchaseToken,@NonNull BillingEasyListener listener);

    void queryOrderAsync(@NonNull String type, @NonNull BillingEasyListener listener);

    void queryOrderLocal(@NonNull String type,@NonNull BillingEasyListener listener);

    void queryOrderHistory(@NonNull String type,@NonNull BillingEasyListener listener);

    void onActivityResult(int requestCode, int resultCode,@Nullable Intent data);

    @BillingName
    String getBillingName();
}
