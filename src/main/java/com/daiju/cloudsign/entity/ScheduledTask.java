package com.daiju.cloudsign.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.io.Serializable;

/**
 * @author WDY
 * @since 2021-01-25
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledTask implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 用户手机号
     */
    private String taskGroup;

    /**
     * 课程名
     */
    private String taskDesc;

    /**
     * 课程编号
     */
    private String taskId;

    /**
     * 开始时间
     */
    private String starTime;

    /**
     * 结束时间
     */
    private String endTime;

    /**
     * 创建时间
     */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

}
