package com.tjhello.lib.billing.base.info;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class BillingEasyResult {

    /**
     * 是否成功
     */
    public boolean isSuccess = false;

    /**
     * 是否用户取消
     * @deprecated 改为判断State
     */
    public boolean isCancel = false;

    /**
     * 是否发生错误
     * @deprecated 改为判断State
     */
    public boolean isError = false;

    public State state ;

    /**
     * 发生错误，已拥有商品
     * @deprecated 改为判断State
     */
    public boolean isErrorOwned = false;

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


    @NonNull
    public static BillingEasyResult build(boolean isSuccess, String responseCode, String responseMsg, Object obj){
        BillingEasyResult result = new BillingEasyResult();
        result.isSuccess = isSuccess;
        result.responseMsg = responseMsg;
        result.responseCode = responseCode;
        result.baseObj = obj;
        return result;
    }

    @NonNull
    public static BillingEasyResult build(boolean isSuccess, int responseCode, String responseMsg, Object obj){
        BillingEasyResult result = new BillingEasyResult();
        result.isSuccess = isSuccess;
        result.responseMsg = responseMsg;
        result.responseCode = ""+responseCode;
        result.baseObj = obj;
        return result;
    }


    public enum State{
        SUCCESS,//成功
        CANCEL,//取消购买
        ERROR_OWNED,//错误-已拥有商品
        ERROR_OTHER,//错误-其他
        ERROR_NOT_OWNED//错误-未拥有该商品-无法被消耗
    }



}
