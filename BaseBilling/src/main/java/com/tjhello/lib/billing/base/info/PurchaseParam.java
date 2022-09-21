package com.tjhello.lib.billing.base.info;

import androidx.annotation.NonNull;

public class PurchaseParam {

    @NonNull
    public String productCode;
    public String obfuscatedAccountId;
    public String obfuscatedProfileId;
    public boolean vrPurchaseFlow;
    public String developerPayload;
    public String reservedInfor ;

    private PurchaseParam(@NonNull String productCode){
        this.productCode = productCode;
    }

    public static class DefBuilder{
        @NonNull
        public final String productCode;
        public DefBuilder(@NonNull String productCode){
            this.productCode = productCode;
        }

        public PurchaseParam build(){
            return new PurchaseParam(productCode);
        }
    }

    public static class GoogleBuilder extends DefBuilder{

        private String obfuscatedAccountId;
        private String obfuscatedProfileId;
        private boolean vrPurchaseFlow;

        public GoogleBuilder(@NonNull String productCode) {
            super(productCode);
        }
        public GoogleBuilder setObfuscatedAccountId(String data){
            obfuscatedAccountId = data;
            return this;
        }

        public GoogleBuilder setObfuscatedProfileId(String data){
            obfuscatedProfileId = data;
            return this;
        }

        public GoogleBuilder setVrPurchaseFlow(boolean bool){
            vrPurchaseFlow = bool;
            return this;
        }

        @Override
        public PurchaseParam build() {
            PurchaseParam param = new PurchaseParam(productCode);
            param.obfuscatedAccountId = obfuscatedAccountId;
            param.obfuscatedProfileId = obfuscatedProfileId;
            param.vrPurchaseFlow = vrPurchaseFlow;
            return param;
        }
    }

    public static class HuaweiBuilder extends DefBuilder{
        private String developerPayload;
        private String reservedInfor ;

        public HuaweiBuilder(@NonNull String productCode) {
            super(productCode);
        }

        public HuaweiBuilder setDeveloperPayload(String developerPayload){
            this.developerPayload = developerPayload;
            return this;
        }

        public HuaweiBuilder setReservedInfor(String reservedInfor){
            this.reservedInfor = reservedInfor;
            return this;
        }

        @Override
        public PurchaseParam build() {
            PurchaseParam param = new PurchaseParam(productCode);
            param.developerPayload = developerPayload;
            param.reservedInfor = reservedInfor;
            return param;
        }
    }
}
