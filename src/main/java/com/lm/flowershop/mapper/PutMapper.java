package com.lm.flowershop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.entity.Putstorage;
import com.lm.flowershop.entity.Sale;

/**
 * @author lm
 * @create 2022-03-26 15:27
 */
public interface PutMapper extends BaseMapper<Putstorage> {
    Page<Putstorage> findPage(Page<Putstorage> page, String putId, String putOper, String startTime, String endTime);

    Double sumByToday();

    public Integer totalput();
}
