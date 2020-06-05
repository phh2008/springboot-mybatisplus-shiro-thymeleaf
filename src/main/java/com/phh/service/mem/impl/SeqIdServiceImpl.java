package com.phh.service.mem.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.common.base.Strings;
import com.phh.entity.mem.SeqId;
import com.phh.mapper.mem.SeqIdMapper;
import com.phh.service.mem.ISeqIdService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * 描述
 *
 * @author phh
 * @version V1.0
 * @date 2020/6/5
 */
@Service
public class SeqIdServiceImpl extends ServiceImpl<SeqIdMapper, SeqId> implements ISeqIdService {

    private ConcurrentLinkedQueue<Long> odrQueue = new ConcurrentLinkedQueue<>();

    private DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyMMdd");

    @Override
    public synchronized String getOdrNo() {
        if (odrQueue.size() == 0) {
            LocalDate today = LocalDate.now();
            Integer bizType = 1;
            initSeqList(today, bizType, odrQueue);
        }
        Long seq = odrQueue.poll();
        return "D" + dtf.format(LocalDate.now()) + Strings.padStart(String.valueOf(seq), 3, '0');
    }

    private void initSeqList(LocalDate today, Integer bizType, ConcurrentLinkedQueue<Long> odrQueue) {
        LambdaQueryWrapper<SeqId> query = new LambdaQueryWrapper<>();
        query.eq(SeqId::getForDay, today);
        query.eq(SeqId::getBizType, bizType);
        SeqId seq = this.baseMapper.selectOne(query);
        int preMax = 1;
        if (seq == null) {
            seq = new SeqId();
            seq.setStep(10);
            seq.setMaxNum(seq.getStep());
            seq.setForDay(today);
            seq.setBizType(bizType);
            seq.setVersion(1);
            seq.setUpdateAt(LocalDateTime.now());
            this.baseMapper.insert(seq);
        } else {
            preMax = seq.getMaxNum();
            seq.setMaxNum(seq.getMaxNum() + seq.getStep());
            seq.setVersion(seq.getVersion() + 1);
            seq.setUpdateAt(LocalDateTime.now());
            this.baseMapper.updateById(seq);
        }
        for (long i = preMax; i < seq.getMaxNum(); i++) {
            odrQueue.offer(i);
        }
    }


}
