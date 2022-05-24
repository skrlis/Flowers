package com.lm.flowershop.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.entity.Product;
import com.lm.flowershop.entity.Role;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @author lm
 * @create 2022-03-26 15:27
 */
public interface ProductMapper extends BaseMapper<Product> {
    Page<Product> findPage(Page<Product> page,@Param("name") String name,@Param("psort") String psort);
}
