package com.lm.flowershop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lm.flowershop.entity.Buy;
import com.lm.flowershop.entity.Sale;

/**
 * @author lm
 * @create 2022-03-26 15:24
 */
public interface IBuyService extends IService<Buy> {

    Page<Buy> findPage(Page<Buy> page, String buyid ,String buyApply,String buyer,String buyName,String buyStatus,String startTime,String endTime);
    Page<Buy> findPage1(Page<Buy> objectPage);



}
