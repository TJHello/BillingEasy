# BillingEasy-0.1.1-t01

**QQ交流群(425219113)**

使用该库请遵循Apache License2.0协议，莫要寒了广大开源者的心。

---

一款全新设计的内购聚合，同时支持华为内购与谷歌内购。

1. 使用了模块化技术，可以实现按需切换平台，而不残留任何平台代码。
2. 重新设计了所有接口，去除了业务相关逻辑，还给大家原生操作。
3. 运用了大量callback，提供更加便捷的即用即回API。

**(内测版本，未经测试，功能不全，尝鲜使用，目前还没有支持华为内购)**

### 使用步骤
- ### Step1 添加远程仓库地址-build.gradle(project)
```groovy

allprojects {
     repositories {
        maven { url 'https://tjhello.gitee.io/publiclib/'}
     }
}

```

- ### Step2 配置build.gradle(app)

```groovy

dependencies {
    implementation 'com.TJHello.easy:BillingEasy:0.1.1-t01'//BillingEasy
    implementation 'com.TJHello.lib.billing:google:1.4.0.0-t01'//Google内购
    //华为等这版本跑通了再加
}

```

- ### Step3 代码示例(以下接口只要支持了Java8，都是可选实现)
```java
public class MainActivity extends AppCompatActivity {

    private final BillingEasy billingEasy = BillingEasy.newInstance(this)
            .addListener(new MyBillingEasyListener());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        BillingEasy.setDebug(true);
        BillingEasy.addProductConfig("id", ProductType.TYPE_INAPP_CONSUMABLE);
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
            //购买商品
            if(result.isSuccess){
                for (PurchaseInfo purchaseInfo : purchaseInfoList) {
                    if(purchaseInfo.isValid()){
                        //有效的购买
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



