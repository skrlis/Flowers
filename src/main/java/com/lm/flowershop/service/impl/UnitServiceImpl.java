package com.lm.flowershop.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.lm.flowershop.entity.Menu;
import com.lm.flowershop.entity.Unit;
import com.lm.flowershop.mapper.MenuMapper;
import com.lm.flowershop.mapper.UnitMapper;
import com.lm.flowershop.service.IMenuService;
import com.lm.flowershop.service.IUnitService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author lm
 * @create 2022-03-26 15:23
 */
@Service
public class UnitServiceImpl extends ServiceImpl<UnitMapper, Unit> implements IUnitService {
    @Override
    public List<Unit> findUnits(String unitname) {
        QueryWrapper<Unit> queryWrapper = new QueryWrapper<>();
        if(StrUtil.isNotBlank(unitname)){
            queryWrapper.like("unitname", unitname);
            queryWrapper.orderByDesc("unitsid");
        }

        //查询所有数据
        List<Unit> list= list(queryWrapper);
        //找出pid为null的一级菜单
        List<Unit> parentNodes= list.stream().filter(unit -> unit.getUnitpid() == null).collect(Collectors.toList());
        //找出一级菜单的子菜单
        for (Unit unit : parentNodes){
            //筛选所有数据中pid=父级id的数据就是二级菜单
            unit.setChildren(list.stream().filter(unit1 -> unit.getUnitsid().equals(unit1.getUnitpid())).collect(Collectors.toList()));
        }
        return parentNodes;
    }
}
