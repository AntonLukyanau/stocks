package com.example.stonks.validator;

public interface Validator<T> {

    boolean validate(T data);

}
