package com.tjhello.app.easy.billing;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.tjhello.easy.billing.java.BillingEasy;
import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.lib.billing.base.info.BillingEasyResult;
import com.tjhello.lib.billing.base.info.ProductConfig;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;
import com.tjhello.lib.billing.base.info.PurchaseParam;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;
import com.tjhello.lib.billing.base.listener.EasyCallBack;

import java.util.List;


public class NextActivity extends AppCompatActivity {


    private AppCompatButton btInapp,btSubs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_activity_layout);
        BillingEasy.addListener(new OnBillingListener());
        BillingEasy.queryProduct((result, productInfoList) -> {
            if(result.isSuccess){
                for (ProductInfo productInfo : productInfoList) {
                    switch (productInfo.getType()){
                        case  ProductType.TYPE_INAPP_CONSUMABLE:
                        case ProductType.TYPE_INAPP_NON_CONSUMABLE:
                            btInapp.setText(String.format("发起内购:%s",productInfo.getPrice()));
                            break;
                        case ProductType.TYPE_SUBS:{
                            btSubs.setText(String.format("发起订阅:%s",productInfo.getPrice()));
                        }
                    }
                }
            }
        });

        this.findViewById(R.id.tvBack).setOnClickListener(view -> {
            this.finish();
        });

        btInapp = this.findViewById(R.id.btInapp);
        btInapp.setOnClickListener(view->{
            BillingEasy.purchase(this,"内购商品code");
            PurchaseParam param = new PurchaseParam.Builder("内购商品code").setObfuscatedAccountId("").build();
            BillingEasy.purchase(this, param, new EasyCallBack<List<PurchaseInfo>>() {
                @Override
                public void callback(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfos) {

                }
            });
        });
        btSubs = this.findViewById(R.id.btSubs);
        btSubs.setOnClickListener(view->{
            BillingEasy.purchase(this,"订阅商品code");
        });
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
                                    BillingEasy.consume(purchaseInfo.getPurchaseToken());
                                }break;
                                //内购商品-非消耗||订阅商品
                                case ProductType.TYPE_INAPP_NON_CONSUMABLE:
                                case ProductType.TYPE_SUBS: {
                                    //判断是否已经确认购买
                                    if(!purchaseInfo.isAcknowledged()){
                                        //确认购买
                                        BillingEasy.acknowledge(purchaseInfo.getPurchaseToken());
                                    }

                                }break;
                            }
                        }
//                            //或者
//                            if(productConfig.canConsume()){
//                                //消耗商品
//                                BillingEasy.consume(purchaseInfo.getPurchaseToken());
//                            }else{
//                                //确认购买
//                                if(!purchaseInfo.isAcknowledged()){
//                                    BillingEasy.acknowledge(purchaseInfo.getPurchaseToken());
//                                }
//                            }
                    }
                }
            }
        }
    }

    private class OnBillingListener implements BillingEasyListener{

        @Override
        public void onPurchases(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            utilPurchase(result,purchaseInfoList);
        }
    }
}
