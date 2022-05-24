package com.lm.flowershop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author lm
 * @create 2022-04-02 16:11
 */
@Getter
@Setter
@TableName("fl_unit")
@ApiModel(value = "数量单位", description = "")
public class Unit {

    @ApiModelProperty("id")
    @TableId(value = "unitsid", type = IdType.AUTO)
    private Integer unitsid;

    @ApiModelProperty("名称")
    private String unitname;

    @ApiModelProperty("父级id")
    private Integer unitpid;

    @TableField(exist = false)
    private List<Unit> children;
}
