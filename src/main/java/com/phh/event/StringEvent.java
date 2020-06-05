package com.phh.event;

import org.springframework.context.ApplicationEvent;

/**
 * 描述
 *
 * @author phh
 * @version V1.0
 * @date 2020/6/1
 */
public class StringEvent extends ApplicationEvent {

    private String msg;

    public StringEvent(Object source, String msg) {
        super(source);
        this.msg = msg;
    }

    public String getMsg() {
        return msg;
    }

}
