package com.phh.entity.mem;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.phh.entity.BaseEntity;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 描述
 *
 * @author phh
 * @version V1.0
 * @date 2020/6/5
 */
@Data
@TableName("sys_seq_id")
public class SeqId extends BaseEntity {

    private Long id;

    @TableField("max_num")
    private Integer maxNum;

    private Integer step;

    @TableField("biz_type")
    private Integer bizType;

    private Integer version;

    @TableField("for_day")
    private LocalDate forDay;

    @TableField("update_at")
    private LocalDateTime updateAt;


}
