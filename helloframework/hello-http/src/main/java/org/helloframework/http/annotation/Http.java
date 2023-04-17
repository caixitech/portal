package org.helloframework.http.annotation;

import org.helloframework.http.common.HttpMethod;

public @interface Http {
    HttpMethod method();

    String url();
}
