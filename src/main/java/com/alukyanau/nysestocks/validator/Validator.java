package com.alukyanau.nysestocks.validator;

public interface Validator<T> {

    boolean validate(T data);

}
