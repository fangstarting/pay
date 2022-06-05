package com.fzipp.pay.controller;


import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.enmus.ExcelType;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.constant.SystemKey;
import com.fzipp.pay.common.file.MyFileUtils;
import com.fzipp.pay.common.utils.DateUtil;
import com.fzipp.pay.common.utils.OperateDataUtil;
import com.fzipp.pay.common.utils.RequestUtil;
import com.fzipp.pay.entity.Abtype;
import com.fzipp.pay.entity.Check;
import com.fzipp.pay.entity.SysConfigs;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.params.check.CheckEchartsStatParam;
import com.fzipp.pay.params.check.CheckListsParam;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.results.check.CheckLists;
import com.fzipp.pay.service.AbtypeService;
import com.fzipp.pay.service.CheckService;
import com.fzipp.pay.service.SysConfigsService;
import com.fzipp.pay.service.UserService;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-26
 */
@RestController
@RequestMapping("/check")
public class CheckController {

    @Autowired
    private CheckService checkService;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private RequestUtil requestUtil;

    private Integer getOpUserId() {
        return requestUtil.getUserId();
//        return (Integer) request.getSession().getAttribute("userId");
    }

    @Autowired
    private UserService userService;

    @Autowired
    private AbtypeService abtypeService;

    @PostMapping("/stat")
    public Result echartsStat(@RequestBody CheckEchartsStatParam param){
        Result result = new Result();
        try {
            return  checkService.getEchartsData(param);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/lists")
    public Result getLists(@RequestBody CheckListsParam param) {
        Result result = new Result();
        try {
            Page<Check> checkPage = new Page<>(param.getPageNum(), param.getPageSize());
            QueryWrapper<Check> q = new QueryWrapper<>();
            q.eq(param.getUserId() != null, "userId", param.getUserId())
                    .orderByDesc("updatetime")
                    .eq(param.getStatus() != null, "status", param.getStatus());
            if (param.getDate() != null) {
                if (param.getDate().size() == 2) {
                    q.between("date", param.getDate().get(0), param.getDate().get(1));
                }
            }
            Page<Check> page = checkService.page(checkPage, q);
            List<CheckLists> checkLists = OperateDataUtil.optionShiftList(page.getRecords(), CheckLists.class);
            checkLists.forEach(e -> {
                checkDataOption(e);
            });
            Page<CheckLists> checkListsPage = new Page<>();
            BeanUtils.copyProperties(page, checkListsPage);
            checkListsPage.setRecords(checkLists);
            result.setError(ErrorCode.CORRECT);
            result.setData(checkListsPage);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @Value("${file.dri.cache}")
    private String cache;

    @PostMapping("/exportExcel")
    public ResponseEntity exportExcelList(@RequestBody List<Integer> ids){
        try {
            if (ids==null||ids.size()==0){
                return null;
            }
            List<CheckLists> checkList = this.getCheckListByIds(ids);
            //生成Excel
            String title = "员工考勤信息";
            ExportParams params = new ExportParams(title, "考勤打卡列表", ExcelType.XSSF);
            Workbook sheets = ExcelExportUtil.exportExcel(params, CheckLists.class, checkList);
            File saveFile = new File(cache);
            if (!saveFile.exists()) {
                saveFile.mkdirs();
            }
            String path = cache +title+DateUtil.getDateOfYS()+SysProp.FILE_TYPE_EXCEL_1;
            FileOutputStream fos = new FileOutputStream(path);
            sheets.write(fos);
            fos.close();
            return MyFileUtils.commonExport(new File(path));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private List<CheckLists> getCheckListByIds(List<Integer> ids){
        List<CheckLists> data = new ArrayList<>();
        ids.forEach(id->{
            CheckLists e = new CheckLists();
            Check check = checkService.getById(id);
            BeanUtils.copyProperties(check,e);
            checkDataOption(e);
            data.add(e);
        });
        return data;
    }

    private void checkDataOption(CheckLists e) {
        String realname = userService.getOne(new QueryWrapper<User>().select("realname").eq("userId", e.getUserId())).getRealname();
        e.setRealname(realname);
        String name = "无";
        if (e.getAbtypeId() != null) {
            name = abtypeService.getOne(new QueryWrapper<Abtype>().select("name").eq("abtypeId", e.getAbtypeId())).getName();
        }
        e.setAbtypeName(name);
    }

    @RequestMapping("checkinfo")
    public Map<String, Object> getCheckInfoByUserId() {
        Map<String, Object> map = new HashMap<>();
        List<Map<String, Object>> checkInfo = checkService.getCheckInfo(this.getOpUserId());
        map.put("errorcode", ErrorCode.VERIFY_BIZ(true));
        map.put("checkinfo", checkInfo);
        return map;
    }

    @Transactional
    @RequestMapping("/clockin")
    public Map<String, Object> clockIn(Integer type) {
        Map<String, Object> map = new HashMap<>();
        //type: 0上班 1下班
        if (type == 0) {
            if (!this.ifUpTime()) {
                map.put("errorcode", 101);
                map.put("message", "不在上班打卡时间内！");
                return map;
            }
            if (this.upClockin()) {
                map.put("errorcode", 100);
                map.put("message", "上班打卡成功！");
            } else {
                map.put("errorcode", 101);
                map.put("message", "今日已打卡上班，请勿重复打卡！");
            }

        } else if (type == 1) {
            if (!this.ifDownTime()) {
                map.put("errorcode", 101);
                map.put("message", "不在下班打卡时间内！");
                return map;
            }
            if (this.downClockin()) {
                map.put("errorcode", 100);
                map.put("message", "下班打卡成功！");
            } else {
                map.put("errorcode", 101);
                map.put("message", "今日已打卡下班，请勿重复打卡！");
            }
        }
        return map;
    }

    private boolean upClockin() {
        Check checkById = checkService.getCheckByUserIdAndDate(this.getOpUserId());
        if (checkById != null) {
            if(checkById.getLatetime()!=null&&checkById.getEarlytime() == null){
                //已完成下班打卡项
                checkById.setEarlytime(new Date());
                checkById.setLate(this.ifLate());
                checkById.setHostIp(request.getRemoteAddr());
                checkService.updateById(checkById);
                return true;
            }
            else if (checkById.getEarlytime() != null) {
                return false;//已上班打卡
            }
        }else {
            Check check = new Check();
            check.setUserId(this.getOpUserId());
            check.setUpdatetime(new Date());
            check.setDate(new Date());
            check.setEarlytime(new Date());
            check.setLate(this.ifLate());
            check.setHostIp(request.getRemoteAddr());
            check.setStatus(1);//上班打卡为1 ->下班置为0
            check.setAbtypeId(1);//上班打卡为1 ->下班置为null
            return checkService.save(check);
        }
        return false;
    }

    private boolean downClockin() {
        Check checkById = checkService.getCheckByUserIdAndDate(this.getOpUserId());
        if (checkById != null) {
            if (checkById.getLatetime() != null) return false; //已下班打卡
            Check check = new Check();
            check.setCheckId(checkById.getCheckId());
            check.setLatetime(new Date());
            check.setEarly(this.ifEarly());
            check.setStatus(0);
            check.setAbtypeId(null);
            check.setUpdatetime(new Date());
            return checkService.downClockin(check);
        }
        if (checkById == null) {
            //不存在打卡记录  //没有进行上班打卡
            Check check = new Check();
            check.setUserId(this.getOpUserId());
            check.setDate(new Date());
            check.setLatetime(new Date());
            check.setEarly(this.ifEarly());
            check.setHostIp(request.getRemoteAddr());
            check.setStatus(1);//缺勤
            check.setAbtypeId(1);//打卡异常
            check.setUpdatetime(new Date());
            return checkService.save(check);
        }
        return false;
    }

    @Autowired
    private SysConfigsService sysConfigsService;

    /**
     * 是否在上班打卡时间内
     *
     * @return
     */
    private boolean ifUpTime() {
        //    @Value("${pay.work.up-start}")
        String upStart = sysConfigsService.getById(SystemKey.WORK_UP_START).getSysValueString();
        //    @Value("${pay.work.up-end}")
        String upEnd = sysConfigsService.getById(SystemKey.WORK_UP_END).getSysValueString();
        return timeScopeVerify(upStart, upEnd);
    }

    /**
     * 是否在下班打卡时间内
     *
     * @return
     */
    private boolean ifDownTime() {
        //    @Value("${pay.work.down-start}")
        String downStart = sysConfigsService.getById(SystemKey.WORK_DOWN_START).getSysValueString();
        //    @Value("${pay.work.down-end}")
        String downEnd = sysConfigsService.getById(SystemKey.WORK_DOWN_END).getSysValueString();
        return timeScopeVerify(downStart, downEnd);
    }

    /**
     * 早退判断
     *
     * @return *正常0  *早退1
     */
    private int ifEarly() {
        int flag = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            //    @Value("${pay.work.down-time}")
            String downTime = sysConfigsService.getById(SystemKey.WORK_DOWN_TIME).getSysValueString();
            Date date1 = sdf.parse(downTime);
            Date date2 = sdf.parse(DateUtil.getTime());
            flag = date1.compareTo(date2) <= 0 ? 0 : 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 迟到判断
     *
     * @return *正常0  *迟到1
     */
    private int ifLate() {
        int flag = 0;
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        try {
            //    @Value("${pay.work.up-time}")
            String upTime = sysConfigsService.getById(SystemKey.WORK_UP_TIME).getSysValueString();
            Date date1 = sdf.parse(upTime);
            Date date2 = sdf.parse(DateUtil.getTime());
            flag = date1.compareTo(date2) >= 0 ? 0 : 1;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 当前时间是否在范围内
     *
     * @param start
     * @param end
     * @return
     */
    private boolean timeScopeVerify(String start, String end) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        boolean flag1 = false;
        boolean flag2 = false;
        try {
            Date date1 = sdf.parse(start);
            Date date2 = sdf.parse(DateUtil.getTime());
            Date date3 = sdf.parse(end);
            flag1 = date1.compareTo(date2) >= 0 ? false : true;
            flag2 = date3.compareTo(date2) >= 0 ? true : false;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return flag1 && flag2;
    }
}

