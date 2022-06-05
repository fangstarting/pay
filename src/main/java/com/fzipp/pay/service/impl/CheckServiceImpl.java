package com.fzipp.pay.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.utils.DateUtil;
import com.fzipp.pay.entity.Check;
import com.fzipp.pay.mapper.CheckMapper;
import com.fzipp.pay.params.check.CheckEchartsStatParam;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.results.check.CheckEcharts;
import com.fzipp.pay.service.CheckService;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.util.*;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@Service
public class CheckServiceImpl extends ServiceImpl<CheckMapper, Check> implements CheckService {

    @Override
    public Check getCheckByUserIdAndDate(Integer userId) {
        QueryWrapper<Check> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        wrapper.eq("date", DateUtil.getDate());
        return super.getOne(wrapper);
    }

    @Override
    public boolean downClockin(Check check) {
        UpdateWrapper<Check> wrapper = new UpdateWrapper<>();
        wrapper.eq("checkId", check.getCheckId()).set("abtypeId", null);
        return super.update(check, wrapper);
    }

    @Override
    public List<Map<String, Object>> getCheckInfo(Integer userId) {
        QueryWrapper<Check> wrapper = new QueryWrapper<>();
        wrapper.eq("userId", userId);
        List<Check> checks = super.list(wrapper);
        List<Map<String, Object>> list = new ArrayList<>();
        for (Check e : checks) {
            HashMap<String, Object> map = new HashMap<>();
            map.put("day", DateUtil.getDateStr(e.getDate()));
            map.put("status", e.getStatus());
            list.add(map);
        }
        return list;
    }

    @Override
    public Result getEchartsData(CheckEchartsStatParam param) throws Exception {
        Result result = new Result();
        if (param.getType() == 1) {
            return this.type1(param.getStartDate(), param.getEndDate());
        } else if (param.getType() == 2) {
            return this.type2(param.getStartDate(), param.getEndDate());
        } else if (param.getType() == 3) {
            return this.type3(param.getStartDate(), param.getEndDate());
        } else {
            result.setError(ErrorCode.FAULT);
            result.setMessage("参数类型错误！");
        }
        return result;
    }

    /**
     * 日期范围查询
     *
     * @return
     */
    private Result type1(String start, String end) throws ParseException {
        Result result = new Result();
        List<CheckEcharts> data = new ArrayList<>();
        Date startDate = DateUtil.getDate(start, "yyyy-MM-dd");
        Date endDate = DateUtil.getDate(end, "yyyy-MM-dd");
        while (endDate.compareTo(startDate) >= 0) { //日期循环
            QueryWrapper<Check> wrapper = new QueryWrapper<Check>().select("status", "late", "early").eq("date", startDate);
            CheckEcharts checkEcharts = this.foreachOpStat(wrapper);
            String date = DateUtil.getStrDateFormat(startDate, "yy/MM/dd");
            checkEcharts.setDate(date);
            data.add(checkEcharts);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDate);
            calendar.add(Calendar.DATE, 1);
            startDate = calendar.getTime();
        }
        result.setData(data);
        result.setError(ErrorCode.CORRECT);
        return result;
    }

    /**
     * 月份范围查询
     *
     * @return
     */
    private Result type2(String start, String end) throws ParseException {
        Result result = new Result();
        List<CheckEcharts> data = new ArrayList<>();
        Date startDateM = DateUtil.getDate(start, "yyyy-MM");
        Date endDateM = DateUtil.getDate(end, "yyyy-MM");
        while (endDateM.compareTo(startDateM) >= 0) { //月份循环
            String strDate = DateUtil.getStrDateFormat(startDateM, "yyyy-MM");
            //遍历当前月份数据并统计
            QueryWrapper<Check> wrapper = new QueryWrapper<Check>().select("status", "late", "early").likeRight("date", strDate);
            CheckEcharts checkEcharts = this.foreachOpStat(wrapper);
            //添加数据
            String date = DateUtil.getStrDateFormat(startDateM, "yyyy/MM");
            checkEcharts.setDate(date);
            data.add(checkEcharts);
            //循环条件
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDateM);
            calendar.add(Calendar.MONTH, 1);//月份+1
            startDateM = calendar.getTime();
        }
        result.setData(data);
        result.setError(ErrorCode.CORRECT);
        return result;
    }

    /**
     * 年份范围查询
     *
     * @return
     */
    private Result type3(String start, String end) throws ParseException {
        Result result = new Result();
        List<CheckEcharts> data = new ArrayList<>();
        Date startDateY = DateUtil.getDate(start, "yyyy");
        Date endDateY = DateUtil.getDate(end, "yyyy");
        while (endDateY.compareTo(startDateY) >= 0) { //年份循环
            String strDate = DateUtil.getStrDateFormat(startDateY, "yyyy");
            //遍历当前年份数据并统计
            QueryWrapper<Check> wrapper = new QueryWrapper<Check>().select("status", "late", "early").likeRight("date", strDate);
            CheckEcharts checkEcharts = this.foreachOpStat(wrapper);
            //添加数据
            String date = DateUtil.getStrDateFormat(startDateY, "yyyy");
            checkEcharts.setDate(date);
            data.add(checkEcharts);
            //循环条件
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(startDateY);
            calendar.add(Calendar.YEAR, 1);//年份+1
            startDateY = calendar.getTime();
        }
        result.setData(data);
        result.setError(ErrorCode.CORRECT);
        return result;
    }

    private CheckEcharts foreachOpStat(QueryWrapper<Check> wrapper) {
        int sum1 = 0;//理论考勤
        int sum2 = 0;//实际出勤
        int sum3 = 0;//异常缺勤
        int sum4 = 0;//迟到人数
        int sum5 = 0;//早退人数
        for (Check e : list(wrapper)) {
            sum1++;
            if (SysProp.CHECK_STATUS_OK.equals(e.getStatus())) sum2++;
            if (SysProp.CHECK_STATUS_ON.equals(e.getStatus())) sum3++;
            if (SysProp.COMMON_STATUS_OK.equals(e.getLate())) sum4++;
            if (SysProp.COMMON_STATUS_OK.equals(e.getEarly())) sum5++;
        }
        return new CheckEcharts(sum1, sum2, sum3, sum4, sum5);
    }

}
