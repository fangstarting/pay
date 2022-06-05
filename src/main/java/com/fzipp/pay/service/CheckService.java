package com.fzipp.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fzipp.pay.entity.Check;
import com.fzipp.pay.params.check.CheckEchartsStatParam;
import com.fzipp.pay.results.Result;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
public interface CheckService extends IService<Check> {

    Check getCheckByUserIdAndDate(Integer userId);

    boolean downClockin(Check check);

    List<Map<String,Object>> getCheckInfo(Integer userId);

    Result getEchartsData(CheckEchartsStatParam param) throws Exception;
}
