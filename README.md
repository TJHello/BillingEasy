# BillingEasy-0.1.1-t03

**QQ交流群(425219113)**

使用该库请遵循Apache License2.0协议，莫要寒了广大开源者的心。

---

一款全新设计的内购聚合，同时支持华为内购与谷歌内购。

1. 使用了模块化技术，可以实现按需切换平台，而不残留任何平台代码。
2. 重新设计了所有接口，去除了业务相关逻辑，还给大家原生操作。
3. 运用了大量callback，提供更加便捷的即用即回API。

**(内测版本，未经测试，功能不全，尝鲜使用，目前还没有支持华为内购)**

---

### 使用步骤
- ### Step1 添加远程仓库地址-build.gradle(project)
```groovy

allprojects {
     repositories {
        maven { url 'https://tjhello.gitee.io/publiclib/'}
     }
}

```

- ### Step2 配置-build.gradle(app)

```groovy

android{
    //支持JAVA8，享受更多的语法糖
    compileOptions {
         sourceCompatibility JavaVersion.VERSION_1_8
         targetCompatibility JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation 'com.TJHello.easy:BillingEasy:0.1.1-t03'//BillingEasy
    implementation 'com.TJHello.lib.billing:google:1.4.0.0-t03'//Google内购
    //华为等这版本跑通了再加
}

```

- ### Step3 代码示例(以下接口只要支持了JAVA8，都是可选实现)
```java
public class MainActivity extends AppCompatActivity {

    private final BillingEasy billingEasy = BillingEasy.newInstance(this)
            .addListener(new MyBillingEasyListener());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BillingEasy.setDebug(true);
        BillingEasy.addProductConfig("商品code", ProductType.TYPE_INAPP_CONSUMABLE);
        billingEasy.onCreate();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        billingEasy.onDestroy();
    }

    private class MyBillingEasyListener implements BillingEasyListener{
        @Override
        public void onConnection(@NonNull BillingEasyResult result) {
            //内购服务连接
        }

        @Override
        public void onDisconnected() {
            //内购服务断开
        }

        @Override
        public void onQueryProduct(@NonNull BillingEasyResult result, @NonNull List<ProductInfo> productInfoList) {
            //查询商品信息

        }

        @Override
        public void onPurchases(@NonNull BillingEasyResult result, @NonNull List<PurchaseInfo> purchaseInfoList) {
            //购买商品，判断示例
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
            //查询有效订单
        }

        @Override
        public void onQueryOrderHistory(@NonNull BillingEasyResult result, @NonNull List<PurchaseHistoryInfo> purchaseInfoList) {
            //查询历史订单
        }
    }
}
```

- ### API说明

```java

//添加一个商品信息(必须)
BillingEasy.addProductConfig("商品code", ProductType.TYPE_INAPP_CONSUMABLE);

//查询商品信息
billingEasy.queryProduct();
billingEasy.queryProduct((billingEasyResult, productInfoList) -> {

});
//发起购买
billingEasy.purchase("商品code");
billingEasy.purchase("商品code", (billingEasyResult, purchaseInfoList) -> {

});
//查询订单信息
billingEasy.queryOrderAsync();//联网查询有效订单
billingEasy.queryOrderLocal();//查询本地缓存订单
billingEasy.queryOrderHistory();//查询历史订单
//消耗商品
billingEasy.consume("purchaseToken");
//确认购买
billingEasy.acknowledge("purchaseToken");

```



