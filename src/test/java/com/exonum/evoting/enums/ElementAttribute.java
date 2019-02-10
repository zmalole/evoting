package com.exonum.evoting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ElementAttribute {
    VALUE("value");

    private final String attribute;
}
