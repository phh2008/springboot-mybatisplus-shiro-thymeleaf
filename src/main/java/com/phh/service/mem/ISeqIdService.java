package com.phh.service.mem;

import com.baomidou.mybatisplus.extension.service.IService;
import com.phh.entity.mem.SeqId;

/**
 * 描述
 *
 * @author phh
 * @version V1.0
 * @date 2020/6/5
 */
public interface ISeqIdService extends IService<SeqId> {


    String getOdrNo();


}
