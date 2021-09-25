package com.tjhello.easy.billing.java;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.lib.billing.base.info.BillingEasyResult;
import com.tjhello.lib.billing.base.info.ProductConfig;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseHistoryInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;
import com.tjhello.lib.billing.base.listener.EasyCallBack;
import com.tjhello.lib.billing.base.utils.BillingEasyLog;

import java.util.List;

public class BillingEasyStatic {

    private static final BillingManager billingManager = new BillingManager();

    public static boolean isAutoConsume = false;//是否自动消耗商品
    public static boolean isAutoAcknowledge = false;//是否自动确认购买

    private static final int MAX_CONSUME_RETRY_NUM = 3;//最大消耗事变重试次数
    private static final int MAX_ACKNOWLEDGE_RETRY_NUM = 3;//最大确认购买重试次数
    private static int consumeRetryNum = 0;//消耗重试次数
    private static int acknowledgeRetryNum = 0;//确认购买重试次数

    static {
        billingManager.addListener(new OnBillingListener());
    }

    public static void init(Context context){
        billingManager.init(context);
    }

    public static void init(Context context, EasyCallBack<Boolean> callBack){
        billingManager.init(context,callBack);
    }

    /**
     * 设置自动消耗商品
     * @param bool bool
     */
    public static void setAutoConsume(boolean bool){
        isAutoConsume = bool;
    }

    /**
     * 设置自动确认购买
     * @param bool bool
     */
    public static void setAutoAcknowledge(boolean bool){
        isAutoAcknowledge = bool;
    }

    /**
     * 添加商品配置(自动去重)
     * @param productType 商品类型 {@link ProductType}
     * @param productCodeArray 商品代码
     */
    public static void addProductConfig(@NonNull @ProductType String productType, @NonNull String... productCodeArray) {
        for (String productCode : productCodeArray) {
            if(productCode.isEmpty()) {
                try {
                    throw new Exception("productCode不能为空");
                } catch (Exception e) {
                    e.printStackTrace();
                    BillingEasyLog.e(e.getMessage());
                }
            }
            ProductConfig config = new ProductConfig();
            config.setCode(productCode);
            config.setType(productType);
            billingManager.addProductConfig(config);
        }
    }

    /**
     * 清空商品配置
     */
    public static void cleanProductConfig(){
        billingManager.cleanProductConfig();
    }


    /**
     * 添加一个商品配置(自动去重)
     * @param productConfig {@link ProductConfig#build(String, String)}
     */
    public static void addProductConfig(ProductConfig productConfig){
        if(productConfig.getCode().isEmpty()){
            try {
                throw new Exception("productCode不能为空");
            } catch (Exception e) {
                e.printStackTrace();
                BillingEasyLog.e(e.getMessage());
            }
        }
        billingManager.addProductConfig(productConfig);
    }

    /**
     * 是否开启日志
     * @param bool true|false
     */
    public static void setDebug(boolean bool){
        BillingEasyLog.setDebug(bool);
    }

    /**
     * 添加监听器-之后必须要调用removeListener
     * @param listener listener
     */
    public static void addListener(BillingEasyListener listener){
        billingManager.addListener(listener);
    }

    public static void removeListener(BillingEasyListener listener){
        billingManager.removeListener(listener);
    }

    public static void cleanListener(){
        billingManager.cleanListener();
    }

    /**
     * 查询所有商品信息
     */
    public static void queryProduct() {
        queryProduct(null);
    }
    /**
     * 查询所有商品信息
     * @param callBack 回调
     */
    public static void queryProduct(EasyCallBack<List<ProductInfo>> callBack) {
        billingManager.queryProduct(callBack);
    }

    /**
     * 发起购买
     * @param productCode 商品代码
     */
    public static void purchase(@NonNull Activity activity, @NonNull String productCode) {
        purchase(activity,productCode,null);
    }

    /**
     * 发起购买
     * @param productCode 商品代码
     * @param callBack 回调
     */
    public static void purchase(Activity activity, @NonNull String productCode, @Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        billingManager.purchase(activity,productCode,callBack);
    }

    /**
     * 消耗商品 (消耗商品的操作包含了确认购买)
     * @param purchaseToken {@link PurchaseInfo#getPurchaseToken()}
     */
    public static void consume(@NonNull String purchaseToken) {
        billingManager.consume(purchaseToken,null);
    }

    /**
     * 消耗商品 (消耗商品的操作包含了确认购买)
     * @param purchaseToken {@link PurchaseInfo#getPurchaseToken()}
     * @param callBack 回调
     */
    public static void consume(@NonNull String purchaseToken, @Nullable EasyCallBack<String> callBack) {
        billingManager.consume(purchaseToken,callBack);
    }

    /**
     * 确认购买 (消耗商品的操作包含了确认购买，不确认购买会导致退款)
     * 确认购买前可以使用{@link PurchaseInfo#isAcknowledged()}方法来判断商品是否已经确认购买
     * @param purchaseToken {@link PurchaseInfo#getPurchaseToken()}
     */
    public static void acknowledge(@NonNull String purchaseToken) {
        billingManager.acknowledge(purchaseToken,null);
    }

    /**
     * 确认购买 (消耗商品的操作包含了确认购买，不确认购买会导致退款)
     * 确认购买前可以使用{@link PurchaseInfo#isAcknowledged()}方法来判断商品是否已经确认购买
     * @param purchaseToken {@link PurchaseInfo#getPurchaseToken()}
     * @param callBack 回调
     */
    public static void acknowledge(@NonNull String purchaseToken, @Nullable EasyCallBack<String> callBack) {
        billingManager.acknowledge(purchaseToken,callBack);
    }


    /**
     * 查询有效商品订单-在线或本地缓存
     */
    public static void queryOrderAsync() {
        billingManager.queryOrderAsync(null);
    }

    /**
     * 查询有效商品订单-本地缓存
     */
    public static void queryOrderLocal() {
        billingManager.queryOrderLocal(null);
    }

    /**
     * 查询历史订单
     */
    public static void queryOrderHistory() {
        billingManager.queryOrderHistory(null);
    }

    /**
     * 查询有效商品订单-在线或本地缓存
     */
    public static void queryOrderAsync(@Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        billingManager.queryOrderAsync(callBack);
    }

    /**
     * 查询有效商品订单-本地缓存
     */
    public static void queryOrderLocal(@Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        billingManager.queryOrderLocal(callBack);
    }

    /**
     * 查询历史订单
     */
    public static void queryOrderHistory(@Nullable EasyCallBack<List<PurchaseHistoryInfo>> callBack) {
        billingManager.queryOrderHistory(callBack);
    }

    private static class OnBillingListener implements BillingEasyListener{

        @Override
        public void onPurchases(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            utilsPurchase(purchaseInfoList);
        }

        @Override
        public void onQueryOrder(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            utilsPurchase(purchaseInfoList);
        }

        @Override
        public void onConsume(@NonNull BillingEasyResult result, @NonNull String purchaseToken) {
            if(!result.isSuccess){
                //重试
                if(consumeRetryNum<MAX_CONSUME_RETRY_NUM){
                    consumeRetryNum++;
                    consume(purchaseToken);
                }
            }
        }

        @Override
        public void onAcknowledge(@NonNull BillingEasyResult result, @NonNull String purchaseToken) {
            if(!result.isSuccess){
                //重试
                if(acknowledgeRetryNum<MAX_ACKNOWLEDGE_RETRY_NUM){
                    acknowledgeRetryNum++;
                    acknowledge(purchaseToken);
                }
            }
        }

        private void utilsPurchase(List<PurchaseInfo> purchaseInfoList){
            resetRetryNum();
            for (PurchaseInfo purchaseInfo : purchaseInfoList) {
                if(purchaseInfo.isValid()){
                    for (ProductConfig productConfig : purchaseInfo.getProductList()) {
                        if(productConfig.canConsume()){
                            consume(purchaseInfo.getPurchaseToken());
                        }else{
                            if(!purchaseInfo.isAcknowledged()){
                                acknowledge(purchaseInfo.getPurchaseToken());
                            }
                        }
                    }
                }
            }
        }
    }

    //复位重试次数
    private static void resetRetryNum(){
        consumeRetryNum = 0;
        acknowledgeRetryNum = 0;
    }

}
