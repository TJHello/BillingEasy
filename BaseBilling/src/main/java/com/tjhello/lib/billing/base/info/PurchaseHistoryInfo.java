package com.tjhello.lib.billing.base.info;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class PurchaseHistoryInfo {

    final List<ProductConfig> productList = new ArrayList<>();
    String purchaseToken ;
    long purchaseTime ;
    Object baseObj;
    String json ;

    @Nullable
    GoogleBillingPurchaseHistory googleBillingPurchaseHistory;

    @Nullable
    HuaweiBillingPurchaseHistory huaweiBillingPurchaseHistory;

    @Nullable
    public GoogleBillingPurchaseHistory getGoogleBillingPurchaseHistory() {
        return googleBillingPurchaseHistory;
    }

    public void setGoogleBillingPurchaseHistory(@Nullable GoogleBillingPurchaseHistory googleBillingPurchaseHistory) {
        this.googleBillingPurchaseHistory = googleBillingPurchaseHistory;
    }

    @Nullable
    public HuaweiBillingPurchaseHistory getHuaweiBillingPurchaseHistory() {
        return huaweiBillingPurchaseHistory;
    }

    public void setHuaweiBillingPurchaseHistory(@Nullable HuaweiBillingPurchaseHistory huaweiBillingPurchaseHistory) {
        this.huaweiBillingPurchaseHistory = huaweiBillingPurchaseHistory;
    }

    public static class GoogleBillingPurchaseHistory{
        String developerPayload;
        String originalJson ;
        long purchaseTime ;
        String purchaseToken;
        int quantity;
        String signature;
        List<String> skus;

        public String getDeveloperPayload() {
            return developerPayload;
        }

        public void setDeveloperPayload(String developerPayload) {
            this.developerPayload = developerPayload;
        }

        public String getOriginalJson() {
            return originalJson;
        }

        public void setOriginalJson(String originalJson) {
            this.originalJson = originalJson;
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
    }

    public static class HuaweiBillingPurchaseHistory{
        private String applicationId ;
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

        public String getApplicationId() {
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

        public void setApplicationId(String applicationId) {
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

    public long getPurchaseTime() {
        return purchaseTime;
    }

    public void setPurchaseTime(long purchaseTime) {
        this.purchaseTime = purchaseTime;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public void addProduct(ProductConfig productConfig){
        productList.add(productConfig);
    }

    public List<ProductConfig> getProductList(){
        return productList;
    }


}
