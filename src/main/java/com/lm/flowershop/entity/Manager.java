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
@TableName(value = "fl_admin")
@ApiModel(value = "Manager对象",description = "")
@ToString
public class Manager {
    private static final long serialVersionUID=1L;

    @ApiModelProperty("adid")
    @TableId(value = "adid",type = IdType.AUTO)
    private Integer adid;

    @ApiModelProperty("管理员名称")
    private String adname;

    @ApiModelProperty("管理员密码")
    private String adpwd;

    @ApiModelProperty("角色")
    private String role;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("头像")
    private String avatarUrl;

    @ApiModelProperty("昵称")
    private String nikename;

    private String phone;


    private Boolean isDelete;
    private Boolean enable;

}
