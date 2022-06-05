package com.fzipp.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fzipp.pay.entity.Pay;
import com.fzipp.pay.results.Result;

import java.util.Date;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
public interface PayService extends IService<Pay> {

    /**
     * 月工资核算
     *
     * @param dateStr 月份 yyyy-MM
     * @param daySum 有效考勤天数
     * @return
     * @throws Exception
     */
    Result wageAccounting(String dateStr, Integer daySum) throws Exception;

}
