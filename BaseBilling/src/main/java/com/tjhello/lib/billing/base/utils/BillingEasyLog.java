package com.tjhello.lib.billing.base.utils;

import android.util.Log;

import com.tjhello.lib.billing.base.info.BillingEasyResult;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseHistoryInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;

import java.util.Arrays;
import java.util.List;

public class BillingEasyLog {

    private static boolean IS_DEBUG = false;
    private static String TAG = "BillingEasyLog";

    public static void setVersionName(String versionName){
        TAG="BillingEasyLog-"+versionName;
    }

    public static void i(String log){
        if(!IS_DEBUG) return;
        Log.i(TAG,log);
    }
    public static void e(String log){
        if(!IS_DEBUG) return;
        Log.e(TAG,log);
    }

    public static void logProduct(String activityName,String methodName, BillingEasyResult result, List<ProductInfo> list){
        if(result.isSuccess){
            StringBuilder builder = new StringBuilder();
            builder.append("[").append(activityName).append("]").append("[").append(methodName).append("]:true\n");
            for (ProductInfo productInfo : list) {
                builder.append("{code=").append(productInfo.getCode()).append(",price=").append(productInfo.getPrice()).append("}\n");
            }
            i(builder.toString());
        }else{
            String builder = "["+activityName+"]"+"[" + methodName + "]:false\n" + "{resCode=" + result.responseCode + ",resMsg=" + result.responseMsg+"}";
            i(builder);
        }
    }

    public static void logPurchase(String activityName,String methodName, BillingEasyResult result, List<PurchaseInfo> list){
        if(result.isSuccess){
            StringBuilder builder = new StringBuilder();
            builder.append("[").append(activityName).append("]").append("[").append(methodName).append("]:true\n");
            for (PurchaseInfo info : list) {
                builder.append("{codeList=").append(Arrays.toString(info.getCodeList().toArray())).append("}\n");
            }
            i(builder.toString());
        }else{
            String builder = "["+activityName+"]"+"[" + methodName + "]:false\n" + "{resCode=" + result.responseCode + ",resMsg=" + result.responseMsg+"}";
            i(builder);
        }
    }

    public static void logPurchaseHistory(String activityName,String methodName, BillingEasyResult result, List<PurchaseHistoryInfo> list){
        if(result.isSuccess){
            StringBuilder builder = new StringBuilder();
            builder.append("[").append(activityName).append("]").append("[").append(methodName).append("]:true\n");
            for (PurchaseHistoryInfo info : list) {
                builder.append("{codeList=").append(Arrays.toString(info.getCodeList().toArray())).append("}\n");
            }
            i(builder.toString());
        }else{
            String builder = "["+activityName+"]"+"[" + methodName + "]:false\n" + "{resCode=" + result.responseCode + ",resMsg=" + result.responseMsg+"}";
            i(builder);
        }
    }

    public static void logResult(String activityName,String methodName,BillingEasyResult result){
        if(result.isSuccess){
            i("["+activityName+"]"+"["+methodName+"]:true");
        }else{
            String builder = "["+activityName+"]"+"[" + methodName + "]:false\n" + "{resCode=" + result.responseCode + ",resMsg=" + result.responseMsg+"}";
            i(builder);
        }
    }

    public static void logResult(String activityName,String methodName,BillingEasyResult result,String data){
        if(result.isSuccess){
            i("["+activityName+"]"+"["+methodName+"]:true:"+data);
        }else{
            String builder = "["+activityName+"]"+"[" + methodName + "]:false\n" + "{resCode=" + result.responseCode + ",resMsg=" + result.responseMsg+"}";
            i(builder);
        }
    }

    public static void setDebug(boolean bool){
        IS_DEBUG = bool;
    }
}
