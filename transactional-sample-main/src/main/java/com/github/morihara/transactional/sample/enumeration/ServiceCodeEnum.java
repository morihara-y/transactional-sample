package com.github.morihara.transactional.sample.enumeration;

import lombok.Getter;

public enum ServiceCodeEnum {
    GOODS_ISSUE("GOODS_ISSUE"), 
    GOODS_RECEIPT("GOODS_RECEIPT");

    @Getter
    private final String text;

    private ServiceCodeEnum(final String text) {
        this.text = text;
    }
    
    public static ServiceCodeEnum getEnumName(String str) {
        for (ServiceCodeEnum value : values()) {
            if (value.getText().equals(str)) {
                return value;
            }
        }
        throw new IllegalArgumentException("undefined: " + str);
    }
}
