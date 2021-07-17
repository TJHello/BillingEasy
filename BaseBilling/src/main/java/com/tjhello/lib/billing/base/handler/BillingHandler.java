package com.tjhello.lib.billing.base.handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.lib.billing.base.imp.BillingHandlerImp;
import com.tjhello.lib.billing.base.info.ProductConfig;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;
import com.tjhello.lib.billing.base.utils.BillingEasyLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * BillingEasy
 *一款全新设计的内购聚合，同时支持华为内购与谷歌内购。
 *=========================================
 * 作者:TJHello
 * 日期:2021-07-17
 * qq群:425219113
 * 仓库地址:https://gitee.com/TJHello/BillingEasy
 * ========================================
 * 使用该库请遵循Apache License2.0协议，莫要寒了广大开源者的心
 */
public abstract class BillingHandler implements BillingHandlerImp {

    private static final List<ProductConfig> productConfigList = new ArrayList<>();

    @NonNull
    public static BillingHandler createBillingHandler(BillingEasyListener mBillingEasyListener){
        if(containsClass("com.tjhello.lib.billing.google.GoogleBillingHandler")){
            BillingHandler handler = callConstructor("com.tjhello.lib.billing.google.GoogleBillingHandler",mBillingEasyListener);
            if(handler!=null){
                return handler;
            }
        }
        return new EmptyHandler(mBillingEasyListener);
    }

    @Nullable
    private static BillingHandler callConstructor(String className,BillingEasyListener listener){
        try {
             Class<?> mClass = Class.forName(className);
             Constructor<?> constructor = mClass.getConstructor(BillingEasyListener.class);
             return (BillingHandler) constructor.newInstance(listener);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }


    protected BillingEasyListener mBillingEasyListener;

    public BillingHandler(BillingEasyListener mBillingEasyListener) {
        this.mBillingEasyListener = mBillingEasyListener;
    }

    public void setProductConfigList(List<ProductConfig> list){
        productConfigList.clear();
        productConfigList.addAll(list);
    }

    @Nullable
    protected ProductConfig findProductInfo(@NonNull String productCode){
        for (ProductConfig config : productConfigList) {
            if(config.getCode().equals(productCode)){
                return config;
            }
        }
        return null;
    }

    @NonNull
    public abstract String getProductType(@ProductType String type);


    private static boolean containsClass(@NonNull String className){
        if(className.isEmpty()) return false;
        try {
            Class.forName(className);
            return true;
        }catch (ClassNotFoundException e){
            BillingEasyLog.e("class not found :"+className);
            return false;
        }
    }
}
