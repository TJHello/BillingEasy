package com.tjhello.easy.billing.java.imp;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjhello.lib.billing.base.info.ProductConfig;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;
import com.tjhello.lib.billing.base.info.PurchaseHistoryInfo;
import com.tjhello.lib.billing.base.listener.EasyCallBack;

import java.util.List;

public interface BillingManagerImp {

    void init(@NonNull Context context);

    void addProductConfig(@NonNull ProductConfig productConfig);

    void cleanProductConfig();

    void onDestroy();

    void queryProduct(@Nullable EasyCallBack<List<ProductInfo>> callBack);

    void purchase(@NonNull Activity activity,@NonNull String productCode,@Nullable EasyCallBack<List<PurchaseInfo>> callBack);

    void consume(@NonNull String purchaseToken,@Nullable EasyCallBack<String> callBack);

    void acknowledge(@NonNull String purchaseToken,@Nullable EasyCallBack<String> callBack);

    void queryOrderAsync(@Nullable EasyCallBack<List<PurchaseInfo>> callBack);

    void queryOrderLocal(@Nullable EasyCallBack<List<PurchaseInfo>> callBack);

    void queryOrderHistory(@Nullable EasyCallBack<List<PurchaseHistoryInfo>> callBack);

}
