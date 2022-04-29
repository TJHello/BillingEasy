package com.tjhello.lib.billing.base.info;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PurchaseInfo {

    boolean isValid = false;
    final List<ProductConfig> productList = new ArrayList<>();
    String orderId ;
    String purchaseToken ;
    GoogleBillingPurchase googleBillingPurchase;
    private final Map<String,ProductInfo> productInfoMap = new HashMap<>();
    private boolean isAcknowledged = false;
    private boolean isAutoRenewing = false;
    Object baseObj;

    public static class GoogleBillingPurchase{
        String developerPayload;
        String orderId ;
        String originalJson ;
        String packageName ;
        int purchaseState ;
        long purchaseTime ;
        String purchaseToken;
        int quantity ;
        String signature;
        List<String> skus ;
        boolean isAcknowledged;
        boolean isAutoRenewing;
        public String obfuscatedAccountId;
        public String obfuscatedProfileId;

        public String getDeveloperPayload() {
            return developerPayload;
        }

        public void setDeveloperPayload(String developerPayload) {
            this.developerPayload = developerPayload;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getOriginalJson() {
            return originalJson;
        }

        public void setOriginalJson(String originalJson) {
            this.originalJson = originalJson;
        }

        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public int getPurchaseState() {
            return purchaseState;
        }

        public void setPurchaseState(int purchaseState) {
            this.purchaseState = purchaseState;
        }

        public long getPurchaseTime() {
            return purchaseTime;
        }

        public void setPurchaseTime(long purchaseTime) {
            this.purchaseTime = purchaseTime;
        }

        public String getPurchaseToken() {
            return purchaseToken;
        }

        public void setPurchaseToken(String purchaseToken) {
            this.purchaseToken = purchaseToken;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public List<String> getSkus() {
            return skus;
        }

        public void setSkus(List<String> skus) {
            this.skus = skus;
        }

        public boolean isAcknowledged() {
            return isAcknowledged;
        }

        public void setAcknowledged(boolean acknowledged) {
            isAcknowledged = acknowledged;
        }

        public boolean isAutoRenewing() {
            return isAutoRenewing;
        }

        public void setAutoRenewing(boolean autoRenewing) {
            isAutoRenewing = autoRenewing;
        }

        public void setObfuscatedAccountId(String obfuscatedAccountId) {
            this.obfuscatedAccountId = obfuscatedAccountId;
        }

        public void setObfuscatedProfileId(String obfuscatedProfileId) {
            this.obfuscatedProfileId = obfuscatedProfileId;
        }

        public String getObfuscatedProfileId() {
            return obfuscatedProfileId;
        }

        public String getObfuscatedAccountId() {
            return obfuscatedAccountId;
        }
    }

    public GoogleBillingPurchase getGoogleBillingPurchase() {
        return googleBillingPurchase;
    }

    public void setGoogleBillingPurchase(GoogleBillingPurchase googleBillingPurchase) {
        this.googleBillingPurchase = googleBillingPurchase;
    }

    public void putProductInfo(String code,ProductInfo productInfo){
        productInfoMap.put(code,productInfo);
    }

    @Nullable
    public ProductInfo getProductInfo(String code){
        if(productInfoMap.containsKey(code)){
            return productInfoMap.get(code);
        }
        return null;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }

    public Object getBaseObj() {
        return baseObj;
    }

    public void setBaseObj(Object baseObj) {
        this.baseObj = baseObj;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }

    public void addProduct(ProductConfig productConfig){
        productList.add(productConfig);
    }

    public List<ProductConfig> getProductList(){
        return productList;
    }

    public void setAcknowledged(boolean isAcknowledged){
        this.isAcknowledged = isAcknowledged;
    }

    public boolean isAcknowledged(){
        return isAcknowledged;
    }

    public boolean isAutoRenewing(){
        return isAutoRenewing;
    }

    public void setAutoRenewing(boolean isAutoRenewing){
        this.isAutoRenewing = isAutoRenewing;
    }
}
