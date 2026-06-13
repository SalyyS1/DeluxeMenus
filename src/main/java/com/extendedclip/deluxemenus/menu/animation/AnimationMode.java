package com.extendedclip.deluxemenus.menu.animation;

import java.util.Locale;
import org.jetbrains.annotations.NotNull;

public enum AnimationMode {
    LOOP,
    REVERSE,
    PING_PONG,
    RANDOM;

    public static @NotNull AnimationMode parse(final String value) {
        if (value == null) {
            return LOOP;
        }

        try {
            return valueOf(value.toUpperCase(Locale.ROOT).replace('-', '_'));
        } catch (IllegalArgumentException ignored) {
            return LOOP;
        }
    }
}
