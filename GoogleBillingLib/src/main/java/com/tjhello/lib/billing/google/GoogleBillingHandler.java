package com.tjhello.lib.billing.google;

import android.app.Activity;

import androidx.annotation.Keep;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

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
import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.lib.billing.base.handler.BillingHandler;
import com.tjhello.lib.billing.base.info.BillingEasyResult;
import com.tjhello.lib.billing.base.info.ProductConfig;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;
import com.tjhello.lib.billing.base.info.PurchaseHistoryInfo;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;


import java.util.ArrayList;
import java.util.Collections;
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
@Keep
public class GoogleBillingHandler extends BillingHandler {

    private BillingClient mBillingClient ;
    private final PurchasesUpdatedListener mPurchasesListener = new MyPurchasesUpdatedListener(mBillingEasyListener);

    public GoogleBillingHandler(BillingEasyListener mBillingEasyListener) {
        super(mBillingEasyListener);
    }

    @Override
    public void onInit(@NonNull Activity activity) {
        BillingClient.Builder mBuilder = BillingClient.newBuilder(activity);
        mBuilder.enablePendingPurchases();
        mBillingClient = mBuilder.setListener(mPurchasesListener).build();
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
        if(productCodeList.isEmpty()) return;
        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                .setSkusList(productCodeList)
                .setType(type)
                .build();
        mBillingClient.querySkuDetailsAsync(params, new MySkuDetailsResponseListener(mBillingEasyListener));
    }

    @Override
    public void purchase(@NonNull Activity activity,@NonNull String productCode,@NonNull String type) {
        SkuDetailsParams params = SkuDetailsParams.newBuilder()
                .setSkusList(Collections.singletonList(productCode))
                .setType(type)
                .build();
        mBillingClient.querySkuDetailsAsync(params, (billingResult, list) -> {
            if(list!=null&&!list.isEmpty()){
                BillingFlowParams flowParams = BillingFlowParams.newBuilder()
                        .setSkuDetails(list.get(0))
                        .build();
                mBillingClient.launchBillingFlow(activity,flowParams);
            }
        });
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
    public void queryOrderAsync(@NonNull List<String> typeList,@NonNull BillingEasyListener listener) {
        for (String type : typeList) {
            mBillingClient.queryPurchasesAsync(type,new MyPurchasesResponseListener(listener));
        }
    }

    @Override
    public void queryOrderLocal(@NonNull List<String> typeList,@NonNull BillingEasyListener listener) {
        for (String type : typeList) {
            mBillingClient.queryPurchaseHistoryAsync(type,new MyPurchaseHistoryResponseListener(listener));
        }
    }

    @Override
    public void queryOrderHistory(@NonNull List<String> typeList,@NonNull BillingEasyListener listener) {
        for (String type : typeList) {
            Purchase.PurchasesResult purchasesResult = mBillingClient.queryPurchases(type);
            BillingResult billingResult = purchasesResult.getBillingResult();
            if(purchasesResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                listener.onQueryOrder(
                        BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),
                        toPurchaseInfo(purchasesResult.getPurchasesList())
                );
                mBillingEasyListener.onQueryOrder(
                        BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),
                        toPurchaseInfo(purchasesResult.getPurchasesList())
                );
            }else{
                listener.onQueryOrder(
                        BillingEasyResult.build(false,billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult),
                        toPurchaseInfo(purchasesResult.getPurchasesList())
                );
                mBillingEasyListener.onQueryOrder(
                        BillingEasyResult.build(false,billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult),
                        toPurchaseInfo(purchasesResult.getPurchasesList())
                 );
            }
        }
    }


    private class MyBillingClientStateListener implements BillingClientStateListener {

        private final BillingEasyListener mListener;

        public MyBillingClientStateListener(BillingEasyListener mListener) {
            this.mListener = mListener;
        }

        @Override
        public void onBillingServiceDisconnected() {
            mListener.onDisconnected();
            mBillingEasyListener.onDisconnected();
        }

        @Override
        public void onBillingSetupFinished(@NonNull BillingResult billingResult) {
            if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                mListener.onConnection(BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult));
                mBillingEasyListener.onConnection(BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult));
            }else{
                mListener.onConnection(BillingEasyResult.build(false,billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult));
                mBillingEasyListener.onConnection(BillingEasyResult.build(false,billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult));
            }
        }
    }

    private class MySkuDetailsResponseListener implements SkuDetailsResponseListener{

        private final BillingEasyListener mListener;
        MySkuDetailsResponseListener(BillingEasyListener listener){
            this.mListener = listener;
        }

        @Override
        public void onSkuDetailsResponse(@NonNull BillingResult billingResult, @Nullable List<SkuDetails> list) {
            if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                mListener.onQueryProduct(BillingEasyResult.build(true,
                        BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),toProductInfo(list));
                mBillingEasyListener.onQueryProduct(BillingEasyResult.build(true,
                        BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),toProductInfo(list));
            }else{
                mListener.onQueryProduct(BillingEasyResult.build(false,
                        billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult),toProductInfo(list));
                mBillingEasyListener.onQueryProduct(BillingEasyResult.build(false,
                        billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult),toProductInfo(list));
            }
        }

        private List<ProductInfo> toProductInfo(@Nullable List<SkuDetails> list){
            if(list==null||list.isEmpty()) return new ArrayList<>();
            List<ProductInfo> infoList = new ArrayList<>();
            for(int i=0;i<list.size();i++){
                SkuDetails skuDetails = list.get(i);
                ProductInfo info = new ProductInfo();
                info.setCode(skuDetails.getSku());
                info.setPrice(skuDetails.getPrice());
                ProductConfig find = findProductInfo(skuDetails.getSku());
                if(find!=null){
                    info.setType(find.getType());
                }
                ProductInfo.GoogleSkuDetails googleSkuDetails = new ProductInfo.GoogleSkuDetails();
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
                googleSkuDetails.setTitle(skuDetails.getType());
                info.setGoogleSkuDetails(googleSkuDetails);
                info.setJson(skuDetails.getOriginalJson());
                info.setBaseObj(skuDetails);
                infoList.add(info);
            }
            return infoList;
        }
    }

    private class MyPurchasesUpdatedListener implements PurchasesUpdatedListener{

        private final BillingEasyListener listener;

        public MyPurchasesUpdatedListener(BillingEasyListener listener) {
            this.listener = listener;
        }

        @Override
        public void onPurchasesUpdated(@NonNull BillingResult billingResult, @Nullable List<Purchase> list) {
            if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                listener.onPurchases(
                        BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),
                        toPurchaseInfo(list));
                mBillingEasyListener.onPurchases(
                        BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),
                        toPurchaseInfo(list));
            }else{
                listener.onPurchases(
                        BillingEasyResult.build(false,billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult),
                        toPurchaseInfo(list)
                );
                mBillingEasyListener.onPurchases(
                        BillingEasyResult.build(false,billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult),
                        toPurchaseInfo(list)
                );
            }
        }
    }

    private class MyConsumeResponseListener implements ConsumeResponseListener{

        private final BillingEasyListener listener;

        public MyConsumeResponseListener(BillingEasyListener listener) {
            this.listener = listener;
        }

        @Override
        public void onConsumeResponse(@NonNull BillingResult billingResult, @NonNull String purchaseToken) {
            if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                listener.onConsume(
                        BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),
                        purchaseToken);
                mBillingEasyListener.onConsume(
                        BillingEasyResult.build(false,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),
                        purchaseToken);
            }else{
                listener.onConsume(
                        BillingEasyResult.build(false,billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult),
                        purchaseToken
                );
                mBillingEasyListener.onConsume(
                        BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),
                        purchaseToken);
            }
        }
    }

    private class MyAcknowledgePurchaseResponseListener implements AcknowledgePurchaseResponseListener{

        private final BillingEasyListener listener;
        private final String purchaseToken ;

        public MyAcknowledgePurchaseResponseListener(String purchaseToken,BillingEasyListener listener) {
            this.purchaseToken = purchaseToken;
            this.listener = listener;
        }

        @Override
        public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
            if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                listener.onAcknowledge(
                        BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),
                        purchaseToken);
                mBillingEasyListener.onAcknowledge(
                        BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),
                        purchaseToken);
            }else{
                listener.onAcknowledge(
                        BillingEasyResult.build(false,billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult),
                        purchaseToken);
                mBillingEasyListener.onAcknowledge(
                        BillingEasyResult.build(false,billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult),
                        purchaseToken);
            }
        }
    }

    private class MyPurchasesResponseListener implements PurchasesResponseListener{

        private final BillingEasyListener listener;

        public MyPurchasesResponseListener(BillingEasyListener listener) {
            this.listener = listener;
        }

        @Override
        public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
            if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                listener.onQueryOrder(
                        BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),
                        toPurchaseInfo(list));
                mBillingEasyListener.onQueryOrder(
                        BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),
                        toPurchaseInfo(list));
            }else{
                listener.onQueryOrder(
                        BillingEasyResult.build(false,billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult),
                        toPurchaseInfo(list));
                mBillingEasyListener.onQueryOrder(
                        BillingEasyResult.build(false,billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult),
                        toPurchaseInfo(list));
            }
        }
    }

    private class MyPurchaseHistoryResponseListener implements PurchaseHistoryResponseListener{

        private final BillingEasyListener listener;

        public MyPurchaseHistoryResponseListener(BillingEasyListener listener) {
            this.listener = listener;
        }

        @Override
        public void onPurchaseHistoryResponse(@NonNull BillingResult billingResult, @Nullable List<PurchaseHistoryRecord> list) {
            if(billingResult.getResponseCode()==BillingClient.BillingResponseCode.OK){
                listener.onQueryOrderHistory(
                        BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),
                        toPurchaseHistoryInfo(list));
                mBillingEasyListener.onQueryOrderHistory(
                        BillingEasyResult.build(true,BillingClient.BillingResponseCode.OK,billingResult.getDebugMessage(),billingResult),
                        toPurchaseHistoryInfo(list));
            }else{
                listener.onQueryOrderHistory(
                        BillingEasyResult.build(false,billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult),
                        toPurchaseHistoryInfo(list));
                mBillingEasyListener.onQueryOrderHistory(
                        BillingEasyResult.build(false,billingResult.getResponseCode(),billingResult.getDebugMessage(),billingResult),
                        toPurchaseHistoryInfo(list));
            }
        }

        private List<PurchaseHistoryInfo> toPurchaseHistoryInfo(@Nullable List<PurchaseHistoryRecord> list){
            if(list==null||list.isEmpty()) return new ArrayList<>();
            List<PurchaseHistoryInfo> infoList = new ArrayList<>();
            for(int i=0;i<list.size();i++) {
                PurchaseHistoryRecord purchase = list.get(i);
                PurchaseHistoryInfo info = new PurchaseHistoryInfo();
                info.setCodeList(purchase.getSkus());
                info.setPurchaseToken(purchase.getPurchaseToken());
                info.setPurchaseTime(purchase.getPurchaseTime());
                info.setBaseObj(purchase);

                List<String> typeList = new ArrayList<>();
                for (String sku : purchase.getSkus()) {
                    ProductConfig config = findProductInfo(sku);
                    if(config!=null){
                        typeList.add(config.getType());
                    }
                }
                info.setTypeList(typeList);

                PurchaseHistoryInfo.GoogleBillingPurchaseHistory googleBillingPurchaseHistory = info.new GoogleBillingPurchaseHistory();
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

    private static List<PurchaseInfo> toPurchaseInfo(@Nullable List<Purchase> list){
        if(list==null||list.isEmpty()) return new ArrayList<>();
        List<PurchaseInfo> infoList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            Purchase purchase = list.get(i);
            PurchaseInfo info = new PurchaseInfo();
            info.setCodeList(purchase.getSkus());
            info.setOrderId(purchase.getOrderId());
            info.setPurchaseToken(purchase.getPurchaseToken());
            info.setBaseObj(purchase);

            List<String> typeList = new ArrayList<>();
            for (String sku : purchase.getSkus()) {
                ProductConfig config = findProductInfo(sku);
                if(config!=null){
                    typeList.add(config.getType());
                }
            }
            info.setTypeList(typeList);

            info.setValid(purchase.getPurchaseState()== Purchase.PurchaseState.PURCHASED);

            PurchaseInfo.GoogleBillingPurchase googleBillingPurchase = info.new GoogleBillingPurchase();
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

            info.setGoogleBillingPurchase(googleBillingPurchase);

            infoList.add(info);
        }
        return infoList;
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


}
