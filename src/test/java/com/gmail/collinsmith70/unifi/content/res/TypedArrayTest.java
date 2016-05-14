package com.gmail.collinsmith70.unifi.content.res;

import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.gmail.collinsmith70.unifi.content.res.Resources.Theme;

public class TypedArrayTest {
    
    private final Resources RES = new Resources();
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testObtain() {
        final int len = 0;
        TypedArray array = TypedArray.obtain(RES, len);
        assertTrue(array != null);
        assertTrue(!array.isRecycled());
        assertTrue(array.getResources() == RES);
        assertTrue(array.getData() != null);
        assertTrue(array.getData().length == len);
        assertTrue(array.getTheme() == null);
        array.recycle();
    }
    
    @Test
    public void testObtainParams() {
        exception.expect(IllegalArgumentException.class);
        TypedArray.obtain(null, 0);
        TypedArray.obtain(RES, -1);
    }
    
    @Test
    public void testRecycle() {
        final int len = 0;
        TypedArray array = TypedArray.obtain(RES, len);
        array.recycle();
        assertTrue(array.isRecycled());
        exception.expect(IllegalStateException.class);
        array.getResources();
        array.getData();
        array.getTheme();
    }

    @Test
    public void testSetData() {
        TypedArray array = TypedArray.obtain(RES, 0);
        final Object[] data = new Object[0];
        array.setData(data);
        assertTrue(array.getData() == data);
        exception.expect(IllegalArgumentException.class);
        array.setData(null);
        array.recycle();
    }
    
    @Test
    public void testSetTheme() {
        TypedArray array = TypedArray.obtain(RES, 0);
        final Theme theme = RES.new Theme();
        array.setTheme(theme);
        assertTrue(array.getTheme() == theme);
        exception.expect(IllegalArgumentException.class);
        array.setTheme(null);
        array.recycle();
    }

}
