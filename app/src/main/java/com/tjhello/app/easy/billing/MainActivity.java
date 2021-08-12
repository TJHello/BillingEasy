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

    private final BillingEasy billingEasy = BillingEasy.newInstance(this);

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

        //BillingEasy
        BillingEasy.setDebug(true);
        //这里修改成自己的商品code(GP在这里设置消耗与非消耗，不会影响内购，只是用于自己判断而已)
        BillingEasy.addProductConfig(ProductType.TYPE_INAPP_CONSUMABLE,INAPP_CONSUMABLE_CODE_ARRAY);
        BillingEasy.addProductConfig(ProductType.TYPE_INAPP_NON_CONSUMABLE,INAPP_NON_CONSUMABLE_CODE_ARRAY);
        BillingEasy.addProductConfig(ProductType.TYPE_SUBS,SUBS_CODE_ARRAY);

        //添加监听器放到onCreate里更安全
        billingEasy.addListener(new MyBillingEasyListener());
        billingEasy.onCreate();

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        billingEasy.onDestroy();
    }

    /**
     * 以下接口可视需要，选择实现(JAVA8特性)
      */
    private class MyBillingEasyListener implements BillingEasyListener{

        @Override
        public void onConnection(@NonNull BillingEasyResult result) {


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
        public void onQueryProduct(@NonNull BillingEasyResult result, @NonNull List<ProductInfo> productInfoList) {
            //获取商品信息回调


            //日志输出代码
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

        @Override
        public void onPurchases(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            //处理订单示例
            utilPurchase(result,purchaseInfoList);



            //日志输出代码
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

        @Override
        public void onQueryOrder(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            //处理订单示例
            utilPurchase(result,purchaseInfoList);



            //日志输出代码
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

        @Override
        public void onQueryOrderHistory(@NonNull BillingEasyResult result, @NonNull List<PurchaseHistoryInfo> purchaseInfoList) {
            //获取历史订单回调



            //日志输出代码
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
                            //判断商品类型
                            String type = productConfig.getType();
                            if(type!=null){
                                switch (type){
                                    //内购商品-可消耗
                                    case ProductType.TYPE_INAPP_CONSUMABLE:{
                                        //消耗商品(消耗包括确认购买)
                                        billingEasy.consume(purchaseInfo.getPurchaseToken());
                                    }break;
                                    //内购商品-非消耗||订阅商品
                                    case ProductType.TYPE_INAPP_NON_CONSUMABLE:
                                    case ProductType.TYPE_SUBS: {
                                        //判断是否已经确认购买
                                        if(!purchaseInfo.isAcknowledged()){
                                            //确认购买
                                            billingEasy.acknowledge(purchaseInfo.getPurchaseToken());
                                        }

                                    }break;
                                }
                            }
//                            //或者
//                            if(productConfig.canConsume()){
//                                //消耗商品
//                                billingEasy.consume(purchaseInfo.getPurchaseToken());
//                            }else{
//                                //确认购买
//                                if(!purchaseInfo.isAcknowledged()){
//                                    billingEasy.acknowledge(purchaseInfo.getPurchaseToken());
//                                }
//                            }
                        }
                    }
                }
            }
        }
    }



    //region==============================关系不大的方法
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