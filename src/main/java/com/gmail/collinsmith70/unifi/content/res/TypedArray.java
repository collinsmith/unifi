package com.gmail.collinsmith70.unifi.content.res;

import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;

import org.apache.commons.lang3.Validate;

import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.Pool.Poolable;
import com.gmail.collinsmith70.unifi.annotation.Index;
import com.gmail.collinsmith70.unifi.annotation.Unsigned;
import com.gmail.collinsmith70.unifi.content.res.Resources.Theme;
import com.gmail.collinsmith70.unifi.util.ColorUtils;
import com.gmail.collinsmith70.unifi.util.DataUtils;

public class TypedArray implements Poolable {
    
    private boolean recycled;
    
    @Nullable
    private Resources res;
    
    @Nullable
    private Object[] data;
    
    @Nullable
    private Theme theme;
    
    static TypedArray obtain(@NonNull Resources res,
                             @Size(min = 0) int len) {
        Validate.isTrue(res != null, "res cannot be null");
        Validate.isTrue(len >= 0, "len must be greater than or equal to 0");
        final TypedArray attrs = Pools.obtain(TypedArray.class);
        attrs.setRecycled(false);
        attrs.setResources(res);
        attrs.setData(new Object[len]);
        attrs._setTheme(null);
        return attrs;
    }
    
    public void recycle() {
        checkRecycled();
        Pools.free(this);
    }
    
    @Override
    public void reset() {
        setRecycled(true);
    }

    private void checkRecycled() {
        if (recycled) {
            throw new IllegalStateException(toString() + " has been recycled!");
        }
    }
    
    TypedArray() {
        setRecycled(false);
    }
    
    TypedArray(@NonNull Resources res,
               @Size(min = 0) int len) {
        this();
        setResources(res);
        setData(new Object[len]);
    }
    
    public boolean isRecycled() {
        return recycled;
    }
    
    private void setRecycled(boolean recycled) {
        this.recycled = recycled;
    }
    
    @NonNull
    public Resources getResources() {
        checkRecycled();
        assert res != null : "unrecycled TypedArray instances should not have a null res reference";
        return res;
    }
    
    private void setResources(@NonNull Resources res) {
        checkRecycled();
        Validate.isTrue(res != null, "res cannot be null");
        this.res = res;
    }
    
    @NonNull
    Object[] getData() {
        checkRecycled();
        assert data != null
                : "unrecycled TypedArray instances should not have a null data reference";
        return data;
    }
    
    void setData(@NonNull Object[] data) {
        checkRecycled();
        Validate.isTrue(data != null, "data cannot be null");
        this.data = data;
    }
    
    @Nullable
    public Theme getTheme() {
        checkRecycled();
        return theme;
    }
    
    private void _setTheme(@Nullable Theme theme) {
        checkRecycled();
        this.theme = theme;
    }
    
    void setTheme(@NonNull Theme theme) {
        checkRecycled();
        Validate.isTrue(theme != null, "theme cannot be null");
        _setTheme(theme);
    }
    
    @Nullable
    private Object getData(@Index int index) {
        return getData()[index];
    }
    
    public int getInt(@Index int index, int defaultValue) {
        final Object value = getData(index);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof CharSequence) {
            return DataUtils.convertValueToInt((CharSequence)value, defaultValue);
        }
        
        return defaultValue;
    }
    
    public float getFloat(@Index int index, float defaultValue) {
        final Object value = getData(index);
        if (value instanceof Float) {
            return (Float) value;
        } else if (value instanceof CharSequence) {
            return Float.parseFloat(value.toString());
        }
        
        return defaultValue;
    }
    
    public boolean getBoolean(@Index int index, boolean defaultValue) {
        final Object value = getData(index);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof CharSequence) {
            return DataUtils.convertValueToBoolean((CharSequence)value, defaultValue);
        }
        
        return defaultValue;
    }
    
    @Nullable
    public String getString(@Index int index, @Nullable String defaultValue) {
        final Object value = getData(index);
        if (value instanceof String) {
            return (String) value;
        }
        
        return defaultValue;
    }
    
    @Unsigned
    public int getColor(@Index int index, int defaultValue) {
        final Object value = getData(index);
        if (value instanceof Integer) {
            return (Integer) value;
        } else if (value instanceof CharSequence) {
            return ColorUtils.parseColor(value.toString());
        }
        
        return defaultValue;
    }

}
