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
@TableName(value = "fl_buy")
@ApiModel(value = "采购单号",description = "")
public class Buy {

    @ApiModelProperty("编号")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("采购单号")
    private String buyid;

    @ApiModelProperty("采购日期")
    private Date buyDate;

    @ApiModelProperty("申请人")
    private String buyApply;

    @ApiModelProperty("采购人")
    private String buyer;

    @ApiModelProperty("采购商品")
    private String buyName;

    @ApiModelProperty("供应商")
    private String buySupplier;

    @ApiModelProperty("采购数量")
    private String buyCount;

    @ApiModelProperty("数量单位")
    private String buyUnit;

    @ApiModelProperty("单价")
    private Double buyPrice;

    @ApiModelProperty("审核状态")
    private String buyStatus;

    @ApiModelProperty("审核人")
    private String reviewer;

    @ApiModelProperty("审核日期")
    private String reviewdate;

    @ApiModelProperty("审核备注")
    private String remarks;

    @TableField(exist = false)
    private String startTime;

    @TableField(exist = false)
    private String endTime;



}

