package com.extendedclip.deluxemenus.menu.animation;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AnimationFrameCursorTest {

    @Test
    void waitsForIntervalBeforeAdvancing() {
        final AnimationFrameCursor cursor = new AnimationFrameCursor();

        assertFalse(cursor.tick(3, AnimationMode.LOOP, 3));
        assertFalse(cursor.tick(3, AnimationMode.LOOP, 3));
        assertTrue(cursor.tick(3, AnimationMode.LOOP, 3));
        assertEquals(1, cursor.frame());
    }

    @Test
    void loopsFrames() {
        final AnimationFrameCursor cursor = new AnimationFrameCursor();

        cursor.tick(1, AnimationMode.LOOP, 3);
        assertEquals(1, cursor.frame());
        cursor.tick(1, AnimationMode.LOOP, 3);
        assertEquals(2, cursor.frame());
        cursor.tick(1, AnimationMode.LOOP, 3);
        assertEquals(0, cursor.frame());
    }

    @Test
    void reversesFrames() {
        final AnimationFrameCursor cursor = new AnimationFrameCursor();

        cursor.tick(1, AnimationMode.REVERSE, 3);
        assertEquals(2, cursor.frame());
        cursor.tick(1, AnimationMode.REVERSE, 3);
        assertEquals(1, cursor.frame());
    }

    @Test
    void pingPongsFrames() {
        final AnimationFrameCursor cursor = new AnimationFrameCursor();

        cursor.tick(1, AnimationMode.PING_PONG, 3);
        assertEquals(1, cursor.frame());
        cursor.tick(1, AnimationMode.PING_PONG, 3);
        assertEquals(2, cursor.frame());
        cursor.tick(1, AnimationMode.PING_PONG, 3);
        assertEquals(1, cursor.frame());
        cursor.tick(1, AnimationMode.PING_PONG, 3);
        assertEquals(0, cursor.frame());
    }

    @Test
    void randomNeverRepeatsCurrentFrame() {
        final AnimationFrameCursor cursor = new AnimationFrameCursor();

        for (int i = 0; i < 20; i++) {
            final int previous = cursor.frame();
            cursor.tick(1, AnimationMode.RANDOM, 4);
            assertNotEquals(previous, cursor.frame());
        }
    }

    @Test
    void parsesModeNames() {
        assertEquals(AnimationMode.PING_PONG, AnimationMode.parse("ping-pong"));
        assertEquals(AnimationMode.REVERSE, AnimationMode.parse("REVERSE"));
        assertEquals(AnimationMode.LOOP, AnimationMode.parse("invalid"));
    }
}
