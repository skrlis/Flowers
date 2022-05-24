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
 * @create 2022-04-21 15:38
 */
@Data
@TableName(value = "fl_login")
@ApiModel(value = "登录日志",description = "")
public class Adminlogin {

    @ApiModelProperty("编号")
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty("姓名")
    private String adname;

    @ApiModelProperty("登陆时间")
    private Date createTime;

}
