package com.fzipp.pay.controller;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.LogNotes;
import com.fzipp.pay.common.constant.Messages;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.utils.DateUtil;
import com.fzipp.pay.common.utils.RequestUtil;
import com.fzipp.pay.entity.*;
import com.fzipp.pay.entity.child.AduitInfo;
import com.fzipp.pay.params.audit.AuditGetListParam;
import com.fzipp.pay.params.audit.AuditSubmitAuditParam;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.results.audit.AuditMyList;
import com.fzipp.pay.service.*;
import com.fzipp.pay.service.myService.AuthorityCertificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.*;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-28
 */
@RestController
@RequestMapping("/audit")
@Slf4j
public class AuditController {

    private static final String HEAD_PATH = "/audit/**";
    @Autowired
    private AuthorityCertificationService authorityCertificationService;

    @Autowired
    private UserService userService;
    @Autowired
    private LeaveService leaveService;
    @Autowired
    private ExtraworkService extraworkService;
    @Autowired
    private AuditService auditService;
    @Autowired
    private AuditlogService auditlogService;
    @Autowired
    private MessService messService;
    @Autowired
    private RequestUtil requestUtil;

    @Transactional
    @PostMapping("/submitAudit")
    public Result submitAudit(@RequestBody AuditSubmitAuditParam param){
        Result result = new Result();
        try {
            Audit audit = new Audit();
            audit.setAudtypeId(Integer.valueOf(param.getAudtype()));
            audit.setSubmituserId(requestUtil.getUserId());
            audit.setStatus(SysProp.COMMON_STATUS_NO);
            audit.setNotes(param.getNotes());
            audit.setUpdatetime(new Date());
            if ("2".equals(param.getAudtype())){
                //请假
                Leave leave = param.getLeavex();
                leave.setUserId(requestUtil.getUserId());
                leave.setUpdatetime(new Date());
                leave.setStatus(SysProp.COMMON_STATUS_NO);
                leaveService.save(leave);
                audit.setDataId(leave.getLeaveId());
            }else if ("3".equals(param.getAudtype())){
                //加班
                Extrawork extrawork = param.getExtrawork();
                extrawork.setUserId(requestUtil.getUserId());
                extrawork.setUpdatetime(new Date());
                extrawork.setStatus(SysProp.COMMON_STATUS_NO);
                extraworkService.save(extrawork);
                audit.setDataId(extrawork.getExtraworkId());
            }
            auditService.save(audit);
            result.setError(ErrorCode.CORRECT);
        } catch (NumberFormatException e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/mylist")
    public Result myList(){
        Result result = new Result();
        try {
            QueryWrapper<Audit> wrapper = new QueryWrapper<>();
            wrapper.eq("submituserId",requestUtil.getUserId());
            List<Audit> list = auditService.list(wrapper);
            List<AuditMyList> data = new ArrayList<>();
            list.forEach(e->{
                String realname = "暂无";
                if(e.getAuditoruserId()!=null){
                    realname = userService.getOne(new QueryWrapper<User>().select("realname").eq("userId", e.getAuditoruserId())).getRealname();
                }
                AuditMyList auditMyList = new AuditMyList();
                BeanUtils.copyProperties(e,auditMyList);
                auditMyList.setAuditoruserName(realname);
                data.add(auditMyList);
            });
            result.setError(ErrorCode.CORRECT);
            result.setData(data);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/getlist")
    public Result getList(@RequestBody AuditGetListParam param){
        Result result = new Result();
        try {
            QueryWrapper wrapper = new QueryWrapper();
            wrapper.eq(param.getStatus()!=null,"status",param.getStatus())
                    .eq(param.getAudtypeId()!=null,"audtypeId",param.getAudtypeId());
            Page page = auditService.page(new Page<>(param.getPageNum(), param.getPageSize()), wrapper);
            result.setError(ErrorCode.CORRECT);
            result.setData(page);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    @RequestMapping("/getaudits")
    public Map<String,Object> getAuditsByFinds(@RequestBody(required = false) Map o){
        Map<String,Object> map = new HashMap<>();
        String pageNumS = null;
        String pageSizeS = null;
        String audtypeIdS = null;
        String statusS = null;
        if (o!=null) {
            pageNumS = String.valueOf(o.get("pageNum"));
            pageSizeS = String.valueOf(o.get("pageSize"));
            audtypeIdS = String.valueOf(o.get("audtypeId"));
            statusS = String.valueOf(o.get("status"));
        }
        Integer pageNum;
        Integer pageSize;
        Audit audit = new Audit();
        try {
            pageNum = pageNumS==null||"".equals(pageNumS)?0:Integer.valueOf(pageNumS);
            pageSize = pageSizeS==null||"".equals(pageSizeS)?0:Integer.valueOf(pageSizeS);
            //构建查询条件
            if(audtypeIdS!=null&&!"".equals(audtypeIdS)){
                audit.setAudtypeId(Integer.valueOf(audtypeIdS));
            }
            if(statusS!=null&&!"".equals(statusS)){
                audit.setStatus(Integer.valueOf(statusS));
            }
        } catch (NumberFormatException e) {
            map.put("errorcode", ErrorCode.VERIFY_PARAMS_ERROR);
            return map;
        }
        map.put("errorcode", ErrorCode.VERIFY_BIZ(true));
        Map<String, Object> page = auditService.getAuditsByFinds(audit, pageNum, pageSize);
        map.put("page",page);
        return map;
    }

    @RequestMapping("/getauditbyid")
    public Map<String,Object> getAuditById(Integer auditId){
        Map<String,Object> map = new HashMap<>();
        AduitInfo auditInfoById = auditService.getAuditInfoById(auditId);
        map.put("errorcode",ErrorCode.VERIFY_BIZ(true));
        map.put("auditInfo",auditInfoById);
        return map;
    }

    @Autowired
    private CheckService checkService;

    @Transactional
    @RequestMapping("/upaudit")
    public Map<String,Object> upAudit(@RequestBody Map o){
        Map<String,Object> map = new HashMap<>();
        //获取操作人id
//        Integer opUserId = (Integer) request.getSession().getAttribute("userId");
        Integer opUserId = requestUtil.getUserId();
        Integer auditId = (Integer) o.get("auditId");
        Integer audtypeId = (Integer) o.get("audtypeId");
        if (audtypeId==1){//激活
            User user = JSON.parseObject(JSON.toJSONString(o),User.class);
            user.setUpdatetime(new Date());
            userService.updateById(user);
            if(user.getStatus()== SysProp.COMMON_STATUS_OK){
                //发送消息通知
                String mes = this.newUserMes(user.getRealname(), user.getSex());
                this.sendMess(user.getUserId(),"欢迎使用Pay薪资平台",mes);
            }
        }
        if (audtypeId==2){//请假
            Leave leave = JSON.parseObject(JSON.toJSONString(o),Leave.class);
            if(leave.getStatus()!=SysProp.COMMON_STATUS_NO){
                String mes = this.leaveMes(leave.getStartdate(), leave.getEnddate(), leave.getStatus(), leave.getOpinion());
                this.sendMess(leave.getUserId(),null,mes);
                Leave up = new Leave();
                up.setLeaveId(leave.getLeaveId());
                up.setStatus(leave.getStatus());
                up.setOpinion(leave.getOpinion());
                up.setUpdatetime(new Date());
                leaveService.updateById(up);
                //同步考勤表信息
                Check check = new Check();
                check.setUserId(leave.getUserId());
                check.setStatus(SysProp.CHECK_STATUS_ON);
                check.setAbtypeId(SysProp.ABTYPE_LEAVE_ID_2);
                check.setUpdatetime(new Date());
                Date start = leave.getStartdate();
                Date end = leave.getEnddate();
                while (end.compareTo(start)>=0){
                    check.setDate(start);
                    checkService.save(check);
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(start);
                    calendar.add(Calendar.DATE, 1);
                    start = calendar.getTime();
                }
            }
        }
        if (audtypeId==3){//加班
            Extrawork extrawork = JSON.parseObject(JSON.toJSONString(o),Extrawork.class);
            if(extrawork.getStatus()!=SysProp.COMMON_STATUS_NO){
                String mes = this.extraworkMes(extrawork.getDate(), extrawork.getDuration(), extrawork.getStatus(), extrawork.getOpinion());
                this.sendMess(extrawork.getUserId(),null,mes);
                Extrawork up = new Extrawork();
                up.setExtraworkId(extrawork.getExtraworkId());
                up.setOpinion(extrawork.getOpinion());
                up.setStatus(extrawork.getStatus());
                up.setUpdatetime(new Date());
                extraworkService.updateById(up);
            }
        }
        Auditlog auditlog = new Auditlog();
        auditlog.setUpdatetime(new Date());
        auditlog.setUserId(opUserId);
        auditlog.setNotes(LogNotes.UP_AUDIT(opUserId,auditId,audtypeId));
        auditlogService.save(auditlog);
        Audit audit = new Audit();
        audit.setAuditId(auditId);
        audit.setUpdatetime(new Date());
        audit.setStatus(1);
        audit.setAuditoruserId(opUserId);
        auditService.updateById(audit);
        map.put("errorcode",ErrorCode.VERIFY_BIZ(true));
        return map;
    }

    @RequestMapping("/shelve")
    public Map<String,Object> shelve(Integer auditId){
        Map<String,Object> map = new HashMap<>();
        //获取操作人id
//        Integer opUserId = (Integer) request.getSession().getAttribute("userId");
        Integer opUserId = requestUtil.getUserId();
        if (auditId==null){
            map.put(ErrorCode.TITLE,ErrorCode.VERIFY_PARAMS_ERROR);
            return map;
        }
        Audit audit = new Audit();
        audit.setAuditoruserId(opUserId);
        audit.setAuditId(auditId);
        audit.setStatus(2);
        audit.setUpdatetime(new Date());
        boolean b = auditService.updateById(audit);
        map.put("errorcode",ErrorCode.VERIFY_BIZ(b));
        return map;
    }

    /**
     * 根据审批id获取审批内容
     * @return
     */
    @RequestMapping("/getauditinfo")
    public Map<String,Object> getAuditInfo(Integer auditId){
        Map<String,Object> map = new HashMap<>();
        if(auditId==null){
            map.put("errorcode",ErrorCode.VERIFY_PARAMS_ERROR);
            return map;
        }
        Map<String, Object> auditInfo = auditService.getAuditInfo(auditId);
        map.put("errorcode",ErrorCode.VERIFY_BIZ(true));
        map.put("info",auditInfo);
        return map;
    }

    @GetMapping("remove")
    public Result removeAuditById(Integer id){
        Result result = new Result();
        //此接口需要验证权限路径path
        String mappingPath = "/audit/remove";
        //权限判断
        Boolean aBoolean = authorityCertificationService.verifyPath(HEAD_PATH, mappingPath);
        if (!aBoolean){
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_403);
            return result;
        }
        try {
            boolean b = auditService.removeById(id);
            result.setError(ErrorCode.CORRECT);
            result.setData(b);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            result.setMessage(Messages.API_500);
            e.printStackTrace();
        }
        return result;
    }

    //审批系统通知发送
    private void sendMess(Integer userId,String title,String content){
        Mess mess = new Mess();
        mess.setUserId(userId);
        mess.setMatter(content);
        mess.setIsSystem(SysProp.MESS_IS_SYSTEM_YES);
        mess.setStatus(SysProp.MESS_STATUS_NO);
        mess.setTitle(title==null?"审批中心通知":title);
        mess.setUpdatetime(new Date());
        messService.save(mess);
    }

    private String newUserMes(String realName,String sex){
        String call = "尊敬的" + realName;
        if ("男".equals(sex)) {
            call += "先生,";
        } else if ("女".equals(sex)) {
            call += "女士,";
        } else {
            call += "";
        }
        String content =
                "<html>" +
                        "<body>" +
                        "<p>"+call+"你好!</p>" +
                        "<p>欢迎使用Pay薪资平台！</p>" +
                        "<p>您提交给我们的用户信息经核查已通过平台审核！</p>" +
                        "<p>本平台将严格遵守信息安全法并保护您的数据信息。</p>" +
                        "<p>如有疑问请联系平台管理员</p>" +
                        "<p>祝您工作顺利，身体康健！</p>" +
                        "<hr />"+
                        "<p><em>谢谢!</br>Pay 审批中心</em></p>" +
                        "</body>" +
                        "</html>";
        return content;
    }

    private String extraworkMes(Date date, BigDecimal hh, Integer status, String opinion){
        String mat = "yyyy年MM月dd日";
        String rmes = null;
        if (status==1)rmes = "通过";
        else if(status==2)rmes = "未通过";
        String start = DateUtil.getStrDateFormat(date, mat);
        String content =
                "<html>" +
                        "<body>" +
                        "<p>加班申请审批结果通知：</p>" +
                        "<p>加班日期："+start+"</p>" +
                        "<p>加班时长："+hh+"小时</p>" +
                        "<p>审批结果："+rmes+"</p>" +
                        "<p>审批意见："+opinion+"</p>" +
                        "<p>若对此结果有疑问请联系部门主管</p>" +
                        "<p>祝您工作顺利，身体康健！</p>" +
                        "<hr />"+
                        "<p><em>谢谢!</br>Pay 审批中心</em></p>" +
                        "</body>" +
                        "</html>";
        return content;
    }

    private String leaveMes(Date startDate,Date endDate,Integer status,String opinion){
        String mat = "yyyy年MM月dd日";
        String rmes = null;
        if (status==1)rmes = "通过";
        else if(status==2)rmes = "未通过";
        String start = DateUtil.getStrDateFormat(startDate, mat);
        String end = DateUtil.getStrDateFormat(endDate, mat);
        String content =
                "<html>" +
                        "<body>" +
                        "<p>请假申请审批结果通知：</p>" +
                        "<p>请假日期："+start+"至"+end+"</p>" +
                        "<p>审批结果："+rmes+"</p>" +
                        "<p>审批意见："+opinion+"</p>" +
                        "<p>若对此结果有疑问请联系部门主管</p>" +
                        "<p>祝您工作顺利，身体康健！</p>" +
                        "<hr />"+
                        "<p><em>谢谢!</br>Pay 审批中心</em></p>" +
                        "</body>" +
                        "</html>";
        return content;
    }

}

