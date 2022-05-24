package com.lm.flowershop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.common.Result;
import com.lm.flowershop.entity.Adminlogin;
import com.lm.flowershop.entity.Buy;
import com.lm.flowershop.entity.Manager;
import com.lm.flowershop.service.IAdminloginService;
import com.lm.flowershop.service.IBuyService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lm
 * @create 2022-03-17 16:29
 */
@RestController
@RequestMapping("/adminlogin")
public class AdminloginController {

    @Resource
    private IAdminloginService adminloginService;


    //新增和修改
    @PostMapping
    public Result save(@RequestBody Adminlogin adminlogin) {
        //新增或者更新
        return Result.success(adminloginService.saveOrUpdate(adminlogin));

    }

    //查询所有数据
    @GetMapping("/")
    public Result findAll() {
        return Result.success(adminloginService.list());
    }


    //分页查询——mybatis-plus的方式查询
    @GetMapping("/page")
    public Result findPage(@RequestParam Integer pageNum,
                           @RequestParam Integer pageSize

    ) {
        IPage<Adminlogin> page = new Page<>(pageNum, pageSize);
        QueryWrapper<Adminlogin> queryWrapper = new QueryWrapper<>();
        queryWrapper.orderByAsc("id");
        return Result.success(adminloginService.page(page, queryWrapper));
    }

}
