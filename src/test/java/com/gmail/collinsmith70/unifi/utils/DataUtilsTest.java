package com.gmail.collinsmith70.unifi.utils;

import static org.junit.Assert.*;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import com.gmail.collinsmith70.unifi.util.DataUtils;

public class DataUtilsTest {
    
    private static final Object[][] data = {
            { "0", false },
            { "1", true },
            { "false", false },
            { "true", true },
            { "FALSE", false },
            { "TRUE", true },
            { "False", false },
            { "True", true }
    };

    private static final int[] data2 = {
            2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31, 37, 41,
            43, 47, 53, 59, 61, 67, 71, 73, 79, 83, 89, 97
    };
    
    @Rule
    public final ExpectedException exception = ExpectedException.none();
    
    @Test
    public void testConvertValueToBoolean() {
        boolean test, validator;
        for (Object[] row : data) {
            validator = (Boolean) row[1];
            test = DataUtils.convertValueToBoolean((String)row[0], !validator);
            assertTrue(test == validator);
        }
    }
    
    @Test
    public void testConvertValueToBooleanParams() {
        boolean validator = true;
        boolean test = DataUtils.convertValueToBoolean(null, validator);
        assertTrue(test == validator);
        validator = false;
        test = DataUtils.convertValueToBoolean(null, validator);
        assertTrue(test == validator);
        validator = false;
        test = DataUtils.convertValueToBoolean("", !validator);
        assertTrue(test == validator);
        validator = false;
        test = DataUtils.convertValueToBoolean("not_a_boolean", !validator);
        assertTrue(test == validator);
        validator = false;
        test = DataUtils.convertValueToBoolean("12346578", !validator);
        assertTrue(test == validator);
    }
    
    @Test
    public void testConvertValueToInt() {
        int negValidator;
        for (int validator : data2) {
            negValidator = -validator;
            int test = DataUtils.convertValueToInt(Integer.toString(validator), ~validator);
            assertTrue(test == validator);
            test = DataUtils.convertValueToInt(Integer.toString(negValidator), ~negValidator);
            assertTrue(test == negValidator);
            test = DataUtils.convertValueToInt("0x" + Integer.toString(validator, 16), ~validator);
            assertTrue(test == validator);
            test = DataUtils.convertValueToInt("#" + Integer.toString(validator, 16), ~validator);
            assertTrue(test == validator);
            test = DataUtils.convertValueToInt("0" + Integer.toString(validator, 8), ~validator);
            assertTrue(test == validator);
            test = DataUtils.convertValueToInt(
                    "-0x" + Integer.toString(negValidator, 16).substring(1), ~negValidator);
            assertTrue(test == negValidator);
            test = DataUtils.convertValueToInt(
                    "-#" + Integer.toString(negValidator, 16).substring(1), ~negValidator);
            assertTrue(test == negValidator);
            test = DataUtils.convertValueToInt(
                    "-0" + Integer.toString(negValidator, 8).substring(1), ~negValidator);
            assertTrue(test == negValidator);
        }
    }
    
    @Test
    public void testConvertValueToIntParams() {
        int validator = Integer.MAX_VALUE;
        int test = DataUtils.convertValueToInt(null, validator);
        assertTrue(test == validator);
        exception.expect(StringIndexOutOfBoundsException.class);
        validator = Integer.MAX_VALUE;
        test = DataUtils.convertValueToInt("", validator);
        assertTrue(test == validator);
        validator = Integer.MAX_VALUE;
        test = DataUtils.convertValueToInt("not_an_int", validator);
        assertTrue(test == validator);
    }

}
