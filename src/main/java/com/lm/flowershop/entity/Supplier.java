package com.lm.flowershop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

/**
 * @author lm
 * @create 2022-03-17 16:05
 */
@Data
@Getter
@Setter
@ToString
@TableName(value = "fl_supplier")
@ApiModel(value = "供应商",description = "")
public class Supplier {
    private static final long serialVersionUID=1L;

    @ApiModelProperty("编号")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("负责人")
    private String smanager;

    @ApiModelProperty("联系方式")
    private String supPhone;

    @ApiModelProperty("地址")
    private String supAddress;

}
