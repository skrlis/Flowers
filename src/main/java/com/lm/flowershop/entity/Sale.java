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
 * @create 2022-04-01 15:38
 */
@Data
@TableName(value = "fl_sale")
@ApiModel(value = "出库通知单",description = "")
public class Sale {

    @ApiModelProperty("编号")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("出库通知单编号")
    private String saleId;

    @ApiModelProperty("申请人")
    private String saler;

    @ApiModelProperty("出库申请日期")
    private Date saleDate;

    @ApiModelProperty("出库产品名称")
    private String saleName;

    @ApiModelProperty("供应商")
    private String saleSupplier;

    @ApiModelProperty("出库数量")
    private String saleNum;

    @ApiModelProperty("数量单位")
    private String saleUnit;

    @ApiModelProperty("单价")
    private Double salePrice;

    @ApiModelProperty("总价")
    private Double saleTotal;

    @ApiModelProperty("已付款")
    private Double salePaid;

    @ApiModelProperty("审核人备注")
    private String remarks;

    @ApiModelProperty("审核人")
    private String reviewer;

    @ApiModelProperty("审核结果")
    private String results;

    @ApiModelProperty("审核日期")
    private String reviewdate;

    @ApiModelProperty("申请人备注")
    private String applynote;


    @TableField(exist = false)
    private String proname;

    @TableField(exist = false)
    private String startTime;

    @TableField(exist = false)
    private String endTime;
}

