package org.helloframework.core.bootstrap;

import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by lanjian on 2018/3/15.
 */
public abstract class AbstractBootStrap {

    protected static final Logger logger = LoggerFactory.getLogger(AbstractBootStrap.class);

    public void run() {
        try {
            StopWatch watch = new StopWatch();
            watch.start();
            //添加未捕获异常处理
            ExceptionHandler uncaughtExceptionhandler = new ExceptionHandler();
            Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionhandler);
            service();
            //挂接工具退出事件
            ShutdownHook hook = new ShutdownHook();
            Runtime.getRuntime().addShutdownHook(hook);
            watch.stop();
            //提示信息
            logger.info("*************** 服务进程启动完成 ***************");
            logger.info("*************** 服务启动耗时总计 {} ms ***************", watch.getTime());
            while (true) {
                if (!WorkingLight.RUNNING) {
                    break;
                }
                Thread.sleep(10 * 1000);
            }

        } catch (Exception ex) {
            //记录异常
            logger.error("主线程捕获到异常 ", ex);
        }
    }

    public abstract void service();
}
