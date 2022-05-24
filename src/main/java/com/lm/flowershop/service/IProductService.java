package com.lm.flowershop.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.lm.flowershop.entity.Product;
import com.lm.flowershop.entity.Role;
import com.lm.flowershop.entity.Unit;

import java.util.List;

/**
 * @author lm
 * @create 2022-03-26 15:24
 */
public interface IProductService extends IService<Product> {

    Page<Product> findPage(Page<Product> page, String name,String psort);



}
