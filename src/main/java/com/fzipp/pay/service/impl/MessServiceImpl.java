package com.fzipp.pay.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fzipp.pay.common.constant.LogNotes;
import com.fzipp.pay.common.constant.SysProp;
import com.fzipp.pay.common.utils.RequestUtil;
import com.fzipp.pay.entity.Mess;
import com.fzipp.pay.entity.User;
import com.fzipp.pay.mapper.MessMapper;
import com.fzipp.pay.service.MessService;
import com.fzipp.pay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class MessServiceImpl extends ServiceImpl<MessMapper, Mess> implements MessService {

    @Autowired
    private RequestUtil requestUtil;

    @Autowired
    private UserService userService;

    @Override
    public Boolean addMess(Mess mess) {
        User byId = userService.getById(mess.getUserId());
        if(byId==null){
            return false;
        }
        User user = requestUtil.getUser();
        //记录信件来源信息
        String matter = LogNotes.ADD_MESS_001(mess.getMatter(), user.getRealname(), user.getUserId());
        mess.setMatter(matter);
        mess.setStatus(SysProp.ACCOUNT_STATUS_NO);
        mess.setIsSystem(SysProp.MESS_IS_SYSTEM_NO);
        mess.setUpdatetime(new Date());
        return super.save(mess);
    }
}
