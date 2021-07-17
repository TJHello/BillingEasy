package com.tjhello.lib.billing.base.info;

import androidx.annotation.Nullable;

public class BillingEasyResult {

    public boolean isSuccess = false;

    @Nullable
    public String responseMsg ;

    @Nullable
    public String responseCode ;

    @Nullable
    public Object baseObj;


    public static BillingEasyResult build(boolean isSuccess,String responseCode,String responseMsg,Object obj){
        BillingEasyResult result = new BillingEasyResult();
        result.isSuccess = isSuccess;
        result.responseMsg = responseMsg;
        result.responseCode = responseCode;
        result.baseObj = obj;
        return result;
    }

    public static BillingEasyResult build(boolean isSuccess,int responseCode,String responseMsg,Object obj){
        BillingEasyResult result = new BillingEasyResult();
        result.isSuccess = isSuccess;
        result.responseMsg = responseMsg;
        result.responseCode = ""+responseCode;
        result.baseObj = obj;
        return result;
    }

}
