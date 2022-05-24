package com.lm.flowershop.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.common.Result;
import com.lm.flowershop.entity.Product;
import com.lm.flowershop.entity.Sort;
import com.lm.flowershop.service.IProductService;
import com.lm.flowershop.service.ISortService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author lm
 * @create 2022-03-26 15:29
 */
@RestController
@RequestMapping("/sort")
public class SortController {
    @Resource
    private ISortService sortService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Sort sort) {
        sortService.saveOrUpdate(sort);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        sortService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        sortService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(sortService.list());
    }

//    @GetMapping("/sort/{sortName}")
//    public Result findSortByName(@PathVariable String sortName) {
//        QueryWrapper<Sort> queryWrapper= new QueryWrapper<>();
//        queryWrapper.eq("sortName",sortName);
//        List<Sort> list= sortService.list(queryWrapper);
//        return Result.success(list);
//    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(sortService.getById(id));
    }

    //分页查询——mybatis-plus的方式查询
    @GetMapping("/page")
    public  Result findPage(@RequestParam Integer pageNum,
                            @RequestParam Integer pageSize,
                            @RequestParam(defaultValue = "") String sortname

    ) {
//        IPage<Sort> page=new Page<>(pageNum,pageSize);
//        QueryWrapper<Sort> queryWrapper=new QueryWrapper<>();
//        if (!"".equals(sortName)){
//            queryWrapper.like("sortName",sortName);
//        }
        return Result.success(sortService.findPage(new Page<>(pageNum,pageSize),sortname));
    }

}
