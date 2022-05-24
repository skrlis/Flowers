package com.lm.flowershop.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.common.Result;
import com.lm.flowershop.entity.Manager;
import com.lm.flowershop.entity.Product;
import com.lm.flowershop.entity.Role;
import com.lm.flowershop.entity.Supplier;
import com.lm.flowershop.service.IProductService;
import com.lm.flowershop.service.IRoleService;
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
 * @create 2022-03-26 15:29
 */
@RestController
@RequestMapping("/product")
public class ProductController {
    @Resource
    private IProductService productService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Product product) {
        productService.saveOrUpdate(product);
        return Result.success();
    }
    @GetMapping("/id/{id}")
    public Result findOne(@PathVariable String id){
        QueryWrapper<Product> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("id",id);
        return Result.success(productService.getOne(queryWrapper));
    }
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        productService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        productService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {
        return Result.success(productService.list());
    }

    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(productService.getById(id));
    }


    //分页查询——mybatis-plus的方式查询
    @GetMapping("/page")
    public  Result findPage(@RequestParam Integer pageNum,
                            @RequestParam Integer pageSize,
                            @RequestParam(defaultValue = "")String name,
                            @RequestParam(defaultValue = "")String psort


    ) {
//        IPage<Product> page=new Page<>(pageNum,pageSize);
//        QueryWrapper<Product> queryWrapper=new QueryWrapper<>();
//        if (!"".equals(proName)){
//            queryWrapper.like("proName",proName);
//        }
//        return Result.success(productService.page(page,queryWrapper));
        Page<Product> page=productService.findPage(new Page<>(pageNum,pageSize), name,psort);
        return Result.success(page);
    }




    //分页查询——mybatis-plus的方式查询
    @GetMapping("/page1")
    public  Result findPage1(@RequestParam Integer pageNum,
                            @RequestParam Integer pageSize,
                            @RequestParam(defaultValue = "")String name,
                             @RequestParam(defaultValue = "")String psort


    ) {

        IPage<Product> page=new Page<>(pageNum,pageSize);
        QueryWrapper<Product> queryWrapper=new QueryWrapper<>();
        if (!"".equals(name)){
            queryWrapper.like("name",name);
        }
        if (!"".equals(psort)){
            queryWrapper.like("psort",psort);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(productService.page(page,queryWrapper));
    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response)throws Exception{
        List<Product> list = productService.list();
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","编号");
        writer.addHeaderAlias("name","商品名称");
        writer.addHeaderAlias("psort","所属类别");
        writer.addHeaderAlias("proCost","进价");
        writer.addHeaderAlias("proPrice","售价");
        writer.addHeaderAlias("proStock","库存");
        writer.addHeaderAlias("proFrom","供应商");
        writer.addHeaderAlias("proSales","销量");
        //一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);
        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("商品信息","UTF-8");
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        ServletOutputStream out = response.getOutputStream();
        writer.flush(out,true);
        out.close();
        writer.close();
    }
    /**
     * excel 导入
     * @param file
     * @throws Exception
     */
    @PostMapping("/import")
    public Result imp(MultipartFile file) throws Exception{
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //方式二：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<Product> products = CollUtil.newArrayList();
        for (List<Object> row : list){
            Product product=new Product();
            product.setName(row.get(0).toString());
            product.setPsort(row.get(1).toString());
            product.setProCost(Double.valueOf(row.get(2).toString()));
            product.setProPrice(Double.valueOf(row.get(3).toString()));
            product.setProStock(Integer.valueOf(row.get(4).toString()));
            product.setProFrom(row.get(5).toString());
            product.setProSales(Integer.valueOf(row.get(6).toString()));
            products.add(product);
        }
        productService.saveBatch(products);//插入数据库
        return Result.success(true);
    }
}

