package com.lm.flowershop.controller;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lm.flowershop.common.Constants;
import com.lm.flowershop.common.Result;
import com.lm.flowershop.controller.dto.ManagerDTO;
import com.lm.flowershop.entity.Manager;
import com.lm.flowershop.entity.Supplier;
import com.lm.flowershop.service.IManagerService;
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
@RequestMapping("/supplier")
public class SupplierController {

    @Resource
    private ISupplierService supplierService;


    //新增和修改
    @PostMapping
    public Result save(@RequestBody Supplier supplier){
        //新增或者更新
        return Result.success(supplierService.saveOrUpdate(supplier));

    }
    @GetMapping("/supName/{supName}")
    public Result findOne(@PathVariable String supName){
        QueryWrapper<Supplier> queryWrapper=new QueryWrapper<>();
        queryWrapper.eq("supName",supName);
        return Result.success(supplierService.getOne(queryWrapper));
    }

    //查询所有数据
    @GetMapping("/")
    public Result findAll() {
        return Result.success(supplierService.list());
    }
    //删除数据
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        return Result.success( supplierService.removeById(id));
    }

    //批量删除
    @PostMapping("/del/batch")
    public Result deleteBatch(@RequestBody List<Integer> ids){

        return Result.success(supplierService.removeByIds(ids));
    }


    //分页查询——mybatis-plus的方式查询
    @GetMapping("/page")
    public  Result findPage(@RequestParam(defaultValue = "") String name,
                            @RequestParam(defaultValue = "") String smanager,
                            @RequestParam Integer pageNum,
                            @RequestParam Integer pageSize

                                    ) {

        IPage<Supplier> page=new Page<>(pageNum,pageSize);
        QueryWrapper<Supplier> queryWrapper=new QueryWrapper<>();
        if (!"".equals(name)){
            queryWrapper.like("name",name);
        }
        if (!"".equals(smanager)){
            queryWrapper.like("smanager",smanager);
        }
        queryWrapper.orderByDesc("id");
        return Result.success(supplierService.page(page,queryWrapper));
    }

    //分页查询
    //接口路径：/user/page?pageNum=1&pageSize=10
    //@RequestParam接收
//    @GetMapping("/page")
//    public  Map<String,Object> findPage(@RequestParam Integer pageNum,
//                                        @RequestParam Integer pageSize,
//                                        @RequestParam String adName) {
//        pageNum = (pageNum - 1) * pageSize;
//        List<Manager> data=managerMapper.selectpage(pageNum,pageSize,adName);
//        Integer total=managerMapper.selectTotal(adName);
//        Map<String,Object> res=new HashMap<>();
//        res.put("data",data);
//        res.put("total",total);
//        return res;
//    }

    /**
     * 导出接口
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response)throws Exception{
        //从数据库查询出所有的数据
        List<Supplier> list = supplierService.list();
        //通过工具类创建writer写出到磁盘路径
        //ExcelWriter writer = ExcelUtil.getWriter(filesUploadPath+ "/用户信息.xlsx");
        //在内存操作，写出到浏览器
        ExcelWriter writer = ExcelUtil.getWriter(true);
        //自定义标题别名
        writer.addHeaderAlias("id","供应商编号");
        writer.addHeaderAlias("name","供应商名称");
        writer.addHeaderAlias("smanager","负责人");
        writer.addHeaderAlias("supPhone","联系方式");
        writer.addHeaderAlias("supAddress","地址");

        //一次性写出list内的对象到Excel，使用默认样式，强制输出标题
        writer.write(list,true);
        //设置浏览器响应格式
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("供应商信息","UTF-8");
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
        List<Supplier> suppliers = CollUtil.newArrayList();
        for (List<Object> row : list){
            Supplier supplier=new Supplier();
            supplier.setName(row.get(0).toString());
            supplier.setSmanager(row.get(1).toString());
            supplier.setSupPhone(row.get(2).toString());
            supplier.setSupAddress(row.get(3).toString());
            suppliers.add(supplier);

        }
        supplierService.saveBatch(suppliers);//插入数据库
        return Result.success(true);

    }
}
