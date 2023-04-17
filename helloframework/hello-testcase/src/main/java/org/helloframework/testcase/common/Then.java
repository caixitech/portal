package org.helloframework.testcase.common;


import io.restassured.response.ValidatableResponse;

public interface Then {
    ValidatableResponse then(ValidatableResponse response);
}
