package com.tjhello.lib.billing.base.handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.lib.billing.base.imp.BillingHandlerImp;
import com.tjhello.lib.billing.base.info.ProductConfig;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;
import com.tjhello.lib.billing.base.utils.BillingEasyLog;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

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
        }else if(containsClass("com.tjhello.lib.billing.huawei.HuaweiBillingHandler")){
            BillingHandler handler = callConstructor("com.tjhello.lib.billing.huawei.HuaweiBillingHandler",mBillingEasyListener);
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

    public void addProductConfig(ProductConfig productConfig){
        int size = productConfigList.size();
        for(int i=size-1;i>=0;i--){
            ProductConfig tempConfig = productConfigList.get(i);
            if(tempConfig.getCode().equals(productConfig.getCode())){
                return ;
            }
        }
        productConfigList.add(productConfig);
    }

    public void cleanProductConfig(){
        productConfigList.clear();
    }

    public List<ProductConfig> getProductList(){
        return productConfigList;
    }

    @Nullable
    protected static ProductConfig findProductInfo(@NonNull String productCode){
        for (ProductConfig config : productConfigList) {
            if(config.getCode().equals(productCode)){
                return config;
            }
        }
        return null;
    }

    protected ProductConfig addProductConfig(@ProductType String type,@NonNull String code){
        String tjType = getTJProductType(type);
        int size = productConfigList.size();
        for(int i=size-1;i>=0;i--){
            ProductConfig config = productConfigList.get(i);
            if(config.getCode().equals(code)){
                return config;
            }
        }
        ProductConfig config = ProductConfig.build(tjType,code);
        productConfigList.add(config);
        return config;
    }

    @NonNull
    public abstract String getProductType(@ProductType String type);

    public abstract String getTJProductType(String type);

    @NonNull
    public ProductConfig getProductConfig(String type,String productCode){
        return addProductConfig(type,productCode);
    }

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
