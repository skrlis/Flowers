package com.lm.flowershop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.entity.Foster;

/**
 * @author lm
 * @create 2022-03-26 15:27
 */
public interface FosterMapper extends BaseMapper<Foster> {


    Page<Foster> findPage(Page<Foster> page, String phone, String fosName, String startTime, String fosOper, String fosStatus);
}
