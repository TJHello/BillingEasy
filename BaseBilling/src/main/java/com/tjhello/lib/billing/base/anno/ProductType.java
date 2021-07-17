package com.tjhello.lib.billing.base.anno;


import androidx.annotation.StringDef;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.PARAMETER,ElementType.FIELD})
@StringDef(value = {ProductType.TYPE_INAPP_CONSUMABLE,ProductType.TYPE_INAPP_NON_CONSUMABLE,ProductType.TYPE_SUBS})
public @interface ProductType {

    public static final String TYPE_INAPP_CONSUMABLE = "inapp-consumable";
    public static final String TYPE_INAPP_NON_CONSUMABLE = "inapp-non-consumable";
    public static final String TYPE_SUBS = "subs";

}
