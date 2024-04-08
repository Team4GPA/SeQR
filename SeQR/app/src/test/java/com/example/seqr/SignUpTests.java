package com.example.seqr;

import static junit.framework.TestCase.assertEquals;

import static junit.framework.TestCase.assertNull;

import com.example.seqr.models.SignUp;

import org.junit.Test;

public class SignUpTests {

    @Test
    public void testSettersGetters(){
        SignUp mockSignUp = new SignUp();
        mockSignUp.setUserId("Test userID");
        mockSignUp.setUserName("Test userName");
        assertEquals("Test userID",mockSignUp.getUserId());
        assertEquals("Test userName",mockSignUp.getUserName());
    }

    @Test
    public void testConstructor(){
        SignUp mockSignUp = new SignUp("Test userID", "Test userName");
        assertEquals("Test userID",mockSignUp.getUserId());
        assertEquals("Test userName",mockSignUp.getUserName());
    }

    @Test
    public void testEmptyConstructor(){
        SignUp mockSignUp = new SignUp();
        assertNull(mockSignUp.getUserId());
        assertNull(mockSignUp.getUserName());
    }
}
