package com.lm.flowershop.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lm.flowershop.entity.Customer;
import com.lm.flowershop.mapper.CustomerMapper;
import com.lm.flowershop.service.ICustomerService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lm
 * @create 2022-03-26 15:23
 */
@Service
public class CustomerServiceImpl extends ServiceImpl<CustomerMapper, Customer> implements ICustomerService {

    @Resource
    private CustomerMapper customerMapper;

    @Override
    public Page<Customer> findPage(Page<Customer> page, String cusName) {
        return customerMapper.findPage(page,cusName);

    }

    @Override
    public int nameValidate() {
        Integer Validate ;

            Validate = customerMapper.userNameValidate();

        if(Validate == null)
            return 0;
        return Validate.intValue();
    }

    @Override
    public int saleValidate() {
        Integer Validate ;

        Validate = customerMapper.salerValidate();

        if(Validate == null)
            return 0;
        return Validate.intValue();
    }
}
