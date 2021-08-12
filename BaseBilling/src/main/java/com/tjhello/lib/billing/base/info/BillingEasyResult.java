package com.tjhello.lib.billing.base.info;

import androidx.annotation.Nullable;

public class BillingEasyResult {

    /**
     * 是否成功
     */
    public boolean isSuccess = false;

    /**
     * 是否用户取消
     */
    public boolean isCancel = false;

    /**
     * 是否发生错误
     */
    public boolean isError = false;

    /**
     * 返回信息
     */
    @Nullable
    public String responseMsg ;

    /**
     * 返回码
     */
    @Nullable
    public String responseCode ;

    /**
     * 原始对象
     */
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
