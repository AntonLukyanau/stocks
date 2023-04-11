package com.alukyanau.nysestocks.service;

/**
 * Provide absorbing parameters to one object which contains all of them.
 * Parameters should have same order like fields in class of absorb object.
 * For example
 * <code>
 *     class AbsorbParameters {
 *         String param1;
 *         String param2;
 *         String param3;
 *     }
 *     ...
 *     parameterService.fillParameters("param1Value","param2Value","param3Value");
 * </code>
 * @param <T> Type of object which contains parameters
 */
public interface ParameterService<T> {

    T fillParameters(String... parameters);

}
