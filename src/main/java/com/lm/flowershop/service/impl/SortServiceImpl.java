package com.lm.flowershop.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lm.flowershop.entity.Product;
import com.lm.flowershop.entity.Sale;
import com.lm.flowershop.entity.Sort;
import com.lm.flowershop.mapper.ProductMapper;
import com.lm.flowershop.mapper.SortMapper;
import com.lm.flowershop.service.IProductService;
import com.lm.flowershop.service.ISortService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author lm
 * @create 2022-03-26 15:23
 */
@Service
public class SortServiceImpl extends ServiceImpl<SortMapper, Sort> implements ISortService {

    @Resource
    private SortMapper sortMapper;

    @Override
    public Page<Sort> findPage(Page<Sort> page, String sortname) {

        return sortMapper.findPage(page,sortname);
    }

    @Override
    public Page<Sort> findPage1(Page<Sort> page) {
        return sortMapper.findPage1(page);

    }
}
