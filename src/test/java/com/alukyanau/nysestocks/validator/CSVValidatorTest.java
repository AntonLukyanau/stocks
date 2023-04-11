package com.alukyanau.nysestocks.validator;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CSVValidatorTest {

    private final CSVValidator validator = new CSVValidator();

    @Test
    void testReturnTrueWhenCSVIsValid() {
        //given
        String csv = """
                Date,Open,High,Low,Close,Volume
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
                Date,Open,High,Low,Close,Volume,seventhValue
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
                Date,Open,High,Low,Close,Volume
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
                Date,Open,High,Low,Close
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
        String csv = "Date,Open,High,Low,Close,Volume";
        //when
        boolean isValid = validator.validate(csv);
        //then
        assertFalse(isValid);
    }

}