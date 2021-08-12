package com.tjhello.lib.billing.base.info;

import androidx.annotation.NonNull;

import com.tjhello.lib.billing.base.anno.ProductType;

import java.util.ArrayList;
import java.util.List;

public class ProductConfig {


    private String code ;
    @ProductType
    private String type;

    /**
     * 构建一个商品配置-无属性
     * @param type 商品类型 {@link ProductType}
     * @param code 商品代码
     * @return 商品配置
     */
    public static ProductConfig build(@NonNull @ProductType String type, String code){
        ProductConfig config = new ProductConfig();
        config.setCode(code);
        config.setType(type);
        return config;
    }

    /**
     * 构建一个商品配置-有属性
     * @param type 商品类型 {@link ProductType}
     * @param code 商品代码
     * @param attArray 商品属性 {@link #addAtt(String)}
     * @return 商品配置
     */
    public static ProductConfig build(@NonNull @ProductType String type, String code,String ... attArray){
        ProductConfig config = new ProductConfig();
        config.setCode(code);
        config.setType(type);
        for (String s : attArray) {
            config.addAtt(s);
        }
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

    /**
     * 添加一个商品属性，例如"noads"，这样后吗即可使用containsAtt("noads")来判断该商品是否拥有该属性。
     * @param att 商品属性
     */
    public ProductConfig addAtt(String att){
        if(!containsAtt(att)){
            attList.add(att);
        }
        return this;
    }

    public List<String> allAtt(){
        return new ArrayList<>(attList);
    }


    /**
     * 是否存在某商品属性
     * @param att 商品属性
     * @return true|false
     */
    public boolean containsAtt(String att){
        return attList.contains(att);
    }
}
