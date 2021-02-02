package com.daiju.cloudsign.config;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;

import java.util.List;

/**
 * @Author WDY
 * @Date 2021-01-06 0:18
 * @Description TODO
 */

public interface  MyBaseMapper<T> extends BaseMapper<T> {
    int insertBatchSomeColumn(List<T> entityList);
}
