# BillingEasy-2.0.1

**QQ交流群(425219113)**

使用该库请遵循Apache License2.0协议，莫要寒了广大开源者的心。

---

一款全新设计的内购聚合，同时支持华为内购与谷歌内购。

1. 使用了模块化技术，可以实现按需切换平台，而不残留任何平台代码。
2. 重新设计了所有接口，去除了业务相关逻辑，还给大家原生操作。
3. 运用了大量callback，提供更加便捷的即用即回API。

**(目前还没有支持华为内购)**

---

### 使用步骤
- ### Step1 添加远程仓库地址-build.gradle(project)
```groovy

allprojects {
     repositories {
         maven { url 'https://tjhello.gitee.io/publiclib/'}
         maven {url 'https://developer.huawei.com/repo/'}//华为用到
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

    //测试版
    implementation 'com.TJHello.easy:BillingEasy:2.0.1-t01'//BillingEasy
    implementation 'com.TJHello.publicLib.billing:google:4.0.0.201-t01'//Google内购(按需添加)
    implementation 'com.TJHello.publicLib.billing:huawei:5.1.0.300.201-t01'//Huawei内购(按需添加)
}

```

- ### Step3 代码示例([MainActivity](https://gitee.com/TJHello/BillingEasy/blob/master/app/src/main/java/com/tjhello/app/easy/billing/MainActivity.java))
```java
public class MainActivity extends AppCompatActivity {

    private final MyBillingEasyListener billingEasyListener = new MyBillingEasyListener();
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //BillingEasyStatic的用法看v2/MainActivity

        BillingEasy.setDebug(true);
        BillingEasy.addProductConfig(ProductType.TYPE_INAPP_CONSUMABLE,"可消耗商品code","可消耗商品code");
        BillingEasy.addProductConfig(ProductType.TYPE_INAPP_NON_CONSUMABLE,"非消耗商品code","非消耗商品code");
        BillingEasy.addProductConfig(ProductType.TYPE_SUBS,"订阅商品code","订阅商品code");
        ProductConfig productConfig = ProductConfig.build(ProductType.TYPE_INAPP_NON_CONSUMABLE,"test_code","noads");//添加一个带去广告属性的商品
        BillingEasy.addProductConfig(productConfig);
        BillingEasy.addListener(billingEasyListener);//添加完整监听器
        BillingEasy.init(this);

        //查询商品信息-两种用法
        BillingEasy.queryProduct();
        BillingEasy.queryProduct((billingEasyResult, productInfoList) -> {

        });
        //发起购买-两种用法
        BillingEasy.purchase(this,"商品code");
        BillingEasy.purchase(this,"商品code", (billingEasyResult, purchaseInfoList) -> {

        });

    }

    //仅华为内购需要调用
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        BillingEasy.onActivityResult(requestCode,resultCode,data);
    }

    @Override
    protected void onDestroy() {
        //多activity情况下注意移除监听器，避免内存泄漏
        BillingEasy.removeListener(billingEasyListener);
        super.onDestroy();
    }

    //以下接口只要支持了JAVA8，都是可选实现
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
            //如已开启自动消耗与购买，则不需要手动消耗与购买
            //购买商品，判断示例
            if(result.isSuccess){
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
BillingEasy.addProductConfig( ProductType.TYPE_INAPP_CONSUMABLE,"商品code","商品code");

//查询商品信息
BillingEasy.queryProduct();
BillingEasy.queryProduct((billingEasyResult, productInfoList) -> {

});
//发起购买
BillingEasy.purchase(activity,"商品code");
BillingEasy.purchase(activity,"商品code", (billingEasyResult, purchaseInfoList) -> {

});
//查询订单信息
BillingEasy.queryOrderAsync();//联网查询有效订单
BillingEasy.queryOrderLocal();//查询本地缓存订单
BillingEasy.queryOrderHistory();//查询历史订单
//消耗商品
BillingEasy.consume("purchaseToken");
//确认购买
BillingEasy.acknowledge("purchaseToken");

```

- ### 常见问题

1. 谷歌内购3.0.2以及以上版本要求谷歌商店的版本在:25.8.21以上，已知21.24.18无法购买，升级谷歌商店可到apkpure。


- ### 更新日志

2.0.1-t01 2022/04/28 【测试版】
```
1、该版本支持了华为内购，API略微改动
2、优化了使用体验
```

0.1.2 2022/01/27
```
1、queryOrderLocal与queryOrderHistory不生效的问题
2、移除自动查询订单功能，需要查询订单则自行调用queryOrder系列方法

```


0.1.2-a02 2021/12/8
```
1、修复个别时候在购买成功回调里无法获得商品详情的问题。

```

0.1.2-a01 2021/11/26
```
1、删除原有的BillingEasy做法，将BillingEasyStatic改名为BillingEasy，采用全静态操作的做法。

```

0.1.1-t14 2021/11/23
```
1、PurchaseInfo增加getProductInfo方法，可方便获取商品信息。
```

0.1.1-t13 2021/10/27
```
1、修复自动消耗与自动确认购买的逻辑开关无效的问题
2、修复googleSkuDetails.type没有值的问题
```

0.1.1-t12 2021/10/15
```
1、添加发起购买的时候，因获取商品信息失败而导致的错误回调

```

0.1.1-t11 2021/09/25
```
1、添加自动消耗与自动确认购买的逻辑，默认关闭。
```


0.1.1-t10 2021/09/16
```
1、修复-商品配置信息无法全局动态添加引发的系列的问题。
2、增加-初始化添加链接内购服务器成功的回调
```

0.1.1-t09 2021/09/08
```
1、添加BillingEasyStatic类，全静态操作
2、去除BillingEasy和BillingManager的免混淆规则
```

0.1.1-t08 2021/08/25（存在bug，已删除）
```
1、添加商品配置自动去重功能
2、添加cleanProductConfig接口，清除所有商品配置
```

0.1.1-t07 2021/08/02
```
1.修复购买成功接口会多重复回调一次的bug。
```


0.1.1-t06
```
1.优化商品详情的查询逻辑，避免多余的结果回调。
```

0.1.1-t05
```
1.修复消耗商品没有传入callback会导致空指针报错的问题。
```



