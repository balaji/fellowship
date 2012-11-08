package com.thoughtworks.pumpkin.exception;

public class OAuthFailedException extends Exception {
    public OAuthFailedException() {
        super("Error in retrieving username.");
    }
}
