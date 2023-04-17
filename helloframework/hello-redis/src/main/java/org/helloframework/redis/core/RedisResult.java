package org.helloframework.redis.core;

/**
 * Created by lanjian
 */
public class RedisResult<T> {
    private RedisResultStatus status;
    private T t;

    public RedisResult() { }

    public RedisResult(RedisResultStatus status) {
        this.status = status;
    }

    public RedisResult(RedisResultStatus status, T t) {
        this.status = status;
        this.t = t;
    }

    public static RedisResult success() {
        return new RedisResult(RedisResultStatus.SUCCESS);
    }

    public static RedisResult success(Object t) {
        return new RedisResult(RedisResultStatus.SUCCESS, t);
    }

    public RedisResultStatus getStatus() {
        return status;
    }

    public void setStatus(RedisResultStatus status) {
        this.status = status;
    }

    public T getT() {
        return t;
    }

    public void setT(T t) {
        this.t = t;
    }

    @Override
    public String toString() {
        return "RedisResult{" +
                "status=" + status +
                ", t=" + t +
                '}';
    }
}
