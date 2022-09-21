package com.tjhello.easy.billing.java.imp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;
import com.tjhello.lib.billing.base.info.PurchaseHistoryInfo;
import com.tjhello.lib.billing.base.info.PurchaseParam;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;
import com.tjhello.lib.billing.base.listener.EasyCallBack;

import java.util.List;

public interface BillingEasyImp {

    void onCreate();

    void onDestroy();

    void queryProduct();

    void queryProduct(@Nullable EasyCallBack<List<ProductInfo>> callBack);

    void purchase(@NonNull String productCode);

    void purchase(@NonNull PurchaseParam param);

    void purchase(@NonNull String productCode,@Nullable EasyCallBack<List<PurchaseInfo>> callBack);

    void purchase(@NonNull PurchaseParam param,@Nullable EasyCallBack<List<PurchaseInfo>> callBack);

    void consume(@NonNull String purchaseToken);

    void consume(@NonNull String purchaseToken,@Nullable EasyCallBack<String> callBack);

    void acknowledge(@NonNull String purchaseToken);

    void acknowledge(@NonNull String purchaseToken,@Nullable EasyCallBack<String> callBack);

    void queryOrderAsync();

    void queryOrderLocal();

    void queryOrderHistory();

    void queryOrderAsync(@Nullable EasyCallBack<List<PurchaseInfo>> callBack);

    void queryOrderLocal(@Nullable EasyCallBack<List<PurchaseInfo>> callBack);

    void queryOrderHistory(@Nullable EasyCallBack<List<PurchaseHistoryInfo>> callBack);
}
