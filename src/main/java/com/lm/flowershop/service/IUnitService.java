package com.lm.flowershop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lm.flowershop.entity.Menu;
import com.lm.flowershop.entity.Unit;

import java.util.List;

/**
 * @author lm
 * @create 2022-03-26 15:24
 */
public interface IUnitService extends IService<Unit> {
    List<Unit> findUnits(String unitname);
}
