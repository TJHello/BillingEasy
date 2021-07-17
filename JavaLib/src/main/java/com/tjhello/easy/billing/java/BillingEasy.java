package com.tjhello.easy.billing.java;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.lib.billing.base.imp.BillingEasyImp;
import com.tjhello.lib.billing.base.info.BillingEasyResult;
import com.tjhello.lib.billing.base.info.ProductConfig;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;
import com.tjhello.lib.billing.base.info.PurchaseHistoryInfo;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;
import com.tjhello.lib.billing.base.listener.EasyCallBack;
import com.tjhello.lib.billing.base.utils.BillingEasyLog;


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
public class BillingEasy implements BillingEasyImp {

    private final Activity mActivity ;
    private final MyBillingEasyListener myBillingEasyListener = new MyBillingEasyListener();
    private BillingEasy(@NonNull Activity activity){
        this.mActivity = activity;
        billingManager.addListener(myBillingEasyListener);
    }

    //region============================静态方法============================

    private static final BillingManager billingManager = new BillingManager();

    private static final CopyOnWriteArrayList<BillingEasy> billingEasyList = new CopyOnWriteArrayList<>();
    private static final CopyOnWriteArrayList<ProductConfig> productConfigList = new CopyOnWriteArrayList<>();
    //创建一个实例
    @NonNull
    public static BillingEasy newInstance(@NonNull Activity activity){
        BillingEasy billingEasy = new BillingEasy(activity);
        billingEasyList.add(billingEasy);
        return billingEasy;
    }

    /**
     * 添加商品配置
     * @param productCode 商品Code
     * @param productType 商品类型 @ProductType
     */
    public static void addProductConfig(@NonNull String productCode,@NonNull @ProductType String productType) {
        if(productCode.isEmpty()) {
            try {
                throw new Exception("productCode不能为空");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        ProductConfig config = new ProductConfig();
        config.setCode(productCode);
        config.setType(productType);
        productConfigList.add(config);
    }

    @Nullable
    public static ProductConfig findProductConfig(@NonNull String productCode){
        for(int i=0;i< productConfigList.size();i++){
            ProductConfig config = productConfigList.get(i);
            if(config.getCode().equals(productCode)){
                return config;
            }
        }
        return null;
    }

    @NonNull
    public static List<ProductConfig> getAllProductConfig(){
        return productConfigList;
    }

    public static void setDebug(boolean bool){
        BillingEasyLog.setDebug(bool);
    }

    //endregion

    //region============================变量区域============================

    private final CopyOnWriteArrayList<BillingEasyListener> listenerList = new CopyOnWriteArrayList<>();


    //endregion

    public BillingEasy addListener(@NonNull BillingEasyListener listener) {
        listenerList.add(listener);
        return this;
    }

    public void removeListener(@NonNull BillingEasyListener listener) {
        listenerList.remove(listener);
    }

    @Override
    public void onCreate() {
        billingManager.onCreate(mActivity);
    }

    @Override
    public void onDestroy() {
        billingManager.removeListener(myBillingEasyListener);
        listenerList.clear();
        billingEasyList.remove(this);
    }

    @Override
    public void queryProduct() {
        billingManager.queryProduct(null);
    }

    @Override
    public void queryProduct(EasyCallBack<List<ProductInfo>> callBack) {
        billingManager.queryProduct(callBack);
    }

    @Override
    public void purchase(@NonNull String productCode) {
        purchase(productCode,null);
    }

    @Override
    public void purchase(@NonNull String productCode,@Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        billingManager.purchase(mActivity,productCode,callBack);
    }

    @Override
    public void consume(@NonNull String purchaseCode) {
        billingManager.consume(purchaseCode,null);
    }


    @Override
    public void consume(@NonNull String purchaseCode, @Nullable EasyCallBack<String> callback) {
        billingManager.consume(purchaseCode,callback);
    }


    @Override
    public void queryOrderAsync() {
        billingManager.queryOrderAsync(null);
    }

    @Override
    public void queryOrderLocal() {
        billingManager.queryOrderLocal(null);
    }

    @Override
    public void queryOrderHistory() {
        billingManager.queryOrderHistory(null);
    }

    @Override
    public void queryOrderAsync(@Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        billingManager.queryOrderAsync(callBack);
    }

    @Override
    public void queryOrderLocal(@Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        billingManager.queryOrderLocal(callBack);
    }

    @Override
    public void queryOrderHistory(@Nullable EasyCallBack<List<PurchaseHistoryInfo>> callBack) {
        billingManager.queryOrderHistory(callBack);
    }



    private class MyBillingEasyListener implements BillingEasyListener{

        @Override
        public void onConnection(@NonNull BillingEasyResult result) {
            BillingEasyLog.i("[onConnection]:"+result.isSuccess+":"+result.responseCode+","+result.responseMsg);
        }

        @Override
        public void onDisconnected() {
            BillingEasyLog.i("[onDisconnected]");
        }

        @Override
        public void onQueryProduct(@NonNull BillingEasyResult result, @NonNull List<ProductInfo> productInfoList) {
            for (BillingEasyListener listener : listenerList) {
                listener.onQueryProduct(result,productInfoList);
            }
            BillingEasyLog.i("[onQueryProduct]:"+result.isSuccess+":"+result.responseCode+","+result.responseMsg);
        }

        @Override
        public void onPurchases(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            for (BillingEasyListener listener : listenerList) {
                listener.onPurchases(result,purchaseInfoList);
            }
            BillingEasyLog.i("[onPurchases]:"+result.isSuccess+":"+result.responseCode+","+result.responseMsg);
        }

        @Override
        public void onConsume(@NonNull BillingEasyResult result, @NonNull String purchaseToken) {
            for (BillingEasyListener listener : listenerList) {
                listener.onConsume(result,purchaseToken);
            }
            BillingEasyLog.i("[onConsume]:"+result.isSuccess+":"+result.responseCode+","+result.responseMsg);
        }

        @Override
        public void onAcknowledge(@NonNull BillingEasyResult result, @NonNull String purchaseToken) {
            for (BillingEasyListener listener : listenerList) {
                listener.onAcknowledge(result,purchaseToken);
            }
            BillingEasyLog.i("[onAcknowledge]:"+result.isSuccess+":"+result.responseCode+","+result.responseMsg);
        }

        @Override
        public void onQueryOrder(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            for (BillingEasyListener listener : listenerList) {
                listener.onQueryOrder(result,purchaseInfoList);
            }
            BillingEasyLog.i("[onQueryOrder]:"+result.isSuccess+":"+result.responseCode+","+result.responseMsg);
        }

        @Override
        public void onQueryOrderHistory(@NonNull BillingEasyResult result, @NonNull List<PurchaseHistoryInfo> purchaseInfoList) {
            for (BillingEasyListener listener : listenerList) {
                listener.onQueryOrderHistory(result,purchaseInfoList);
            }
            BillingEasyLog.i("[onQueryOrderHistory]:"+result.isSuccess+":"+result.responseCode+","+result.responseMsg);
        }
    }

}
