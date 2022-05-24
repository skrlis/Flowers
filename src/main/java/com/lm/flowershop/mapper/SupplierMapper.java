package com.lm.flowershop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.entity.Product;
import com.lm.flowershop.entity.Supplier;
import org.apache.ibatis.annotations.Param;

/**
 * @author lm
 * @create 2022-03-26 15:27
 */
public interface SupplierMapper extends BaseMapper<Supplier> {
     Page<Supplier> findPage(Page<Supplier> page, @Param("supName") String supName);
}
