package com.lm.flowershop.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.common.Result;
import com.lm.flowershop.entity.Outstorage;
import com.lm.flowershop.entity.Putstorage;
import com.lm.flowershop.service.IOutService;
import com.lm.flowershop.service.IPutService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.util.List;

/**
 * @author lm
 * @create 2022-03-17 16:29
 */
@RestController
@RequestMapping("/out")
public class OutController {

    @Resource
    private IOutService outService;


    //新增和修改
    @PostMapping
    public Result save(@RequestBody Outstorage outstorage){
        //新增或者更新
        return Result.success(outService.saveOrUpdate(outstorage));

    }
    //查询所有数据
    @GetMapping("/")
    public Result findAll() {
        return Result.success(outService.list());
    }
    //删除数据
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success( outService.removeById(id));
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){

        return Result.success(outService.removeByIds(ids));
    }

    //总收入
    @GetMapping("/findsum")
    public Result sumByToday() {
        return Result.success(outService.sumByToday());
    }
    //总支付
    @GetMapping("/findpaid")
    public Result sumByPaid() {
        return Result.success(outService.sumByPaid());
    }
    //销量
    @GetMapping("/findwholesale")
    public Result findwholesale() {

        return Result.success(outService.saleValidate());
    }
    //分页查询——mybatis-plus的方式查询
    @GetMapping("/page")
    public  Result findPage(@RequestParam(defaultValue = "") String outId,
                            @RequestParam(defaultValue = "") String outOper,
                            @RequestParam(defaultValue = "") String outCustomer,
                            @RequestParam String startTime,
                            @RequestParam String endTime,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize

    ) {

        Page<Outstorage> page=outService.findPage(new Page<>(pageNum,pageSize), outId,outOper,outCustomer,startTime,endTime);

        return Result.success(page);

    }
    @GetMapping("/page1")
    public  Result findPage1(
                            @RequestParam(defaultValue = "") String startTime,
                            @RequestParam(defaultValue = "") String endTime,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize

    ) {

        Page<Outstorage> page=outService.findPage1(new Page<>(pageNum,pageSize),startTime,endTime);

        return Result.success(page);

    }
    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response)throws Exception{
        //从数据库查询出所有的数据
        List<Outstorage> list = outService.list();
        //通过工具类创建writer写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath+ "/用户信息.xlsx");
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","编号");
        writer.addHeaderAlias("outId","出库单编号");
        writer.addHeaderAlias("outOper","操作申请人");
        writer.addHeaderAlias("outDate","出库日期");
        writer.addHeaderAlias("outName","出库产品名称");
        writer.addHeaderAlias("outCustomer","客户");
        writer.addHeaderAlias("outSupplier","供应商");
        writer.addHeaderAlias("outNum","出库数量");
        writer.addHeaderAlias("outUnit","数量单位");
        writer.addHeaderAlias("outPrice","单价");
        writer.addHeaderAlias("outTotal","总价");
        writer.addHeaderAlias("outProfit","利润");
        writer.addHeaderAlias("outPaid","已付款");
        writer.addHeaderAlias("status","状态");


        //一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);
        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("出库单信息","UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }
}


