package com.tjhello.app.easy.billing.v2;

import android.app.Application;

import androidx.annotation.NonNull;

import com.tjhello.easy.billing.java.BillingEasyStatic;
import com.tjhello.lib.billing.base.info.BillingEasyResult;
import com.tjhello.lib.billing.base.listener.EasyCallBack;

public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();


        BillingEasyStatic.init(this, new EasyCallBack<Boolean>() {
            @Override
            public void callback(@NonNull BillingEasyResult result, @NonNull Boolean aBoolean) {

            }
        });
    }
}
