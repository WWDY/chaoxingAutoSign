package com.daiju.cloudsign.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @Author WDY
 * @Date 2021-01-30 11:40
 * @Description TODO
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @TableId(type = IdType.INPUT)
    private String question;
    private String answer;
}
