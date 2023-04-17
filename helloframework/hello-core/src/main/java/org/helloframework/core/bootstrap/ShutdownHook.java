package org.helloframework.core.bootstrap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 工具退出钩子类
 */
public class ShutdownHook extends Thread {
    /**
     * 日志对象
     */
    private static Logger logger = LoggerFactory.getLogger(ShutdownHook.class);

    /**
     * 线程运行
     */
    @Override
    public void run() {

        try {
            //通知所有工作线程停止新的任务处理
            WorkingLight.RUNNING = false;

            //记录日志
            logger.info("接收到退出命令，已通知工作线程停止工作，为保证数据完整性，休眠60秒后退出");

            // 当前线程休眠
            Thread.sleep(60 * 10);

        } catch (Exception e) {

        }
    }
}
