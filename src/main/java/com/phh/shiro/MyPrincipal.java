package com.phh.shiro;

import com.phh.entity.mem.User;
import lombok.Data;

import java.io.Serializable;

/**
 * 身份信息
 *
 * @author phh
 * @version V1.0
 * @date 2019/5/16
 */
@Data
public class MyPrincipal implements Serializable {

    private static final long serialVersionUID = -1991861423759041275L;

    private String userName;

    public MyPrincipal(User userVO) {
        this.userName = userVO.getUsername();
    }

}
