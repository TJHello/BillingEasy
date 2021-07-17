package com.tjhello.lib.billing.base.info;

import java.util.List;

public class PurchaseInfo {

    boolean isValid = false;
    List<String> codeList ;
    String orderId ;
    String purchaseToken ;
    int purchaseState ;
    GoogleBillingPurchase googleBillingPurchase;
    Object baseObj;

    public class GoogleBillingPurchase{
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

    public GoogleBillingPurchase getGoogleBillingPurchase() {
        return googleBillingPurchase;
    }

    public void setGoogleBillingPurchase(GoogleBillingPurchase googleBillingPurchase) {
        this.googleBillingPurchase = googleBillingPurchase;
    }

    public List<String> getCodeList() {
        return codeList;
    }

    public void setCodeList(List<String> codeList) {
        this.codeList = codeList;
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

    public int getPurchaseState() {
        return purchaseState;
    }

    public void setPurchaseState(int purchaseState) {
        this.purchaseState = purchaseState;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
