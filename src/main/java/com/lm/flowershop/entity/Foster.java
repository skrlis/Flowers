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
 * @create 2022-04-17 17:02
 */
@Data
@TableName(value = "fl_foster")
@ApiModel(value = "寄养信息",description = "")
public class Foster {

    @ApiModelProperty("编号")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("寄养人")
    private String sender;

    @ApiModelProperty("联系方式")
    private String phone;

    @ApiModelProperty("寄养植物名称")
    private String fosName;

    @ApiModelProperty("品种")
    private String fosVariety;

    @ApiModelProperty("图片")
    private String fosUrl;

    @ApiModelProperty("养护备注")
    private String fosNote;

    @ApiModelProperty("寄养开始日期")
    private Date fosStarttime;

    @ApiModelProperty("寄养结束日期")
    private Date fosEndtime;

    @ApiModelProperty("单天价格")
    private Double fosPrice;

    @ApiModelProperty("总价")
    private Double fosTotal;

    @ApiModelProperty("支付状态")
    private String fosStatus;

    @ApiModelProperty("操作人")
    private String fosOper;

    @TableField(exist = false)
    private String startTime;

}
