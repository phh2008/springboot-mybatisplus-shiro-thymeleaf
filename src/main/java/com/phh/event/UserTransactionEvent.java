package com.phh.event;

import com.phh.entity.mem.User;
import org.springframework.context.ApplicationEvent;

/**
 * 描述
 *
 * @author phh
 * @version V1.0
 * @date 2020/6/1
 */
public class UserTransactionEvent extends ApplicationEvent {

    private User user;

    public UserTransactionEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
