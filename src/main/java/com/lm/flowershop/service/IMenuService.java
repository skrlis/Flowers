package com.lm.flowershop.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.lm.flowershop.entity.Menu;

import java.util.List;

/**
 * @author lm
 * @create 2022-03-26 15:24
 */
public interface IMenuService extends IService<Menu> {
    List<Menu> findMenus(String name);
}
