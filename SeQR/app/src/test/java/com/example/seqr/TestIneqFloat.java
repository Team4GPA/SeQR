package com.example.seqr;

import android.util.Log;

import junit.framework.TestCase;

public class TestIneqFloat extends TestCase {
    double lhs;
    double rhs;
    public void testGTE(){
        assertTrue(lhs >= rhs);
    }

    public void testLTE(){
        assertTrue(lhs <= rhs);
    }
}