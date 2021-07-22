package com.tjhello.lib.billing.base.info;

import androidx.annotation.Nullable;

import com.tjhello.lib.billing.base.anno.ProductType;

public class ProductInfo {

    String code ;
    String price ;
    @ProductType
    String type ;
    String json ;

    GoogleSkuDetails googleSkuDetails ;

    @Nullable
    Object baseObj;

    public static class GoogleSkuDetails{

        String description ;
        String freeTrialPeriod;
        String iconUrl ;
        String introductoryPrice;
        long introductoryPriceAmountMicros;
        int introductoryPriceCycles;
        String introductoryPricePeriod;
        String originalJson ;
        String originalPrice;
        long originalPriceAmountMicros ;
        String price ;
        long priceAmountMicros ;
        String priceCurrencyCode;
        String sku ;
        String subscriptionPeriod ;
        String title ;
        String type ;


        public String getOriginalPrice() {
            return originalPrice;
        }

        public void setOriginalPrice(String originalPrice) {
            this.originalPrice = originalPrice;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getFreeTrialPeriod() {
            return freeTrialPeriod;
        }

        public void setFreeTrialPeriod(String freeTrialPeriod) {
            this.freeTrialPeriod = freeTrialPeriod;
        }

        public String getIconUrl() {
            return iconUrl;
        }

        public void setIconUrl(String iconUrl) {
            this.iconUrl = iconUrl;
        }

        public String getIntroductoryPrice() {
            return introductoryPrice;
        }

        public void setIntroductoryPrice(String introductoryPrice) {
            this.introductoryPrice = introductoryPrice;
        }

        public long getIntroductoryPriceAmountMicros() {
            return introductoryPriceAmountMicros;
        }

        public void setIntroductoryPriceAmountMicros(long introductoryPriceAmountMicros) {
            this.introductoryPriceAmountMicros = introductoryPriceAmountMicros;
        }

        public int getIntroductoryPriceCycles() {
            return introductoryPriceCycles;
        }

        public void setIntroductoryPriceCycles(int introductoryPriceCycles) {
            this.introductoryPriceCycles = introductoryPriceCycles;
        }

        public String getIntroductoryPricePeriod() {
            return introductoryPricePeriod;
        }

        public void setIntroductoryPricePeriod(String introductoryPricePeriod) {
            this.introductoryPricePeriod = introductoryPricePeriod;
        }

        public String getOriginalJson() {
            return originalJson;
        }

        public void setOriginalJson(String originalJson) {
            this.originalJson = originalJson;
        }

        public long getOriginalPriceAmountMicros() {
            return originalPriceAmountMicros;
        }

        public void setOriginalPriceAmountMicros(long originalPriceAmountMicros) {
            this.originalPriceAmountMicros = originalPriceAmountMicros;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public long getPriceAmountMicros() {
            return priceAmountMicros;
        }

        public void setPriceAmountMicros(long priceAmountMicros) {
            this.priceAmountMicros = priceAmountMicros;
        }

        public String getPriceCurrencyCode() {
            return priceCurrencyCode;
        }

        public void setPriceCurrencyCode(String priceCurrencyCode) {
            this.priceCurrencyCode = priceCurrencyCode;
        }

        public String getSku() {
            return sku;
        }

        public void setSku(String sku) {
            this.sku = sku;
        }

        public String getSubscriptionPeriod() {
            return subscriptionPeriod;
        }

        public void setSubscriptionPeriod(String subscriptionPeriod) {
            this.subscriptionPeriod = subscriptionPeriod;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public GoogleSkuDetails getGoogleSkuDetails() {
        return googleSkuDetails;
    }

    public void setGoogleSkuDetails(GoogleSkuDetails googleSkuDetails) {
        this.googleSkuDetails = googleSkuDetails;
    }

    @Nullable
    public Object getBaseObj() {
        return baseObj;
    }

    public void setBaseObj(@Nullable Object baseObj) {
        this.baseObj = baseObj;
    }
}
