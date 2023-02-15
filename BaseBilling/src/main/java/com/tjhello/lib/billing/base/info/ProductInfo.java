package com.tjhello.lib.billing.base.info;

import androidx.annotation.Nullable;

import com.tjhello.lib.billing.base.anno.ProductType;


public class ProductInfo {

    String code ;
    String price ;
    @ProductType
    String type ;
    String json ;

    String title ;
    String desc ;
    long priceAmountMicros ;
    String priceCurrencyCode ;

    @Nullable
    GoogleSkuDetails googleSkuDetails ;
    @Nullable
    HuaweiProductInfo huaweiProductInfo ;

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

    public static class HuaweiProductInfo{
        private String productId ;//获取商品ID
        private int priceType ;//获取商品类型
        private String price ;//获取商品的展示价格
        private long microsPrice ;//获取商品微单位价格
        private String originalLocalPrice ;//获取商品的原价
        private long originalMicroPrice ;//获取商品原价的微单位价格
        private String currency ;//获取商品的币种
        private String productName ;//获取商品名称
        private String productDesc;//获取商品描述信息
        private String subPeriod ;//获取订阅型商品周期单位
        private String subSpecialPrice ;//获取订阅型商品优惠价格
        private long subSpecialPriceMicros;//获取优惠微单位价格
        private String subSpecialPeriod;//获取订阅型商品优惠周期单位
        private int subSpecialPeriodCycles;//获取订阅型商品优惠周期数
        private String subFreeTrialPeriod;//获取订阅型商品免费周期
        private String subGroupId;//获取订阅型商品所属订阅组ID
        private String subGroupTitle;//获取订阅型商品所属订阅组描述
        private int subProductLevel;//获取订阅型商品在订阅组里的级别
        private int status ;//获取商品的状态

        public String getProductId() {
            return productId;
        }

        public int getPriceType() {
            return priceType;
        }

        public String getPrice() {
            return price;
        }

        public long getMicrosPrice() {
            return microsPrice;
        }

        public String getOriginalLocalPrice() {
            return originalLocalPrice;
        }

        public long getOriginalMicroPrice() {
            return originalMicroPrice;
        }

        public String getCurrency() {
            return currency;
        }

        public String getProductName() {
            return productName;
        }

        public String getProductDesc() {
            return productDesc;
        }

        public String getSubPeriod() {
            return subPeriod;
        }

        public String getSubSpecialPrice() {
            return subSpecialPrice;
        }

        public int getSubProductLevel() {
            return subProductLevel;
        }

        public int getStatus() {
            return status;
        }

        public int getSubSpecialPeriodCycles() {
            return subSpecialPeriodCycles;
        }

        public long getSubSpecialPriceMicros() {
            return subSpecialPriceMicros;
        }

        public String getSubFreeTrialPeriod() {
            return subFreeTrialPeriod;
        }

        public String getSubGroupId() {
            return subGroupId;
        }

        public String getSubGroupTitle() {
            return subGroupTitle;
        }

        public String getSubSpecialPeriod() {
            return subSpecialPeriod;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public void setMicrosPrice(long microsPrice) {
            this.microsPrice = microsPrice;
        }

        public void setOriginalLocalPrice(String originalLocalPrice) {
            this.originalLocalPrice = originalLocalPrice;
        }

        public void setOriginalMicroPrice(long originalMicroPrice) {
            this.originalMicroPrice = originalMicroPrice;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public void setPriceType(int priceType) {
            this.priceType = priceType;
        }

        public void setProductDesc(String productDesc) {
            this.productDesc = productDesc;
        }

        public void setProductId(String productId) {
            this.productId = productId;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public void setSubFreeTrialPeriod(String subFreeTrialPeriod) {
            this.subFreeTrialPeriod = subFreeTrialPeriod;
        }

        public void setSubGroupId(String subGroupId) {
            this.subGroupId = subGroupId;
        }

        public void setSubGroupTitle(String subGroupTitle) {
            this.subGroupTitle = subGroupTitle;
        }

        public void setSubPeriod(String subPeriod) {
            this.subPeriod = subPeriod;
        }

        public void setSubProductLevel(int subProductLevel) {
            this.subProductLevel = subProductLevel;
        }

        public void setSubSpecialPeriod(String subSpecialPeriod) {
            this.subSpecialPeriod = subSpecialPeriod;
        }

        public void setSubSpecialPeriodCycles(int subSpecialPeriodCycles) {
            this.subSpecialPeriodCycles = subSpecialPeriodCycles;
        }

        public void setSubSpecialPrice(String subSpecialPrice) {
            this.subSpecialPrice = subSpecialPrice;
        }

        public void setSubSpecialPriceMicros(long subSpecialPriceMicros) {
            this.subSpecialPriceMicros = subSpecialPriceMicros;
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

    @Nullable
    public GoogleSkuDetails getGoogleSkuDetails() {
        return googleSkuDetails;
    }

    public void setGoogleSkuDetails(@Nullable GoogleSkuDetails googleSkuDetails) {
        this.googleSkuDetails = googleSkuDetails;
    }

    @Nullable
    public HuaweiProductInfo getHuaweiProductInfo() {
        return huaweiProductInfo;
    }

    public void setHuaweiProductInfo(@Nullable HuaweiProductInfo huaweiProductInfo) {
        this.huaweiProductInfo = huaweiProductInfo;
    }

    @Nullable
    public Object getBaseObj() {
        return baseObj;
    }

    public void setBaseObj(@Nullable Object baseObj) {
        this.baseObj = baseObj;
    }

    @Deprecated
    public long getPriceMicros() {
        return priceAmountMicros;
    }

    @Deprecated
    public void setPriceMicros(long priceMicros) {
        this.priceAmountMicros = priceMicros;
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

    public String getDesc() {
        return desc;
    }

    public String getTitle() {
        return title;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
