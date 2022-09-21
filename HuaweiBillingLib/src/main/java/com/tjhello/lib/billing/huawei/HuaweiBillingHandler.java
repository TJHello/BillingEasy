package com.tjhello.lib.billing.huawei;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huawei.hmf.tasks.Task;
import com.huawei.hms.iap.Iap;
import com.huawei.hms.iap.IapApiException;
import com.huawei.hms.iap.IapClient;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseReq;
import com.huawei.hms.iap.entity.ConsumeOwnedPurchaseResult;
import com.huawei.hms.iap.entity.InAppPurchaseData;
import com.huawei.hms.iap.entity.IsEnvReadyResult;
import com.huawei.hms.iap.entity.OrderStatusCode;
import com.huawei.hms.iap.entity.OwnedPurchasesReq;
import com.huawei.hms.iap.entity.OwnedPurchasesResult;
import com.huawei.hms.iap.entity.ProductInfoReq;
import com.huawei.hms.iap.entity.ProductInfoResult;
import com.huawei.hms.iap.entity.PurchaseIntentReq;
import com.huawei.hms.iap.entity.PurchaseIntentResult;
import com.huawei.hms.iap.entity.PurchaseResultInfo;
import com.huawei.hms.support.api.client.Result;
import com.huawei.hms.support.api.client.Status;
import com.tjhello.lib.billing.base.anno.BillingName;
import com.tjhello.lib.billing.base.anno.ProductType;
import com.tjhello.lib.billing.base.handler.BillingHandler;
import com.tjhello.lib.billing.base.info.BillingEasyResult;
import com.tjhello.lib.billing.base.info.ProductConfig;
import com.tjhello.lib.billing.base.info.ProductInfo;
import com.tjhello.lib.billing.base.info.PurchaseHistoryInfo;
import com.tjhello.lib.billing.base.info.PurchaseInfo;
import com.tjhello.lib.billing.base.info.PurchaseParam;
import com.tjhello.lib.billing.base.listener.BillingEasyListener;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public class HuaweiBillingHandler extends BillingHandler {

    private static final int REQ_CODE_BUY = 3001;

    private IapClient mIapClient ;
    private static final Map<String,ProductInfo> productInfoMap = new HashMap<>();



    public HuaweiBillingHandler(BillingEasyListener mBillingEasyListener) {
        super(mBillingEasyListener);
    }

    @NonNull
    @Override
    public String getProductType(String type) {
        switch (type){
            case ProductType.TYPE_INAPP_CONSUMABLE:{
                return String.valueOf(IapClient.PriceType.IN_APP_CONSUMABLE);
            }
            case ProductType.TYPE_INAPP_NON_CONSUMABLE:
                return String.valueOf(IapClient.PriceType.IN_APP_NONCONSUMABLE);
            default:
                return String.valueOf(IapClient.PriceType.IN_APP_SUBSCRIPTION);
        }
    }

    @Override
    public void onInit(@NonNull Activity activity) {
        if(mIapClient==null){
            mIapClient = Iap.getIapClient(activity);
        }
    }

    private final AtomicBoolean isFistConnection = new AtomicBoolean(true);
    private boolean isEnvReady = true;
    @Override
    public boolean connection(@NonNull BillingEasyListener listener) {
        if(!isEnvReady) return false;
        if(isFistConnection.getAndSet(false)){
            Task<IsEnvReadyResult> task = mIapClient.isEnvReady();
            task.addOnSuccessListener(isEnvReadyResult -> {
                isEnvReady = true;
                listener.onConnection(BillingEasyResult.build(true,1,null,null));
            });
            task.addOnFailureListener(e -> {
                isEnvReady = false;
                if (e instanceof IapApiException) {
                    IapApiException apiException = (IapApiException) e;
                    Status status = apiException.getStatus();
                    if (status.getStatusCode() == OrderStatusCode.ORDER_HWID_NOT_LOGIN) {
                        // 未登录帐号
                        listener.onConnection(BillingEasyResult.build(false,status.getStatusCode(),"未登录帐号",e));
                    } else if (status.getStatusCode() == OrderStatusCode.ORDER_ACCOUNT_AREA_NOT_SUPPORTED) {
                        // 用户当前登录的华为帐号所在的服务地不在华为IAP支持结算的国家/地区中
                        listener.onConnection(BillingEasyResult.build(false,status.getStatusCode(),"华为帐号所在地区不支持华为IAP",e));
                    }else{
                        listener.onConnection(BillingEasyResult.build(false,status.getStatusCode(),"其他外部错误",e));
                    }
                }
            });
        }
        return true;
    }

    @Override
    public void queryProduct(@NonNull List<String> productCodeList, @NonNull String type, @NonNull BillingEasyListener listener) {
        Task<ProductInfoResult> task = mIapClient.obtainProductInfo(createProductInfoReq(Integer.parseInt(type),productCodeList));
        task.addOnSuccessListener((result)->{
            BillingEasyResult easyResult = createResultSuccess(result);
            List<ProductInfo> productInfoList = toProductInfo(result.getProductInfoList());
            for (ProductInfo productInfo : productInfoList) {
                productInfoMap.put(productInfo.getCode(),productInfo);
            }
            listener.onQueryProduct(easyResult,productInfoList);
            mBillingEasyListener.onQueryProduct(easyResult,productInfoList);
        });
        task.addOnFailureListener(e -> {
            BillingEasyResult easyResult = createResultFailure(e);
            List<ProductInfo> productInfoList = new ArrayList<>();
            listener.onQueryProduct(easyResult,productInfoList);
            mBillingEasyListener.onQueryProduct(easyResult,productInfoList);
        });
    }

    private ProductInfoReq createProductInfoReq(int type, List<String> ids){
        ProductInfoReq productInfoReq = new ProductInfoReq();
        productInfoReq.setPriceType(type);
        List<String> productIds = new ArrayList<>(ids);
        productInfoReq.setProductIds(productIds);
        return productInfoReq;
    }

    @Override
    public void purchase(@NonNull Activity activity, @NonNull PurchaseParam param, @NonNull String type) {
        PurchaseIntentReq req = new PurchaseIntentReq();
        req.setProductId(param.productCode);
        req.setPriceType(Integer.parseInt(type));
        req.setDeveloperPayload(param.developerPayload);
        req.setReservedInfor(param.reservedInfor);
        Task<PurchaseIntentResult> task = mIapClient.createPurchaseIntent(req);
        task.addOnSuccessListener(purchaseIntentResult -> {
            if(purchaseIntentResult==null){
                mBillingEasyListener.onPurchases(BillingEasyResult.build(false,-1,"未知错误",null),new ArrayList<>());
                return ;
            }
            Status status = purchaseIntentResult.getStatus();
            if(status==null){
                mBillingEasyListener.onPurchases(BillingEasyResult.build(false,-1,"未知错误",purchaseIntentResult),new ArrayList<>());
                return ;
            }

            if(status.hasResolution()){
                try {
                    status.startResolutionForResult(activity,REQ_CODE_BUY);
                }catch (IntentSender.SendIntentException e){
                    e.printStackTrace();
                }
            }
        });
        task.addOnFailureListener(e -> {
            BillingEasyResult easyResult = createResultFailure(e);
            List<PurchaseInfo> lis = new ArrayList<>();
            mBillingEasyListener.onPurchases(easyResult,lis);
        });
    }

    @Override
    public void consume(@NonNull String purchaseToken, @NonNull BillingEasyListener listener) {
        ConsumeOwnedPurchaseReq req = new ConsumeOwnedPurchaseReq();
        req.setPurchaseToken(purchaseToken);

        Task<ConsumeOwnedPurchaseResult> task = mIapClient.consumeOwnedPurchase(req);
        task.addOnSuccessListener(result->{
            BillingEasyResult easyResult = createResultSuccess(result);
            listener.onConsume(easyResult,purchaseToken);
            mBillingEasyListener.onConsume(easyResult,purchaseToken);
        });
        task.addOnFailureListener(e->{
            BillingEasyResult easyResult = createResultFailure(e);
            listener.onConsume(easyResult,purchaseToken);
            mBillingEasyListener.onConsume(easyResult,purchaseToken);
        });
    }

    @Override
    public void acknowledge(@NonNull String purchaseToken, @NonNull BillingEasyListener listener) {
        listener.onAcknowledge(BillingEasyResult.build(true,-1,null,null),purchaseToken);
    }

    @Override
    public void queryOrderAsync(@NonNull String type, @NonNull BillingEasyListener listener) {
        queryOrderLocal(type, listener);
    }

    @Override
    public void queryOrderLocal(@NonNull String type, @NonNull BillingEasyListener listener) {
        OwnedPurchasesReq req = new OwnedPurchasesReq();
        req.setPriceType(Integer.parseInt(type));
        Task<OwnedPurchasesResult> task = mIapClient.obtainOwnedPurchases(req);
        task.addOnSuccessListener(ownedPurchasesResult -> {
            BillingEasyResult easyResult = createResultSuccess(ownedPurchasesResult);
            List<PurchaseInfo> productInfoList = toPurchaseInfo(ownedPurchasesResult.getInAppPurchaseDataList());
            listener.onQueryOrder(easyResult,type,productInfoList);
            mBillingEasyListener.onQueryOrder(easyResult,type,productInfoList);
        });
        task.addOnFailureListener(e -> {
            BillingEasyResult easyResult = createResultFailure(e);
            List<PurchaseInfo> productInfoList = new ArrayList<>();
            listener.onQueryOrder(easyResult,type,productInfoList);
            mBillingEasyListener.onQueryOrder(easyResult,type,productInfoList);
        });
    }

    @Override
    public void queryOrderHistory(@NonNull String type, @NonNull BillingEasyListener listener) {
        OwnedPurchasesReq req = new OwnedPurchasesReq();
        req.setPriceType(Integer.parseInt(type));
        Task<OwnedPurchasesResult> task = mIapClient.obtainOwnedPurchaseRecord(req);
        task.addOnSuccessListener(ownedPurchasesResult -> {
            BillingEasyResult easyResult = createResultSuccess(ownedPurchasesResult);
            List<PurchaseHistoryInfo> productInfoList = toPurchaseHistoryInfo(ownedPurchasesResult.getInAppPurchaseDataList());
            listener.onQueryOrderHistory(easyResult,type,productInfoList);
            mBillingEasyListener.onQueryOrderHistory(easyResult,type,productInfoList);
        });
        task.addOnFailureListener(e -> {
            BillingEasyResult easyResult = createResultFailure(e);
            List<PurchaseHistoryInfo> productInfoList = new ArrayList<>();
            listener.onQueryOrderHistory(easyResult,type,productInfoList);
            mBillingEasyListener.onQueryOrderHistory(easyResult,type,productInfoList);
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQ_CODE_BUY){
            if (data == null) {
                mBillingEasyListener.onPurchases(BillingEasyResult.build(false,-1,"未知错误",null),new ArrayList<>());
                return;
            }
            PurchaseResultInfo purchaseResultInfo = mIapClient.parsePurchaseResultInfoFromIntent(data);

            BillingEasyResult easyResult =  createPurchaseResultSuccess(purchaseResultInfo);
            List<PurchaseInfo> infoList = new ArrayList<>();
            PurchaseInfo purchaseInfo = toPurchaseInfo(purchaseResultInfo.getInAppPurchaseData());
            if(purchaseInfo!=null){
                infoList.add(purchaseInfo);
            }
            mBillingEasyListener.onPurchases(easyResult,infoList);
        }
    }

    @Override
    public String getBillingName() {
        return BillingName.HUAWEI;
    }

    private static List<ProductInfo> toProductInfo(@Nullable List<com.huawei.hms.iap.entity.ProductInfo> list){
        if(list==null||list.isEmpty()) return new ArrayList<>();
        List<ProductInfo> infoList = new ArrayList<>();
        for(int i=0;i<list.size();i++){
            com.huawei.hms.iap.entity.ProductInfo skuDetails = list.get(i);
            ProductInfo info = toProductInfo(skuDetails);
            infoList.add(info);
        }
        return infoList;
    }

    private static List<PurchaseHistoryInfo> toPurchaseHistoryInfo(@Nullable List<String> list){
        if(list==null||list.isEmpty()) return new ArrayList<>();
        List<PurchaseHistoryInfo> purchaseHistoryInfoList = new ArrayList<>();
        for (String data : list) {
            try {
                InAppPurchaseData inAppPurchaseData = new InAppPurchaseData(data);
                int state = inAppPurchaseData.getPurchaseState();

                PurchaseHistoryInfo info = new PurchaseHistoryInfo();
                info.setPurchaseToken(inAppPurchaseData.getPurchaseToken());
                info.setPurchaseTime(inAppPurchaseData.getPurchaseTime());
                info.setBaseObj(inAppPurchaseData);

                ProductConfig productConfig = findProductInfo(inAppPurchaseData.getProductId());
                if(productConfig!=null){
                    info.addProduct(productConfig);
                }

                PurchaseHistoryInfo.HuaweiBillingPurchaseHistory huaweiBillingPurchaseHistory = new PurchaseHistoryInfo.HuaweiBillingPurchaseHistory();

                huaweiBillingPurchaseHistory.setApplicationId(inAppPurchaseData.getApplicationId());
                huaweiBillingPurchaseHistory.setAutoRenewing(inAppPurchaseData.isAutoRenewing());
                huaweiBillingPurchaseHistory.setOrderId(inAppPurchaseData.getOrderID());
                huaweiBillingPurchaseHistory.setPackageName(inAppPurchaseData.getPackageName());
                huaweiBillingPurchaseHistory.setProductId(inAppPurchaseData.getProductId());
                huaweiBillingPurchaseHistory.setProductName(inAppPurchaseData.getProductName());
                huaweiBillingPurchaseHistory.setPurchaseTime(inAppPurchaseData.getPurchaseTime());
                huaweiBillingPurchaseHistory.setPurchaseState(inAppPurchaseData.getPurchaseState());
                huaweiBillingPurchaseHistory.setDeveloperPayload(inAppPurchaseData.getDeveloperPayload());
                huaweiBillingPurchaseHistory.setPurchaseToken(inAppPurchaseData.getPurchaseToken());
                huaweiBillingPurchaseHistory.setPurchaseType(inAppPurchaseData.getPurchaseType());
                huaweiBillingPurchaseHistory.setCurrency(inAppPurchaseData.getCurrency());
                huaweiBillingPurchaseHistory.setPrice(inAppPurchaseData.getPrice());
                huaweiBillingPurchaseHistory.setCountry(inAppPurchaseData.getCountry());
                huaweiBillingPurchaseHistory.setLastOrderId(inAppPurchaseData.getLastOrderId());
                huaweiBillingPurchaseHistory.setProductGroup(inAppPurchaseData.getProductGroup());
                huaweiBillingPurchaseHistory.setOriPurchaseTime(inAppPurchaseData.getOriPurchaseTime());
                huaweiBillingPurchaseHistory.setSubscriptionId(inAppPurchaseData.getSubscriptionId());
                info.setHuaweiBillingPurchaseHistory(huaweiBillingPurchaseHistory);


                purchaseHistoryInfoList.add(info);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return purchaseHistoryInfoList;
    }

    private static List<PurchaseInfo> toPurchaseInfo(@Nullable List<String> list){
        if(list==null||list.isEmpty()) return new ArrayList<>();
        List<PurchaseInfo> purchaseInfoList = new ArrayList<>();
        for (String data : list) {
            PurchaseInfo purchaseInfo = toPurchaseInfo(data);
            if(purchaseInfo!=null){
                purchaseInfoList.add(purchaseInfo);
            }
        }
        return purchaseInfoList;
    }

    @Nullable
    private static PurchaseInfo toPurchaseInfo(@Nullable String data){
        if(data==null||data.isEmpty()) return null;
        try {
            InAppPurchaseData inAppPurchaseData = new InAppPurchaseData(data);
            int state = inAppPurchaseData.getPurchaseState();

            PurchaseInfo info = new PurchaseInfo();
            info.setPurchaseToken(inAppPurchaseData.getPurchaseToken());
            info.setOrderId(inAppPurchaseData.getOrderID());
            info.setBaseObj(inAppPurchaseData);

            info.setAcknowledged(true);//华为不需要确认购买
            info.setAutoRenewing(inAppPurchaseData.isAutoRenewing());
            info.setValid(state== InAppPurchaseData.PurchaseState.PURCHASED);
            info.setPurchaseTime(inAppPurchaseData.getPurchaseTime());

            ProductConfig productConfig = findProductInfo(inAppPurchaseData.getProductId());
            if(productConfig!=null){
                info.addProduct(productConfig);
                if(productInfoMap.containsKey(productConfig.getCode())){
                    ProductInfo productInfo = productInfoMap.get(productConfig.getCode());
                    if(productInfo!=null){
                        info.putProductInfo(productConfig.getCode(),productInfo);
                    }
                }
            }

            PurchaseInfo.HuaweiBillingPurchase huaweiBillingPurchase = new PurchaseInfo.HuaweiBillingPurchase();

            huaweiBillingPurchase.setApplicationId(inAppPurchaseData.getApplicationId());
            huaweiBillingPurchase.setAutoRenewing(inAppPurchaseData.isAutoRenewing());
            huaweiBillingPurchase.setOrderId(inAppPurchaseData.getOrderID());
            huaweiBillingPurchase.setPackageName(inAppPurchaseData.getPackageName());
            huaweiBillingPurchase.setProductId(inAppPurchaseData.getProductId());
            huaweiBillingPurchase.setProductName(inAppPurchaseData.getProductName());
            huaweiBillingPurchase.setPurchaseTime(inAppPurchaseData.getPurchaseTime());
            huaweiBillingPurchase.setPurchaseState(inAppPurchaseData.getPurchaseState());
            huaweiBillingPurchase.setDeveloperPayload(inAppPurchaseData.getDeveloperPayload());
            huaweiBillingPurchase.setPurchaseToken(inAppPurchaseData.getPurchaseToken());
            huaweiBillingPurchase.setPurchaseType(inAppPurchaseData.getPurchaseType());
            huaweiBillingPurchase.setCurrency(inAppPurchaseData.getCurrency());
            huaweiBillingPurchase.setPrice(inAppPurchaseData.getPrice());
            huaweiBillingPurchase.setCountry(inAppPurchaseData.getCountry());
            huaweiBillingPurchase.setLastOrderId(inAppPurchaseData.getLastOrderId());
            huaweiBillingPurchase.setProductGroup(inAppPurchaseData.getProductGroup());
            huaweiBillingPurchase.setOriPurchaseTime(inAppPurchaseData.getOriPurchaseTime());
            huaweiBillingPurchase.setSubscriptionId(inAppPurchaseData.getSubscriptionId());
            info.setHuaweiBillingPurchase(huaweiBillingPurchase);

            return info;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    private static ProductInfo toProductInfo(com.huawei.hms.iap.entity.ProductInfo skuDetails){
        ProductInfo info = new ProductInfo();
        info.setCode(skuDetails.getProductId());
        info.setPrice(skuDetails.getPrice());
        info.setPriceMicros(skuDetails.getMicrosPrice());
        info.setTitle(skuDetails.getProductName());
        info.setDesc(skuDetails.getProductDesc());
        ProductConfig find = findProductInfo(skuDetails.getProductId());
        if(find!=null){
            info.setType(find.getType());
        }
        ProductInfo.HuaweiProductInfo huaweiProductInfo = new ProductInfo.HuaweiProductInfo();
        huaweiProductInfo.setProductId(skuDetails.getProductId());
        huaweiProductInfo.setPriceType(skuDetails.getPriceType());
        huaweiProductInfo.setPrice(skuDetails.getPrice());
        huaweiProductInfo.setMicrosPrice(skuDetails.getMicrosPrice());
        huaweiProductInfo.setOriginalLocalPrice(skuDetails.getOriginalLocalPrice());
        huaweiProductInfo.setOriginalMicroPrice(skuDetails.getOriginalMicroPrice());
        huaweiProductInfo.setCurrency(skuDetails.getCurrency());
        huaweiProductInfo.setProductName(skuDetails.getProductName());
        huaweiProductInfo.setProductDesc(skuDetails.getProductDesc());
        huaweiProductInfo.setSubPeriod(skuDetails.getSubPeriod());
        huaweiProductInfo.setSubSpecialPrice(skuDetails.getSubSpecialPrice());
        huaweiProductInfo.setSubSpecialPriceMicros(skuDetails.getSubSpecialPriceMicros());
        huaweiProductInfo.setSubSpecialPeriod(skuDetails.getSubSpecialPeriod());
        huaweiProductInfo.setSubSpecialPeriodCycles(skuDetails.getSubSpecialPeriodCycles());
        huaweiProductInfo.setSubFreeTrialPeriod(skuDetails.getSubFreeTrialPeriod());
        huaweiProductInfo.setSubGroupId(skuDetails.getSubGroupId());
        huaweiProductInfo.setSubGroupTitle(skuDetails.getSubGroupTitle());
        huaweiProductInfo.setSubProductLevel(skuDetails.getSubProductLevel());
        huaweiProductInfo.setStatus(skuDetails.getStatus());
        info.setBaseObj(skuDetails);
        return info;
    }


    private <T extends Result>BillingEasyResult createResultSuccess(T result){
        BillingEasyResult easyResult = new BillingEasyResult();
        easyResult.isSuccess = true;
        easyResult.isError = false;
        easyResult.isErrorOwned = false;
        easyResult.isCancel = false;
        easyResult.state = BillingEasyResult.State.SUCCESS;
        Status status = result.getStatus();
        easyResult.responseCode = String.valueOf(status.getStatusCode());
        easyResult.responseMsg = status.getStatusMessage();
        return easyResult;
    }

    private BillingEasyResult createPurchaseResultSuccess(PurchaseResultInfo result){
        BillingEasyResult easyResult = new BillingEasyResult();
        easyResult.state = BillingEasyResult.State.SUCCESS;
        easyResult.responseCode = String.valueOf(result.getReturnCode());
        easyResult.isSuccess = result.getReturnCode()==OrderStatusCode.ORDER_STATE_SUCCESS;
        easyResult.isError = !easyResult.isSuccess;
        easyResult.isCancel = result.getReturnCode()==OrderStatusCode.ORDER_STATE_CANCEL;
        easyResult.isErrorOwned = result.getReturnCode()==OrderStatusCode.ORDER_PRODUCT_OWNED;

        switch (result.getReturnCode()){
            case OrderStatusCode.ORDER_STATE_SUCCESS:{
                easyResult.state = BillingEasyResult.State.SUCCESS;
            }break;
            case OrderStatusCode.ORDER_STATE_CANCEL:{
                easyResult.state = BillingEasyResult.State.CANCEL;
            }break;
            case OrderStatusCode.ORDER_PRODUCT_OWNED:{
                easyResult.state = BillingEasyResult.State.ERROR_OWNED;

            }break;
            case OrderStatusCode.ORDER_PRODUCT_NOT_OWNED:{
                easyResult.state = BillingEasyResult.State.ERROR_NOT_OWNED;

            }break;
            default:{
                easyResult.state = BillingEasyResult.State.ERROR_OTHER;
            }
        }
        easyResult.responseMsg = result.getErrMsg();
        return easyResult;
    }

    private BillingEasyResult createResultFailure(Exception result){
        BillingEasyResult easyResult = new BillingEasyResult();
        easyResult.isSuccess = false;
        easyResult.isError = true;
        easyResult.isErrorOwned = false;
        easyResult.isCancel = false;
        easyResult.state = BillingEasyResult.State.ERROR_OTHER;

        if(result instanceof IapApiException){
            IapApiException apiException = (IapApiException) result;
            int returnCode = apiException.getStatusCode();
            easyResult.responseCode = String.valueOf(returnCode);
            easyResult.responseMsg = apiException.getMessage();
        }else{
            easyResult.responseCode = "-1";
            easyResult.responseMsg = "其他外部错误";
        }

        return easyResult;
    }
}
