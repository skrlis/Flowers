package com.lm.flowershop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author lm
 * @create 2022-03-30 15:28
 */
@Data
@TableName(value = "fl_customer")
@ApiModel(value = "顾客",description = "")
public class Customer {
    private static final long serialVersionUID=1L;

    @ApiModelProperty("编号")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    private String cusName;

    @ApiModelProperty("类别")
    private String cusSort;

    @ApiModelProperty("联系方式")
    private String cusPhone;

    @ApiModelProperty("地址")
    private String cusAddress;

    @ApiModelProperty("总花费")
    private String cusCost;

    @ApiModelProperty("总支付")
    private String cusPay;

    @ApiModelProperty("创建时间")
    private Date createTime;
}
