package com.gmail.collinsmith70.unifi.utils;

import static org.junit.Assert.*;

import java.util.Locale;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.badlogic.gdx.graphics.Color;
import com.gmail.collinsmith70.unifi.util.ColorUtils;

public class ColorUtilsTest {
    
    private static final Object[][] data = {
            { "clear", ColorUtils.CLEAR },
            { "transparent", ColorUtils.TRANSPARENT },
            { "black", ColorUtils.BLACK },
            { "white", ColorUtils.WHITE },
            { "darkgray", ColorUtils.DARK_GRAY },
            { "darkgrey", ColorUtils.DARK_GRAY },
            { "gray", ColorUtils.GRAY },
            { "grey", ColorUtils.GRAY },
            { "lightgray", ColorUtils.LIGHT_GRAY },
            { "lightgrey", ColorUtils.LIGHT_GRAY },
            { "red", ColorUtils.RED },
            { "green", ColorUtils.GREEN },
            { "blue", ColorUtils.BLUE },
            { "yellow", ColorUtils.YELLOW },
            { "cyan", ColorUtils.CYAN },
            { "magenta", ColorUtils.MAGENTA }
    };

    @Test
    public void testParseColor() {
        Color color;
        int validator;
        for (Object[] row : data) {
            color = ColorUtils.parseColor((String)row[0]);
            validator = (Integer)row[1];
            assertTrue(Color.argb8888(color) == validator);
            color = ColorUtils.parseColor(String.format(Locale.ROOT, "#%08X", validator));
            assertTrue(Color.argb8888(color) == validator);
        }
    }
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testParseColorParams() {
        exception.expect(IllegalArgumentException.class);
        ColorUtils.parseColor(null);
        ColorUtils.parseColor("");
        ColorUtils.parseColor(String.format(Locale.ROOT, "0x%08X", ColorUtils.BLACK));
        ColorUtils.parseColor("#0");
        ColorUtils.parseColor("#00000");
        ColorUtils.parseColor("#0000000");
        ColorUtils.parseColor("#000000000");
        ColorUtils.parseColor(String.format(Locale.ROOT, "%08X", ColorUtils.BLACK));
        ColorUtils.parseColor(String.format(Locale.ROOT, "#%X", ColorUtils.CLEAR));
        ColorUtils.parseColor("black1");
        ColorUtils.parseColor("not_a_standard_color");
        ColorUtils.parseColor("12345678");
    }
    
}
