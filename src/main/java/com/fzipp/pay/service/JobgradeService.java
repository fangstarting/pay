package com.fzipp.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fzipp.pay.entity.Jobgrade;

import java.util.List;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
public interface JobgradeService extends IService<Jobgrade> {

    /**
     * 返回List<Jobgrade> {jobgradeId,jobtitle}
     * @return
     */
    List<Jobgrade> getJobgradesOfIdTitle();
}
