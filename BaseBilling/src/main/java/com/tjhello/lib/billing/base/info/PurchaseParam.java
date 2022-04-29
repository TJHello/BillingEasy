package com.tjhello.lib.billing.base.info;

import androidx.annotation.NonNull;

public class PurchaseParam {

    @NonNull
    public String productCode;
    public String obfuscatedAccountId;
    public String obfuscatedProfileId;
    public boolean vrPurchaseFlow;

    public PurchaseParam(@NonNull String productCode){
        this.productCode = productCode;
    }

    public static class Builder{

        @NonNull
        public String productCode;
        public String obfuscatedAccountId;
        public String obfuscatedProfileId;
        public boolean vrPurchaseFlow;

        public Builder(@NonNull String productCode){
            this.productCode = productCode;
        }

        public Builder setObfuscatedAccountId(String data){
            obfuscatedAccountId = data;
            return this;
        }

        public Builder setObfuscatedProfileId(String data){
            obfuscatedProfileId = data;
            return this;
        }

        public Builder setVrPurchaseFlow(boolean bool){
            vrPurchaseFlow = bool;
            return this;
        }

        public PurchaseParam build(){
            PurchaseParam param = new PurchaseParam(productCode);
            param.obfuscatedAccountId = obfuscatedAccountId;
            param.obfuscatedProfileId = obfuscatedProfileId;
            param.vrPurchaseFlow = vrPurchaseFlow;
            return param;
        }
    }

}
