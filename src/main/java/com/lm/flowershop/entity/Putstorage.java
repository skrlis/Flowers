package com.lm.flowershop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author lm
 * @create 2022-04-10 9:44
 */
@Data
@TableName(value = "fl_put")
@ApiModel(value = "入库",description = "")
public class Putstorage {

    @ApiModelProperty("编号")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("入库单号")
    private String putId;

    @ApiModelProperty("入库商品")
    private String putName;

    @ApiModelProperty("入库日期")
    private Date putDate;

    @ApiModelProperty("供应商")
    private String putSupplier;

    @ApiModelProperty("操作人")
    private String putOper;

    @ApiModelProperty("入库数量")
    private Integer putNum;

    @ApiModelProperty("数量单位")
    private String putUnit;

    @ApiModelProperty("单价")
    private Double putPrice;

    @ApiModelProperty("总价")
    private Double putTotal;

    @ApiModelProperty("状态")
    private String status;

    @TableField(exist = false)
    private String proname;

    @TableField(exist = false)
    private String prostock;

    @TableField(exist = false)
    private String startTime;

    @TableField(exist = false)
    private String endTime;

    @TableField(exist = false)
    private Double sumByToday;
}

