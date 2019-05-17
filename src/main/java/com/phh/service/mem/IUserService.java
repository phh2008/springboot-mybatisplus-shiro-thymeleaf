package com.phh.service.mem;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phh.entity.mem.User;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author phh
 * @since 2019-05-17
 */
public interface IUserService extends IService<User> {

    User getByUsername(String username);

}
