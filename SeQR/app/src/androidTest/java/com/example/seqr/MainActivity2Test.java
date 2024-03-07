package com.example.seqr;


import android.content.Context;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

public class MainActivity2Test {

    @Test
    public void TestMain(){
        String myMain = "X";
        myMain = myMain.replace("X", "This is a text test.");
        assertEquals(myMain, "This is a text test.");
    }

}


