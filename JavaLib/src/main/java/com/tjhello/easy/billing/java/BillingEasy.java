package com.tjhello.easy.billing.java;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.easy.billing.java.imp.BillingEasyImp;
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
    private static String ACTIVITY_TAG ;
    private BillingEasy(@NonNull Activity activity){
        this.mActivity = activity;
        billingManager.addListener(myBillingEasyListener);
        ACTIVITY_TAG = activity.getClass().getSimpleName();
    }

    //region============================静态方法============================

    private static final BillingManager billingManager = new BillingManager();

//    private static final CopyOnWriteArrayList<BillingEasy> billingEasyList = new CopyOnWriteArrayList<>();

    /**
     * 创建一个新的实例(可以创建任意多个，但必须要调用{@link #onDestroy()}来回收资源，避免内存泄漏)
     * @param activity Activity
     * @return BillingEasy
     */
    @NonNull
    public static BillingEasy newInstance(@NonNull Activity activity){
        BillingEasy billingEasy = new BillingEasy(activity);
//        billingEasyList.add(billingEasy);
        return billingEasy;
    }

    /**
     * 添加商品配置(自动去重)
     * @param productType 商品类型 {@link ProductType}
     * @param productCodeArray 商品代码
     */
    public static void addProductConfig(@NonNull @ProductType String productType,@NonNull String... productCodeArray) {
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

    //endregion

    //region============================变量区域============================

    private final CopyOnWriteArrayList<BillingEasyListener> listenerList = new CopyOnWriteArrayList<>();


    //endregion

    /**
     * 添加一个万能监听器
     * @param listener {@link BillingEasyListener}
     * @return BillingEasy
     */
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
//        billingEasyList.remove(this);
    }

    /**
     * 查询所有商品信息
     */
    @Override
    public void queryProduct() {
        billingManager.queryProduct(null);
    }

    /**
     * 查询所有商品信息
     * @param callBack 回调
     */
    @Override
    public void queryProduct(EasyCallBack<List<ProductInfo>> callBack) {
        billingManager.queryProduct(callBack);
    }

    /**
     * 发起购买
     * @param productCode 商品代码
     */
    @Override
    public void purchase(@NonNull String productCode) {
        purchase(productCode,null);
    }

    /**
     * 发起购买
     * @param productCode 商品代码
     * @param callBack 回调
     */
    @Override
    public void purchase(@NonNull String productCode,@Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        billingManager.purchase(mActivity,productCode,callBack);
    }

    /**
     * 消耗商品 (消耗商品的操作包含了确认购买)
     * @param purchaseToken {@link PurchaseInfo#getPurchaseToken()}
     */
    @Override
    public void consume(@NonNull String purchaseToken) {
        billingManager.consume(purchaseToken,null);
    }

    /**
     * 消耗商品 (消耗商品的操作包含了确认购买)
     * @param purchaseToken {@link PurchaseInfo#getPurchaseToken()}
     * @param callBack 回调
     */
    @Override
    public void consume(@NonNull String purchaseToken, @Nullable EasyCallBack<String> callBack) {
        billingManager.consume(purchaseToken,callBack);
    }

    /**
     * 确认购买 (消耗商品的操作包含了确认购买，不确认购买会导致退款)
     * 确认购买前可以使用{@link PurchaseInfo#isAcknowledged()}方法来判断商品是否已经确认购买
     * @param purchaseToken {@link PurchaseInfo#getPurchaseToken()}
     */
    @Override
    public void acknowledge(@NonNull String purchaseToken) {
        billingManager.acknowledge(purchaseToken,null);
    }

    /**
     * 确认购买 (消耗商品的操作包含了确认购买，不确认购买会导致退款)
     * 确认购买前可以使用{@link PurchaseInfo#isAcknowledged()}方法来判断商品是否已经确认购买
     * @param purchaseToken {@link PurchaseInfo#getPurchaseToken()}
     * @param callBack 回调
     */
    @Override
    public void acknowledge(@NonNull String purchaseToken, @Nullable EasyCallBack<String> callBack) {
        billingManager.acknowledge(purchaseToken,callBack);
    }


    /**
     * 查询有效商品订单-在线或本地缓存
     */
    @Override
    public void queryOrderAsync() {
        billingManager.queryOrderAsync(null);
    }

    /**
     * 查询有效商品订单-本地缓存
     */
    @Override
    public void queryOrderLocal() {
        billingManager.queryOrderLocal(null);
    }

    /**
     * 查询历史订单
     */
    @Override
    public void queryOrderHistory() {
        billingManager.queryOrderHistory(null);
    }

    /**
     * 查询有效商品订单-在线或本地缓存
     */
    @Override
    public void queryOrderAsync(@Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        billingManager.queryOrderAsync(callBack);
    }

    /**
     * 查询有效商品订单-本地缓存
     */
    @Override
    public void queryOrderLocal(@Nullable EasyCallBack<List<PurchaseInfo>> callBack) {
        billingManager.queryOrderLocal(callBack);
    }

    /**
     * 查询历史订单
     */
    @Override
    public void queryOrderHistory(@Nullable EasyCallBack<List<PurchaseHistoryInfo>> callBack) {
        billingManager.queryOrderHistory(callBack);
    }



    private class MyBillingEasyListener implements BillingEasyListener{

        /**
         * 连接内购服务结果
         * @param result {@link BillingEasyResult}
         */
        @Override
        public void onConnection(@NonNull BillingEasyResult result) {
            BillingEasyLog.logResult(ACTIVITY_TAG,"onConnection",result);
        }

        /**
         * 断开内购服务连接
         */
        @Override
        public void onDisconnected() {
            BillingEasyLog.i("["+ACTIVITY_TAG+"][onDisconnected]");
        }

        /**
         * 查询商品信息回调
         * @param result {@link BillingEasyResult}
         * @param productInfoList 商品信息列表
         */
        @Override
        public void onQueryProduct(@NonNull BillingEasyResult result, @NonNull List<ProductInfo> productInfoList) {
            for (BillingEasyListener listener : listenerList) {
                listener.onQueryProduct(result,productInfoList);
            }
            if(!listenerList.isEmpty()){
                BillingEasyLog.logProduct(ACTIVITY_TAG,"onQueryProduct",result,productInfoList);
            }
        }

        /**
         * 购买回调
         *
         * //判断购买是否成功
         * if(result.isSuccess){
         *     //有可能返回多个订单
         *     for (PurchaseInfo purchaseInfo : purchaseInfoList) {
         *          //判断商品订单是否有效
         *          if(purchaseInfo.isValid()){
         *              //获取商品配置列表
         *              for (ProductConfig productConfig : purchaseInfo.getProductList()) {
         *                  if(productConfig.canConsume()){
         *                      //消耗商品
         *                      billingEasy.consume(purchaseInfo.getPurchaseToken());
         *                  }else{
         *                      if(!purchaseInfo.isAcknowledged()){
         *                          //确认购买
         *                          billingEasy.acknowledge(purchaseInfo.getPurchaseToken());
         *                      }
         *                  }
         *              }
         *          }
         *     }
         * }
         *
         *
         * @param result {@link BillingEasyResult}
         * @param purchaseInfoList 订单信息列表
         */
        @Override
        public void onPurchases(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            for (BillingEasyListener listener : listenerList) {
                listener.onPurchases(result,purchaseInfoList);
            }
            if(!listenerList.isEmpty()){
                BillingEasyLog.logPurchase(ACTIVITY_TAG,"onPurchases",result,purchaseInfoList);
            }
        }

        @Override
        public void onConsume(@NonNull BillingEasyResult result, @NonNull String purchaseToken) {
            for (BillingEasyListener listener : listenerList) {
                listener.onConsume(result,purchaseToken);
            }
            if(!listenerList.isEmpty()){
                BillingEasyLog.logResult(ACTIVITY_TAG,"onConsume",result,purchaseToken);
            }
        }

        @Override
        public void onAcknowledge(@NonNull BillingEasyResult result, @NonNull String purchaseToken) {
            for (BillingEasyListener listener : listenerList) {
                listener.onAcknowledge(result,purchaseToken);
            }
            if(!listenerList.isEmpty()){
                BillingEasyLog.logResult(ACTIVITY_TAG,"onAcknowledge",result,purchaseToken);
            }
        }

        @Override
        public void onQueryOrder(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            for (BillingEasyListener listener : listenerList) {
                listener.onQueryOrder(result,purchaseInfoList);
            }
            if(!listenerList.isEmpty()){
                BillingEasyLog.logPurchase(ACTIVITY_TAG,"onQueryOrder",result,purchaseInfoList);
            }
        }

        @Override
        public void onQueryOrderHistory(@NonNull BillingEasyResult result, @NonNull List<PurchaseHistoryInfo> purchaseInfoList) {
            for (BillingEasyListener listener : listenerList) {
                listener.onQueryOrderHistory(result,purchaseInfoList);
            }
            if(!listenerList.isEmpty()){
                BillingEasyLog.logPurchaseHistory(ACTIVITY_TAG,"onQueryOrderHistory",result,purchaseInfoList);
            }
        }
    }

}
