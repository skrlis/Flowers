package com.lm.flowershop.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

/**
 * @author lm
 * @create 2022-03-24 9:30
 */
@Data
@TableName("sys_file")
public class Files {
    @TableId(type = IdType.AUTO)
    private Integer id;
    private String name;
    private String type;
    private Long size;
    private String url;
    private String md5;
    private Boolean isDelete;
    private Boolean enable;

}
