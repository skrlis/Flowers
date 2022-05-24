package com.lm.flowershop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lm.flowershop.entity.Buy;
import com.lm.flowershop.entity.Manager;
import com.lm.flowershop.entity.Putstorage;
import com.lm.flowershop.entity.Sale;

import java.util.Date;
import java.util.List;

/**
 * @author lm
 * @create 2022-03-26 15:24
 */
public interface ISaleService extends IService<Sale> {

    Page<Sale> findPage(Page<Sale> page, String saleId);
    Page<Sale> findPage1(Page<Sale> page, String saleId,String saler, String reviewer,String results,String startTime,String endTime);
    Page<Sale> findPage2(Page<Sale> objectPage);
}
