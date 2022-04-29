package com.tjhello.easy.billing.java.imp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.lib.billing.base.info.ProductConfig;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;
import com.tjhello.lib.billing.base.info.PurchaseHistoryInfo;
import com.tjhello.lib.billing.base.info.PurchaseParam;
import com.tjhello.lib.billing.base.listener.EasyCallBack;

import java.util.List;

public interface BillingManagerImp {

    void init(@NonNull Activity activity);
    void init(@NonNull Activity activity, EasyCallBack<Boolean> callBack);

    void addProductConfig(@NonNull ProductConfig productConfig);

    void cleanProductConfig();

    void onDestroy();

    void queryProduct(@Nullable EasyCallBack<List<ProductInfo>> callBack);

    void queryProduct(@ProductType String type, @Nullable EasyCallBack<List<ProductInfo>> callBack);

    void queryProduct(@ProductType String type, @NonNull List<String> codeList, @Nullable EasyCallBack<List<ProductInfo>> callBack);

    void purchase(@NonNull Activity activity, @NonNull PurchaseParam param);

    void consume(@NonNull String purchaseToken,@Nullable EasyCallBack<String> callBack);

    void acknowledge(@NonNull String purchaseToken,@Nullable EasyCallBack<String> callBack);

    void queryOrderAsync(@Nullable EasyCallBack<List<PurchaseInfo>> callBack);

    void queryOrderLocal(@Nullable EasyCallBack<List<PurchaseInfo>> callBack);

    void queryOrderHistory(@Nullable EasyCallBack<List<PurchaseHistoryInfo>> callBack);

    void onActivityResult(int requestCode, int resultCode,@Nullable Intent data);
}
