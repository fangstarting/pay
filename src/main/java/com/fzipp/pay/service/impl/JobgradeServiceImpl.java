package com.fzipp.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzipp.pay.entity.Jobgrade;
import com.fzipp.pay.mapper.JobgradeMapper;
import com.fzipp.pay.service.JobgradeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@Service
public class JobgradeServiceImpl extends ServiceImpl<JobgradeMapper, Jobgrade> implements JobgradeService {

    @Autowired
    private JobgradeMapper jobgradeMapper;

    @Override
    public List<Jobgrade> getJobgradesOfIdTitle() {
        List<Jobgrade> jobgradesOfIdTitle = jobgradeMapper.getJobgradesOfIdTitle();
        return jobgradesOfIdTitle;
    }
}
