package com.fzipp.pay.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fzipp.pay.common.constant.ErrorCode;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.utils.RequestUtil;
import com.fzipp.pay.entity.Mess;
import com.fzipp.pay.params.mess.GetMyListParam;
import com.fzipp.pay.results.Result;
import com.fzipp.pay.results.mess.MessUnread;
import com.fzipp.pay.service.MessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mess")
@Transactional
public class MessController {

    @Autowired
    private MessService messService;

    @Autowired
    private RequestUtil requestUtil;

    @RequestMapping("/list")
    public Result getListByStatus(@RequestParam(required = false) Integer status) {
        Result result = new Result();
        try {
            QueryWrapper<Mess> wrapper = new QueryWrapper<>();
            wrapper.eq(status != null, "status", status);
            List<Mess> list = messService.list(wrapper);
            result.setData(list);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 拉取消息列表：非系统通知
     * @param param
     * @return
     */
    @PostMapping("/mylist")
    public Result getMyList(@RequestBody GetMyListParam param) {
        Result result = new Result();
        try {
            Integer userId = requestUtil.getUserId();
            QueryWrapper<Mess> wrapper = new QueryWrapper<>();
            wrapper
                    .eq(param.getStatus() != null, "status", param.getStatus())
                    .eq("isSystem",SysProp.MESS_IS_SYSTEM_NO)
                    .orderByDesc("updatetime")
                    .eq("userId", userId);
            Page<Mess> page = messService.page(new Page<>(param.getPageNum(), param.getPageSize()), wrapper);
            result.setError(ErrorCode.CORRECT);
            result.setData(page);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 拉取消息列表：系统通知
     * @param param
     * @return
     */
    @PostMapping("/mysyslist")
    public Result getMySysList(@RequestBody GetMyListParam param) {
        Result result = new Result();
        try {
            Integer userId = requestUtil.getUserId();
            QueryWrapper<Mess> wrapper = new QueryWrapper<>();
            wrapper
                    .eq(param.getStatus() != null, "status", param.getStatus())
                    .eq("isSystem",SysProp.MESS_IS_SYSTEM_YES)
                    .orderByDesc("updatetime")
                    .eq("userId", userId);
            Page<Mess> page = messService.page(new Page<>(param.getPageNum(), param.getPageSize()), wrapper);
            result.setError(ErrorCode.CORRECT);
            result.setData(page);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 标为已读
     * @param messId
     * @return
     */
    @GetMapping("/upstatus")
    public Result upStatusById(@RequestParam(required = true) Integer messId) {
        Result result = new Result();
        try {
            Mess mess = new Mess();
            mess.setMessId(messId);
            mess.setStatus(SysProp.MESS_STATUS_OK);
            boolean b = messService.updateById(mess);
            result.setError(ErrorCode.CORRECT);
            result.setSign(b);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/remove")
    public Result removeById(Integer messId){
        Result result = new Result();
        try {
            boolean b = messService.removeById(messId);
            result.setError(ErrorCode.CORRECT);
            result.setSign(b);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/add")
    public Result addMess(@RequestBody Mess mess){
        Result result = new Result();
        try {
            Boolean b = messService.addMess(mess);
            result.setError(ErrorCode.CORRECT);
            result.setSign(b);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }
        return result;
    }

    @GetMapping("/unread")
    public Result getUnreadCount(){
        Result result = new Result();
        try {
            QueryWrapper<Mess> myWrapper = new QueryWrapper<>();
            QueryWrapper<Mess> sysWrapper = new QueryWrapper<>();
            myWrapper.eq("userId",requestUtil.getUserId()).eq("status",SysProp.MESS_STATUS_NO).eq("isSystem",SysProp.MESS_IS_SYSTEM_NO);
            sysWrapper.eq("userId",requestUtil.getUserId()).eq("status",SysProp.MESS_STATUS_NO).eq("isSystem",SysProp.MESS_IS_SYSTEM_YES);
            MessUnread messUnread = new MessUnread();
            messUnread.setMyUnreadCount(messService.count(myWrapper));
            messUnread.setSysUnreadCount(messService.count(sysWrapper));
            result.setData(messUnread);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }
        return result;
    }

    @PostMapping("/batchremove")
    public Result batchRemove(@RequestBody List<Integer> ids){
        Result result = new Result();
        try {
            boolean b = messService.removeByIds(ids);
            result.setSign(b);
            result.setError(ErrorCode.CORRECT);
        } catch (Exception e) {
            result.setError(ErrorCode.FAULT);
            e.printStackTrace();
        }
        return result;
    }

}
