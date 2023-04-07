package com.example.stonks.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CSVValidatorTest {

    private final CSVValidator validator = new CSVValidator();

    @Test
    void testReturnTrueWhenCSVIsValid() {
        //given
        String csv = """
                q,w,e,r,t,y
                111,"2","3","4","5","6"
                666,"5","4","3","2","1"
                """;
        //when
        boolean isValid = validator.validate(csv);
        //then
        assertTrue(isValid);
    }

    @Test
    void testReturnFalseWhenDataIsValidHeaderIsNotValid() {
        //given
        String csv = """
                q,w,e,r,t,y,y
                111,"2","3","4","5","6"
                666,"5","4","3","2","1"
                """;
        //when
        boolean isValid = validator.validate(csv);
        //then
        assertFalse(isValid);
    }

    @Test
    void testReturnFalseWhenDataNotValidHeaderIsValid() {
        //given
        String csv = """
                q,w,e,r,t,y
                1,2,3,4,5,6
                """;
        //when
        boolean isValid = validator.validate(csv);
        //then
        assertFalse(isValid);
    }

    @Test
    void testReturnFalseWhenDataNotValidHeaderIsNotValid() {
        //given
        String csv = """
                q,w,e,r,t,y,y
                1,2,3,4,5,6,7
                """;
        //when
        boolean isValid = validator.validate(csv);
        //then
        assertFalse(isValid);
    }

    @Test
    void testReturnFalseWhenCSVisNull() {
        //given
        String csv = null;
        //when
        boolean isValid = validator.validate(csv);
        //then
        assertFalse(isValid);
    }

    @Test
    void testReturnFalseWhenCSVHasOnlyHeader() {
        //given
        String csv = "1,2,3,4,5,6";
        //when
        boolean isValid = validator.validate(csv);
        //then
        assertFalse(isValid);
    }

}