package com.daiju.cloudsign.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.daiju.cloudsign.entity.SignLocation;
import com.daiju.cloudsign.mapper.SignLocationMapper;
import com.daiju.cloudsign.service.ISignLocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author WDY
 * @Date 2021-01-20 14:39
 * @Description TODO
 */
@Service
public class SignLocationServiceImpl implements ISignLocationService {
    @Autowired
    SignLocationMapper signLocationMapper;
    @Override
    public SignLocation findSignLocationById(String id) {
        return signLocationMapper.selectById(id);
    }

    @Override
    public List<SignLocation> findSignLocationsByQueryWrapper(QueryWrapper<SignLocation> queryWrapper) {
        return signLocationMapper.selectList(queryWrapper);
    }

    @Override
    public int deleteSignLocationById(String id) {
        return signLocationMapper.deleteById(id);
    }

    @Override
    public int updateSignLocationById(SignLocation signLocation) {
        return signLocationMapper.updateById(signLocation);
    }

    @Override
    public int addSignLocation(SignLocation signLocation) {
        return signLocationMapper.insert(signLocation);
    }
}
