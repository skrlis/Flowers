package com.lm.flowershop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author lm
 * @create 2022-04-10 17:17
 */
@TableName("fl_out")
@Data
public class Outstorage {
    @ApiModelProperty("编号")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("出库单编号")
    private String outId;

    @ApiModelProperty("操作申请人")
    private String outOper;

    @ApiModelProperty("出库日期")
    private Date outDate;

    @ApiModelProperty("出库产品名称")
    private String outName;

    @ApiModelProperty("客户")
    private String outCustomer;

    @ApiModelProperty("供应商")
    private String outSupplier;

    @ApiModelProperty("出库数量")
    private Integer outNum;

    @ApiModelProperty("数量单位")
    private String outUnit;

    @ApiModelProperty("单价")
    private Double outPrice;

    @ApiModelProperty("总价")
    private Double outTotal;

    @ApiModelProperty("利润")
    private Double outProfit;

    @ApiModelProperty("已付款")
    private Double outPaid;

    @ApiModelProperty("状态")
    private String status;

    @TableField(exist = false)
    private String proname;

    @TableField(exist = false)
    private String prostock;

    @TableField(exist = false)
    private String prosales;

    @TableField(exist = false)
    private String startTime;

    @TableField(exist = false)
    private String endTime;

    @TableField(exist = false)
    private Double sumByToday;

    @TableField(exist = false)
    private Double sumByPaid;

}
