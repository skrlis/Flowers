package com.lm.flowershop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author lm
 * @create 2022-03-28 17:42
 */
@TableName("fl_sort")
@Data
public class Sort {

    @ApiModelProperty("类别编号")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("类别名称")
    private String sortname;

    @TableField(exist = false)
    private List<Product> products;
}
