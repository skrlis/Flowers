package com.lm.flowershop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lm.flowershop.entity.Outstorage;
import com.lm.flowershop.entity.Putstorage;

/**
 * @author lm
 * @create 2022-03-26 15:24
 */
public interface IOutService extends IService<Outstorage> {

    Page<Outstorage> findPage(Page<Outstorage> page, String outId, String outOper, String outCustomer,String startTime, String endTime);
    Page<Outstorage> findPage1(Page<Outstorage> page,String startTime, String endTime);
    Double sumByToday();
    Double sumByPaid();

    int saleValidate();


}
