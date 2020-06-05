package com.phh.entity.mem;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phh.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.time.LocalDateTime;

/**
 * @author phh
 * @since 2019-05-17
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@ToString
@TableName("mem_user")
public class User extends BaseEntity {

    private static final long serialVersionUID = 1L;

    private Long id;

    private String username;

    private String password;

    private String realname;

    private Integer state;

    @TableField(value = "create_at")
    private LocalDateTime createAt;


}
