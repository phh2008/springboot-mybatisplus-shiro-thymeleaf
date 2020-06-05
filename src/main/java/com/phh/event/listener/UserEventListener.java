package com.phh.event.listener;

import com.phh.entity.mem.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * 描述
 *
 * @author phh
 * @version V1.0
 * @date 2020/6/1
 */
@Slf4j
@Component
public class UserEventListener {


    @EventListener
    public void handler(User user) {
        log.info("---------user listener---------");
        log.info(Thread.currentThread().getName());
        log.info(user.toString());
    }

}
