package com.lm.flowershop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.entity.Buy;
import com.lm.flowershop.entity.Manager;
import com.lm.flowershop.entity.Sale;
import org.apache.ibatis.annotations.Param;

import java.util.Date;

/**
 * @author lm
 * @create 2022-03-26 15:27
 */
public interface SaleMapper extends BaseMapper<Sale> {
     Page<Sale> findPage(Page<Sale> page, String saleId);

     Page<Sale> findPage1(Page<Sale> page, String saleId,String saler,String reviewer ,String results ,String startTime,String endTime);

     Page<Sale> findPage2(Page<Sale> page);
}