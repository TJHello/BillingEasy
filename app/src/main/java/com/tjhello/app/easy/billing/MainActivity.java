package com.tjhello.app.easy.billing;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.widget.TextView;

import com.tjhello.easy.billing.java.BillingEasy;
import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.lib.billing.base.info.BillingEasyResult;
import com.tjhello.lib.billing.base.info.ProductConfig;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseHistoryInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private final Handler handler = new Handler();
    //统一监听器
    private final MyBillingEasyListener listener = new MyBillingEasyListener();
    //内购-消耗型商品-商品code
    private final static String[] INAPP_CONSUMABLE_CODE_ARRAY = new String[]{"商品code"};
    //内购-非消耗型商品-商品code
    private final static String[] INAPP_NON_CONSUMABLE_CODE_ARRAY = new String[]{"商品code"};
    //订阅-商品code
    private final static String[] SUBS_CODE_ARRAY = new String[]{"商品code"};
    private TextView tvLog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BillingEasy.setDebug(true);//按需打开日志

        //这里修改成自己的商品code(GP在这里设置消耗与非消耗，不会影响内购，只是用于自己判断而已)
        BillingEasy.addProductConfig(ProductType.TYPE_INAPP_CONSUMABLE,INAPP_CONSUMABLE_CODE_ARRAY);
        BillingEasy.addProductConfig(ProductType.TYPE_INAPP_NON_CONSUMABLE,INAPP_NON_CONSUMABLE_CODE_ARRAY);
        BillingEasy.addProductConfig(ProductType.TYPE_SUBS,SUBS_CODE_ARRAY);
        ProductConfig productConfig = ProductConfig.build(ProductType.TYPE_INAPP_NON_CONSUMABLE,"test_code","noads");//添加一个带去广告属性的商品
        BillingEasy.addProductConfig(productConfig);

        BillingEasy.setAutoConsume(true);//打开自动消耗(可按需关闭，默认关闭)
        BillingEasy.setAutoAcknowledge(true);//打开自动确认购买(可按需关闭，默认关闭)

        BillingEasy.addListener(listener);
        BillingEasy.init(this);

        //demoUI代码
        initActivityUI();
    }

    @Override
    protected void onDestroy() {
        BillingEasy.removeListener(listener);
        super.onDestroy();
    }

    /**
     * 以下接口可视需要，选择实现(JAVA8特性)
      */
    private class MyBillingEasyListener implements BillingEasyListener{

        @Override
        public void onConnection(@NonNull BillingEasyResult result) {
            if(result.isSuccess){
                //任何时候都不要忘记判断isSuccess

            }

            //日志输出代码
            log("内购服务已连接:"+result.isSuccess
                    +":responseCode="+result.responseCode
                    +",responseMsg="+result.responseMsg);
        }

        @Override
        public void onDisconnected() {
            log("内购服务已断开");
        }

        @Override
        public void onQueryProduct(@NonNull BillingEasyResult result,@NonNull String type, @NonNull List<ProductInfo> productInfoList) {
            //获取商品信息回调
            if(result.isSuccess){

            }

            //日志输出代码
            printQueryProductLog(result,type,productInfoList);
        }

        @Override
        public void onPurchases(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            //处理订单示例
            utilPurchase(result,purchaseInfoList);

            //日志输出代码
            printPurchasesLog(result,purchaseInfoList);
        }

        @Override
        public void onQueryOrder(@NonNull BillingEasyResult result,@NonNull String type, @NonNull List<PurchaseInfo> purchaseInfoList) {
            //处理订单示例
            utilPurchase(result,purchaseInfoList);

            //日志输出代码
            printQueryOrderLog(result,type,purchaseInfoList);
        }

        @Override
        public void onQueryOrderHistory(@NonNull BillingEasyResult result,@NonNull String type, @NonNull List<PurchaseHistoryInfo> purchaseInfoList) {
            //获取历史订单回调
            if(result.isSuccess){

            }


            //日志输出代码
            printQueryOrderHistoryLog(result, type, purchaseInfoList);
        }

        /**
         * 处理订单,自动消耗或自动确认购买
         * @param purchaseInfoList 商品列表
         */
        private void utilPurchase(BillingEasyResult billingEasyResult,List<PurchaseInfo> purchaseInfoList){
            //判断购买成功
            if(billingEasyResult.isSuccess){
                for (PurchaseInfo purchaseInfo : purchaseInfoList) {
                    //判断商品是否有效
                    if(purchaseInfo.isValid()){
                        for (ProductConfig productConfig : purchaseInfo.getProductList()) {
                            //这里进行发货
                            //如果用户使用了延迟付款，应用启动的时候，这里可能会有订单回调，这时也是需要进行发货的
                            //应当建立简单订单系统，需要避免重复发货
                        }
                    }
                }
            }
        }
    }

    //region==============================关系不大的方法

    private void initActivityUI(){
        //ui初始化
        tvLog = this.findViewById(R.id.tvLog);
        this.findViewById(R.id.tvLogClean).setOnClickListener(view -> tvLog.setText(null));
        this.findViewById(R.id.ivHelp).setOnClickListener(view->{
            Intent intent = new Intent();
            Uri uri = Uri.parse("https://gitee.com/TJHello/BillingEasy");
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        });
        this.findViewById(R.id.btNext).setOnClickListener(view->
                startActivity(new Intent(this,NextActivity.class)));
    }

    private void printQueryProductLog(@NonNull BillingEasyResult result,@NonNull String type, @NonNull List<ProductInfo> productInfoList){
        log("查询商品信息:"+result.isSuccess);
        StringBuilder tempBuilder = new StringBuilder();
        for (ProductInfo info : productInfoList) {
            String details = String.format(Locale.getDefault(),"%s , %s\n",
                    info.getCode(),info.getPrice());
            tempBuilder.append(details);
        }
        if(!tempBuilder.toString().isEmpty()){
            log(tempBuilder.toString());
        }
    }

    private void printPurchasesLog(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList){
        log("购买商品:"+result.isSuccess);
        StringBuilder tempBuilder = new StringBuilder();
        for (PurchaseInfo info : purchaseInfoList) {
            for (ProductConfig productConfig : info.getProductList()) {
                String details = String.format(Locale.getDefault(),"%s:%s\n",
                        productConfig.getCode(),info.getOrderId());
                tempBuilder.append(details);
            }
        }
        if(!tempBuilder.toString().isEmpty()){
            log(tempBuilder.toString());
        }
    }

    private void printQueryOrderLog(@NonNull BillingEasyResult result,@NonNull String type, @NonNull List<PurchaseInfo> purchaseInfoList){
        log("查询有效订单:"+result.isSuccess);
        StringBuilder tempBuilder = new StringBuilder();
        for (PurchaseInfo info : purchaseInfoList) {
            for (ProductConfig productConfig : info.getProductList()) {
                String details = String.format(Locale.getDefault(),"%s:%s\n",
                        productConfig.getCode(),info.getOrderId());
                tempBuilder.append(details);
            }
        }
        if(!tempBuilder.toString().isEmpty()){
            log(tempBuilder.toString());
        }
    }

    private void printQueryOrderHistoryLog(@NonNull BillingEasyResult result,@NonNull String type, @NonNull List<PurchaseHistoryInfo> purchaseInfoList){
        log("查询历史订单:"+result.isSuccess);
        StringBuilder tempBuilder = new StringBuilder();
        for (PurchaseHistoryInfo info : purchaseInfoList) {
            for (ProductConfig productConfig : info.getProductList()) {
                String details = String.format(Locale.getDefault(),"%s:%s\n",
                        productConfig.getCode(),info.getPurchaseToken());
                tempBuilder.append(details);
            }
        }
        if(!tempBuilder.toString().isEmpty()){
            log(tempBuilder.toString());
        }
    }

    private final StringBuffer logBuffer = new StringBuffer();
    private void log(String msg){
        handler.post(() -> {
            String log = String.format(Locale.getDefault(),"%s\n%s\n\n",getTime(),msg);
            logBuffer.append(log);
            tvLog.setText(logBuffer.toString());
        });
    }
    private String getTime(){
        Date dt = new Date();
        SimpleDateFormat sdf = (SimpleDateFormat)DateFormat.getInstance();
        sdf.applyPattern("HH:mm:ss.SSS");
        return sdf.format(dt);
    }
    //endregion
}