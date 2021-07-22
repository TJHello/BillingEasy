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

    GoogleBillingPurchaseHistory googleBillingPurchaseHistory;

    public GoogleBillingPurchaseHistory getGoogleBillingPurchaseHistory() {
        return googleBillingPurchaseHistory;
    }

    public void setGoogleBillingPurchaseHistory(GoogleBillingPurchaseHistory googleBillingPurchaseHistory) {
        this.googleBillingPurchaseHistory = googleBillingPurchaseHistory;
    }

    public class GoogleBillingPurchaseHistory{
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
