package com.lm.flowershop.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.common.Result;
import com.lm.flowershop.entity.Buy;
import com.lm.flowershop.entity.Customer;
import com.lm.flowershop.entity.Sale;
import com.lm.flowershop.entity.Supplier;
import com.lm.flowershop.service.IBuyService;
import com.lm.flowershop.service.ISupplierService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author lm
 * @create 2022-03-17 16:29
 */
@RestController
@RequestMapping("/buy")
public class BuyController {

    @Resource
    private IBuyService buyService;


    //新增和修改
    @PostMapping
    public Result save(@RequestBody Buy buy){
        //新增或者更新
        return Result.success(buyService.saveOrUpdate(buy));

    }
    @GetMapping("/supName/{supName}")
    public Result findOne(@PathVariable String buyid){
        QueryWrapper<Buy> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("buyid",buyid);
        return Result.success(buyService.getOne(queryWrapper));
    }

    //查询所有数据
    @GetMapping("/")
    public Result findAll() {
        return Result.success(buyService.list());
    }
    //删除数据
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success( buyService.removeById(id));
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){
        return Result.success(buyService.removeByIds(ids));
    }
    //查询已通过审核的采购申请单
    @GetMapping("/passbuy")
    public  Result findPage1(@RequestParam Integer pageNum,
                             @RequestParam Integer pageSize

    ) {
        return Result.success(
                buyService.findPage1(new Page<>(pageNum,pageSize)));
    }

    //分页查询——mybatis-plus的方式查询
    @GetMapping("/page")
    public  Result findPage(@RequestParam(defaultValue = "") String buyid,
                            @RequestParam(defaultValue = "") String buyApply,
                            @RequestParam(defaultValue = "") String buyer,
                            @RequestParam(defaultValue = "") String buyName,
                            @RequestParam(defaultValue = "") String buyStatus,
                            @RequestParam String startTime,
                            @RequestParam String endTime,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize
                                    ) {

        Page<Buy> page=buyService.findPage(new Page<>(pageNum,pageSize), buyid,buyApply,buyer,buyName,buyStatus,startTime,endTime);

        return Result.success(page);
    }
    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response)throws Exception{
        //从数据库查询出所有的数据
        List<Buy> list = buyService.list();
        //通过工具类创建writer写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath+ "/用户信息.xlsx");
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","编号");
        writer.addHeaderAlias("buyid","采购单号");
        writer.addHeaderAlias("buyDate","采购日期");
        writer.addHeaderAlias("buyApply","申请人");
        writer.addHeaderAlias("buyer","采购人");
        writer.addHeaderAlias("buyName","供应商");
        writer.addHeaderAlias("buySupplier","采购数量");
        writer.addHeaderAlias("buyCount","数量单位");
        writer.addHeaderAlias("buyUnit","单价");
        writer.addHeaderAlias("buyPrice","类别");
        writer.addHeaderAlias("buyStatus","审核状态");
        writer.addHeaderAlias("reviewer","审核人");
        writer.addHeaderAlias("reviewdate","审核日期");
        writer.addHeaderAlias("remarks","审核备注");



        //一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);
        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("采购申请单信息","UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }}


