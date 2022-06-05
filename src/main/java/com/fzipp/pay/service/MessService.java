package com.fzipp.pay.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.fzipp.pay.entity.Mess;

public interface MessService extends IService<Mess> {

    /**
     * 添加：非系统消息通知
     * @param mess
     * @return
     */
    Boolean addMess(Mess mess);
}
