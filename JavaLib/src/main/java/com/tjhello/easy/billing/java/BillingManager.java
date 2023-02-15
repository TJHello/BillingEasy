package com.tjhello.easy.billing.java;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

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
import com.tjhello.lib.billing.base.info.PurchaseParam;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;
import com.tjhello.lib.billing.base.listener.EasyCallBack;
import com.tjhello.lib.billing.base.utils.BillingEasyLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

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
    private static BillingHandler billingHandler = null;
    private static final CopyOnWriteArrayList<BillingEasyListener> publicListenerList = new CopyOnWriteArrayList<>();

    private static final CopyOnWriteArrayList<EasyCallBack<List<PurchaseInfo>>> purchaseEasyCallBackList = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<ProductConfig> productConfigList = new CopyOnWriteArrayList<>();

    private static final AtomicBoolean isInit = new AtomicBoolean(false);

    @Override
    public void init(@NonNull Activity activity) {
        init(activity,null);
    }

    @Override
    public void init(@NonNull Activity activity,@Nullable EasyCallBack<Boolean> callBack) {
        if(!isInit.getAndSet(true)){
            getHandler().setProductConfigList(productConfigList);
            BillingEasyLog.setVersionName(BuildConfig.VERSION_NAME);
            billingHandler.onInit(activity);
            billingHandler.connection(new ConnectionBillingEasyListener(callBack));
        }
    }

    private BillingHandler getHandler(){
        if(billingHandler==null)
        {
            billingHandler = BillingHandler.createBillingHandler(mBillingEasyListener);
        }
        return billingHandler;
    }

    @Override
    public void addProductConfig(@NonNull ProductConfig productConfig) {
        int size = productConfigList.size();
        for(int i=size-1;i>=0;i--){
            ProductConfig config = productConfigList.get(i);
            if(config.getCode().equals(productConfig.getCode())){
                BillingEasyLog.e("请勿重复添加商品配置:"+config.getCode());
                return ;
            }
        }
        productConfigList.add(productConfig);
        getHandler().addProductConfigList(productConfig);
    }

    @Override
    public void cleanProductConfig() {
        productConfigList.clear();
        getHandler().cleanProductConfigList();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void queryProduct(@Nullable EasyCallBack<List<ProductInfo>> callBack) {
        if(!isInit.get()) {
            BillingEasyLog.e("请先初始化SDK");
            return;
        }
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            queryProductAll(callBack);
        }
    }

    @Override
    public void queryProduct(String type, @Nullable EasyCallBack<List<ProductInfo>> callBack) {
        if(!isInit.get()) {
            BillingEasyLog.e("请先初始化SDK");
            return;
        }
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            List<String> list = getProductCodeList(type);
            ProductBillingEasyListener listener = new ProductBillingEasyListener(callBack);
            billingHandler.queryProduct(list,billingHandler.getProductType(type),listener);
        }
    }

    @Override
    public void queryProduct(String type, @NonNull List<String> codeList, @Nullable EasyCallBack<List<ProductInfo>> callBack) {
        if(!isInit.get()) {
            BillingEasyLog.e("请先初始化SDK");
            return;
        }
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            ProductBillingEasyListener listener = new ProductBillingEasyListener(callBack);
            billingHandler.queryProduct(codeList,billingHandler.getProductType(type),listener);
        }
    }

    private static void queryProductAll(@Nullable EasyCallBack<List<ProductInfo>> callBack){
        if(!isInit.get()) {
            BillingEasyLog.e("请先初始化SDK");
            return;
        }
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
    public void purchase(@NonNull Activity activity, @NonNull PurchaseParam param) {
        if(!isInit.get()) {
            BillingEasyLog.e("请先初始化SDK");
            return;
        }
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            ProductConfig config = findProductConfig(param.productCode);
            if(config!=null){
                billingHandler.purchase(activity,param,billingHandler.getProductType(config.getType()));
            }
        }
    }

    @Override
    public void consume(@NonNull String purchaseToken, @Nullable EasyCallBack<String> callBack) {
        if(!isInit.get()) {
            BillingEasyLog.e("请先初始化SDK");
            return;
        }
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            PurchaseTokenBillingEasyListener listener = new PurchaseTokenBillingEasyListener(callBack);
            billingHandler.consume(purchaseToken,listener);
        }
    }

    @Override
    public void acknowledge(@NonNull String purchaseToken, @Nullable EasyCallBack<String> callBack) {
        if(!isInit.get()) {
            BillingEasyLog.e("请先初始化SDK");
            return;
        }
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            PurchaseTokenBillingEasyListener listener = new PurchaseTokenBillingEasyListener(callBack);
            billingHandler.acknowledge(purchaseToken,listener);
        }
    }

    @Override
    public void queryOrderAsync(@ProductType String type,@Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        if(!isInit.get()) {
            BillingEasyLog.e("请先初始化SDK");
            return;
        }
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            PurchaseBillingEasyListener listener = new PurchaseBillingEasyListener(callBack);
            billingHandler.queryOrderAsync(type,listener);
        }
    }

    @Override
    public void queryOrderLocal(@ProductType String type,@Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        if(!isInit.get()) {
            BillingEasyLog.e("请先初始化SDK");
            return;
        }
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            PurchaseBillingEasyListener listener = new PurchaseBillingEasyListener(callBack);
            billingHandler.queryOrderLocal(type,listener);
        }
    }

    @Override
    public void queryOrderHistory(@ProductType String type,@Nullable EasyCallBack<List<PurchaseHistoryInfo>> callBack) {
        if(!isInit.get()) {
            BillingEasyLog.e("请先初始化SDK");
            return;
        }
        if(billingHandler.connection(new ConnectionBillingEasyListener())){
            PurchaseHistoryBillingEasyListener listener = new PurchaseHistoryBillingEasyListener(callBack);
            billingHandler.queryOrderHistory(getTypeByHandler(type),listener);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(!isInit.get()) {
            BillingEasyLog.e("请先初始化SDK");
            return;
        }
        billingHandler.onActivityResult(requestCode,resultCode,data);
    }


    public void addListener(@NonNull BillingEasyListener listener) {
        if(!publicListenerList.contains(listener)){
            publicListenerList.add(listener);
        }
    }

    public void removeListener(@NonNull BillingEasyListener listener) {
        publicListenerList.remove(listener);
    }

    public void cleanListener(){
        publicListenerList.clear();
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
        public void onQueryOrder(@NonNull BillingEasyResult result,@NonNull String type, @NonNull List<PurchaseInfo> purchaseInfoList) {
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
        public void onQueryOrderHistory(@NonNull BillingEasyResult result,@NonNull String type, @NonNull List<PurchaseHistoryInfo> purchaseInfoList) {
            if(callback==null) return;
            callback.callback(result,purchaseInfoList);
        }
    }

    private static class ConnectionBillingEasyListener implements BillingEasyListener{

        @Nullable
        private EasyCallBack<Boolean> callback;

        public ConnectionBillingEasyListener(@Nullable EasyCallBack<Boolean> callback) {
            this.callback = callback;
        }

        public ConnectionBillingEasyListener() {
        }

        @Override
        public void onConnection(@NonNull BillingEasyResult result) {
            if(callback==null) return;
            callback.callback(result,result.isSuccess);
        }
    }

    private static class MyBillingEasyListener implements BillingEasyListener{

        @Override
        public void onConnection(@NonNull BillingEasyResult result) {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onConnection(result);
            }
            if(result.isSuccess){
                BillingEasyLog.i("【onConnection】success");
                queryProductAll(null);
            }else{
                BillingEasyLog.e("【onConnection】reqCode:"+result.responseCode+",reqMsg:"+result.responseMsg);
            }
        }

        @Override
        public void onDisconnected() {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onDisconnected();
            }
        }

        @Override
        public void onQueryProduct(@NonNull BillingEasyResult result,@ProductType String type, @NonNull List<ProductInfo> productInfoList) {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onQueryProduct(result,type,productInfoList);
            }
            if(!result.isSuccess){
                BillingEasyLog.e("【onQueryProduct】reqCode:"+result.responseCode+",reqMsg:"+result.responseMsg);
            }else{
                BillingEasyLog.i("【onQueryProduct】success");
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
            if(!result.isSuccess){
                BillingEasyLog.e("【onPurchases】reqCode:"+result.responseCode+",reqMsg:"+result.responseMsg);
            }else{
                BillingEasyLog.i("【onPurchases】success");
            }
        }

        @Override
        public void onConsume(@NonNull BillingEasyResult result, @NonNull String purchaseToken) {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onConsume(result, purchaseToken);
            }
            if(!result.isSuccess){
                BillingEasyLog.e("【onConsume】reqCode:"+result.responseCode+",reqMsg:"+result.responseMsg);
            }else{
                BillingEasyLog.i("【onConsume】success");
            }
        }

        @Override
        public void onAcknowledge(@NonNull BillingEasyResult result, @NonNull String purchaseToken) {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onAcknowledge(result, purchaseToken);
            }
            if(!result.isSuccess){
                BillingEasyLog.e("【onAcknowledge】reqCode:"+result.responseCode+",reqMsg:"+result.responseMsg);
            }else{
                BillingEasyLog.i("【onAcknowledge】success");
            }
        }

        @Override
        public void onQueryOrder(@NonNull BillingEasyResult result,@NonNull String type, @NonNull List<PurchaseInfo> purchaseInfoList) {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onQueryOrder(result,type, purchaseInfoList);
            }
        }

        @Override
        public void onQueryOrderHistory(@NonNull BillingEasyResult result,@NonNull String type, @NonNull List<PurchaseHistoryInfo> purchaseInfoList) {
            for (BillingEasyListener listener : publicListenerList) {
                listener.onQueryOrderHistory(result,type,purchaseInfoList);
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

    public static List<String> getTypeListAll(){
        List<String> list = new ArrayList<>();
        if(billingHandler==null) return list;
        if(Objects.equals(billingHandler.getBillingName(), BillingName.GOOGLE)){
            list.add(ProductType.TYPE_INAPP_CONSUMABLE);
            list.add(ProductType.TYPE_SUBS);
        }else{
            list.add(ProductType.TYPE_INAPP_CONSUMABLE);
            list.add(ProductType.TYPE_SUBS);
            list.add(ProductType.TYPE_INAPP_NON_CONSUMABLE);
        }
        return list;
    }

    @NonNull
    private static String getTypeByHandler(@ProductType @NonNull String type){
        if(billingHandler==null) return "";
        return billingHandler.getProductType(type);
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
