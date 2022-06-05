package com.fzipp.pay.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.constant.SystemKey;
import com.fzipp.pay.common.utils.ArithmeticUtils;
import com.fzipp.pay.common.utils.DateUtil;
import com.fzipp.pay.common.utils.RequestUtil;
import com.fzipp.pay.entity.*;
import com.fzipp.pay.mapper.PayMapper;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
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
@Transactional
public class PayServiceImpl extends ServiceImpl<PayMapper, Pay> implements PayService {

    private static final Logger LOGGER = LoggerFactory.getLogger(PayServiceImpl.class);

    @Autowired
    private CheckService checkService;


    @Override
    public Result wageAccounting(String dateStr, Integer daySum) throws Exception {
        Result result = new Result();
        //覆盖数据
        payService.remove(new QueryWrapper<Pay>().eq("date", dateStr));
        String dateString = dateStr + "-01";
        Date date = DateUtil.getDate(dateString, "yyyy-MM-dd");
        //获取当月总天数
//        String yyyy = DateUtil.getStrDateFormat(date, "yyyy");
//        String mm = DateUtil.getStrDateFormat(date, "MM");
//        Integer days = DateUtil.getMonthLastDay(Integer.valueOf(yyyy), Integer.valueOf(mm));
        //查询出当前月分所有打卡列表
        QueryWrapper<Check> wrapper = new QueryWrapper<>();
        wrapper.likeRight("date", dateStr);
        List<Check> checks = checkService.list(wrapper);
        if (checks.size() == 0) {//判断有无数据源
            result.setError(ErrorCode.FAULT);
            result.setMessage("当前月份考勤信息为空！");
            return result;
        }
        //获取当月考勤Map<userId,记录数>
        Map<Integer, Integer> usersMap = new HashMap<>();
        //使用Map统计当前月每个人有效打卡次数即考勤天数 key：员工Id，val：打卡天数
        Map<Integer, Integer> map = new HashMap<>();
        //统计缺勤状态信息 key:userId，val：Map->key:缺勤类型，val缺勤次数
        HashMap<Integer, HashMap<Integer, Integer>> map2 = new HashMap<>();
        //统计迟到次数 key:userId，val：次数
        Map<Integer, Integer> map3 = new HashMap<>();
        //统计早退次数 key:userId，val：次数
        Map<Integer, Integer> map4 = new HashMap<>();
        //统计加班时长 map->key:userId;val:时长
        Map<Integer, BigDecimal> map5 = this.getExtraWorkByDateStr(dateStr);
        checks.forEach(e -> {
            Integer userId = e.getUserId();
            if (usersMap.containsKey(userId)) {
                usersMap.put(userId, usersMap.get(userId) + 1);
            } else {
                usersMap.put(userId, 1);
            }
            if (SysProp.CHECK_STATUS_OK.equals(e.getStatus())) { //考勤成功
                if (map.containsKey(userId)) {
                    map.put(userId, map.get(userId) + 1);
                } else {
                    map.put(userId, 1);
                }
                if (SysProp.COMMON_STATUS_OK.equals(e.getLate())) {//迟到
                    if (map3.containsKey(userId)) {
                        map3.put(userId, map3.get(userId) + 1);
                    } else {
                        map3.put(userId, 1);
                    }
                }
                if (SysProp.COMMON_STATUS_OK.equals(e.getEarly())) {//早退
                    if (map4.containsKey(userId)) {
                        map4.put(userId, map4.get(userId) + 1);
                    } else {
                        map4.put(userId, 1);
                    }
                }
            } else if (SysProp.CHECK_STATUS_ON.equals(e.getStatus())) { //缺勤异常

                Integer abtypeId = e.getAbtypeId();

                if (map2.containsKey(userId)) {//<1001,<1,2>>

                    HashMap<Integer, Integer> mapUserId = map2.get(userId); //mapUserId-> <abtypeId,n>

                    if (mapUserId.containsKey(abtypeId)) {
                        mapUserId.put(abtypeId, mapUserId.get(abtypeId) + 1); //mapUserId-> <abtypeId,n+1>
                    } else {
                        mapUserId.put(abtypeId, 1); //mapUserId-> <abtypeId,1>
                    }
                    map2.put(userId, mapUserId); // <userId,mapUserId-><abtypeId,n+1>>
                } else {
                    //map2 -> key 不存在当前userId
                    HashMap<Integer, Integer> newMap = new HashMap<>();
                    newMap.put(abtypeId, 1);
                    map2.put(userId, newMap); // <userId,<abtypeId,1>>
                }
            }
        });
        boolean flag = true;
        for (Map.Entry<Integer, Integer> entry : usersMap.entrySet()) {
            Integer userId = entry.getKey();//用户id
            Integer checkSum = map.get(userId);//有效打卡天数
            if (checkSum == null) {//当前员工本月无打卡记录
//                continue;
                checkSum = 0;
            }
            //缺勤信息Map->key:缺勤类型，val缺勤次数
            HashMap<Integer, Integer> onCheckInfo = map2.get(userId);
            //迟到次数
            Integer lateSum = map3.get(userId);
            if (lateSum == null) lateSum = 0;
            //早退次数
            Integer earlySUm = map4.get(userId);
            if (earlySUm == null) earlySUm = 0;
            //加班时长
            BigDecimal duration = map5.get(userId);
            if (duration == null) duration = new BigDecimal(0);
            Boolean aBoolean = this.computeSalary(userId, checkSum, daySum, dateStr, date, onCheckInfo, lateSum, earlySUm, duration);
            if (!aBoolean) flag = false;
        }
        String[] split = dateStr.split("-");
        if (!flag) {
            result.setSign(false);
            String mes = split[0] + "年" + split[1] + "月份薪资核算存在异常！";
            result.setError(ErrorCode.FAULT);
            result.setMessage(mes);
            this.addAuditLog(mes);
        } else {
            result.setSign(true);
            String mes = split[0] + "年" + split[1] + "月份薪资核算成功！";
            result.setError(ErrorCode.CORRECT);
            result.setMessage(mes);
            this.addAuditLog(mes);
        }
        return result;
    }


    @Autowired
    private PayconfigService payconfigService;

    @Autowired
    private UserService userService;

    @Autowired
    private JobgradeService jobgradeService;

    @Autowired
    private SysConfigsService sysConfigsService;

    @Autowired
    private PayService payService;

    /**
     * 计算薪水
     *
     * @param userId
     * @param checkSum    考勤次数
     * @param daySum      考核天数
     * @param dateStr     yyyy-MM
     * @param date        当前月一号的时间Date对象
     * @param onCheckInfo 缺勤信息Map->key:缺勤类型，val缺勤次数
     * @param lateSum     迟到次数
     * @param earlySUm    早退次数
     * @param duration    加班时长
     * @return
     */
    private Boolean computeSalary(Integer userId, Integer checkSum, Integer daySum, String dateStr,
                                  Date date, HashMap<Integer, Integer> onCheckInfo,
                                  Integer lateSum, Integer earlySUm, BigDecimal duration) {
        LOGGER.info("userId:" + JSON.toJSONString(userId) +
                "checkSum:" + JSON.toJSONString(checkSum) +
                "daySum:" + JSON.toJSONString(daySum) +
                "dateStr:" + JSON.toJSONString(dateStr) +
                "date:" + JSON.toJSONString(date) +
                "onCheckInfo:" + JSON.toJSONString(onCheckInfo) +
                "lateSum:" + JSON.toJSONString(lateSum) +
                "earlySUm:" + JSON.toJSONString(earlySUm) +
                "duration:" + JSON.toJSONString(duration)
        );
        User user = userService.getById(userId);
        //计算出勤比率
        String ratio = ArithmeticUtils.div(checkSum.toString(), daySum.toString(), 4);
        //若出勤率大于1则使其等于1
        if (Double.valueOf(ratio) > 1.00) {
            ratio = "1.00";
        }
        Pay pay = new Pay();
        //1工号
        pay.setUserId(userId);
        //2工资月分yyyy-MM
        pay.setDate(dateStr);
        //3迟到早退次数
        pay.setLate(lateSum + earlySUm);
        //4考勤次数
        pay.setEarly(checkSum);
        BigDecimal basepay = user.getBasepay();//员工基本工资 //基本工资计算个税
        //5员工基本工资
        pay.setBasepay(basepay);
        BigDecimal jobbonus = jobgradeService.getById(user.getJobgradeId()).getJobbonus();//职称奖金
        //6职称奖金
        pay.setJobbonus(jobbonus);
        //7发放状态(0 未发放 1 已发放)
        pay.setStatus(SysProp.COMMON_STATUS_NO);
        //8更新时间
        pay.setUpdatetime(new Date());
        //9全勤奖金
        //10加班费
        //11请假天数
        Integer insure = 0;
        if (onCheckInfo != null) {
            if (onCheckInfo.get(SysProp.ABTYPE_LEAVE_ID_2) != null) {
                insure = onCheckInfo.get(SysProp.ABTYPE_LEAVE_ID_2);
            }
        }
        pay.setInsure(insure);
        //12缺勤天数
        pay.setAbsenteeism(daySum - checkSum < 0 ? 0 : daySum - checkSum);
        //迟到早退总数
        int pay3sum = lateSum + earlySUm;
        //应发工资
        BigDecimal idealpay;
        //实发工资
        BigDecimal factpay;
        //个人所得税
        BigDecimal tax = new BigDecimal(0);
        //补贴
        BigDecimal subsidy = new BigDecimal("0");
        //扣款金额
        BigDecimal submoney = new BigDecimal("0");
        //补贴扣款详情
        StringBuffer leave = new StringBuffer();
        //详细信息记录
        List<String> mess = new ArrayList<>();
        mess.add("工号：" + user.getUserId());
        mess.add("姓名：" + user.getRealname());
        mess.add("工资月份：" + dateStr);
        mess.add("考勤天数：" + daySum + "天");
        mess.add("出勤天数：" + checkSum + "天");
        mess.add("缺勤天数：" + pay.getAbsenteeism() + "天");
        mess.add("请假天数：" + pay.getInsure() + "天");
        mess.add("迟到次数：" + lateSum + "次");
        mess.add("早退次数：" + earlySUm + "次");
        mess.add("基本工资：" + user.getBasepay() + "元");
        mess.add("职称奖金：" + pay.getJobbonus() + "元");
        //获取薪资配置信息
        QueryWrapper<Payconfig> wrapper = new QueryWrapper<>();
        wrapper.eq("status", SysProp.COMMON_STATUS_OK).eq("jobgradeId", user.getJobgradeId()).or().isNull("jobgradeId");
        List<Payconfig> payconfigs = payconfigService.list(wrapper);
        for (Payconfig payconfig : payconfigs) {
            if (SysProp.COMMON_STATUS_NO.equals(payconfig.getStatus())) continue;
            String name = payconfig.getName();
            String type = payconfig.getType();
            BigDecimal money = payconfig.getMoney();
            if (payconfig.getPayconfigId().equals(1)) {//1.工龄工资计算
                Date hiredate = user.getHiredate();
                long l = date.getTime() - hiredate.getTime();
                long workYear = l / 1000 / 60 / 60 / 24 / 365;
                String mul = ArithmeticUtils.mul(String.valueOf(workYear), money.toString(), 2);
                //13工龄工资
                pay.setSenioritypay(new BigDecimal(mul));
                String mes = name + "：" + mul + "元，工龄：" + workYear + "年";
                mess.add(mes);
            } else if (payconfig.getPayconfigId().equals(2)) {//2.全勤
                String d1 = ArithmeticUtils.div(ratio, "1", 10);
                String d2 = ArithmeticUtils.div("1", "1", 10);
                String mes;
                //9全勤奖金
                if (d1.equals(d2)) {// 判断是否全勤
                    pay.setAttendance(money);
                    mes = name + "：" + money + "元";
                } else {
                    pay.setAttendance(new BigDecimal("0"));
                    mes = name + "：0元";
                }
                mess.add(mes);
            } else if (payconfig.getPayconfigId().equals(3)) {//3.迟到早退
                String mul = ArithmeticUtils.mul(String.valueOf(pay3sum), money.toString(), 2);
                String add = ArithmeticUtils.add(submoney.toString(), mul, 2);
                submoney = new BigDecimal(add);
                String mes = name + "扣款：-" + add + "元";
                mess.add(mes);
            } else if (payconfig.getPayconfigId().equals(4)) {//4.加班费用
                String mul = ArithmeticUtils.mul(duration.toString(), money.toString(), 2);
                String mes = name + "：" + mul + "元，加班时长：" + duration + "小时";
                //10加班费
                pay.setOvertime(new BigDecimal(mul));
                mess.add(mes);
            } else if (payconfig.getPayconfigId().equals(5)) {//5.个人所得税
                //基本薪资*出勤率>5000元
                String payRatio = ArithmeticUtils.mul(basepay.toString(), ratio, 2);
                if (Double.valueOf(payRatio) > Double.valueOf(5000)) {
                    //计算个税
                    String mul = ArithmeticUtils.mul(basepay.toString(), money.toString(), 2);
                    tax = new BigDecimal(mul);
                }
                String mes = name + "：" + tax + "元";
                mess.add(mes);
            } else {//其余配置项：无需计算的
                if (SysProp.PAY_CONFIG_TYPE_ADD.equals(type)) {//增加
                    String add = ArithmeticUtils.add(subsidy.toString(), money.toString(), 2);
                    subsidy = new BigDecimal(add);
                    String mes = name + "：" + money + "元";
                    mess.add(mes);
                } else if (SysProp.PAY_CONFIG_TYPE_SUB.equals(type)) {//减少
                    String add = ArithmeticUtils.add(submoney.toString(), money.toString(), 2);
                    submoney = new BigDecimal(add);
                    String mes = name + "：-" + money + "元";
                    mess.add(mes);
                }
            }
        }
        pay.setSubsidy(subsidy);//14补贴
        mess.add("补贴总金额：" + pay.getSubsidy() + "元");
        pay.setTax(tax);//15个税
        mess.add("个人所得税：" + pay.getTax() + "元");

        //出勤率低于**% 扣除职称奖金
        SysConfigs sys = sysConfigsService.getById(SystemKey.PAY_102);
        String sysValueNumber = sys.getSysValueNumber().toString();
        if (Double.valueOf(ratio) < Double.valueOf(sysValueNumber)) {
            String a = ArithmeticUtils.mul(ratio, "100", 2);
            String b = ArithmeticUtils.mul(sysValueNumber, "100", 2);
            String mes = "出勤率：" + a + "%；低于考核标准" + b + "%；故扣除职称奖金";
            String mul = ArithmeticUtils.mul(ratio, jobbonus.toString(), 2);
            String sub = ArithmeticUtils.add(submoney.toString(), mul, 2);
            submoney = new BigDecimal(sub);
            mess.add(mes);
        }
        pay.setSubmoney(submoney);//16扣款金额
        mess.add("扣款总金额：" + pay.getSubmoney() + "元");
        //应法工资
        double v = Double.valueOf(ratio) * (Double.valueOf(basepay.toString()) + Double.valueOf(jobbonus.toString())
                + Double.valueOf(pay.getAttendance().toString()) + Double.valueOf(pay.getSenioritypay().toString())
                + Double.valueOf(pay.getSubsidy().toString()))
                + Double.valueOf(pay.getOvertime().toString()) - Double.valueOf(pay.getTax().toString());
        String mul = ArithmeticUtils.mul(String.valueOf(v), "1", 2);
        idealpay = new BigDecimal(mul);
        //实发工资
        double k = Double.valueOf(ratio) * (Double.valueOf(basepay.toString()) + Double.valueOf(jobbonus.toString())
                + Double.valueOf(pay.getAttendance().toString()) + Double.valueOf(pay.getSenioritypay().toString())
                + Double.valueOf(pay.getSubsidy().toString()))
                + Double.valueOf(pay.getOvertime().toString()) - Double.valueOf(pay.getTax().toString()) - Double.valueOf(pay.getSubmoney().toString());
        //出勤天数为零
        if (checkSum.equals(0)) k = 0.00;
        String mul1 = ArithmeticUtils.mul(String.valueOf(k), "1", 2);
        factpay = new BigDecimal(mul1);

        pay.setIdealpay(idealpay);//17应发工资
        mess.add("应发工资：" + pay.getIdealpay() + "元");
        pay.setFactpay(factpay);//18实发工资
        mess.add("实发工资：" + pay.getFactpay() + "元");
        for (int i = 0; i < mess.size(); i++) {
            if (i == mess.size() - 1) {
                leave.append(mess.get(i) + "。");
            } else {
                leave.append(mess.get(i) + "；");
            }
        }
        pay.setLeavex(leave.toString());//19工资详情
        this.sendMess(user, pay.getLeavex(), dateStr);
        System.out.println(pay);
        return payService.save(pay);
    }


    @Autowired
    private ExtraworkService extraworkService;

    /**
     * 获取 yyyy-MM 月分的员工加班信息
     *
     * @param dateStr yyyy-MM
     * @return
     */
    private Map<Integer, BigDecimal> getExtraWorkByDateStr(String dateStr) {
        Map<Integer, BigDecimal> map = new HashMap<>();
        QueryWrapper<Extrawork> wrapper = new QueryWrapper<>();
        wrapper.select("userId", "duration")
                .eq("status", SysProp.COMMON_STATUS_OK)
                .likeRight("date", dateStr);
        List<Extrawork> extraworks = extraworkService.list(wrapper);
        extraworks.forEach(e -> {
            Integer userId = e.getUserId();
            BigDecimal duration = e.getDuration();
            if (map.containsKey(userId)) {
                String add = ArithmeticUtils.add(map.get(userId).toString(), duration.toString(), 1);
                map.put(userId, new BigDecimal(add));
            } else {
                map.put(userId, duration);
            }
        });
        return map;
    }

    @Autowired
    private MessService messService;

    /**
     * 发送message
     *
     * @param user
     * @param content
     */
    private void sendMess(User user, String content, String dateStr) {
        Mess mess = new Mess();
        mess.setUserId(user.getUserId());
        String[] split = dateStr.split("-");
        mess.setTitle(split[0] + "年" + split[1] + "月份工资信息通知");
        mess.setIsSystem(SysProp.MESS_IS_SYSTEM_YES);
        String call = "尊敬的" + user.getRealname();
        if ("男".equals(user.getSex())) {
            call += "先生：";
        } else if ("女".equals(user.getSex())) {
            call += "女士：";
        } else {
            call += "：";
        }
        call += "您的" + split[0] + "年" + split[1] + "月份薪资已核算，工资信息：";
        mess.setMatter(call + content + "祝您生活愉快，工作顺利！        ----来自Pay薪资核算中心");
        mess.setStatus(SysProp.MESS_STATUS_NO);
        mess.setUpdatetime(new Date());
        messService.save(mess);
    }

    @Autowired
    private AuditlogService auditlogService;

    @Autowired
    private RequestUtil requestUtil;

    private void addAuditLog(String content) {
        Auditlog auditlog = new Auditlog();
        auditlog.setUserId(requestUtil.getUserId());
        auditlog.setUpdatetime(new Date());
        String notes = "内容:进行薪资核算操作;操作工号:" + requestUtil.getUserId() + "消息:" + content;
        auditlog.setNotes(notes);
        auditlogService.save(auditlog);
    }
}
