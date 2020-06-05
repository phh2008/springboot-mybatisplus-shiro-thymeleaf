package com.phh.event.listener;

import com.phh.event.StringEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * 描述
 *
 * @author phh
 * @version V1.0
 * @date 2020/6/1
 */
@Slf4j
@EnableAsync
@Component
public class StringEventListener {

    @Async
    @EventListener
    public void handler(StringEvent event) throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        log.info("---------string listener---------");
        log.info(Thread.currentThread().getName());
        log.info(event.getMsg());
        log.info(event.getSource().getClass().getName());
    }


}
