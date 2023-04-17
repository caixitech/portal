package org.helloframework.mongodb;

public interface MongoValid<T> {
    boolean valid(T t);
}
