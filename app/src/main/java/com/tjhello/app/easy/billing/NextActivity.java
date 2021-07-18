package com.tjhello.app.easy.billing;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.tjhello.easy.billing.java.BillingEasy;
import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.lib.billing.base.info.BillingEasyResult;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;

import java.util.List;


public class NextActivity extends AppCompatActivity {

    private final BillingEasy billingEasy = BillingEasy.newInstance(this);

    private AppCompatButton btInapp,btSubs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.next_activity_layout);
        billingEasy.onCreate();

        billingEasy.queryProduct((result, productInfoList) -> {
            if(result.isSuccess){
                for (ProductInfo productInfo : productInfoList) {
                    switch (productInfo.getType()){
                        case  ProductType.TYPE_INAPP_CONSUMABLE:
                        case ProductType.TYPE_INAPP_NON_CONSUMABLE:
                            btInapp.setText(String.format("发起内购:%s",productInfo.getPrice()));
                            break;
                        case ProductType.TYPE_SUBS:{
                            btSubs.setText(String.format("发起内购:%s",productInfo.getPrice()));
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
            billingEasy.purchase("内购商品code", this::utilPurchase);
        });
        btSubs = this.findViewById(R.id.btSubs);
        btSubs.setOnClickListener(view->{
            billingEasy.purchase("订阅商品code", this::utilPurchase);
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        billingEasy.onDestroy();
    }

    private void utilPurchase(BillingEasyResult billingEasyResult,List<PurchaseInfo> purchaseInfoList){
        if(billingEasyResult.isSuccess){
            for (PurchaseInfo purchaseInfo : purchaseInfoList) {
                if(purchaseInfo.isValid()){
                    //消耗商品
                    String type = purchaseInfo.getFirstType();
                    if(type!=null){
                        switch (type){
                            case ProductType.TYPE_INAPP_CONSUMABLE:{
                                billingEasy.consume(purchaseInfo.getPurchaseToken());
                                //发放奖励
                            }
                            case ProductType.TYPE_INAPP_NON_CONSUMABLE:{
                                billingEasy.acknowledge(purchaseInfo.getPurchaseToken());
                                //发放奖励
                            }
                            case ProductType.TYPE_SUBS:{
                                //订阅商品
                                //发放奖励
                            }
                        }
                    }
                }
            }
        }
    }
}
