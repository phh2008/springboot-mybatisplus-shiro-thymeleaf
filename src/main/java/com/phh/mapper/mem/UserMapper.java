package com.phh.mapper.mem;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phh.entity.mem.User;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author phh
 * @since 2019-05-17
 */
public interface UserMapper extends BaseMapper<User> {

    /**
     * 根据账号查询
     *
     * @param username
     * @return
     */
    User getByUsername(@Param("username") String username);

}
