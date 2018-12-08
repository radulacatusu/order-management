package com.mine.ordermgm.exception;

/**
 * @stefanl
 */
public class ProductNotFoundException extends Exception {
    public ProductNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}