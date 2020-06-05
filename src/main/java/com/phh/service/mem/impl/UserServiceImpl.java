package com.phh.service.mem.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phh.entity.mem.User;
import com.phh.event.UserTransactionEvent;
import com.phh.mapper.mem.UserMapper;
import com.phh.service.mem.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author phh
 * @since 2019-05-17
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private ApplicationEventPublisher publisher;

    @Override
    @Transactional(propagation = Propagation.REQUIRED, rollbackFor = Exception.class)
    public User getByUsername(String username) {
        User user = baseMapper.getByUsername(username);
        publisher.publishEvent(user);
        //这个事件必需要开启事务，才能被监听到
        publisher.publishEvent(new UserTransactionEvent(this, user));
        return user;
    }

}
