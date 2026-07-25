# BillingEasy AI 接入文档

本文档面向 AI/协作代理，用于快速判断 BillingEasy 的接入方式、核心代码位置和修改边界。正式 README 面向使用者展示完整示例；本文档更偏向代码导航、接入决策和常见误区约束。

## 1. 项目定位

BillingEasy 是一个 Android 内购聚合库，核心 API 在 `JavaLib`，平台能力由独立适配器模块按需提供。

核心原则：

- `JavaLib` 是主 SDK，暴露 `BillingEasy` 静态门面。
- `BaseBilling` 是公共模型、监听器、抽象处理器和反射装配层。
- `GoogleBillingLib*` 是 Google Play Billing 适配器，按需接入。
- `HuaweiBillingLib*` 是 Huawei IAP 适配器，按需接入。
- 不要同时接入 Google 和 Huawei 适配器。代码中会优先发现 Google 适配器，但业务接入上应只保留一个平台适配器。
- `app` 是示例工程，不是 SDK 主体。
- `KotlinLib` 不是当前文档的核心接入目标；用户明确要求时再单独阅读。

## 2. 模块导航

| 模块 | 作用 | AI 阅读优先级 |
| --- | --- | --- |
| `JavaLib` | 主 SDK，提供 `com.tjhello.easy.billing.java.BillingEasy` | 必读 |
| `BaseBilling` | 公共类型、商品配置、订单模型、监听器、适配器抽象 | 必读 |
| `GoogleBillingLib600` | Google Billing 7.1.1 适配器，发布版本 `7.1.1.204` | Google 接入优先读 |
| `GoogleBillingLib500` | Google Billing 5.1.0 适配器，发布版本 `5.1.0.205` | 旧版本兼容时读 |
| `GoogleBillingLib` | Google Billing 4.0.0 适配器，发布版本 `4.0.0.204` | 旧版本兼容时读 |
| `HuaweiBillingLib640` | Huawei IAP 6.4.0.301 适配器，发布版本 `6.4.0.301.204-t09` | Huawei 接入时读 |
| `HuaweiBillingLib` | Huawei IAP 5.1.0.300 适配器，发布版本 `5.1.0.300.204-t13` | Huawei 旧版本兼容时读 |
| `app` | 示例 Activity 和示例依赖 | 查示例时读 |

关键文件：

- `JavaLib/src/main/java/com/tjhello/easy/billing/java/BillingEasy.java`
- `JavaLib/src/main/java/com/tjhello/easy/billing/java/BillingManager.java`
- `BaseBilling/src/main/java/com/tjhello/lib/billing/base/handler/BillingHandler.java`
- `BaseBilling/src/main/java/com/tjhello/lib/billing/base/listener/BillingEasyListener.java`
- `BaseBilling/src/main/java/com/tjhello/lib/billing/base/info/ProductConfig.java`
- `BaseBilling/src/main/java/com/tjhello/lib/billing/base/info/ProductInfo.java`
- `BaseBilling/src/main/java/com/tjhello/lib/billing/base/info/PurchaseInfo.java`
- `BaseBilling/src/main/java/com/tjhello/lib/billing/base/info/PurchaseParam.java`
- `GoogleBillingLib600/src/main/java/com/tjhello/lib/billing/google/GoogleBillingHandler.java`

## 3. 依赖接入

项目发布坐标来自各模块 `build.gradle`。

主 SDK：

```groovy
implementation 'com.TJHello.easy:BillingEasy:2.0.4'
```

Google 适配器按需选择一个版本。当前 README 推荐 Google Billing 7.1.1 对应版本：

```groovy
implementation 'com.TJHello.publicLib.billing:google:7.1.1.204'
```

旧 Google 适配器版本：

```groovy
implementation 'com.TJHello.publicLib.billing:google:5.1.0.205'
implementation 'com.TJHello.publicLib.billing:google:4.0.0.204'
```

Huawei 适配器按需选择一个版本：

```groovy
implementation 'com.TJHello.publicLib.billing:huawei:6.4.0.301.204-t09'
implementation 'com.TJHello.publicLib.billing:huawei:5.1.0.300.204-t13'
```

仓库配置参考根目录 `README.md`，需要包含 Google、MavenCentral、私有 Maven 仓库；接入 Huawei 时还需要 Huawei Maven 仓库。

本地模块调试时示例依赖方式：

```groovy
api project(':JavaLib')
api project(':GoogleBillingLib600')
```

不要在同一个 App 中同时加入 `GoogleBillingLib*` 和 `HuaweiBillingLib*`。`BaseBilling` 通过类名反射查找适配器：

- `com.tjhello.lib.billing.google.GoogleBillingHandler`
- `com.tjhello.lib.billing.huawei.HuaweiBillingHandler`

如果找不到任何适配器，会退回 `EmptyHandler`，所有支付能力不可用，并输出“没有检测到任何内购库”日志。

## 4. 最小接入流程

接入顺序应保持稳定：

1. 添加主 SDK 依赖。
2. 添加一个平台适配器依赖。
3. 配置商品。
4. 添加监听器。
5. 初始化 SDK。
6. 查询商品或订单。
7. 发起购买。
8. 在购买回调或补单回调中发货，并按商品类型消耗或确认购买。
9. 页面销毁时移除监听器。

最小 Java 示例：

```java
public class PayActivity extends AppCompatActivity {

    private final BillingEasyListener billingListener = new BillingEasyListener() {
        @Override
        public void onConnection(@NonNull BillingEasyResult result) {
            if (result.isSuccess) {
                BillingEasy.queryProduct();
                BillingEasy.queryOrderAsync(ProductType.TYPE_INAPP_CONSUMABLE);
                BillingEasy.queryOrderAsync(ProductType.TYPE_SUBS);
            }
        }

        @Override
        public void onPurchases(@NonNull BillingEasyResult result,
                                @NonNull List<PurchaseInfo> purchaseInfoList) {
            handlePurchases(result, purchaseInfoList);
        }

        @Override
        public void onQueryOrder(@NonNull BillingEasyResult result,
                                 @NonNull String type,
                                 @NonNull List<PurchaseInfo> purchaseInfoList) {
            handlePurchases(result, purchaseInfoList);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        BillingEasy.setDebug(BuildConfig.DEBUG);
        BillingEasy.addProductConfig(ProductType.TYPE_INAPP_CONSUMABLE, "coins_100");
        BillingEasy.addProductConfig(ProductType.TYPE_INAPP_NON_CONSUMABLE, "remove_ads");
        BillingEasy.addProductConfig(ProductType.TYPE_SUBS, "vip_monthly");

        BillingEasy.addListener(billingListener);
        BillingEasy.init(this);
    }

    private void buyCoins() {
        BillingEasy.purchase(this, "coins_100");
    }

    private void buySubscription(String offerToken) {
        PurchaseParam param = new PurchaseParam.GoogleBuilder("vip_monthly")
                .setOfferToken(offerToken)
                .build();
        BillingEasy.purchase(this, param);
    }

    private void handlePurchases(BillingEasyResult result, List<PurchaseInfo> purchaseInfoList) {
        if (!result.isSuccess && result.state != BillingEasyResult.State.ERROR_OWNED) {
            return;
        }

        for (PurchaseInfo purchaseInfo : purchaseInfoList) {
            if (!purchaseInfo.isValid()) {
                continue;
            }

            for (ProductConfig productConfig : purchaseInfo.getProductList()) {
                // 先由业务侧校验订单、去重发货，再消耗或确认购买。
                deliverOnce(purchaseInfo, productConfig);

                if (productConfig.canConsume()) {
                    BillingEasy.consume(purchaseInfo.getPurchaseToken());
                } else if (!purchaseInfo.isAcknowledged()) {
                    BillingEasy.acknowledge(purchaseInfo.getPurchaseToken());
                }
            }
        }
    }

    private void deliverOnce(PurchaseInfo purchaseInfo, ProductConfig productConfig) {
        // 业务自行实现：服务端验签、订单去重、发货入账。
    }

    @Override
    protected void onDestroy() {
        BillingEasy.removeListener(billingListener);
        super.onDestroy();
    }
}
```

## 5. 核心 API

`BillingEasy` 是主要入口，位于 `JavaLib`。

初始化：

```java
BillingEasy.setDebug(true);
BillingEasy.addListener(listener);
BillingEasy.init(activity);
BillingEasy.init(activity, (result, isSuccess) -> {});
```

商品配置：

```java
BillingEasy.addProductConfig(ProductType.TYPE_INAPP_CONSUMABLE, "product_code");
BillingEasy.addProductConfig(ProductType.TYPE_INAPP_NON_CONSUMABLE, "product_code");
BillingEasy.addProductConfig(ProductType.TYPE_SUBS, "product_code");

ProductConfig config = ProductConfig.build(
        ProductType.TYPE_INAPP_NON_CONSUMABLE,
        "remove_ads",
        "noads"
);
BillingEasy.addProductConfig(config);
BillingEasy.cleanProductConfig();
```

查询商品：

```java
BillingEasy.queryProduct();
BillingEasy.queryProduct((result, productInfoList) -> {});
BillingEasy.queryProduct(ProductType.TYPE_INAPP_CONSUMABLE);
BillingEasy.queryProduct(ProductType.TYPE_SUBS, (result, productInfoList) -> {});
BillingEasy.queryProduct(ProductType.TYPE_SUBS, codeList, (result, productInfoList) -> {});
```

购买：

```java
BillingEasy.purchase(activity, "product_code");

PurchaseParam googleParam = new PurchaseParam.GoogleBuilder("product_code")
        .setObfuscatedAccountId("account_id_hash")
        .setObfuscatedProfileId("profile_id_hash")
        .setOfferToken("subscription_offer_token")
        .build();
BillingEasy.purchase(activity, googleParam);
```

订单查询：

```java
BillingEasy.queryOrderAsync(ProductType.TYPE_INAPP_CONSUMABLE);
BillingEasy.queryOrderLocal(ProductType.TYPE_INAPP_CONSUMABLE);
BillingEasy.queryOrderHistory(ProductType.TYPE_SUBS);
```

消耗与确认：

```java
BillingEasy.consume(purchaseToken);
BillingEasy.consume(purchaseToken, (result, token) -> {});

BillingEasy.acknowledge(purchaseToken);
BillingEasy.acknowledge(purchaseToken, (result, token) -> {});
```

监听器：

```java
public interface BillingEasyListener {
    default void onConnection(@NonNull BillingEasyResult result) {}
    default void onDisconnected() {}
    default void onQueryProduct(@NonNull BillingEasyResult result,
                                @ProductType String type,
                                @NonNull List<ProductInfo> productInfoList) {}
    default void onPurchases(@NonNull BillingEasyResult result,
                             @NonNull List<PurchaseInfo> purchaseInfoList) {}
    default void onConsume(@NonNull BillingEasyResult result,
                           @NonNull String purchaseToken) {}
    default void onAcknowledge(@NonNull BillingEasyResult result,
                               @NonNull String purchaseToken) {}
    default void onQueryOrder(@NonNull BillingEasyResult result,
                              @ProductType String type,
                              @NonNull List<PurchaseInfo> purchaseInfoList) {}
    default void onQueryOrderHistory(@NonNull BillingEasyResult result,
                                     @ProductType String type,
                                     @NonNull List<PurchaseHistoryInfo> purchaseInfoList) {}
}
```

## 6. 商品类型语义

BillingEasy 自定义商品类型位于 `ProductType`：

| 类型 | 值 | 业务语义 |
| --- | --- | --- |
| `TYPE_INAPP_CONSUMABLE` | `inapp-consumable` | 可消耗内购，例如金币 |
| `TYPE_INAPP_NON_CONSUMABLE` | `inapp-non-consumable` | 非消耗内购，例如去广告 |
| `TYPE_SUBS` | `subs` | 订阅商品 |

Google Play Billing 只有 `INAPP` 和 `SUBS` 两类。BillingEasy 在 Google 适配器中把 `TYPE_INAPP_CONSUMABLE` 与 `TYPE_INAPP_NON_CONSUMABLE` 都映射到 Google `INAPP`，两者差异只用于业务侧判断是否消耗。

因此：

- Google 查询商品时，同一个内购商品 code 不要同时配置为消耗型和非消耗型。
- 购买成功后必须根据 `ProductConfig.canConsume()` 或 `ProductConfig.getType()` 决定消耗还是确认。
- `consume()` 包含确认购买语义；非消耗和订阅使用 `acknowledge()`。

## 7. 自动消耗与自动确认

默认关闭：

```java
BillingEasy.setAutoConsume(false);
BillingEasy.setAutoAcknowledge(false);
```

开启后，`BillingEasy` 内部监听购买与补单回调：

- 可消耗商品自动调用 `consume()`。
- 非消耗和订阅在未确认时自动调用 `acknowledge()`。
- 自动消耗和自动确认失败时最多重试 3 次。

AI 修改业务接入代码时应谨慎开启自动模式。更稳妥的流程是业务先完成服务端校验、订单去重和发货，再手动调用 `consume()` 或 `acknowledge()`。

## 8. Google 适配器注意事项

Google 当前推荐看 `GoogleBillingLib600`，虽然目录名包含 `600`，但发布版本是 `7.1.1.204`，内部依赖：

```groovy
api 'com.android.billingclient:billing:7.1.1'
```

关键实现点：

- 初始化时构建 `BillingClient`，调用 `enablePendingPurchases()`。
- 商品查询优先使用 `ProductDetails`；客户端不支持时回退 `SkuDetails`。
- 发起购买前会从缓存中取 `ProductDetails` 或 `SkuDetails`。
- 如果购买时没有商品详情缓存，会先查询该商品再尝试购买。
- 订阅商品如果没有显式传入 `offerToken`，会默认使用第一个 `SubscriptionOfferDetails.offerToken`。
- 购买结果统一回调到 `onPurchases()`。
- 有效订单查询通过 `queryPurchasesAsync()`。
- 历史订单查询通过 `queryPurchaseHistoryAsync()`。
- 适配器类有 `@Keep`，consumer rules 也保留 `GoogleBillingHandler`，因为 `BaseBilling` 需要反射创建。

订阅折扣或多 offer 场景：

1. 先调用 `BillingEasy.queryProduct(ProductType.TYPE_SUBS, callback)`。
2. 从 `ProductInfo.getGoogleSkuDetails().getSubscriptionOfferDetails()` 中选择业务需要的 offer。
3. 使用 `new PurchaseParam.GoogleBuilder(productCode).setOfferToken(token).build()` 发起购买。

## 9. 结果与订单处理

`BillingEasyResult` 关键字段：

| 字段 | 说明 |
| --- | --- |
| `isSuccess` | 操作是否成功 |
| `state` | 推荐判断字段，包含成功、取消、已拥有、未拥有、其他错误 |
| `responseCode` | 平台返回码字符串 |
| `responseMsg` | 平台返回消息 |
| `baseObj` | 平台原始返回对象 |
| `isCancel`、`isError`、`isErrorOwned` | 旧字段，保留兼容，新增代码优先判断 `state` |

`PurchaseInfo` 关键字段：

- `isValid()`：订单是否有效。Google 中等于 `PurchaseState.PURCHASED`。
- `getProductList()`：该订单包含的 `ProductConfig` 列表。
- `getPurchaseToken()`：消耗、确认和服务端校验的关键 token。
- `getOrderId()`：订单号。
- `isAcknowledged()`：是否已经确认购买。
- `isAutoRenewing()`：订阅是否自动续订。
- `getGoogleBillingPurchase()`：Google 详细订单字段。
- `getHuaweiBillingPurchase()`：Huawei 详细订单字段。

业务建议：

- 任何回调都先判断 `result.isSuccess` 或 `result.state`。
- 发货前必须做订单去重，避免重复回调造成重复发货。
- 延迟付款或补单场景可能在 App 启动后通过 `queryOrderAsync()` 再次返回订单。
- 可消耗商品发货后调用 `consume()`。
- 非消耗商品和订阅发货后，如果 `!purchaseInfo.isAcknowledged()`，调用 `acknowledge()`。

## 10. AI 修改边界

当用户要求“接入 BillingEasy”时，优先改宿主 App：

- 添加主 SDK 依赖和一个平台适配器依赖。
- 在支付入口或 Application/主 Activity 配置商品与监听器。
- 在 Activity 生命周期中调用 `BillingEasy.init(activity)`。
- 在销毁时移除监听器，避免 Activity 泄漏。
- 购买按钮只调用 `BillingEasy.purchase()`。
- 补单逻辑放在 `onQueryOrder()` 或启动后主动调用 `queryOrderAsync()`。

不要默认修改 SDK 内部模块，除非用户明确要求开发库能力：

- 不要随意改 `BillingHandler.createBillingHandler()` 的反射顺序。
- 不要把 Google 与 Huawei 适配器合并为同时接入。
- 不要删除 consumer rules 中的适配器 keep 规则。
- 不要把 `TYPE_INAPP_NON_CONSUMABLE` 映射成 Google 新类型；Google 仍走 `INAPP`。
- 不要把 `queryOrderLocal()` 文案理解成真正离线缓存。Google 7 适配器里它同样调用 `queryPurchasesAsync()`。

代码维护约束：

- 本项目协作要求中文注释、中文日志、中文交流。
- 修改代码前先给出方案和评分。
- Codex 不直接运行项目。
- 如果需要 `flutter analyze`，只能指定当次修改过的脚本；本项目是 Android/Java 库，通常不涉及 Flutter。

## 11. 常见接入错误

1. 只接入 `BillingEasy`，没有接入平台适配器。
   - 结果：反射找不到 `GoogleBillingHandler` 或 `HuaweiBillingHandler`，退回 `EmptyHandler`。

2. 同时接入 Google 与 Huawei 适配器。
   - 结果：代码会优先创建 Google handler，但业务平台选择不清晰，打包也会残留不需要的平台代码。

3. 购买前没有配置商品 code。
   - 结果：`BillingManager.findProductConfig()` 找不到配置，购买不会继续。

4. Google 非消耗商品误配置为消耗型。
   - 结果：购买成功后可能被业务逻辑调用 `consume()`，导致用户可重复购买。

5. 只处理 `onPurchases()`，不处理 `onQueryOrder()`。
   - 结果：补单、延迟付款、App 重启后的未处理订单可能漏发货。

6. 发货前没有去重。
   - 结果：购买回调、补单回调、历史查询等多次返回同一订单时可能重复发货。

7. 订阅多 offer 不传 `offerToken`。
   - 结果：Google 7 适配器默认使用第一个 offer，可能不是业务期望的折扣方案。

## 12. 快速检查清单

AI 生成或审查接入代码时，至少检查：

- 是否引入 `com.TJHello.easy:BillingEasy:2.0.4`。
- 是否只引入一个平台适配器。
- 是否添加了所有商品 code。
- 是否在初始化前添加监听器。
- 是否在页面销毁时移除监听器。
- 是否处理 `onPurchases()` 与 `onQueryOrder()`。
- 是否先校验 `result.isSuccess` 或 `result.state`。
- 是否先发货并去重，再消耗或确认购买。
- Google 订阅多 offer 是否显式选择 `offerToken`。
- 是否避免把示例 `商品code`、`内购商品code`、`订阅商品code` 留在正式代码中。
