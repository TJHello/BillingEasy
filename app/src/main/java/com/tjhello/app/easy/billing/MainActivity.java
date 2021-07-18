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

    private final BillingEasy billingEasy = BillingEasy.newInstance(this)
            .addListener(new MyBillingEasyListener());

    private TextView tvLog ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //BillingEasy
        BillingEasy.setDebug(true);
        BillingEasy.addProductConfig("商品code", ProductType.TYPE_INAPP_CONSUMABLE);
        BillingEasy.addProductConfig("商品code", ProductType.TYPE_INAPP_NON_CONSUMABLE);
        BillingEasy.addProductConfig("商品code", ProductType.TYPE_SUBS);
        billingEasy.onCreate();

        //ui初始化
        tvLog = this.findViewById(R.id.tvLog);
        this.findViewById(R.id.tvLogClean).setOnClickListener(view -> {
            tvLog.setText(null);
        });

        this.findViewById(R.id.ivHelp).setOnClickListener(view->{
            Intent intent = new Intent();
            Uri uri = Uri.parse("https://gitee.com/TJHello/BillingEasy");
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(uri);
            startActivity(intent);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        billingEasy.onDestroy();
    }

    @SuppressWarnings("InnerClassMayBeStatic")
    private class MyBillingEasyListener implements BillingEasyListener{

        @Override
        public void onConnection(@NonNull BillingEasyResult result) {
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
            log("购买商品:"+result.isSuccess);
            StringBuilder tempBuilder = new StringBuilder();
            for (PurchaseInfo info : purchaseInfoList) {
                String details = String.format(Locale.getDefault(),"%s:%s\n",
                        Arrays.toString(info.getCodeList().toArray()),info.getOrderId());
                tempBuilder.append(details);
            }
            if(!tempBuilder.toString().isEmpty()){
                log(tempBuilder.toString());
            }

            //判断示例
            if(result.isSuccess){
                for (PurchaseInfo purchaseInfo : purchaseInfoList) {
                    if(purchaseInfo.isValid()){
                        //有效商品
                        String type = purchaseInfo.getFirstType();
                        if(type!=null){
                            if(type.equals(ProductType.TYPE_INAPP_CONSUMABLE)){
                                //可消耗商品，则消耗
                                billingEasy.consume(purchaseInfo.getPurchaseToken());
                            }else if(type.equals(ProductType.TYPE_INAPP_NON_CONSUMABLE)){
                                //不可消耗商品，则确认购买
                                billingEasy.acknowledge(purchaseInfo.getPurchaseToken());
                            }
                        }
                    }
                }
            }
        }

        @Override
        public void onQueryOrder(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            log("查询有效订单:"+result.isSuccess);
            StringBuilder tempBuilder = new StringBuilder();
            for (PurchaseInfo info : purchaseInfoList) {
                String details = String.format(Locale.getDefault(),"%s:%s\n",
                        Arrays.toString(info.getCodeList().toArray()),info.getOrderId());
                tempBuilder.append(details);
            }
            if(!tempBuilder.toString().isEmpty()){
                log(tempBuilder.toString());
            }
        }

        @Override
        public void onQueryOrderHistory(@NonNull BillingEasyResult result, @NonNull List<PurchaseHistoryInfo> purchaseInfoList) {
            log("查询历史订单:"+result.isSuccess);
            StringBuilder tempBuilder = new StringBuilder();
            for (PurchaseHistoryInfo info : purchaseInfoList) {
                String details = String.format(Locale.getDefault(),"%s:%s\n",
                        Arrays.toString(info.getCodeList().toArray()),info.getPurchaseToken());
                tempBuilder.append(details);
            }
            if(!tempBuilder.toString().isEmpty()){
                log(tempBuilder.toString());
            }
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
}