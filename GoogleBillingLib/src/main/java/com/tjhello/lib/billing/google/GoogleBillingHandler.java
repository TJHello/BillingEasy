package com.tjhello.lib.billing.google;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.android.billingclient.api.AccountIdentifiers;
import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchaseHistoryRecord;
import com.android.billingclient.api.PurchaseHistoryResponseListener;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.PurchasesUpdatedListener;
import com.android.billingclient.api.SkuDetails;
import com.android.billingclient.api.SkuDetailsParams;
import com.android.billingclient.api.SkuDetailsResponseListener;
import com.tjhello.lib.billing.base.anno.BillingName;
import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.lib.billing.base.handler.BillingHandler;
import com.tjhello.lib.billing.base.info.BillingEasyResult;
import com.tjhello.lib.billing.base.info.ProductConfig;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseHistoryInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;
import com.tjhello.lib.billing.base.info.PurchaseParam;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;
import com.tjhello.lib.billing.base.utils.BillingEasyLog;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
@Keep
public class GoogleBillingHandler extends BillingHandler {

    private BillingClient mBillingClient ;
    private final PurchasesUpdatedListener mPurchasesListener = new MyPurchasesUpdatedListener();
    private final static Map<String, SkuDetails> skuDetailsMap = new HashMap<>();
    private final static Handler handler = new Handler(Looper.getMainLooper());

    public GoogleBillingHandler(BillingEasyListener mBillingEasyListener) {
        super(mBillingEasyListener);
    }

    @Override
    public void onInit(@NonNull Activity activity) {

        BillingClient.Builder mBuilder = BillingClient.newBuilder(activity)
                .enablePendingPurchases()
                .setListener(mPurchasesListener);
        mBillingClient = mBuilder.build();
    }

    @Override
    public boolean connection(@NonNull BillingEasyListener listener) {
        if(!mBillingClient.isReady()){
            mBillingClient.startConnection(new MyBillingClientStateListener(listener));
            return true;
        }
        return true;
    }

    @Override
    public void queryProduct(@NonNull List<String> productCodeList,@NonNull String type,@NonNull BillingEasyListener listener) {
        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                .setSkusList(productCodeList)
                .setType(type)
                .build();
        mBillingClient.querySkuDetailsAsync(params, new MySkuDetailsResponseListener(listener));
    }

    @Override
    public void purchase(@NonNull Activity activity, @NonNull PurchaseParam param, @NonNull String type) {
        if(skuDetailsMap.containsKey(param.productCode)){
            SkuDetails skuDetails = skuDetailsMap.get(param.productCode);
            if(skuDetails!=null){
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(skuDetails)
                        .setObfuscatedAccountId(param.obfuscatedAccountId)
                        .setObfuscatedProfileId(param.obfuscatedProfileId)
                        .setVrPurchaseFlow(param.vrPurchaseFlow)
                        .build();
                mBillingClient.launchBillingFlow(activity,flowParams);
            }
        }else{
            SkuDetailsParams params = SkuDetailsParams.newBuilder()
                    .setSkusList(Collections.singletonList(param.productCode))
                    .setType(type)
                    .build();
            mBillingClient.querySkuDetailsAsync(params, (billingResult, list) -> {
                if(billingResult.getResponseCode()== BillingClient.BillingResponseCode.OK&&list!=null&&!list.isEmpty()){
                    BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                            .setSkuDetails(list.get(0))
                            .setObfuscatedAccountId(param.obfuscatedAccountId)
                            .setObfuscatedProfileId(param.obfuscatedProfileId)
                            .setVrPurchaseFlow(param.vrPurchaseFlow)
                            .build();
                    for (SkuDetails skuDetails : list) {
                        skuDetailsMap.put(skuDetails.getSku(),skuDetails);
                    }
                    mBillingClient.launchBillingFlow(activity,flowParams);
                }else{
                    String msg = "获取商品详情失败:"+param.productCode+",code="+billingResult.getResponseCode()+",msg="+billingResult.getDebugMessage();
                    BillingEasyLog.e("[GoogleBilling]:"+msg);
                    runMainThread(() -> {
                        mBillingEasyListener.onPurchases(buildResult(billingResult,msg),new ArrayList<>());
                    });
                }
            });
        }
    }

    @Override
    public void consume(@NonNull String purchaseToken,@NonNull BillingEasyListener listener) {
        ConsumeParams params = ConsumeParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build();
        mBillingClient.consumeAsync(params, new MyConsumeResponseListener(listener));
    }

    @Override
    public void acknowledge(@NonNull String purchaseToken,@NonNull BillingEasyListener listener) {
        AcknowledgePurchaseParams params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchaseToken)
                .build();
        mBillingClient.acknowledgePurchase(params, new MyAcknowledgePurchaseResponseListener(purchaseToken, listener));
    }

    @Override
    public void queryOrderAsync(@ProductType @NonNull String type,@NonNull BillingEasyListener listener) {
        mBillingClient.queryPurchasesAsync(getProductType(type),new MyPurchasesResponseListener(type,listener));

    }

    @Override
    public void queryOrderLocal(@ProductType @NonNull String type,@NonNull BillingEasyListener listener) {
        Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(getProductType(type));
        BillingResult billingResult = purchasesResult.getBillingResult();
        BillingEasyResult result = buildResult(billingResult);
        List<PurchaseInfo> tempList = toPurchaseInfo(purchasesResult.getPurchasesList());
        listener.onQueryOrder(result,type,tempList );
        mBillingEasyListener.onQueryOrder(result,type, tempList);
    }

    @Override
    public void queryOrderHistory(@ProductType @NonNull String type,@NonNull BillingEasyListener listener) {
        mBillingClient.queryPurchaseHistoryAsync(getProductType(type),new MyPurchaseHistoryResponseListener(type,listener));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

    }

    @Override
    @BillingName
    public String getBillingName() {
        return BillingName.GOOGLE;
    }

    private class MyBillingClientStateListener implements BillingClientStateListener {

        private final BillingEasyListener mListener;

        public MyBillingClientStateListener(BillingEasyListener mListener) {
            this.mListener = mListener;
        }

        @Override
        public void onBillingServiceDisconnected() {
            runMainThread(() -> {
                mListener.onDisconnected();
                mBillingEasyListener.onDisconnected();
            });
        }

        @Override
        public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
            runMainThread(() -> {
                BillingEasyResult result = buildResult(billingResult);
                mListener.onConnection(result);
                mBillingEasyListener.onConnection(result);
            });
        }
    }

    private class MySkuDetailsResponseListener implements SkuDetailsResponseListener {

        private final BillingEasyListener mListener;
        MySkuDetailsResponseListener(BillingEasyListener listener){
            this.mListener = listener;
        }

        @Override
        public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
            runMainThread(() -> {
                //存储SkuDetails缓存，用于发起购买的时候
                if(list!=null&&!list.isEmpty()){
                    for (SkuDetails skuDetails : list) {
                        skuDetailsMap.put(skuDetails.getSku(),skuDetails);
                    }
                }
                BillingEasyResult result = buildResult(billingResult);
                List<ProductInfo> tempList = toProductInfo(list);
                mListener.onQueryProduct(result,tempList);
                mBillingEasyListener.onQueryProduct(result,tempList);
            });
        }
    }

    private class MyPurchasesUpdatedListener implements PurchasesUpdatedListener{


        @Override
        public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
            runMainThread(() -> {
                mBillingEasyListener.onPurchases(buildResult(billingResult),toPurchaseInfo(list));
            });
        }
    }

    private class MyConsumeResponseListener implements ConsumeResponseListener {

        private final BillingEasyListener listener;

        public MyConsumeResponseListener(BillingEasyListener listener) {
            this.listener = listener;
        }

        @Override
        public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String purchaseToken) {
            runMainThread(() -> {
                BillingEasyResult result = buildResult(billingResult);
                listener.onConsume(result,purchaseToken);
                mBillingEasyListener.onConsume(result,purchaseToken);
            });
        }
    }

    private class MyAcknowledgePurchaseResponseListener implements AcknowledgePurchaseResponseListener {

        private final BillingEasyListener listener;
        private final String purchaseToken ;

        public MyAcknowledgePurchaseResponseListener(String purchaseToken,BillingEasyListener listener) {
            this.purchaseToken = purchaseToken;
            this.listener = listener;
        }

        @Override
        public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
            runMainThread(() -> {
                BillingEasyResult result = buildResult(billingResult);
                listener.onAcknowledge(result,purchaseToken);
                mBillingEasyListener.onAcknowledge(result,purchaseToken);
            });
        }
    }

    private class MyPurchasesResponseListener implements PurchasesResponseListener {

        private final BillingEasyListener listener;
        private final String type;

        public MyPurchasesResponseListener(String type,BillingEasyListener listener) {
            this.listener = listener;
            this.type = type;
        }

        @Override
        public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
            runMainThread(() -> {
                BillingEasyResult result = buildResult(billingResult);
                List<PurchaseInfo> tempList = toPurchaseInfo(list);
                listener.onQueryOrder(result,type,tempList);
                mBillingEasyListener.onQueryOrder(result,type, tempList);
            });
        }
    }

    private class MyPurchaseHistoryResponseListener implements PurchaseHistoryResponseListener {

        private final BillingEasyListener listener;
        private final String type ;

        public MyPurchaseHistoryResponseListener(String type,BillingEasyListener listener) {
            this.listener = listener;
            this.type = type;
        }

        @Override
        public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @Nullable List<PurchaseHistoryRecord> list) {
            runMainThread(() -> {
                BillingEasyResult result = buildResult(billingResult);
                List<PurchaseHistoryInfo> tempList = toPurchaseHistoryInfo(list);
                listener.onQueryOrderHistory(result,type, tempList);
                mBillingEasyListener.onQueryOrderHistory(result,type, tempList);
            });
        }

        private List<PurchaseHistoryInfo> toPurchaseHistoryInfo(@Nullable List<PurchaseHistoryRecord> list){
            if(list==null||list.isEmpty()) return new ArrayList<>();
            List<PurchaseHistoryInfo> infoList = new ArrayList<>();
            for(int i=0;i<list.size();i++) {
                PurchaseHistoryRecord purchase = list.get(i);
                PurchaseHistoryInfo info = new PurchaseHistoryInfo();
                info.setPurchaseToken(purchase.getPurchaseToken());
                info.setPurchaseTime(purchase.getPurchaseTime());
                info.setBaseObj(purchase);

                for (String sku : purchase.getSkus()) {
                    ProductConfig productConfig = findProductInfo(sku);
                    if(productConfig!=null){
                        info.addProduct(productConfig);
                    }
                }


                PurchaseHistoryInfo.GoogleBillingPurchaseHistory googleBillingPurchaseHistory = new PurchaseHistoryInfo.GoogleBillingPurchaseHistory();
                googleBillingPurchaseHistory.setDeveloperPayload(purchase.getDeveloperPayload());
                googleBillingPurchaseHistory.setOriginalJson(purchase.getOriginalJson());
                googleBillingPurchaseHistory.setPurchaseTime(purchase.getPurchaseTime());
                googleBillingPurchaseHistory.setPurchaseToken(purchase.getPurchaseToken());
                googleBillingPurchaseHistory.setQuantity(purchase.getQuantity());
                googleBillingPurchaseHistory.setSignature(purchase.getSignature());
                googleBillingPurchaseHistory.setSkus(purchase.getSkus());

                info.setGoogleBillingPurchaseHistory(googleBillingPurchaseHistory);
                infoList.add(info);
            }
            return infoList;
        }
    }

    private static List<ProductInfo> toProductInfo(@Nullable List<SkuDetails> list){
        if(list==null||list.isEmpty()) return new ArrayList<>();
        List<ProductInfo> infoList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            SkuDetails skuDetails = list.get(i);
            ProductInfo info = toProductInfo(skuDetails);
            infoList.add(info);
        }
        return infoList;
    }

    private static ProductInfo toProductInfo(SkuDetails skuDetails){
        ProductInfo info = new ProductInfo();
        info.setCode(skuDetails.getSku());
        info.setPrice(skuDetails.getPrice());
        ProductConfig find = findProductInfo(skuDetails.getSku());
        if(find!=null){
            info.setType(find.getType());
        }
        info.setPriceMicros(skuDetails.getPriceAmountMicros());
        info.setPriceAmountMicros(skuDetails.getPriceAmountMicros());
        info.setPriceCurrencyCode(skuDetails.getPriceCurrencyCode());
        info.setTitle(skuDetails.getTitle());
        info.setDesc(skuDetails.getDescription());

        ProductInfo.GoogleSkuDetails googleSkuDetails = new ProductInfo.GoogleSkuDetails();
        googleSkuDetails.setType(skuDetails.getType());
        googleSkuDetails.setDescription(skuDetails.getDescription());
        googleSkuDetails.setFreeTrialPeriod(skuDetails.getFreeTrialPeriod());
        googleSkuDetails.setIconUrl(skuDetails.getIconUrl());
        googleSkuDetails.setIntroductoryPrice(skuDetails.getIntroductoryPrice());
        googleSkuDetails.setIntroductoryPriceAmountMicros(skuDetails.getIntroductoryPriceAmountMicros());
        googleSkuDetails.setIntroductoryPriceCycles(skuDetails.getIntroductoryPriceCycles());
        googleSkuDetails.setIntroductoryPricePeriod(skuDetails.getIntroductoryPricePeriod());
        googleSkuDetails.setOriginalJson(skuDetails.getOriginalJson());
        googleSkuDetails.setOriginalPrice(skuDetails.getOriginalPrice());
        googleSkuDetails.setOriginalPriceAmountMicros(skuDetails.getPriceAmountMicros());
        googleSkuDetails.setPrice(skuDetails.getPrice());
        googleSkuDetails.setPriceAmountMicros(skuDetails.getPriceAmountMicros());
        googleSkuDetails.setPriceCurrencyCode(skuDetails.getPriceCurrencyCode());
        googleSkuDetails.setSku(skuDetails.getSku());
        googleSkuDetails.setSubscriptionPeriod(skuDetails.getSubscriptionPeriod());
        googleSkuDetails.setTitle(skuDetails.getTitle());
        info.setGoogleSkuDetails(googleSkuDetails);
        info.setJson(skuDetails.getOriginalJson());
        info.setBaseObj(skuDetails);
        return info;
    }

    private static List<PurchaseInfo> toPurchaseInfo(@Nullable List<Purchase> list){
        if(list==null||list.isEmpty()) return new ArrayList<>();
        List<PurchaseInfo> infoList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Purchase purchase = list.get(i);
            PurchaseInfo info = new PurchaseInfo();

            for (String sku : purchase.getSkus()) {
                ProductConfig productConfig = findProductInfo(sku);
                if(productConfig!=null){
                    info.addProduct(productConfig);
                }else{
                    BillingEasyLog.e("未找到该商品配置，请检查:"+sku);
                }
                if(skuDetailsMap.containsKey(sku)){
                    SkuDetails skuDetails = skuDetailsMap.get(sku);
                    if(skuDetails!=null){
                        ProductInfo productInfo = toProductInfo(skuDetails);
                        info.putProductInfo(sku,productInfo);
                    }
                }
            }
            info.setPurchaseTime(purchase.getPurchaseTime());
            info.setOrderId(purchase.getOrderId());
            info.setPurchaseToken(purchase.getPurchaseToken());
            info.setBaseObj(purchase);
            info.setAcknowledged(purchase.isAcknowledged());
            info.setAutoRenewing(purchase.isAutoRenewing());
            info.setValid(purchase.getPurchaseState() == Purchase.PurchaseState.PURCHASED);

            PurchaseInfo.GoogleBillingPurchase googleBillingPurchase = new PurchaseInfo.GoogleBillingPurchase();
            googleBillingPurchase.setDeveloperPayload(purchase.getDeveloperPayload());
            googleBillingPurchase.setOrderId(purchase.getOrderId());
            googleBillingPurchase.setOriginalJson(purchase.getOriginalJson());
            googleBillingPurchase.setPackageName(purchase.getPackageName());
            googleBillingPurchase.setPurchaseState(purchase.getPurchaseState());
            googleBillingPurchase.setPurchaseTime(purchase.getPurchaseTime());
            googleBillingPurchase.setPurchaseToken(purchase.getPurchaseToken());
            googleBillingPurchase.setQuantity(purchase.getQuantity());
            googleBillingPurchase.setSignature(purchase.getSignature());
            googleBillingPurchase.setSkus(purchase.getSkus());
            googleBillingPurchase.setAutoRenewing(purchase.isAutoRenewing());
            googleBillingPurchase.setAcknowledged(purchase.isAcknowledged());

            AccountIdentifiers accountIdentifiers = purchase.getAccountIdentifiers();
            if(accountIdentifiers!=null){
                googleBillingPurchase.setObfuscatedAccountId(accountIdentifiers.getObfuscatedAccountId());
                googleBillingPurchase.setObfuscatedProfileId(accountIdentifiers.getObfuscatedProfileId());
            }

            info.setGoogleBillingPurchase(googleBillingPurchase);

            infoList.add(info);
        }
        return infoList;
    }

    private void runMainThread(Runnable runnable){
        handler.post(runnable);
    }

    private BillingEasyResult buildResult(@NonNull BillingResult billingResult){
        boolean isSuccess = billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK;
        boolean isCancel = billingResult.getResponseCode()==BillingClient.BillingResponseCode.USER_CANCELED;
        boolean isErrorOwned = billingResult.getResponseCode()==BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED;
        boolean isErrorNotOwned = billingResult.getResponseCode()==BillingClient.BillingResponseCode.ITEM_NOT_OWNED;
        BillingEasyResult result = BillingEasyResult.build(isSuccess,
                billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult);
        result.isCancel = isCancel;
        result.isError = !isSuccess&&!isCancel;
        result.isErrorOwned = isErrorOwned;
        if(isSuccess){
            result.state = BillingEasyResult.State.SUCCESS;
        }else if(isCancel){
            result.state = BillingEasyResult.State.CANCEL;
        }else if(isErrorOwned){
            result.state = BillingEasyResult.State.ERROR_OWNED;
        }else if(isErrorNotOwned){
            result.state = BillingEasyResult.State.ERROR_NOT_OWNED;
        }else{
            result.state = BillingEasyResult.State.ERROR_OTHER;
        }
        return result;
    }

    private BillingEasyResult buildResult(@NonNull BillingResult billingResult,String msg){
        boolean isSuccess = billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK;
        boolean isCancel = billingResult.getResponseCode()==BillingClient.BillingResponseCode.USER_CANCELED;
        boolean isErrorOwned = billingResult.getResponseCode()==BillingClient.BillingResponseCode.ITEM_ALREADY_OWNED;
        boolean isErrorNotOwned = billingResult.getResponseCode()==BillingClient.BillingResponseCode.ITEM_NOT_OWNED;
        BillingEasyResult result = BillingEasyResult.build(isSuccess,
                billingResult.getResponseCode(),msg,billingResult);
        result.isCancel = isCancel;
        result.isError = !isSuccess&&!isCancel;
        result.isErrorOwned =isErrorOwned;
        if(isSuccess){
            result.state = BillingEasyResult.State.SUCCESS;
        }else if(isCancel){
            result.state = BillingEasyResult.State.CANCEL;
        }else if(isErrorOwned){
            result.state = BillingEasyResult.State.ERROR_OWNED;
        }else if(isErrorNotOwned){
            result.state = BillingEasyResult.State.ERROR_NOT_OWNED;
        }else{
            result.state = BillingEasyResult.State.ERROR_OTHER;
        }
        return result;
    }

    @NonNull
    @Override
    public String getProductType(@ProductType String type) {
        switch (type){
            case ProductType.TYPE_INAPP_CONSUMABLE:
            case ProductType.TYPE_INAPP_NON_CONSUMABLE:
                return BillingClient.SkuType.INAPP;
            default:return BillingClient.SkuType.SUBS;
        }
    }

    @NonNull
    @Override
    public ProductConfig getProductConfig(@NonNull String productCode, String type) {
        if(Objects.equals(type, BillingClient.SkuType.INAPP)){
            return ProductConfig.build(ProductType.TYPE_INAPP_CONSUMABLE,productCode);
        }else{
            return ProductConfig.build(ProductType.TYPE_SUBS,productCode);
        }
    }

}
