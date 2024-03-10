package com.apiinterface.model.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 
 * @TableName call_record
 */
@TableName(value ="call_record")
@Data
public class CallRecord implements Serializable {
    /**
     * 调用记录ID
     */
    @TableId(value = "id")
    private Long id;

    /**
     * 调用者id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 调用接口id
     */
    @TableField(value = "interface_id")
    private Long interfaceId;

    /**
     * 调用次数
     */
    @TableField(value = "call_count")
    private Long callCount;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 修改时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}