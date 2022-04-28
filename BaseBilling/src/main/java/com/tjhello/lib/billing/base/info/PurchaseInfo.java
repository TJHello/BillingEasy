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
    long purchaseTime ;

    @Nullable
    GoogleBillingPurchase googleBillingPurchase;

    @Nullable
    HuaweiBillingPurchase huaweiBillingPurchase;

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
    }

    public static class HuaweiBillingPurchase{
        private long applicationId ;
        private boolean isAutoRenewing ;
        private String orderId ;
        private String packageName ;
        private String productId ;
        private String productName ;
        private long purchaseTime ;
        private int purchaseState ;
        private String developerPayload ;
        private String purchaseToken ;
        private int purchaseType ;
        private String currency ;
        private long price ;
        private String country;
        private String lastOrderId ;
        private String productGroup ;
        private long oriPurchaseTime ;
        private String subscriptionId ;
        private int quantity;
        private long daysLasted ;
        private long numOfPeriods ;
        private long numOfDiscount ;
        private long expirationDate ;
        private int expirationIntent;
        private int retryFlag ;
        private int introductoryFlag ;
        private int trialFlag ;

        public String getProductName() {
            return productName;
        }

        public String getCurrency() {
            return currency;
        }

        public String getProductId() {
            return productId;
        }

        public int getExpirationIntent() {
            return expirationIntent;
        }

        public int getIntroductoryFlag() {
            return introductoryFlag;
        }

        public int getPurchaseState() {
            return purchaseState;
        }

        public int getPurchaseType() {
            return purchaseType;
        }

        public int getRetryFlag() {
            return retryFlag;
        }

        public int getTrialFlag() {
            return trialFlag;
        }

        public long getApplicationId() {
            return applicationId;
        }

        public long getDaysLasted() {
            return daysLasted;
        }

        public long getExpirationDate() {
            return expirationDate;
        }

        public long getPurchaseTime() {
            return purchaseTime;
        }

        public long getNumOfDiscount() {
            return numOfDiscount;
        }

        public long getNumOfPeriods() {
            return numOfPeriods;
        }

        public long getOriPurchaseTime() {
            return oriPurchaseTime;
        }

        public String getLastOrderId() {
            return lastOrderId;
        }

        public String getOrderId() {
            return orderId;
        }

        public String getPackageName() {
            return packageName;
        }

        public String getProductGroup() {
            return productGroup;
        }

        public String getSubscriptionId() {
            return subscriptionId;
        }

        public void setPurchaseTime(long purchaseTime) {
            this.purchaseTime = purchaseTime;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public void setApplicationId(long applicationId) {
            this.applicationId = applicationId;
        }

        public void setAutoRenewing(boolean autoRenewing) {
            isAutoRenewing = autoRenewing;
        }

        public void setDaysLasted(long daysLasted) {
            this.daysLasted = daysLasted;
        }

        public void setDeveloperPayload(String developerPayload) {
            this.developerPayload = developerPayload;
        }

        public void setExpirationDate(long expirationDate) {
            this.expirationDate = expirationDate;
        }

        public void setExpirationIntent(int expirationIntent) {
            this.expirationIntent = expirationIntent;
        }

        public void setIntroductoryFlag(int introductoryFlag) {
            this.introductoryFlag = introductoryFlag;
        }

        public void setLastOrderId(String lastOrderId) {
            this.lastOrderId = lastOrderId;
        }

        public void setNumOfDiscount(long numOfDiscount) {
            this.numOfDiscount = numOfDiscount;
        }

        public void setNumOfPeriods(long numOfPeriods) {
            this.numOfPeriods = numOfPeriods;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public void setOriPurchaseTime(long oriPurchaseTime) {
            this.oriPurchaseTime = oriPurchaseTime;
        }

        public void setPackageName(String packageName) {
            this.packageName = packageName;
        }

        public void setProductGroup(String productGroup) {
            this.productGroup = productGroup;
        }

        public void setPurchaseState(int purchaseState) {
            this.purchaseState = purchaseState;
        }

        public void setPurchaseToken(String purchaseToken) {
            this.purchaseToken = purchaseToken;
        }

        public void setPurchaseType(int purchaseType) {
            this.purchaseType = purchaseType;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }

        public void setRetryFlag(int retryFlag) {
            this.retryFlag = retryFlag;
        }

        public void setSubscriptionId(String subscriptionId) {
            this.subscriptionId = subscriptionId;
        }

        public void setTrialFlag(int trialFlag) {
            this.trialFlag = trialFlag;
        }

        public String getPurchaseToken() {
            return purchaseToken;
        }

        public String getDeveloperPayload() {
            return developerPayload;
        }

        public int getQuantity() {
            return quantity;
        }

        public long getPrice() {
            return price;
        }

        public String getCountry() {
            return country;
        }

        public void setPrice(long price) {
            this.price = price;
        }

        public void setCountry(String country) {
            this.country = country;
        }
    }

    @Nullable
    public GoogleBillingPurchase getGoogleBillingPurchase() {
        return googleBillingPurchase;
    }

    public void setGoogleBillingPurchase(@Nullable GoogleBillingPurchase googleBillingPurchase) {
        this.googleBillingPurchase = googleBillingPurchase;
    }

    @Nullable
    public HuaweiBillingPurchase getHuaweiBillingPurchase() {
        return huaweiBillingPurchase;
    }

    public void setHuaweiBillingPurchase(@Nullable HuaweiBillingPurchase huaweiBillingPurchase) {
        this.huaweiBillingPurchase = huaweiBillingPurchase;
    }

    public void putProductInfo(String code, ProductInfo productInfo){
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

    public long getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(long purchaseTime) {
        this.purchaseTime = purchaseTime;
    }
}
