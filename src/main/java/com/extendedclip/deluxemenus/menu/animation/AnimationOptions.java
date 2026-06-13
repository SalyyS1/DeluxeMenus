package com.extendedclip.deluxemenus.menu.animation;

import com.extendedclip.deluxemenus.menu.options.MenuItemOptions;
import java.util.List;
import org.jetbrains.annotations.NotNull;

public class AnimationOptions {

    private final int interval;
    private final AnimationMode mode;
    private final List<MenuItemOptions> frames;

    public AnimationOptions(final int interval, final @NotNull AnimationMode mode, final @NotNull List<MenuItemOptions> frames) {
        this.interval = Math.max(1, interval);
        this.mode = mode;
        this.frames = List.copyOf(frames);
    }

    public int interval() {
        return interval;
    }

    public @NotNull AnimationMode mode() {
        return mode;
    }

    public @NotNull List<MenuItemOptions> frames() {
        return frames;
    }
}
