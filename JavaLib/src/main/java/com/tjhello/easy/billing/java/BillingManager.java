package com.tjhello.easy.billing.java;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjhello.lib.billing.base.anno.BillingName;
import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.lib.billing.base.handler.BillingHandler;
import com.tjhello.easy.billing.java.imp.BillingManagerImp;
import com.tjhello.lib.billing.base.info.BillingEasyResult;
import com.tjhello.lib.billing.base.info.ProductConfig;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;
import com.tjhello.lib.billing.base.info.PurchaseHistoryInfo;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;
import com.tjhello.lib.billing.base.listener.EasyCallBack;
import com.tjhello.lib.billing.base.utils.BillingEasyLog;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

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
public class BillingManager implements BillingManagerImp {

    private static final MyBillingEasyListener mBillingEasyListener = new MyBillingEasyListener();
    private static BillingHandler billingHandler;
    private static final CopyOnWriteArrayList<BillingEasyListener> publicListenerList = new CopyOnWriteArrayList<>();

    private static final CopyOnWriteArrayList<EasyCallBack<List<PurchaseInfo>>> purchaseEasyCallBackList = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<ProductConfig> productConfigList = new CopyOnWriteArrayList<>();

    private boolean isFirstOnCreate = true;
    @Override
    public void onCreate(@NonNull Activity activity) {
        if(isFirstOnCreate){
            isFirstOnCreate = false;
            billingHandler = BillingHandler.createBillingHandler(mBillingEasyListener);
            billingHandler.setProductConfigList(productConfigList);
            BillingEasyLog.setVersionName(BuildConfig.VERSION_NAME);
            billingHandler.onInit(activity);
            billingHandler.connection(new ConnectionBillingEasyListener());
        }
    }

    @Override
    public void addProductConfig(@NonNull ProductConfig productConfig) {
        productConfigList.add(productConfig);
    }


    @Override
    public void onDestroy() {

    }

    @Override
    public void queryProduct(@Nullable EasyCallBack<List<ProductInfo>> callBack) {
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            queryProductAll(callBack);
        }
    }

    private static void queryProductAll(@Nullable EasyCallBack<List<ProductInfo>> callBack){
        List<String> list1 = getProductCodeList(ProductType.TYPE_INAPP_CONSUMABLE);
        List<String> list2 = getProductCodeList(ProductType.TYPE_INAPP_NON_CONSUMABLE);
        List<String> list3 = getProductCodeList(ProductType.TYPE_SUBS);
        ProductBillingEasyListener listener = new ProductBillingEasyListener(callBack);
        if(billingHandler.getBillingName().equals(BillingName.GOOGLE)){
            list1.addAll(list2);
            billingHandler.queryProduct(list1,billingHandler.getProductType(ProductType.TYPE_INAPP_CONSUMABLE),listener);
            billingHandler.queryProduct(list3,billingHandler.getProductType(ProductType.TYPE_SUBS),listener);
        }else{
            billingHandler.queryProduct(list1,billingHandler.getProductType(ProductType.TYPE_INAPP_CONSUMABLE),listener);
            billingHandler.queryProduct(list2,billingHandler.getProductType(ProductType.TYPE_INAPP_NON_CONSUMABLE),listener);
            billingHandler.queryProduct(list3,billingHandler.getProductType(ProductType.TYPE_SUBS),listener);
        }
    }


    @Override
    public void purchase(@NonNull Activity activity, @NonNull String productCode, @Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            ProductConfig config = findProductConfig(productCode);
            if(config!=null){
                addEasyCallback(callBack);
                billingHandler.purchase(activity,productCode,billingHandler.getProductType(config.getType()));
            }
        }
    }

    @Override
    public void consume(@NonNull String purchaseToken, @Nullable EasyCallBack<String> callBack) {
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            PurchaseTokenBillingEasyListener listener = new PurchaseTokenBillingEasyListener(callBack);
            billingHandler.consume(purchaseToken,listener);
        }
    }

    @Override
    public void acknowledge(@NonNull String purchaseToken, @Nullable EasyCallBack<String> callBack) {
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            PurchaseTokenBillingEasyListener listener = new PurchaseTokenBillingEasyListener(callBack);
            billingHandler.acknowledge(purchaseToken,listener);
        }
    }

    @Override
    public void queryOrderAsync(@Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            queryOrderAsyncAll(callBack);
        }
    }

    private static void queryOrderAsyncAll(@Nullable EasyCallBack<List<PurchaseInfo>> callBack){
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            PurchaseBillingEasyListener listener = new PurchaseBillingEasyListener(callBack);
            billingHandler.queryOrderAsync(getTypeListAll(),listener);
        }
    }

    @Override
    public void queryOrderLocal( @Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            PurchaseBillingEasyListener listener = new PurchaseBillingEasyListener(callBack);
            billingHandler.queryOrderLocal(getTypeListAll(),listener);
        }
    }

    @Override
    public void queryOrderHistory(@Nullable EasyCallBack<List<PurchaseHistoryInfo>> callBack) {
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            queryOrderHistoryAll(callBack);
        }
    }

    private static void queryOrderHistoryAll(@Nullable EasyCallBack<List<PurchaseHistoryInfo>> callBack){
        PurchaseHistoryBillingEasyListener listener = new PurchaseHistoryBillingEasyListener(callBack);
        billingHandler.queryOrderHistory(getTypeListAll(),listener);
    }

    public void addListener(@NonNull BillingEasyListener listener) {
        publicListenerList.add(listener);
    }

    public void removeListener(@NonNull BillingEasyListener listener) {
        publicListenerList.remove(listener);
    }

    private static List<String> getProductCodeList(@ProductType String type){
        List<String> list = new ArrayList<>();
        for (ProductConfig productConfig : productConfigList) {
            if(productConfig.getType().equals(type)){
                list.add(productConfig.getCode());
            }
        }
        return list;
    }

    private static class ProductBillingEasyListener implements BillingEasyListener{
        @Nullable
        private final EasyCallBack<List<ProductInfo>> productCallback ;

        public ProductBillingEasyListener(@Nullable EasyCallBack<List<ProductInfo>> productCallback) {
            this.productCallback = productCallback;
        }

        @Override
        public void onQueryProduct(@NonNull BillingEasyResult result, @NonNull List<ProductInfo> productInfoList) {
            if(productCallback==null) return;
            productCallback.callback(result,productInfoList);
        }
    }

    private static class PurchaseBillingEasyListener implements BillingEasyListener{
        @Nullable
        private final EasyCallBack<List<PurchaseInfo>> purchaseCallback ;

        public PurchaseBillingEasyListener(@Nullable EasyCallBack<List<PurchaseInfo>> purchaseCallback) {
            this.purchaseCallback = purchaseCallback;
        }

        @Override
        public void onPurchases(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            if(purchaseCallback==null) return;
            purchaseCallback.callback(result,purchaseInfoList);
        }

        @Override
        public void onQueryOrder(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            if(purchaseCallback==null) return;
            purchaseCallback.callback(result,purchaseInfoList);
        }
    }

    private static class PurchaseTokenBillingEasyListener implements BillingEasyListener{
        @Nullable
        private final EasyCallBack<String> purchaseCallback ;

        public PurchaseTokenBillingEasyListener(@Nullable EasyCallBack<String> purchaseCallback) {
            this.purchaseCallback = purchaseCallback;
        }

        @Override
        public void onConsume(@NonNull BillingEasyResult result, @NonNull String purchaseToken) {
            if(purchaseCallback==null) return;
            purchaseCallback.callback(result,purchaseToken);
        }

        @Override
        public void onAcknowledge(@NonNull BillingEasyResult result, @NonNull String purchaseToken) {
            if(purchaseCallback==null) return;
            purchaseCallback.callback(result,purchaseToken);
        }

    }

    private static class PurchaseHistoryBillingEasyListener implements BillingEasyListener{

        @Nullable
        private final EasyCallBack<List<PurchaseHistoryInfo>> callback ;

        public PurchaseHistoryBillingEasyListener(@Nullable EasyCallBack<List<PurchaseHistoryInfo>> callback) {
            this.callback = callback;
        }

        @Override
        public void onQueryOrderHistory(@NonNull BillingEasyResult result, @NonNull List<PurchaseHistoryInfo> purchaseInfoList) {
            if(callback==null) return;
            callback.callback(result,purchaseInfoList);
        }
    }

    private static class ConnectionBillingEasyListener implements BillingEasyListener{
        @Override
        public void onConnection(@NonNull BillingEasyResult result) {

        }
    }

    private static class MyBillingEasyListener implements BillingEasyListener{

        @Override
        public void onConnection(@NonNull BillingEasyResult result) {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onConnection(result);
            }
            if(result.isSuccess){
                queryProductAll(null);
                queryOrderAsyncAll(null);
                queryOrderHistoryAll(null);
            }
        }

        @Override
        public void onDisconnected() {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onDisconnected();
            }
        }

        @Override
        public void onQueryProduct(@NonNull BillingEasyResult result, @NonNull List<ProductInfo> productInfoList) {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onQueryProduct(result,productInfoList);
            }
        }

        @Override
        public void onPurchases(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            for (EasyCallBack<List<PurchaseInfo>> listEasyCallBack : purchaseEasyCallBackList) {
                listEasyCallBack.callback(result,purchaseInfoList);
            }
            cleanEasyCallback();
            for (BillingEasyListener listener : publicListenerList) {
                listener.onPurchases(result, purchaseInfoList);
            }
        }

        @Override
        public void onConsume(@NonNull BillingEasyResult result, @NonNull String purchaseToken) {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onConsume(result, purchaseToken);
            }
        }

        @Override
        public void onAcknowledge(@NonNull BillingEasyResult result, @NonNull String purchaseToken) {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onAcknowledge(result, purchaseToken);
            }
        }

        @Override
        public void onQueryOrder(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onQueryOrder(result, purchaseInfoList);
            }
        }

        @Override
        public void onQueryOrderHistory(@NonNull BillingEasyResult result, @NonNull List<PurchaseHistoryInfo> purchaseInfoList) {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onQueryOrderHistory(result,purchaseInfoList);
            }
        }
    }

    private static void addEasyCallback(@Nullable EasyCallBack<List<PurchaseInfo>> callback){
        if(callback==null) return;
        purchaseEasyCallBackList.add(callback);
    }

    private static void cleanEasyCallback(){
        purchaseEasyCallBackList.clear();
    }

    private static List<String> getTypeListAll(){
        return getTypeList(ProductType.TYPE_INAPP_CONSUMABLE,ProductType.TYPE_INAPP_NON_CONSUMABLE,ProductType.TYPE_SUBS);
    }

    private static List<String> getTypeList(String... types){
        List<String> list = new ArrayList<>();
        for (String type : types) {
            String billingType = billingHandler.getProductType(type);
            if(!list.contains(billingType)){
                list.add(billingType);
            }
        }
        return list;
    }

    @Nullable
    public static ProductConfig findProductConfig(@NonNull String productCode){
        for(int i=0;i< productConfigList.size();i++){
            ProductConfig config = productConfigList.get(i);
            if(config.getCode().equals(productCode)){
                return config;
            }
        }
        return null;
    }
}
