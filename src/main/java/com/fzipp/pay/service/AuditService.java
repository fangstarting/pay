package com.fzipp.pay.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.fzipp.pay.entity.Audit;
import com.fzipp.pay.entity.child.AduitInfo;

import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author FengFang123
 * @since 2021-12-28
 */
public interface AuditService extends IService<Audit> {

    /**
     * 高级组合查询
     * @param audit
     * @param pageNum
     * @param pageSize
     * @return
     */
    Map<String, Object> getAuditsByFinds(Audit audit, Integer pageNum, Integer pageSize);

    AduitInfo getAuditInfoById(Integer auditId);

    /**
     * 根据审批id获取审批内容
     * @param auditId
     * @return
     */
    Map<String,Object> getAuditInfo(Integer auditId);

}
