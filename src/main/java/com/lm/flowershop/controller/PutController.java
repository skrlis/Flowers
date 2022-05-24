package com.lm.flowershop.controller;

import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.common.Result;
import com.lm.flowershop.entity.Buy;
import com.lm.flowershop.entity.Putstorage;
import com.lm.flowershop.entity.Sale;
import com.lm.flowershop.service.IPutService;
import com.lm.flowershop.service.ISaleService;
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
@RequestMapping("/put")
public class PutController {

    @Resource
    private IPutService putService;


    //新增和修改
    @PostMapping
    public Result save(@RequestBody Putstorage putstorage){
        //新增或者更新
        return Result.success(putService.saveOrUpdate(putstorage));

    }
    //查询所有数据
    @GetMapping("/")
    public Result findAll() {
        return Result.success(putService.list());
    }
    //删除数据
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success( putService.removeById(id));
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){

        return Result.success(putService.removeByIds(ids));
    }

    //总支出
    @GetMapping("/findsum")
    public Result sumByToday() {
        return Result.success(putService.sumByToday());
    }
    //入库量
    @GetMapping("/findwholput")
    public Result findwholput() {

        return Result.success(putService.saleValidate());
    }
    //分页查询——mybatis-plus的方式查询
    @GetMapping("/page")
    public  Result findPage(@RequestParam(defaultValue = "") String putId,
                             @RequestParam(defaultValue = "") String putOper,
                             @RequestParam String startTime,
                             @RequestParam String endTime,
                             @RequestParam Integer pageNum,
                             @RequestParam Integer pageSize

    ) {

        Page<Putstorage> page=putService.findPage(new Page<>(pageNum,pageSize), putId,putOper,startTime,endTime);

        return Result.success(page);

    }
    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response)throws Exception{
        //从数据库查询出所有的数据
        List<Putstorage> list = putService.list();
        //通过工具类创建writer写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath+ "/用户信息.xlsx");
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","编号");
        writer.addHeaderAlias("putId","入库单号");
        writer.addHeaderAlias("putName","入库商品");
        writer.addHeaderAlias("putDate","入库日期");
        writer.addHeaderAlias("putSupplier","供应商");
        writer.addHeaderAlias("putOper","操作人");
        writer.addHeaderAlias("putNum","入库数量");
        writer.addHeaderAlias("putUnit","数量单位");
        writer.addHeaderAlias("putPrice","单价");
        writer.addHeaderAlias("putTotal","总价");
        writer.addHeaderAlias("status","状态");

        //一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);
        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("入库单信息","UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");

        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }
}


