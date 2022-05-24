package com.lm.flowershop.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.common.Result;
import com.lm.flowershop.entity.Customer;
import com.lm.flowershop.entity.Manager;
import com.lm.flowershop.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lm
 * @create 2022-03-25 16:57
 */
@RestController
@RequestMapping("/echarts")
public class EchartsController {
    @Autowired
    private IPutService putService;
    @Autowired
    private IOutService outService;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IProductService productService;
    @Autowired
    private ISortService sortService;

    @GetMapping("/members")
    public Result members(){
        List<Customer> list =customerService.list();
        int q1=0;//第一季度
        int q2=0;//第二季度
        int q3=0;//第三季度
        int q4=0;//第四季度
        for (Customer customer : list){
            Date createTime=customer.getCreateTime();
            Quarter quarter= DateUtil.quarterEnum(createTime);
            switch (quarter){
                case Q1:q1 += 1; break;
                case Q2:q2 += 1; break;
                case Q3:q3 += 1; break;
                case Q4:q4 += 1; break;
                default: break;
            }
        }
        return Result.success(CollUtil.newArrayList(q1,q2,q3,q4));
    }
    @GetMapping("/members1")
    public Result members1(){
        {
            return Result.success(productService.list());
        }}
    @GetMapping("/members2")
    public  Result findPage1(

    ) {
        return Result.success(
                sortService.findPage1(new Page<>()));
    }
    //统计入库信息
    @GetMapping("/members3")
    public  Result findPage2(

    ) {
        return Result.success(putService.list());
    }
    //统计出库信息
    @GetMapping("/members4")
    public  Result findPage3(

    ) {
        return Result.success(outService.list());
    }
}
