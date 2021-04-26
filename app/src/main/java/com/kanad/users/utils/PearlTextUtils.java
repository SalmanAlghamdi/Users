package com.kanad.users.utils;

import androidx.annotation.Nullable;

public class PearlTextUtils {
    public static boolean isBlank(@Nullable String value) {
        return value == null || value.trim().length() == 0;
    }

}
