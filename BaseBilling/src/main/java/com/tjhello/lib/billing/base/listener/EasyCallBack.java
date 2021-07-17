package com.tjhello.lib.billing.base.listener;

import androidx.annotation.NonNull;

import com.tjhello.lib.billing.base.info.BillingEasyResult;

/**
 * BillingEasy
 *一款全新设计的内购聚合，同时支持华为内购与谷歌内购。
 *=========================================
 * 作者:TJHello
 * 日期:2021-07-17
 * qq群:425219113
 * 仓库地址:https://gitee.com/TJHello/BillingEasy
 * ========================================
 * 使用该库请遵循Apache License2.0协议，莫要寒了广大开源者的心
 */
public interface EasyCallBack<T> {

    void callback(@NonNull BillingEasyResult result,@NonNull T info);

}
