package com.example.seqr.models;

/**
 * Represents signup sub-collection
 */
public class SignUp {
    private String userId;
    private String userName;

    /**
     * Empty constructor method
     */
    public SignUp() {

    }

    /**
     * Constructor method to make a SignUp
     * @param userId the user's ID
     * @param userName the user's name
     */
    public SignUp(String userId, String userName) {
        this.userId = userId;
        this.userName = userName;
    }

    /**
     * Getter for userId
     * @return String userId
     */
    public String getUserId() {
        return userId;
    }

    /**
     * Setter for userId
     * @param userId Id of users
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * Getter for userName
     * @return String userId
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter for userName
     * @param userName name of users
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
}
