package com.tjhello.lib.billing.base.info;

import androidx.annotation.NonNull;

import com.tjhello.lib.billing.base.anno.ProductType;

import java.util.ArrayList;
import java.util.List;

public class ProductConfig {


    private String code ;
    @ProductType
    private String type;

    public static ProductConfig build(@NonNull @ProductType String type, String code){
        ProductConfig config = new ProductConfig();
        config.setCode(code);
        config.setType(type);
        return config;
    }

    private final List<String> attList = new ArrayList<>();

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

    public boolean canConsume(){
        return type.equals(ProductType.TYPE_INAPP_CONSUMABLE);
    }

    public ProductConfig addAtt(String att){
        if(!containsAtt(att)){
            attList.add(att);
        }
        return this;
    }

    public List<String> allAtt(){
        return new ArrayList<>(attList);
    }

    public boolean containsAtt(String att){
        return attList.contains(att);
    }
}
