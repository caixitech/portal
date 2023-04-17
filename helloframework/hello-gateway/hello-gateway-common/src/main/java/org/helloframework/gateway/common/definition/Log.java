package org.helloframework.gateway.common.definition;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * Created by lanjian
 */
public class Log {
    private final static Map<Class, Logger> loggers = new HashMap();

    private Logger findLogger(Class c) {
        Logger logger = loggers.get(c);
        if (logger == null) {
            logger = LoggerFactory.getLogger(c);
            loggers.put(c, logger);
        }
        return logger;
    }

    public void warn(String info) {
        Logger logger = findLogger(getClass());
        if (logger.isWarnEnabled()) {
            logger.warn(info);
        }

    }

    public void warn(String info, Throwable t) {
        Logger logger = findLogger(getClass());
        if (logger.isWarnEnabled()) {
            logger.warn(info, t);
        }
    }

    public void debug(String info) {
        Logger logger = findLogger(getClass());
        if (logger.isDebugEnabled()) {
            logger.debug(info);
        }
    }

    public void debug(String info, Throwable t) {
        Logger logger = findLogger(getClass());
        if (logger.isDebugEnabled()) {
            logger.debug(info, t);
        }
    }

    public void info(String info) {
        Logger logger = findLogger(getClass());
        if (logger.isInfoEnabled()) {
            logger.info(info);
        }
    }

    public void info(String info, Throwable t) {
        Logger logger = findLogger(getClass());
        if (logger.isInfoEnabled()) {
            logger.info(info, t);
        }
    }

    public void error(String info) {
        Logger logger = findLogger(getClass());
        if (logger.isErrorEnabled()) {
            logger.error(info);
        }
    }

    public void error(String info, Throwable t) {
        Logger logger = findLogger(getClass());
        if (logger.isErrorEnabled()) {
            logger.error(info, t);
        }
    }
}
