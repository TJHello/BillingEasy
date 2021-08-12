package com.tjhello.lib.billing.base.anno;

import androidx.annotation.StringDef;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD,ElementType.METHOD})
@StringDef(value = {BillingName.GOOGLE,BillingName.HUAWEI,BillingName.EMPTY})
public @interface BillingName {

    public static final String GOOGLE = "GoogleBilling";
    public static final String HUAWEI = "HuaweiBilling";
    public static final String EMPTY = "Empty";
}
