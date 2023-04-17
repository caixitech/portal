package org.helloframework.testcase.common;


import io.restassured.specification.RequestSpecification;

public interface SignHandler {
    void sign(RequestSpecification requestSpecification, TestCase runCase);
}
