package com.lm.flowershop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.entity.Customer;
import com.lm.flowershop.entity.Product;
import com.lm.flowershop.entity.Supplier;
import org.apache.ibatis.annotations.Param;

/**
 * @author lm
 * @create 2022-03-26 15:27
 */
public interface CustomerMapper extends BaseMapper<Customer> {
    Page<Customer> findPage(Page<Customer> page, @Param("cusName") String cusName);

    public Integer userNameValidate();

    public Integer salerValidate();
}
