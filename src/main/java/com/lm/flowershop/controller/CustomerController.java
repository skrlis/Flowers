package com.lm.flowershop.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.date.Quarter;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.common.Result;
import com.lm.flowershop.entity.Customer;
import com.lm.flowershop.entity.Manager;
import com.lm.flowershop.entity.Product;
import com.lm.flowershop.entity.Supplier;
import com.lm.flowershop.service.ICustomerService;
import com.lm.flowershop.service.IProductService;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Date;
import java.util.List;

/**
 * @author lm
 * @create 2022-03-30 15:38
 */
@RestController
@RequestMapping("/customer")
public class CustomerController {
    @Resource
    private ICustomerService customerService;

    // 新增或者更新
    @PostMapping
    public Result save(@RequestBody Customer customer) {
        customerService.saveOrUpdate(customer);
        return Result.success();
    }

    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id) {
        customerService.removeById(id);
        return Result.success();
    }

    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids) {
        customerService.removeByIds(ids);
        return Result.success();
    }

    @GetMapping
    public Result findAll() {

        return Result.success(customerService.list());
    }
    @GetMapping("/findsale")
    public Result findsale() {

        return Result.success(customerService.nameValidate());
    }
    @GetMapping("/findwholesale")
    public Result findwholesale() {

        return Result.success(customerService.saleValidate());
    }
    @GetMapping("/{id}")
    public Result findOne(@PathVariable Integer id) {
        return Result.success(customerService.getById(id));
    }
    //分页查询——mybatis-plus的方式查询
    @GetMapping("/page")
    public  Result findPage(@RequestParam Integer pageNum,
                            @RequestParam Integer pageSize,
                            @RequestParam(defaultValue = "") String cusName,
                            @RequestParam(defaultValue = "") String cusPhone,
                            @RequestParam(defaultValue = "") String cusSort



    ) {
        IPage<Customer> page=new Page<>(pageNum,pageSize);
        QueryWrapper<Customer> queryWrapper=new QueryWrapper<>();
        if (!"".equals(cusName)){
            queryWrapper.like("cus_name",cusName);
        }
        if (!"".equals(cusPhone)){
            queryWrapper.like("cus_phone",cusPhone);
        }
        if (!"".equals(cusSort)){
            queryWrapper.like("cus_sort",cusSort);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(customerService.page(page,queryWrapper));
    }
    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response)throws Exception{
        //从数据库查询出所有的数据
        List<Customer> list = customerService.list();
        //通过工具类创建writer写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath+ "/用户信息.xlsx");
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","客户编号");
        writer.addHeaderAlias("cusName","名称");
        writer.addHeaderAlias("cusSort","类别");
        writer.addHeaderAlias("cusPhone","联系方式");
        writer.addHeaderAlias("cusAddress","地址");
        writer.addHeaderAlias("cusCost","总花费");
        writer.addHeaderAlias("cusPay","总支付");


        //一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);
        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("客户信息","UTF-8");
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
        //方式一：通过javabean的方式读取Excel内的对象，但是要求表头必须是英文，跟Javabean的属性要对应起来。
        //List<Manager> list = reader.readAll(Manager.class);

        //方式二：忽略表头的中文，直接读取表的内容
        List<List<Object>> list = reader.read(1);
        List<Customer> customers = CollUtil.newArrayList();
        for (List<Object> row : list){
            Customer customer=new Customer();
            customer.setCusName(row.get(0).toString());
            customer.setCusSort(row.get(1).toString());
            customer.setCusPhone(row.get(2).toString());
            customer.setCusAddress(row.get(3).toString());
            customer.setCusCost(row.get(4).toString());
            customer.setCusPay(row.get(5).toString());
            customers.add(customer);
        }
        customerService.saveBatch(customers);//插入数据库
        return Result.success(true);

    }
}

