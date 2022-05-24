package com.lm.flowershop.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lm.flowershop.entity.Product;
import com.lm.flowershop.mapper.ProductMapper;
import com.lm.flowershop.service.IProductService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;

/**
 * @author lm
 * @create 2022-03-26 15:23
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements IProductService {

    @Resource
    private ProductMapper productMapper;

    @Override
    public Page<Product> findPage(Page<Product> page, String name,String psort) {
        return productMapper.findPage(page,name,psort);
    }
}
