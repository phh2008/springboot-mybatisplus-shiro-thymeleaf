package com.phh.event.listener;

import com.phh.event.UserTransactionEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

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
public class TransactionEventListener {


    /**
     * 监听事务提交后的事件
     */
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handler(UserTransactionEvent event) {
        log.info("---------user transaction listener---------");
        log.info(Thread.currentThread().getName());
        log.info(event.getUser().toString());
    }


}
