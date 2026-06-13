package com.extendedclip.deluxemenus.menu.animation;

import java.util.concurrent.ThreadLocalRandom;

public final class AnimationFrameCursor {

    private int frame;
    private int elapsed;
    private int direction = 1;

    public int frame() {
        return frame;
    }

    public boolean tick(final int interval, final AnimationMode mode, final int frameCount) {
        elapsed++;
        if (elapsed < Math.max(1, interval)) {
            return false;
        }

        elapsed = 0;
        advance(mode, frameCount);
        return true;
    }

    private void advance(final AnimationMode mode, final int frameCount) {
        if (frameCount <= 1) {
            frame = 0;
            return;
        }

        switch (mode) {
            case REVERSE:
                frame = (frame - 1 + frameCount) % frameCount;
                break;
            case PING_PONG:
                frame += direction;
                if (frame >= frameCount - 1 || frame <= 0) {
                    direction *= -1;
                }
                break;
            case RANDOM:
                final int next = ThreadLocalRandom.current().nextInt(frameCount - 1);
                frame = next >= frame ? next + 1 : next;
                break;
            case LOOP:
            default:
                frame = (frame + 1) % frameCount;
                break;
        }
    }
}
