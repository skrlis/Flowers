package com.lm.flowershop.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.common.Result;
import com.lm.flowershop.entity.Buy;
import com.lm.flowershop.entity.Product;
import com.lm.flowershop.entity.Putstorage;
import com.lm.flowershop.entity.Sale;
import com.lm.flowershop.mapper.SaleMapper;
import com.lm.flowershop.service.IBuyService;
import com.lm.flowershop.service.ISaleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @author lm
 * @create 2022-03-17 16:29
 */
@RestController
@RequestMapping("/sale")
public class SaleController {

    @Resource
    private ISaleService saleService;


    //新增和修改
    @PostMapping
    public Result save(@RequestBody Sale sale){
        //新增或者更新
        return Result.success(saleService.saveOrUpdate(sale));

    }
    @PostMapping("/")
    public Result update(@RequestBody Sale sale){
        //新增或者更新
        return Result.success(saleService.updateById(sale));

    }
    //查询所有数据
    @GetMapping("/")
    public Result findAll() {
        return Result.success(saleService.list());
    }
    //删除数据
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success( saleService.removeById(id));
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){

        return Result.success(saleService.removeByIds(ids));
    }

    //查询身份为销售员的用户
    @GetMapping("/passsale")
    public  Result findPage2(@RequestParam Integer pageNum,
                             @RequestParam Integer pageSize

    ) {
        return Result.success(
                saleService.findPage2(new Page<>(pageNum,pageSize)));
    }
    //分页查询——mybatis-plus的方式查询
    @GetMapping("/page")
    public  Result findPage(@RequestParam(defaultValue = "") String saleId,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize

                                    ) {

        Page<Sale> page=saleService.findPage(new Page<>(pageNum,pageSize), saleId);

        return Result.success(page);

    }
    //分页查询——mybatis-plus的方式查询
    @GetMapping("/page1")
    public  Result findPage1(@RequestParam(defaultValue = "") String saleId,
                             @RequestParam(defaultValue = "") String saler,
                             @RequestParam(defaultValue = "") String reviewer,
                             @RequestParam(defaultValue = "") String results,
                             @RequestParam String startTime,
                             @RequestParam String endTime,
                             @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize

    ) {

        Page<Sale> page=saleService.findPage1(new Page<>(pageNum,pageSize), saleId,saler,reviewer,results,startTime,endTime);

        return Result.success(page);

    }
    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response)throws Exception{
        //从数据库查询出所有的数据
        List<Sale> list = saleService.list();
        //通过工具类创建writer写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath+ "/用户信息.xlsx");
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","编号");
        writer.addHeaderAlias("saleId","出库通知单编号");
        writer.addHeaderAlias("saler","申请人");
        writer.addHeaderAlias("saleDate","出库申请日期");
        writer.addHeaderAlias("saleName","出库产品名称");
        writer.addHeaderAlias("saleSupplier","供应商");
        writer.addHeaderAlias("saleNum","出库数量");
        writer.addHeaderAlias("saleUnit","数量单位");
        writer.addHeaderAlias("salePrice","单价");
        writer.addHeaderAlias("saleTotal","总价");
        writer.addHeaderAlias("salePaid","已付款");
        writer.addHeaderAlias("remarks","审核人备注");
        writer.addHeaderAlias("reviewer","审核人");
        writer.addHeaderAlias("results","审核结果");
        writer.addHeaderAlias("reviewdate","审核日期");
        writer.addHeaderAlias("applynote","申请人备注");



        //一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);
        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("出库通知单信息","UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }

    }





