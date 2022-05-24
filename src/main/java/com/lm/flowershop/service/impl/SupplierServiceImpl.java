package com.lm.flowershop.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lm.flowershop.entity.Supplier;
import com.lm.flowershop.mapper.SupplierMapper;
import com.lm.flowershop.service.ISupplierService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lm
 * @create 2022-03-26 15:23
 */
@Service
public class SupplierServiceImpl extends ServiceImpl<SupplierMapper, Supplier> implements ISupplierService {

    @Resource
    private SupplierMapper supplierMapper;


    @Override
    public Page<Supplier> findPage(Page<Supplier> page, String supName) {
        return supplierMapper.findPage(page,supName);

    }
}
