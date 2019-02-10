package com.exonum.evoting.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@AllArgsConstructor
@Getter
public enum UnsignedBallotButton {

    SAVE_3WORD("Save 3-word memo and ballot hash"),
    DISCARD("DISCARD"),
    DECRYPT("DECRYPT"),
    SIGN("SIGN");

    private final String label;

    public static List<String> getLabels() {
        return new ArrayList<String>() {{
            Arrays.asList(values()).forEach(v -> add(v.getLabel().toUpperCase()));
        }};
    }

}
