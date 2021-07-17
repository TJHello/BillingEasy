package com.tjhello.lib.billing.base.info;

import com.tjhello.lib.billing.base.anno.ProductType;

public class ProductConfig {


    private String code ;
    @ProductType
    private String type;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
